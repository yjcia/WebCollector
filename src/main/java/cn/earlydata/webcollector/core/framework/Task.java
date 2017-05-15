package cn.earlydata.webcollector.core.framework;

import cn.earlydata.webcollector.common.CrawlerAttribute;
import cn.earlydata.webcollector.core.crawler.Crawler;
import cn.earlydata.webcollector.plugin.berkeley.BerkeleyDBManager;
import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by YanJun on 2017/5/15.
 */
public class Task {

    private Logger LOG = Logger.getLogger(Task.class);
    private BerkeleyDBManager berkeleyDBManager;

    public Task(DBManager dbManager){
        this.berkeleyDBManager = (BerkeleyDBManager)dbManager;
    }

    public void doExecute(Collection<Runnable> runnableArrayList, String taskName) {
        try {
            final long start = System.currentTimeMillis();
            ExecutorService pool = Executors.newFixedThreadPool(50);
            for (Runnable runnable : runnableArrayList) {
                pool.execute(runnable);
            }
            pool.shutdown();
            while(true){
                if(pool.isTerminated()){
                    System.out.println(berkeleyDBManager);
                    berkeleyDBManager.list(CrawlerAttribute.ERRORDB_NAME);
                    LOG.info("ExecutorService finished");
                    break;
                }
                Thread.sleep(1000);
            }
            final long end = System.currentTimeMillis();
            LOG.info(taskName + " ExecutorService cost time :" + (end-start)/1000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
