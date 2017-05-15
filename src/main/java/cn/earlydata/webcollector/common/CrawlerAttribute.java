package cn.earlydata.webcollector.common;

/**
 * Created by Yanjun on 2017/04/18.
 */
public class CrawlerAttribute {
    public static final String CHROME_WEBDRIVER = "webdriver.chrome.driver";
    public static final String CHROME_DRIVER_PATH
            = "D:\\code\\SeleniumCrawler\\src\\main\\resources\\chromedriver.exe";
    public static final String TMALL_ITEM_URL = "https://detail.tmall.com/item.htm?id=";
    public static final String TMALL_M_ITEM_URL = "https://detail.m.tmall.com/item.htm?id=";
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";
    public static final String PROXY_IP_URL = "http://www.ip-adress.com/proxy_list/?k=name";
    public static final String TEST_PROXY_IP_URL = "https://www.tmall.com/";
    public static final String TEST_PROXY_IP_URL2 = "https://chaoshi.detail.tmall.com/item.htm?id=";
    public static final int PROXYIP_DANGER_TIMES = 5;
    public static final int PROXYIP_ERROR_TIMES = 10;
    public static final String PHANTOMJS_PATH = "D:\\develop\\tool\\phantomjs\\bin\\phantomjs.exe ";
    public static final String PHANTOMJS_SCRIPT = " D:\\code\\SeleniumCrawler\\src\\main\\resources\\phantom_screenshot.js ";
    public static final String PHANTOMJS_PATH_NAME = "phantomjs.binary.path";
    public static final String PLATFORM_TMALL = "Tmall";
    public static final String DEFAULT_DATASOURCE = "dataSource1";
    public static final String STATUS_ON = "1"; // 上架状态
    public static final String STATUS_OFF = "0";// 下架状态
    public static final String PLATFORM_AMAZON = "amazon";
    public static final String CUST_KEY_WORD_ID = "custKeyWordId";
    public static final String AMAZON_ITEM_ID = "amazonItemId";
    public static final String AMAZON_SHOP_ID = "5";
    public static final String AMAZON_SHOP_TYPE = "自营";
    public static final String AMAZON_CATECODE = "2077416031";
    public static final String GOODS_PIC_URL = "goodsPicUrl";
    public static final String GOODS_URL = "goodsUrl";
    public static final String AMAZON_DE_URL = "https://www.amazon.de/dp/";
    public static final String CHANNEL_PC = "PC";
    public static final String CHANNEL_MOBILE = "MOBILE";
    public static final int AMAZON_CUST_ACCOUNT_ID = 13;
    public static final String SCREENSHOT_TYPE = "SKU";
    public static final String PIC_PATH = "/goodsPic/";
    public static final String IMAGE_JPG = ".jpg";
    public static final String IMAGE_PNG = ".png";
    public static final String FTP_IP_ADDRESS = "101.231.74.130";
    public static final int FTP_PORT = 21;
    public static final String FTP_USERNAME = "admin";
    public static final String FTP_PASSWORD = "infopower2016";
    public static final String BATCH_TIME = "batch_time";

}
