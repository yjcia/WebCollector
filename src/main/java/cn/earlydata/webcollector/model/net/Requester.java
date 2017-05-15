
package cn.earlydata.webcollector.model.net;

import cn.earlydata.webcollector.model.CrawlDatum;
import org.apache.http.HttpResponse;

public interface Requester {
     public HttpCrawResponse getResponse(CrawlDatum crawlDatum) throws Exception;
     public HttpResponse getHttpResponse(CrawlDatum crawlDatum) throws Exception;
}
