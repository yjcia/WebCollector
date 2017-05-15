package cn.earlydata.webcollector.model;

import java.util.Date;

/**
 * Created by Yanjun on 2017/04/25.
 */
public class CrawlerGoodsInfo {
    private int id;
    private int custKeywordId;
    private int custAccountId;
    private String goodsId;
    private String egoodsId;
    private String message;
    private String platformGoodsName;
    private String platformNameEn;
    private String platformCategory;
    private String platformSellerId;
    private String platformSellerName;
    private String platformShopId;
    private String platformShopName;
    private String platformShopType;
    private String deliveryInfo;
    private String deliveryPlace;
    private String sellerLocation;
    private int goodsStatus;
    private String inventory;
    private String saleQty;
    private String ttlCommentNum;
    private String posCommentNum;
    private String negCommentNum;
    private String neuCommentNum;
    private String goodsUrl;
    private String goodsPicUrl;
    private Date updateTime;
    private Date updateDate;
    private String feature;
    private Date batchTime;
    private String deposit;
    private String toUseAmount;
    private String reserveNum;
    private String priceStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustKeywordId() {
        return custKeywordId;
    }

    public void setCustKeywordId(int custKeywordId) {
        this.custKeywordId = custKeywordId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getEgoodsId() {
        return egoodsId;
    }

    public void setEgoodsId(String egoodsId) {
        this.egoodsId = egoodsId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlatformGoodsName() {
        return platformGoodsName;
    }

    public void setPlatformGoodsName(String platformGoodsName) {
        this.platformGoodsName = platformGoodsName;
    }

    public String getPlatformNameEn() {
        return platformNameEn;
    }

    public void setPlatformNameEn(String platformNameEn) {
        this.platformNameEn = platformNameEn;
    }

    public String getPlatformCategory() {
        return platformCategory;
    }

    public void setPlatformCategory(String platformCategory) {
        this.platformCategory = platformCategory;
    }

    public String getPlatformSellerId() {
        return platformSellerId;
    }

    public void setPlatformSellerId(String platformSellerId) {
        this.platformSellerId = platformSellerId;
    }

    public String getPlatformSellerName() {
        return platformSellerName;
    }

    public void setPlatformSellerName(String platformSellerName) {
        this.platformSellerName = platformSellerName;
    }

    public String getPlatformShopId() {
        return platformShopId;
    }

    public void setPlatformShopId(String platformShopId) {
        this.platformShopId = platformShopId;
    }

    public String getPlatformShopName() {
        return platformShopName;
    }

    public void setPlatformShopName(String platformShopName) {
        this.platformShopName = platformShopName;
    }

    public String getPlatformShopType() {
        return platformShopType;
    }

    public void setPlatformShopType(String platformShopType) {
        this.platformShopType = platformShopType;
    }

    public String getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(String deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public String getSellerLocation() {
        return sellerLocation;
    }

    public void setSellerLocation(String sellerLocation) {
        this.sellerLocation = sellerLocation;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }

    public String getTtlCommentNum() {
        return ttlCommentNum;
    }

    public void setTtlCommentNum(String ttlCommentNum) {
        this.ttlCommentNum = ttlCommentNum;
    }

    public String getPosCommentNum() {
        return posCommentNum;
    }

    public void setPosCommentNum(String posCommentNum) {
        this.posCommentNum = posCommentNum;
    }

    public String getNegCommentNum() {
        return negCommentNum;
    }

    public void setNegCommentNum(String negCommentNum) {
        this.negCommentNum = negCommentNum;
    }

    public String getNeuCommentNum() {
        return neuCommentNum;
    }

    public void setNeuCommentNum(String neuCommentNum) {
        this.neuCommentNum = neuCommentNum;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getGoodsPicUrl() {
        return goodsPicUrl;
    }

    public void setGoodsPicUrl(String goodsPicUrl) {
        this.goodsPicUrl = goodsPicUrl;
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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Date getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Date batchTime) {
        this.batchTime = batchTime;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getToUseAmount() {
        return toUseAmount;
    }

    public void setToUseAmount(String toUseAmount) {
        this.toUseAmount = toUseAmount;
    }

    public String getReserveNum() {
        return reserveNum;
    }

    public void setReserveNum(String reserveNum) {
        this.reserveNum = reserveNum;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public int getCustAccountId() {
        return custAccountId;
    }

    public void setCustAccountId(int custAccountId) {
        this.custAccountId = custAccountId;
    }

    @Override
    public String toString() {
        return "CrawlerGoodsInfo{" +
                "id=" + id +
                ", custKeywordId=" + custKeywordId +
                ", custAccountId=" + custAccountId +
                ", goodsId='" + goodsId + '\'' +
                ", egoodsId='" + egoodsId + '\'' +
                ", message='" + message + '\'' +
                ", platformGoodsName='" + platformGoodsName + '\'' +
                ", platformNameEn='" + platformNameEn + '\'' +
                ", platformCategory='" + platformCategory + '\'' +
                ", platformSellerId='" + platformSellerId + '\'' +
                ", platformSellerName='" + platformSellerName + '\'' +
                ", platformShopId='" + platformShopId + '\'' +
                ", platformShopName='" + platformShopName + '\'' +
                ", platformShopType='" + platformShopType + '\'' +
                ", deliveryInfo='" + deliveryInfo + '\'' +
                ", deliveryPlace='" + deliveryPlace + '\'' +
                ", sellerLocation='" + sellerLocation + '\'' +
                ", goodsStatus=" + goodsStatus +
                ", inventory='" + inventory + '\'' +
                ", saleQty='" + saleQty + '\'' +
                ", ttlCommentNum='" + ttlCommentNum + '\'' +
                ", posCommentNum='" + posCommentNum + '\'' +
                ", negCommentNum='" + negCommentNum + '\'' +
                ", neuCommentNum='" + neuCommentNum + '\'' +
                ", goodsUrl='" + goodsUrl + '\'' +
                ", goodsPicUrl='" + goodsPicUrl + '\'' +
                ", updateTime=" + updateTime +
                ", updateDate=" + updateDate +
                ", feature='" + feature + '\'' +
                ", batchTime=" + batchTime +
                ", deposit='" + deposit + '\'' +
                ", toUseAmount='" + toUseAmount + '\'' +
                ", reserveNum='" + reserveNum + '\'' +
                ", priceStr='" + priceStr + '\'' +
                '}';
    }
}
