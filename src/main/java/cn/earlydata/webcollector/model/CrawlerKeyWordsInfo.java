package cn.earlydata.webcollector.model;

import java.util.Date;

/**
 * Created by Yanjun on 2017/04/25.
 */
public class CrawlerKeyWordsInfo {
    private int id;
    private int custAccountId;
    private String custAccountName;
    private String platformName;
    private String custKeywordId;
    private String custkeywordName;
    private String custKeywordType;
    private String feature;
    private int crawlingStatus;
    private String crawlingFre;
    private Date updateTime;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustAccountId() {
        return custAccountId;
    }

    public void setCustAccountId(int custAccountId) {
        this.custAccountId = custAccountId;
    }

    public String getCustAccountName() {
        return custAccountName;
    }

    public void setCustAccountName(String custAccountName) {
        this.custAccountName = custAccountName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getCustKeywordId() {
        return custKeywordId;
    }

    public void setCustKeywordId(String custKeywordId) {
        this.custKeywordId = custKeywordId;
    }

    public String getCustkeywordName() {
        return custkeywordName;
    }

    public void setCustkeywordName(String custkeywordName) {
        this.custkeywordName = custkeywordName;
    }

    public String getCustKeywordType() {
        return custKeywordType;
    }

    public void setCustKeywordType(String custKeywordType) {
        this.custKeywordType = custKeywordType;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getCrawlingStatus() {
        return crawlingStatus;
    }

    public void setCrawlingStatus(int crawlingStatus) {
        this.crawlingStatus = crawlingStatus;
    }

    public String getCrawlingFre() {
        return crawlingFre;
    }

    public void setCrawlingFre(String crawlingFre) {
        this.crawlingFre = crawlingFre;
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

    @Override
    public String toString() {
        return "CrawlerKeyWordsInfo{" +
                "id=" + id +
                ", custAccountId=" + custAccountId +
                ", custAccountName='" + custAccountName + '\'' +
                ", platformName='" + platformName + '\'' +
                ", custKeywordId='" + custKeywordId + '\'' +
                ", custkeywordName='" + custkeywordName + '\'' +
                ", custKeywordType='" + custKeywordType + '\'' +
                ", feature='" + feature + '\'' +
                ", crawlingStatus=" + crawlingStatus +
                ", crawlingFre='" + crawlingFre + '\'' +
                ", updateTime=" + updateTime +
                ", updateDate=" + updateDate +
                '}';
    }
}
