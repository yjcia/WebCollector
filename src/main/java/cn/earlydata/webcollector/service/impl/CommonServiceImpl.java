package cn.earlydata.webcollector.service.impl;


import cn.earlydata.webcollector.service.CommonService;
import cn.earlydata.webcollector.dao.DaoSupport;
import cn.earlydata.webcollector.model.CrawlerGoodsInfo;
import cn.earlydata.webcollector.model.CrawlerKeyWordsInfo;
import cn.earlydata.webcollector.model.CrawlerPriceInfo;
import cn.earlydata.webcollector.model.CrawlerScreenshotInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Yanjun on 2017/04/24.
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {

    private Logger log = Logger.getLogger(CommonServiceImpl.class);
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public synchronized void saveCrawlerGoodsInfoList(List<CrawlerGoodsInfo> crawlerGoodsInfoList){
        if(crawlerGoodsInfoList != null && crawlerGoodsInfoList.size() > 0){
            try {
                dao.batchSave("crawlerMapper.insertGoodsInfoList",crawlerGoodsInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            log.info(" ------ crawlerGoodsInfoList is empty ! ------ ");
        }

    }

    public synchronized void saveCrawlerGoodsInfo(CrawlerGoodsInfo info){
        if(info != null){
            try {
                dao.save("crawlerMapper.insertGoodsInfo",info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            log.info(" ------ info is empty ! ------ ");
        }

    }

    public synchronized void saveCrawlerPriceInfo(CrawlerPriceInfo crawlerPriceInfo){
        try {
            dao.save("crawlerMapper.insertGoodsPriceInfo",crawlerPriceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveCrawlerPriceInfoList(List<CrawlerPriceInfo> crawlerPriceInfoList){
        if(crawlerPriceInfoList != null && crawlerPriceInfoList.size() > 0){
            try {
                dao.batchSave("crawlerMapper.insertGoodsPriceInfoList",crawlerPriceInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            log.info(" ------ crawlerPriceInfoList is empty ! ------ ");
        }

    }

    public synchronized void saveCrawlerScreenInfoList(List<CrawlerScreenshotInfo> crawlerScreenshotInfoList){
        if(crawlerScreenshotInfoList != null && crawlerScreenshotInfoList.size() > 0){
            try {
                dao.batchSave("crawlerMapper.insertScreenshotList",crawlerScreenshotInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            log.info(" ------ crawlerScreenshotInfoList is empty ! ------ ");
        }
    }

    public synchronized void saveCrawlerScreenInfo(CrawlerScreenshotInfo crawlerScreenshotInfo){
        try {
            dao.save("crawlerMapper.insertScreenshot",crawlerScreenshotInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CrawlerKeyWordsInfo> findKeywordInfoList(int custAccountId, String platformName){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("custAccountId",custAccountId);
        paramMap.put("platformName",platformName);
        List<CrawlerKeyWordsInfo> keyWordsInfoList = new ArrayList<CrawlerKeyWordsInfo>();
        try {
            keyWordsInfoList = (ArrayList<CrawlerKeyWordsInfo>)
                    dao.findForList("crawlerMapper.findKeywordId",paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyWordsInfoList;
    }

    public void mergeOldGoodsInfoData(String platformNameEn,int custAccountId){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("platformNameEn",platformNameEn);
        paramMap.put("custAccountId",custAccountId);
        try {
            dao.save("crawlerMapper.mergeToGoodsInfoHistory",paramMap);
            log.info("merge old goods info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mergeOldPriceInfoData(String platformNameEn,int custAccountId){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("platformNameEn",platformNameEn);
        paramMap.put("custAccountId",custAccountId);
        try {
            dao.save("crawlerMapper.mergeToGoodsPriceInfoHistory",paramMap);
            log.info("merge old price info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mergeOldGoodsPicData(String platformNameEn,int custAccountId){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("platformNameEn",platformNameEn);
        paramMap.put("custAccountId",custAccountId);
        try {
            dao.save("crawlerMapper.mergeToScreenshotInfoHistory",paramMap);
            log.info("merge old screenshot info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteGoodsInfoData(String platformNameEn,int custAccountId){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("platformNameEn",platformNameEn);
        paramMap.put("custAccountId",custAccountId);
        try {
            dao.delete("crawlerMapper.cleanGoodsInfo",paramMap);
            log.info("delete old goods info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void deletePriceInfoData(String platformNameEn,int custAccountId){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("platformNameEn",platformNameEn);
        paramMap.put("custAccountId",custAccountId);
        try {
            dao.delete("crawlerMapper.cleanGoodsPriceInfo",paramMap);
            log.info("delete old price info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String findCustKeyWordIdByName(String keywordName){
        String custKeywordId = "";
        try {
            custKeywordId = (String)dao.findForObject("crawlerMapper.findCustKeyWordIdByName",keywordName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return custKeywordId;
    }

}
