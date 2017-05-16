package cn.earlydata.webcollector.test;

import cn.earlydata.webcollector.common.ConfigAttribute;
import cn.earlydata.webcollector.plugin.berkeley.BerkeleyDBManager;
import cn.earlydata.webcollector.task.AmazonTask;
import cn.earlydata.webcollector.util.PropertiesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

/**
 * Created by Yanjun on 2017/05/11.
 */
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class AmazonTaskTest {

    @Autowired
    public AmazonTask amazonTask;

    @Resource
    public BerkeleyDBManager berkeleyDBManager;

    @Test
    public void testAmazonTask(){
        try {
            amazonTask.taskInit("crawlPath",false);
            amazonTask.taskRun();
            System.out.println("-----" + berkeleyDBManager);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testProperties(){
        String value = PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.DEFAULT_HTTP_METHOD);
        System.out.println(value);
    }
}
