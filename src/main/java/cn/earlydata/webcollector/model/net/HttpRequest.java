/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.earlydata.webcollector.model.net;

import cn.earlydata.webcollector.common.CrawlerAttribute;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.common.ConfigAttribute;
import cn.earlydata.webcollector.util.PropertiesUtil;
import com.virjar.dungproxy.client.ippool.IpPool;
import com.virjar.dungproxy.client.model.AvProxy;
import com.virjar.dungproxy.client.util.CommonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpRequest {

    public static final Logger LOG = Logger.getLogger(HttpRequest.class);
    protected String method = PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.DEFAULT_HTTP_METHOD);
    protected int MAX_REDIRECT = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.MAX_REDIRECT));
    protected int MAX_RECEIVE_SIZE = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.MAX_RECEIVE_SIZE));
    protected int timeoutForConnect = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.TIMEOUT_CONNECT));
    protected int timeoutForRead = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.TIMEOUT_READ));
    protected byte[] outputData = null;
    protected boolean doinput = true;
    protected boolean dooutput = true;
    protected boolean followRedirects = false;
    protected Proxy proxy = null;
    protected Map<String, String> headerMap = null;
    protected CrawlDatum crawlDatum = null;
    protected IpPool ipPool = IpPool.getInstance();

    static {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ex) {
            LOG.info("Exception", ex);
        }
    }

    public HttpRequest(String url) throws Exception {
        this.crawlDatum = new CrawlDatum(url);
        setUserAgent(CrawlerAttribute.DEFAULT_USER_AGENT);
    }

    public HttpRequest(String url, Proxy proxy) throws Exception {
        this(url);
        this.proxy = proxy;
    }

    public HttpRequest(CrawlDatum crawlDatum) throws Exception {
        this.crawlDatum = crawlDatum;
        setUserAgent(CrawlerAttribute.DEFAULT_USER_AGENT);
    }

    public HttpRequest(CrawlDatum crawlDatum, Proxy proxy) throws Exception {
        this(crawlDatum);
        this.proxy = proxy;
    }

    public HttpCrawResponse response() throws Exception {
        String urlString = crawlDatum.url();
        URL url = new URL(urlString);
        HttpCrawResponse response = new HttpCrawResponse(url);
        int code = -1;
        int maxRedirect = Math.max(0, MAX_REDIRECT);
        HttpURLConnection con = null;
        InputStream is = null;
        AvProxy bind = null;
        try {
            for (int redirect = 0; redirect <= maxRedirect; redirect++) {
                if (proxy != null) {
                    con = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    bind = ipPool.bind(CommonUtil.extractDomain(urlString), urlString);
                    if (bind != null && crawlDatum.isNeedAutoProxy()) {
                        bind.recordUsage();
                        con = (HttpURLConnection) url.openConnection(
                                new Proxy(Proxy.Type.HTTP, new InetSocketAddress(bind.getIp(), bind.getPort())));
                    } else {
                        con = (HttpURLConnection) url.openConnection();
                    }
                }

                config(con);

                if (outputData != null) {
                    OutputStream os = con.getOutputStream();
                    os.write(outputData);
                    os.close();
                }

                code = con.getResponseCode();
                /*只记录第一次返回的code*/
                if (redirect == 0) {
                    response.code(code);
                }

                if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                    response.setNotFound(true);
                    return response;
                }

                boolean needBreak = false;
                switch (code) {

                    case HttpURLConnection.HTTP_MOVED_PERM:
                    case HttpURLConnection.HTTP_MOVED_TEMP:
                        response.setRedirect(true);
                        if (redirect == MAX_REDIRECT) {
                            throw new Exception("redirect to much time");
                        }
                        String location = con.getHeaderField("Location");
                        if (location == null) {
                            throw new Exception("redirect with no location");
                        }
                        String originUrl = url.toString();
                        url = new URL(url, location);
                        response.setRealUrl(url);
                        LOG.info("redirect from " + originUrl + " to " + url.toString());
                        continue;
                    default:
                        needBreak = true;
                        break;
                }
                if (needBreak) {
                    break;
                }

            }

            is = con.getInputStream();
            String contentEncoding = con.getContentEncoding();
            if (contentEncoding != null && contentEncoding.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            byte[] buf = new byte[2048];
            int read;
            int sum = 0;
            int maxsize = MAX_RECEIVE_SIZE;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((read = is.read(buf)) != -1) {
                if (maxsize > 0) {
                    sum = sum + read;

                    if (maxsize > 0 && sum > maxsize) {
                        read = maxsize - (sum - read);
                        bos.write(buf, 0, read);
                        break;
                    }
                }
                bos.write(buf, 0, read);
            }

            response.content(bos.toByteArray());
            response.headers(con.getHeaderFields());
            bos.close();

            return response;
        } catch (Exception ex) {
            if (bind != null) {
                bind.recordFailed();// 当前不判断那种类型异常导致下线
            }
            throw ex;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public void config(HttpURLConnection con) throws Exception {

        con.setRequestMethod(method);
        con.setInstanceFollowRedirects(followRedirects);
        con.setDoInput(doinput);
        con.setDoOutput(dooutput);
        con.setConnectTimeout(timeoutForConnect);
        con.setReadTimeout(timeoutForRead);

        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {

                con.addRequestProperty(entry.getKey(), entry.getValue());
            }

        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public CrawlDatum getCrawlDatum() {
        return crawlDatum;
    }

    public void setCrawlDatum(CrawlDatum crawlDatum) {
        this.crawlDatum = crawlDatum;
    }

    private void initHeaderMap() {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
    }

    public void setUserAgent(String userAgent) {
        setHeader("User-Agent", userAgent);
    }

    public void setCookie(String cookie) {
        setHeader("Cookie", cookie);
    }

    public void removeHeader(String key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        if (headerMap != null) {
            headerMap.remove(key);
        }
    }

    public void setHeader(String key, String value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        initHeaderMap();
        headerMap.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headerMap;
    }

    public String getHeader(String key) {
        if (headerMap == null) {
            return null;
        }
        return headerMap.get(key);
    }

    public boolean isDoinput() {
        return doinput;
    }

    public void setDoinput(boolean doinput) {
        this.doinput = doinput;
    }

    public boolean isDooutput() {
        return dooutput;
    }

    public void setDooutput(boolean dooutput) {
        this.dooutput = dooutput;
    }

    public int getTimeoutForConnect() {
        return timeoutForConnect;
    }

    public void setTimeoutForConnect(int timeoutForConnect) {
        this.timeoutForConnect = timeoutForConnect;
    }

    public int getTimeoutForRead() {
        return timeoutForRead;
    }

    public void setTimeoutForRead(int timeoutForRead) {
        this.timeoutForRead = timeoutForRead;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public byte[] getOutputData() {
        return outputData;
    }

    public void setOutputData(byte[] outputData) {
        this.outputData = outputData;
        this.dooutput = true;
    }


}
