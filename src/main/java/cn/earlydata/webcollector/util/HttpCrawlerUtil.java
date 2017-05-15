package cn.earlydata.webcollector.util;

import cn.earlydata.webcollector.common.CrawlerAttribute;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Random;

/**
 * Created by Yanjun on 2017/04/19.
 */
public class HttpCrawlerUtil {
    private static Logger log = Logger.getLogger(HttpCrawlerUtil.class);
    private static PoolingHttpClientConnectionManager connManager;
    private static final int TIMEOUT_VALUE = 70000;
    /**
     * SSL处理
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }

            public void checkServerTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    /**
     * 初始化httpclientpool
     * @return
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static void init()
    {
        SSLContext sslcontext = null;
        try {
            sslcontext = HttpCrawlerUtil.createIgnoreVerifySSL();
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext))
                    .build();
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connManager.setDefaultMaxPerRoute(20);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得HttpClient
     * @return
     */
    public static CloseableHttpClient getConnection(){
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIMEOUT_VALUE)
                .setConnectTimeout(TIMEOUT_VALUE).setSocketTimeout(TIMEOUT_VALUE).build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();
        if(connManager != null && connManager.getTotalStats()!= null)
        {
            log.info("now client pool " + connManager.getTotalStats().toString());
        }
        return httpClient;
    }

    /**
     * 获得HttpGet
     * @param url
     * @return
     */
    public static HttpGet getHttpGet(String url){
        HttpGet httpGet = new HttpGet(url);
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_VALUE)
                .setConnectTimeout(TIMEOUT_VALUE)
                .setConnectionRequestTimeout(TIMEOUT_VALUE)
                .build();

        RequestConfig config = RequestConfig.copy(defaultRequestConfig).build();
        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", CrawlerAttribute.DEFAULT_USER_AGENT);
        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        httpGet.setHeader("Connection", "keep-alive");
        return httpGet;
    }
    /**
     * 获得请求地址参数列表
     * @param paramUrl
     * @param paramKey
     * @return
     */
    public static String getParamValue(String paramUrl, String paramKey) {
        int indexNum = paramUrl.indexOf("?");
        String result = "";
        String paramPart = paramUrl.substring(indexNum + 1);
        String[] parmArr = paramPart.split("&");
        for (String paramStr : parmArr) {
            String key = paramStr.split("=")[0];
            String value = paramStr.split("=")[1];
            if (key != null && !"".equals(key) && key.equals(paramKey)) {
                result = value;
                break;
            }
        }
        return result;
    }

    /**
     * 判断元素是否存在
     * @param driver
     * @param locator
     * @return
     */
    public static boolean checkElementExist(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }





    /**
     * 得到httpclient请求返回的html
     * @param closeableHttpClient
     * @param url
     * @return
     * @throws IOException
     */
    public static String getEntityHtmlStr(CloseableHttpClient closeableHttpClient,String url) throws IOException {
        HttpGet httpGet = HttpCrawlerUtil.getHttpGet(url);
        HttpResponse response = closeableHttpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        String httpEntityHtml = EntityUtils.toString(httpEntity, "UTF-8");
        httpGet.abort();
        return httpEntityHtml;
    }

    public static HttpResponse getHttpResponse(CloseableHttpClient closeableHttpClient,String url) throws IOException {
        HttpGet httpGet = HttpCrawlerUtil.getHttpGet(url);
        HttpResponse response = closeableHttpClient.execute(httpGet);
        httpGet.abort();
        return response;
    }

    /**
     * 判断页面的某个节点是否存在子节点
     * @param element
     * @return
     */
    public static boolean checkElementChildrenSize(Element element){
        return element != null && element.children().size() > 0;
    }

    /**
     * 判断节点是否存在
     * @return
     */
    public static boolean checkElementExist(Element element){
        return element != null;
    }

}
