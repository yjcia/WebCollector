package cn.earlydata.webcollector.service;

import cn.earlydata.webcollector.model.CrawlerGoodsInfo;
import cn.earlydata.webcollector.model.CrawlerKeyWordsInfo;
import cn.earlydata.webcollector.model.CrawlerPriceInfo;
import cn.earlydata.webcollector.model.CrawlerScreenshotInfo;

import java.util.List;

/**
 * Created by Yanjun on 2017/05/12.
 */
public interface CommonService {

    void saveCrawlerGoodsInfoList(List<CrawlerGoodsInfo> crawlerGoodsInfoList);
    void saveCrawlerGoodsInfo(CrawlerGoodsInfo info);
    void saveCrawlerPriceInfo(CrawlerPriceInfo crawlerPriceInfo);
    void saveCrawlerPriceInfoList(List<CrawlerPriceInfo> crawlerPriceInfoList);
    void saveCrawlerScreenInfoList(List<CrawlerScreenshotInfo> crawlerScreenshotInfoList);
    void saveCrawlerScreenInfo(CrawlerScreenshotInfo crawlerScreenshotInfo);
    List<CrawlerKeyWordsInfo> findKeywordInfoList(int custAccountId, String platformName);
    void mergeOldGoodsInfoData(String platformNameEn,int custAccountId);
    void mergeOldPriceInfoData(String platformNameEn,int custAccountId);
    void mergeOldGoodsPicData(String platformNameEn,int custAccountId);
    void deleteGoodsInfoData(String platformNameEn,int custAccountId);
    void deletePriceInfoData(String platformNameEn,int custAccountId);
    String findCustKeyWordIdByName(String keywordName);


}
