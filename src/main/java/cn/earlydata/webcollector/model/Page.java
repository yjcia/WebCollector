/*
 * Copyright (C) 2014 hu
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
package cn.earlydata.webcollector.model;

import cn.earlydata.webcollector.model.net.HttpCrawResponse;
import cn.earlydata.webcollector.util.CharsetDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Page是爬取过程中，内存中保存网页爬取信息的一个容器，Page只在内存中存 放，用于保存一些网页信息，方便用户进行自定义网页解析之类的操作。
 *
 * @author hu
 */
public class Page {

    public static final Logger LOG = Logger.getLogger(Page.class);

    private CrawlDatum crawlDatum = null;

    private HttpCrawResponse response = null;
    private HttpResponse httpResponse = null;
    private Exception exception = null;

    private String html = null;
    private Document doc = null;

    private String charset = null;

    /**
     * 判断当前Page的URL是否和输入正则匹配
     *
     * @param urlRegex
     * @return
     */
    public boolean matchUrl(String urlRegex) {
        return Pattern.matches(urlRegex, url());
    }

    /**
     * 判断当前Page(CrawlDatum)的type是否为type
     * @param type
     * @return 是否相等
     */
    public boolean matchType(String type) {
        return crawlDatum.matchType(type);
    }
    
    /**
     * 判断当前Page的Http响应头的Content-Type是否符合正则
     * @param contentTypeRegex
     * @return 
     */
    public boolean matchContentType(String contentTypeRegex){
        if(contentTypeRegex==null){
            return contentType()==null;
        }
        return Pattern.matches(contentTypeRegex, contentType());
    }
    
     /**
     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
     * 例如通过getAttrs("img[src]","abs:src")可获取网页中所有图片的链接
     *
     * @param cssSelector
     * @param attrName
     * @return
     */
    public ArrayList<String> attrs(String cssSelector, String attrName) {
        ArrayList<String> result = new ArrayList<String>();
        Elements eles = select(cssSelector);
        for (Element ele : eles) {
            if (ele.hasAttr(attrName)) {
                result.add(ele.attr(attrName));
            }
        }
        return result;
    }


    /**
     * 获取满足选择器的元素中的链接 选择器cssSelector必须定位到具体的超链接 例如我们想抽取id为content的div中的所有超链接，这里
     * 就要将cssSelector定义为div[id=content] a
     *
     * @param cssSelector
     * @return
     */
    public Links getLinks(String cssSelector) {
        Links links = new Links().addBySelector(doc(), cssSelector);
        return links;
    }
    

    public Elements select(String cssSelector) {
        return this.doc().select(cssSelector);
    }

    public Element select(String cssSelector, int index) {
        Elements eles = select(cssSelector);
        int realIndex = index;
        if (index < 0) {
            realIndex = eles.size() + index;
        }
        return eles.get(realIndex);
    }

    public String regex(String regex, int group, String defaultResult) {
        Pattern pattern = Pattern.compile(regex);
        try {
            Matcher matcher = pattern.matcher(html());
            if (matcher.find()) {
                return matcher.group(group);
            } else {
                return defaultResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String regex(String regex, int group) {
        Pattern pattern = Pattern.compile(regex);
        try {
            Matcher matcher = pattern.matcher(html());
            matcher.find();
            return matcher.group(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String regex(String regex, String defaultResult) {
        return regex(regex, 0, defaultResult);
    }

    public String regex(String regex) {
        return regex(regex, 0);
    }

    public Page(CrawlDatum datum, HttpCrawResponse response) {
        this.crawlDatum = datum;
        this.response = response;
    }

    public Page(CrawlDatum datum, HttpResponse httpResponse) {
        this.crawlDatum = datum;
        this.httpResponse = httpResponse;
    }

    public long content() {
        if (httpResponse == null) {
            return 0;
        }
        return httpResponse.getEntity().getContentLength();
    }

    /**
     * 返回网页的url
     *
     * @return 网页的url
     */
    public String url() {
        return crawlDatum.url();
    }


    /**
     * 返回网页的源码字符串
     *
     * @return 网页的源码字符串
     */
    public String html() throws IOException {
        String httpResponseHtml = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        if (html != null) {
            return html;
        }
        
        if((html = httpResponseHtml)!=null){
            return html;
        }

        if (content() > 0) {
            return null;
        }

        return html;
    }

    /**
     * 设置网页的源码字符串
     *
     * @param html 网页的源码字符串
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * 返回网页解析后的DOM树(Jsoup的Document对象) 已废弃，使用doc()方法代替
     *
     * @return 网页解析后的DOM树
     */
    @Deprecated
    public Document getDoc() {
        return doc();
    }

    public List<String> header(String name) {
        return response.header(name);
    }

    public String contentType() {
        return response.contentType();
    }

    public Document doc() {
        if (doc != null) {
            return doc;
        }
        try {
            this.doc = Jsoup.parse(html(), url());
            return doc;
        } catch (Exception ex) {
            LOG.info("Exception", ex);
            return null;
        }
    }

    /**
     * 设置网页解析后的DOM树(Jsoup的Document对象)
     *
     * @param doc 网页解析后的DOM树
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }
    
    public HttpCrawResponse response() {
        return response;
    }

    public void response(HttpCrawResponse response) {
        this.response = response;
    }
    @Deprecated
    public HttpCrawResponse getResponse() {
        return response;
    }

    @Deprecated
    public void setResponse(HttpCrawResponse response) {
        this.response = response;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    
    public CrawlDatum crawlDatum() {
        return crawlDatum;
    }
    
    public void crawlDatum(CrawlDatum crawlDatum) {
        this.crawlDatum = crawlDatum;
    }

    public void meta(String key, String value) {
        this.crawlDatum.meta(key, value);
    }

    public Object meta(String key) {
        return this.crawlDatum.meta(key);
    }
    
    public String key(){
        return crawlDatum.key();
    }
    
    public int code() {
        return response.code();
    }




}
