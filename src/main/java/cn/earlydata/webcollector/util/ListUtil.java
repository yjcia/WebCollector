package cn.earlydata.webcollector.util;




import cn.earlydata.webcollector.model.CrawlerKeyWordsInfo;

import java.util.List;

/**
 * Created by Yanjun on 2017/04/28.
 */
public class ListUtil {

    public static List<CrawlerKeyWordsInfo> copyCrawlerKeywordInfoList(
            List<CrawlerKeyWordsInfo> targetList, List<CrawlerKeyWordsInfo> sourceList){
        for(int i=0;i<sourceList.size();i++){
            targetList.add(sourceList.get(i));
        }
        return targetList;
    }
}
