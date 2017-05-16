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
package cn.earlydata.webcollector.core.crawler;

import cn.earlydata.webcollector.core.framework.Executor;
import cn.earlydata.webcollector.core.framework.Visitor;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.model.CrawlDatums;
import cn.earlydata.webcollector.model.Links;
import cn.earlydata.webcollector.model.net.HttpRequest;
import cn.earlydata.webcollector.model.Page;
import cn.earlydata.webcollector.model.net.HttpCrawResponse;
import cn.earlydata.webcollector.model.net.Requester;
import cn.earlydata.webcollector.util.HttpCrawlerUtil;
import cn.earlydata.webcollector.util.RegexRule;
import org.apache.http.HttpResponse;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.util.Map;

public abstract class AutoParseCrawler extends Crawler implements Executor, Visitor, Requester {

    public static final Logger LOG = Logger.getLogger(AutoParseCrawler.class);

    protected boolean autoParse = true;
    protected boolean parseImg = false;
    protected Visitor visitor;
    protected Requester requester;
    protected RegexRule regexRule = new RegexRule();
    protected Map<String,String> headerMap;
    public AutoParseCrawler() {
    }

    public void initCrawler(boolean autoParse,Map<String,String> headerMap) {
        this.autoParse = autoParse;
        this.visitor = this;
        this.requester = this;
        this.executor = this;
        this.headerMap = headerMap;
    }

    /**
     * 针对框架原先获取Response的方式
     *
     * @param crawlDatum
     * @return
     * @throws Exception
     */
    public HttpCrawResponse getResponse(CrawlDatum crawlDatum) throws Exception {
        HttpRequest request = new HttpRequest(crawlDatum);
        return request.response();
    }

    /**
     * 针对HttpClient获取response的方式
     * @param crawlDatum
     * @return
     * @throws Exception
     */
    public HttpResponse getHttpResponse(CrawlDatum crawlDatum,Map<String,String> headerMap) throws Exception {
        HttpCrawlerUtil.init(headerMap);
        return HttpCrawlerUtil.getHttpResponse(HttpCrawlerUtil.getConnection(), crawlDatum.url());
    }

    public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
        long sleepTime = 2000L;
        Thread.sleep(sleepTime);
        HttpResponse response = requester.getHttpResponse(datum,headerMap);
        Page page = new Page(datum, response);
        visitor.visit(page, next);
        if (autoParse && !regexRule.isEmpty()) {
            parseLink(page, next);
        }
        afterParse(page, next);
    }

    protected void afterParse(Page page, CrawlDatums next) {
    }

    protected void parseLink(Page page, CrawlDatums next) {
        String contentType = page.contentType();
        if (contentType != null && contentType.contains("text/html")) {
            Document doc = page.doc();
            if (doc != null) {
                Links links = new Links().addByRegex(doc, regexRule, parseImg);
                next.add(links);
            }
        }
    }

    public void addRegex(String urlRegex) {
        regexRule.addRule(urlRegex);
    }

    public boolean isAutoParse() {
        return autoParse;
    }

    public void setAutoParse(boolean autoParse) {
        this.autoParse = autoParse;
    }

    public RegexRule getRegexRule() {
        return regexRule;
    }

    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Requester getRequester() {
        return requester;
    }

    public void setRequester(Requester requester) {
        this.requester = requester;
    }

    public boolean isParseImg() {
        return parseImg;
    }

    public void setParseImg(boolean parseImg) {
        this.parseImg = parseImg;
    }


}
