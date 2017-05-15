package cn.earlydata.webcollector.model;

import java.util.Date;

/**
 * Created by Yanjun on 2017/04/25.
 */
public class CrawlerPriceInfo {
    private int id;
    private int custkeywordId;
    private String goodsId;
    private String skuId;
    private String channel;
    private String originalPrice;
    private String currentPrice;
    private String promotion;
    private Date updateTime;
    private Date updateDate;
    private Date batchTime;
    private String carnivalPrice;
    private String headPromotion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustkeywordId() {
        return custkeywordId;
    }

    public void setCustkeywordId(int custkeywordId) {
        this.custkeywordId = custkeywordId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Date batchTime) {
        this.batchTime = batchTime;
    }

    public String getCarnivalPrice() {
        return carnivalPrice;
    }

    public void setCarnivalPrice(String carnivalPrice) {
        this.carnivalPrice = carnivalPrice;
    }

    public String getHeadPromotion() {
        return headPromotion;
    }

    public void setHeadPromotion(String headPromotion) {
        this.headPromotion = headPromotion;
    }

    @Override
    public String toString() {
        return "CrawlerPriceInfo{" +
                "id=" + id +
                ", custkeywordId=" + custkeywordId +
                ", goodsId='" + goodsId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", channel='" + channel + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", promotion='" + promotion + '\'' +
                ", updateTime=" + updateTime +
                ", updateDate=" + updateDate +
                ", batchTime=" + batchTime +
                ", carnivalPrice='" + carnivalPrice + '\'' +
                ", headPromotion='" + headPromotion + '\'' +
                '}';
    }
}
