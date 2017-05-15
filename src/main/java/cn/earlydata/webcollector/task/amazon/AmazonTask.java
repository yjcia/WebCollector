package cn.earlydata.webcollector.task.amazon;

import cn.earlydata.webcollector.common.CrawlerAttribute;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.model.CrawlDatums;
import cn.earlydata.webcollector.model.CrawlerKeyWordsInfo;
import cn.earlydata.webcollector.model.Page;
import cn.earlydata.webcollector.core.crawler.BreadthCrawler;
import cn.earlydata.webcollector.plugin.berkeley.BerkeleyDBManager;
import cn.earlydata.webcollector.service.CommonService;
import com.sleepycat.je.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yanjun on 2017/05/11.
 */
@Controller
public class AmazonTask extends BreadthCrawler{

    @Autowired
    private CommonService commonService;


    public AmazonTask() {}

    /**
     * 爬虫任务初始化
     * @param crawlPath
     * @param autoParse
     */
    public void taskInit(String crawlPath, boolean autoParse) {
        super.initCrawler(crawlPath, autoParse);
    }

    /**
     * 执行爬虫任务
     */
    public void taskRun(){
        try {
            Date batchTime = new Date();
            List<CrawlerKeyWordsInfo> crawlerKeyWordsInfoList =
                    commonService.findKeywordInfoList(CrawlerAttribute.AMAZON_CUST_ACCOUNT_ID,CrawlerAttribute.PLATFORM_AMAZON);
            for(CrawlerKeyWordsInfo keyWordsInfo:crawlerKeyWordsInfoList){
                Map<String,Object> paramMap = new HashMap<String,Object>();
                String url = CrawlerAttribute.AMAZON_DE_URL + keyWordsInfo.getCustkeywordName() + "/";
                String key = CrawlerAttribute.PLATFORM_AMAZON + "_" + url;
                paramMap.put(CrawlerAttribute.AMAZON_ITEM_ID,keyWordsInfo.getCustkeywordName());
                paramMap.put(CrawlerAttribute.CUST_KEY_WORD_ID, keyWordsInfo.getCustKeywordId());
                paramMap.put(CrawlerAttribute.GOODS_URL, url);
                paramMap.put(CrawlerAttribute.BATCH_TIME,batchTime);
                addSeed(url,key,paramMap);
                //break;
            }
            setExecuteInterval(1000);
            setThreads(10);
            /**
             * true表示支持断点爬取
             * 如果第一次爬取成功CrawlDatum的status=1，这样
             * 再次爬取时就不会进行，因为status=1说明上一次爬取成功
             */
            setResumable(false);
            start(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void visit(Page page, CrawlDatums next) {
        System.out.println("find url : " + page.url());
        //假设爬取失败
        page.crawlDatum().setCrawlSuccess(false);
        //errorNext.add("https://www.amazon.de/dp/B01DFKC22A/");
    }

}
