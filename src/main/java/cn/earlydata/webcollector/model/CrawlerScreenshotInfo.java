package cn.earlydata.webcollector.model;

import java.util.Date;

/**
 * Created by Yanjun on 2017/04/26.
 */
public class CrawlerScreenshotInfo {
    private int id;
    private int custAccountId;
    private String screenshotType;
    private String screenshotId;
    private String skuid;
    private String screenshotUrl;
    private Date updateTime;
    private Date updateDate;
    private Date screenshotTime;
    private Date batchTime;
    private String channel;
    private String picUploadPath;

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

    public String getScreenshotType() {
        return screenshotType;
    }

    public void setScreenshotType(String screenshotType) {
        this.screenshotType = screenshotType;
    }

    public String getScreenshotId() {
        return screenshotId;
    }

    public void setScreenshotId(String screenshotId) {
        this.screenshotId = screenshotId;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
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

    public Date getScreenshotTime() {
        return screenshotTime;
    }

    public void setScreenshotTime(Date screenshotTime) {
        this.screenshotTime = screenshotTime;
    }

    public Date getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Date batchTime) {
        this.batchTime = batchTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPicUploadPath() {
        return picUploadPath;
    }

    public void setPicUploadPath(String picUploadPath) {
        this.picUploadPath = picUploadPath;
    }

    @Override
    public String toString() {
        return "CrawlerScreenshotInfo{" +
                "id=" + id +
                ", custAccountId=" + custAccountId +
                ", screenshotType='" + screenshotType + '\'' +
                ", screenshotId='" + screenshotId + '\'' +
                ", skuid='" + skuid + '\'' +
                ", screenshotUrl='" + screenshotUrl + '\'' +
                ", updateTime=" + updateTime +
                ", updateDate=" + updateDate +
                ", screenshotTime=" + screenshotTime +
                ", batchTime=" + batchTime +
                ", channel='" + channel + '\'' +
                '}';
    }
}
