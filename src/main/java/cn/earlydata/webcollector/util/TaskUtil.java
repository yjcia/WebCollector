package cn.earlydata.webcollector.util;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by YanJun on 2017/5/15.
 */
public class TaskUtil {
    private static Logger LOG = Logger.getLogger(TaskUtil.class);
    public static void doExecute(Collection<Runnable> runnableArrayList, String taskName) {
        try {
            final long start = System.currentTimeMillis();
            ExecutorService pool = Executors.newFixedThreadPool(50);
            for (Runnable runnable : runnableArrayList) {
                pool.execute(runnable);
            }
            pool.shutdown();
            //判断线程池中子线程是否都执行完毕
            while(true){
                if(pool.isTerminated()){
                    LOG.info("ExecutorService finished");
                    break;
                }
                Thread.sleep(1000);
            }
            final long end = System.currentTimeMillis();
            LOG.info(taskName + " ExecutorService cost time :" + (end-start)/1000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }
}
