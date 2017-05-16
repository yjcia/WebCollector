package cn.earlydata.webcollector.core.framework;

import cn.earlydata.webcollector.common.ConfigAttribute;
import cn.earlydata.webcollector.common.CrawlerAttribute;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.plugin.berkeley.BerkeleyDBManager;
import cn.earlydata.webcollector.task.AmazonTask;
import cn.earlydata.webcollector.util.PropertiesUtil;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by YanJun on 2017/5/15.
 */
public class Task {

    private Logger LOG = Logger.getLogger(Task.class);
    private BerkeleyDBManager berkeleyDBManager;
    private static final int MAX_RETRY_TIMES = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.RETRY_TIMES));
    private static int retryTimes = 0;

    public Task(DBManager dbManager) {
        this.berkeleyDBManager = (BerkeleyDBManager) dbManager;
    }

    public void doExecute(Collection<Runnable> runnableArrayList, String taskName) {
        try {
            final long start = System.currentTimeMillis();
            ExecutorService pool = Executors.newFixedThreadPool(50);
            for (Runnable runnable : runnableArrayList) {
                pool.execute(runnable);
            }
            pool.shutdown();
            while (true) {
                if (pool.isTerminated()) {
                    List<CrawlDatum> crawlDatumList =
                            berkeleyDBManager.list(CrawlerAttribute.ERRORDB_NAME, taskName);
                    if (crawlDatumList.size() > 0 && retryTimes < MAX_RETRY_TIMES) {
                        String databaseName = CrawlerAttribute.AMAZON_TASK + "_error_" + retryTimes;
                        berkeleyDBManager.clear(databaseName);
                        Object obj = Class.forName(CrawlerAttribute.TASKCLASS_PACKAGE + taskName).newInstance();
                        if (taskName.equals(CrawlerAttribute.AMAZON_TASK)) {
                            retryTimes++;
                            AmazonTask amazonTask = (AmazonTask) obj;
                            LOG.info("retry times : " + retryTimes);
                            amazonTask.taskRun(crawlDatumList, databaseName);
                        }
                    } else {
                        LOG.info("ExecutorService finished");
                        break;
                    }
                }
                Thread.sleep(1000);
            }
            final long end = System.currentTimeMillis();
            LOG.info(taskName + " ExecutorService cost time :" + (end - start) / 1000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
