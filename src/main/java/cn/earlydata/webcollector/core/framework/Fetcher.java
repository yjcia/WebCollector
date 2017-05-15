/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.earlydata.webcollector.core.framework;

import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.model.CrawlDatums;
import cn.earlydata.webcollector.common.Config;
import cn.earlydata.webcollector.util.TaskUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 抓取器
 */
@Component
public class Fetcher {

    public static final Logger LOG = Logger.getLogger(Fetcher.class);

    public DBManager dbManager;
    public Executor executor;
    private AtomicInteger activeThreads;
    private AtomicInteger startedThreads;
    private AtomicInteger spinWaiting;
    private AtomicLong lastRequestStart;
    private QueueFeeder feeder;
    private FetchQueue fetchQueue;
    private long executeInterval = 0;
    private int threads = 50;
    public volatile boolean running;

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public static class FetchItem {

        public CrawlDatum datum;

        public FetchItem(CrawlDatum datum) {
            this.datum = datum;
        }
    }

    public static class FetchQueue {

        public AtomicInteger totalSize = new AtomicInteger(0);
        public final List<FetchItem> queue = Collections.synchronizedList(new LinkedList<FetchItem>());

        public void clear() {
            queue.clear();
        }

        public int getSize() {
            return queue.size();
        }

        public synchronized void addFetchItem(FetchItem item) {
            if (item == null) {
                return;
            }
            queue.add(item);
            totalSize.incrementAndGet();
        }

        public synchronized FetchItem getFetchItem() {
            if (queue.isEmpty()) {
                return null;
            }
            return queue.remove(0);
        }

        public synchronized void dump() {
            for (int i = 0; i < queue.size(); i++) {
                FetchItem it = queue.get(i);
                LOG.info("  " + i + ". " + it.datum.url());
            }

        }

    }

    public static class QueueFeeder extends Thread {

        public FetchQueue queue;
        public Generator generator;
        public int size;
        public boolean running = true;

        public QueueFeeder(FetchQueue queue, Generator generator, int size) {
            this.queue = queue;
            this.generator = generator;
            this.size = size;
        }

        public void stopFeeder() {
            running = false;
            while (this.isAlive()) {
                try {
                    Thread.sleep(1000);
                    LOG.info("stopping feeder......");
                } catch (InterruptedException ex) {
                }
            }
        }

        @Override
        public void run() {
            boolean hasMore = true;
            running = true;
            while (hasMore && running) {
                int feed = size - queue.getSize();
                if (feed <= 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                    continue;
                }
                while (feed > 0 && hasMore && running) {
                    CrawlDatum datum = generator.next();
                    hasMore = (datum != null);
                    if (hasMore) {
                        queue.addFetchItem(new FetchItem(datum));
                        feed--;
                    }
                }
            }
        }
    }

    private class FetcherThread implements Runnable {
        @Override
        public void run() {
            startedThreads.incrementAndGet();
            activeThreads.incrementAndGet();
            FetchItem item = null;
            try {
                while (running) {
                    try {
                        item = fetchQueue.getFetchItem();
                        if (item == null) {
                            if (feeder.isAlive() || fetchQueue.getSize() > 0) {
                                spinWaiting.incrementAndGet();
                                try {
                                    Thread.sleep(500);
                                } catch (Exception ex) {
                                }
                                spinWaiting.decrementAndGet();
                                continue;
                            } else {
                                return;
                            }
                        }
                        lastRequestStart.set(System.currentTimeMillis());
                        CrawlDatum crawlDatum = item.datum;
                        CrawlDatums next = new CrawlDatums();
                        try {
                            executor.execute(crawlDatum, next);
                            if(crawlDatum.isCrawlSuccess()){
                                LOG.info("done: " + crawlDatum.key());
                                crawlDatum.setStatus(CrawlDatum.STATUS_DB_SUCCESS);
                            }else{
                                LOG.info("failed: " + crawlDatum.key());
                                crawlDatum.setStatus(CrawlDatum.STATUS_DB_FAILED);
                                dbManager.writeErrorSegment(crawlDatum);
                            }

                        } catch (Exception ex) {
                            //如果爬取败异常
                            LOG.info("failed: " + crawlDatum.key(), ex);
                            crawlDatum.setStatus(CrawlDatum.STATUS_DB_FAILED);
                            crawlDatum.setCrawlSuccess(false);
                            dbManager.writeErrorSegment(crawlDatum);
                        }
                        crawlDatum.incrExecuteCount(1);
                        crawlDatum.setExecuteTime(System.currentTimeMillis());
                        try {
                            dbManager.writeFetchSegment(crawlDatum);
                            if (crawlDatum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS && !next.isEmpty()) {
                                dbManager.writeParseSegment(next);
                            }
                        } catch (Exception ex) {
                            LOG.info("Exception when updating db", ex);
                        }
                        if (executeInterval > 0) {
                            try {
                                Thread.sleep(executeInterval);
                            } catch (Exception sleepEx) {
                            }
                        }

                    } catch (Exception ex) {
                        LOG.info("Exception", ex);
                    }
                }

            } catch (Exception ex) {
                LOG.info("Exception", ex);
            } finally {
                activeThreads.decrementAndGet();
            }
        }
    }

