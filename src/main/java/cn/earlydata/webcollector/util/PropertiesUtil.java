package cn.earlydata.webcollector.util;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by YanJun on 2017/5/16.
 */
public class PropertiesUtil {
    private static Properties crawlProps;
    static {
        crawlProps = new Properties();
        try {
            crawlProps = PropertiesLoaderUtils.loadAllProperties("crawler_config.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCrawlerConfigValue(String key){
        return crawlProps.getProperty(key);
    }
}
