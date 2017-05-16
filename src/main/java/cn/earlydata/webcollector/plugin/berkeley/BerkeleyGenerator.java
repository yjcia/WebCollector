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
package cn.earlydata.webcollector.plugin.berkeley;

import cn.earlydata.webcollector.core.framework.Generator;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.common.ConfigAttribute;
import cn.earlydata.webcollector.util.PropertiesUtil;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class BerkeleyGenerator implements Generator {

    public static final Logger LOG = Logger.getLogger(BerkeleyGenerator.class);

    private Cursor cursor = null;
    private Database crawldbDatabase = null;
    private Environment env = null;
    protected int totalGenerate = 0;
    protected int topN = -1;
    protected int maxExecuteCount = Integer.parseInt(PropertiesUtil.getCrawlerConfigValue(ConfigAttribute.MAX_EXECUTE_COUNT));
    protected DatabaseEntry key = new DatabaseEntry();
    protected DatabaseEntry value = new DatabaseEntry();

    public BerkeleyGenerator(){}

    public BerkeleyGenerator(Environment env) {
        this.env=env;
    }

    @Override
    public void init() throws Exception {
        totalGenerate = 0;
    }

    public void close() throws Exception {
        if(cursor!=null){
            cursor.close();
        }
        cursor = null;
        if(crawldbDatabase!=null){
            crawldbDatabase.close();
        }
    }

    @Override
    public CrawlDatum next() {
        if (topN >= 0) {
            if (totalGenerate >= topN) {
                return null;
            }
        }
        if (cursor == null) {
            crawldbDatabase = env.openDatabase(null, "crawldb", BerkeleyDBManager.defaultDBConfig);
            cursor = crawldbDatabase.openCursor(null, CursorConfig.DEFAULT);
        }
        while (true) {
            if (cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                try {
                    CrawlDatum datum = BerkeleyDBManager.createCrawlDatum(key, value);
                    if (datum.getStatus() == CrawlDatum.STATUS_DB_SUCCESS) {
                        continue;
                    } else {
                        if (datum.getExecuteCount() > maxExecuteCount) {
                            continue;
                        }
                        totalGenerate++;
                        return datum;
                    }
                } catch (Exception ex) {
                    LOG.info("Exception when generating", ex);
                    continue;
                }
            } else {
                return null;
            }
        }
    }

    @Override
    public int getTotalGenerate() {
        return totalGenerate;
    }

    public int getTopN() {
        return topN;
    }

    @Override
    public void setTopN(int topN) {
        this.topN = topN;
    }

    public int getMaxExecuteCount() {
        return maxExecuteCount;
    }

    @Override
    public void setMaxExecuteCount(int maxExecuteCount) {
        this.maxExecuteCount = maxExecuteCount;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }
}