    /**
     * 抓取当前所有任务，会阻塞到爬取完成
     *
     * @param generator 给抓取提供任务的Generator(抓取任务生成器)
     * @throws IOException 异常
     */
    public void fetchAll(Generator generator) throws Exception {
        if (executor == null) {
            LOG.info("Please Specify A Executor!");
            return;
        }

        try {
            dbManager.merge();
            generator.init();
            LOG.info("open generator:" + generator.getClass().getName());
            dbManager.initSegmentWriter();
            LOG.info("init segmentWriter:" + dbManager.getClass().getName());
            running = true;
            lastRequestStart = new AtomicLong(System.currentTimeMillis());
            activeThreads = new AtomicInteger(0);
            startedThreads = new AtomicInteger(0);
            spinWaiting = new AtomicInteger(0);
            fetchQueue = new FetchQueue();
            feeder = new QueueFeeder(fetchQueue, generator, 1000);
            feeder.start();
            Collection<Runnable> runnableArrayList = new ArrayList<Runnable>();
            for (int i = 0; i < threads; i++) {
                runnableArrayList.add(new FetcherThread());
            }
            TaskUtil.doExecute(runnableArrayList,"FetchTask");
            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                LOG.info("-activeThreads=" + activeThreads.get()
                        + ", spinWaiting=" + spinWaiting.get() + ", fetchQueue.size="
                        + fetchQueue.getSize());

                if (!feeder.isAlive() && fetchQueue.getSize() < 5) {
                    fetchQueue.dump();
                }

                if ((System.currentTimeMillis() - lastRequestStart.get()) > Config.THREAD_KILLER) {
                    LOG.info("Aborting with " + activeThreads + " hung threads.");
                    break;
                }

            } while (running && (startedThreads.get() != threads || activeThreads.get() > 0));
            running = false;
            long waitThreadEndStartTime = System.currentTimeMillis();
            if (activeThreads.get() > 0) {
                LOG.info("wait for activeThreads to end");
            }
            /*等待存活线程结束*/
            while (activeThreads.get() > 0) {
                LOG.info("-activeThreads=" + activeThreads.get());
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                }
                if (System.currentTimeMillis() - waitThreadEndStartTime > Config.WAIT_THREAD_END_TIME) {
                    LOG.info("kill threads");
                    FetcherThread[] runnableArr = (FetcherThread[]) runnableArrayList.toArray();
                    for (int i = 0; i < runnableArr.length; i++) {
                        Thread t = new Thread(runnableArr[i]);
                        if (t.isAlive()) {
                            try {
                                t.stop();
                                LOG.info("kill thread " + i);
                            } catch (Exception ex) {
                                LOG.info("Exception", ex);
                            }
                        }
                    }
                    break;
                }
            }
            LOG.info("clear all activeThread");
            feeder.stopFeeder();
            fetchQueue.clear();
        } finally {
            generator.close();
            LOG.info("close generator:" + generator.getClass().getName());
            dbManager.closeSegmentWriter();
            LOG.info("close segmentwriter:" + dbManager.getClass().getName());
        }
    }



    /**
     * 停止爬取
     */
    public void stop() {
        running = false;
    }

    /**
     * 返回爬虫的线程数
     *
     * @return 爬虫的线程数
     */
    public int getThreads() {
        return threads;
    }

    /**
     * 设置爬虫的线程数
     *
     * @param threads 爬虫的线程数
     */
    public void setThreads(int threads) {
        this.threads = threads;
    }

    public DBManager getDBManager() {
        return dbManager;
    }

    public void setDBManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public long getExecuteInterval() {
        return executeInterval;
    }

    public void setExecuteInterval(long executeInterval) {
        this.executeInterval = executeInterval;
    }

}
