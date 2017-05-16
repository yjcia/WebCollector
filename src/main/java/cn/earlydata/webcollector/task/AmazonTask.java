package cn.earlydata.webcollector.task;

import cn.earlydata.webcollector.common.ConfigAttribute;
import cn.earlydata.webcollector.common.CrawlerAttribute;
import cn.earlydata.webcollector.core.spring.ApplicationContextHolder;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.model.CrawlDatums;
import cn.earlydata.webcollector.model.CrawlerKeyWordsInfo;
import cn.earlydata.webcollector.model.Page;
import cn.earlydata.webcollector.core.crawler.BreadthCrawler;
import cn.earlydata.webcollector.plugin.berkeley.BerkeleyDBManager;
import cn.earlydata.webcollector.service.CommonService;
import cn.earlydata.webcollector.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yanjun on 2017/05/11.
 */
@Controller
public class AmazonTask extends BreadthCrawler {

    private static final Logger LOG = Logger.getLogger(AmazonTask.class);
    private static Map<String, String> headerMap;
    private static int threadsCount;
    private static int executeInterval;
    static {
        headerMap = new HashMap<String, String>();
        headerMap.put("User-Agent", CrawlerAttribute.DEFAULT_USER_AGENT);
        headerMap.put("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headerMap.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Accept-Language", "zh-cn,zh;q=0.5");
        headerMap.put("Connection", "keep-alive");
        threadsCount = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.THREAD_COUNT));
        executeInterval = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.EXECUTE_INTERVAL));
    }

    @Autowired
    private CommonService commonService;

    public AmazonTask() {
    }

    /**
     * 爬虫任务初始化
     *
     * @param crawlPath
     * @param autoParse
     */
    public void taskInit(String crawlPath, boolean autoParse) {
        super.initCrawler(crawlPath, autoParse, headerMap);
    }

    /**
     * 执行爬虫任务
     */
    public void taskRun() {
        try {

            Date batchTime = new Date();
            List<CrawlerKeyWordsInfo> crawlerKeyWordsInfoList =
                    commonService.findKeywordInfoList(CrawlerAttribute.AMAZON_CUST_ACCOUNT_ID, CrawlerAttribute.PLATFORM_AMAZON);
            for (CrawlerKeyWordsInfo keyWordsInfo : crawlerKeyWordsInfoList) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                String url = CrawlerAttribute.AMAZON_DE_URL + keyWordsInfo.getCustkeywordName() + "/";
                String key = CrawlerAttribute.AMAZON_TASK + "_" + url;
                paramMap.put(CrawlerAttribute.AMAZON_ITEM_ID, keyWordsInfo.getCustkeywordName());
                paramMap.put(CrawlerAttribute.CUST_KEY_WORD_ID, keyWordsInfo.getCustKeywordId());
                paramMap.put(CrawlerAttribute.GOODS_URL, url);
                paramMap.put(CrawlerAttribute.BATCH_TIME, batchTime);
                addSeed(url, key,paramMap);
            }
            setExecuteInterval(executeInterval);
            setThreads(threadsCount);
            /**
             * true表示支持断点爬取
             * 如果第一次爬取成功CrawlDatum的status=1，这样
             * 再次爬取时就不会进行，因为status=1说明上一次爬取成功
             */
            setResumable(false);
            setCrawlerTaskName(CrawlerAttribute.AMAZON_TASK);
            start(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据执行项目
     * 执行爬虫任务
     */
    public void taskRun(List<CrawlDatum> crawlDatumList, String databaseName) {
        try {
            BerkeleyDBManager dbManager = new BerkeleyDBManager();
            dbManager.setCrawlPath(databaseName);
            super.initCrawler(databaseName, false, dbManager, headerMap);
            for (CrawlDatum crawlDatum : crawlDatumList) {
                addSeed(crawlDatum.url(), crawlDatum.key(),crawlDatum.getMetaData());

            }
            setExecuteInterval(executeInterval);
            setThreads(threadsCount);
            setResumable(false);
            setCrawlerTaskName(CrawlerAttribute.AMAZON_TASK);
            start(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 具体业务实现
     * @param page
     * @param next
     */
    public void visit(Page page, CrawlDatums next) {
        LOG.info("find url : " + page.url() + " title: " + page.select("span[id=productTitle]").text());



    }

}
