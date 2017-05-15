package cn.earlydata.webcollector.util;


import cn.earlydata.webcollector.common.CrawlerAttribute;
import cn.earlydata.webcollector.model.CrawlerGoodsInfo;

/**
 * Created by Yanjun on 2017/04/26.
 */
public class AmazonUtil {

    public static String formatEurPrice(String eurPrice){
        String priceTemp = eurPrice.substring(eurPrice.indexOf("EUR ") + 4);
        if(priceTemp.indexOf(".") > -1){
            priceTemp = priceTemp.replaceAll("\\.","");
        }
        return (priceTemp.replaceAll(",","."));
    }

    /**
     * 获得amazon url的itemid信息
     * @param url
     * @return
     */
    public static String getAmazonItemId(String url){
        String itemId = null;
        int dpIndex = url.indexOf("dp/");
        int dbEndFlagIndex = url.substring(dpIndex + 3).indexOf("/");
        if(dbEndFlagIndex == 0){
            itemId = url.substring(dpIndex + 3);
        }else{
            itemId = url.substring(dpIndex + 3,dpIndex + 3 + dbEndFlagIndex);
        }
        return itemId;
    }

    public static String imgSrcUrlBak(String str){
        return "https:" + str.split(":")[1].split("\"")[0];
    }

    public static String getImgUploadFileName(CrawlerGoodsInfo goodsInfo){
        String uploadFileName = "/" + CrawlerAttribute.PLATFORM_AMAZON
                + "/" + goodsInfo.getCustAccountId() + CrawlerAttribute.PIC_PATH
                + DateUtil.getSdfTimes()+ "/" + CrawlerAttribute.CHANNEL_PC + "/" +
                goodsInfo.getEgoodsId() +".png";
        return uploadFileName;
    }

    public static String getUploadPath(CrawlerGoodsInfo goodsInfo){
        String uploadPath = "/" + CrawlerAttribute.PLATFORM_AMAZON
                + "/" + goodsInfo.getCustAccountId() + CrawlerAttribute.PIC_PATH
                + DateUtil.getSdfTimes()+ "/" + CrawlerAttribute.CHANNEL_PC;
        return uploadPath;
    }

    public static String optItemName(String itemName){
        if(itemName.length() > 200){
            return itemName.substring(0,200);
        }
        return itemName;
    }
}
