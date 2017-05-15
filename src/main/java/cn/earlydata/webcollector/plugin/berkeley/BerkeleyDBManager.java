package cn.earlydata.webcollector.plugin.berkeley;

import cn.earlydata.webcollector.core.framework.DBManager;
import cn.earlydata.webcollector.core.framework.Generator;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.model.CrawlDatums;
import cn.earlydata.webcollector.util.CrawlDatumFormater;
import cn.earlydata.webcollector.util.FileUtils;
import com.sleepycat.je.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Berkeley DB 操作类
 */
@Component
public class BerkeleyDBManager implements DBManager {

    private static Logger LOG = Logger.getLogger(BerkeleyDBManager.class);
    private Environment env;
    private String crawlPath;
    @Resource
    private BerkeleyGenerator generator;
    private Database fetchDatabase = null;
    private Database linkDatabase = null;
    private Database errorDatabase = null;
    private Cursor cursor = null;
    public static DatabaseConfig defaultDBConfig;

    static {
        defaultDBConfig = createDefaultDBConfig();
    }

    public BerkeleyDBManager() {
    }

    public BerkeleyDBManager(String crawlPath) {
        this.crawlPath = crawlPath;
    }

    public static DatabaseConfig createDefaultDBConfig() {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setAllowCreate(true);
        return databaseConfig;
    }

    @Override
    public void initSegmentWriter() {
        fetchDatabase = env.openDatabase(null, "fetch", defaultDBConfig);
        linkDatabase = env.openDatabase(null, "link", defaultDBConfig);
        errorDatabase = env.openDatabase(null, "error", defaultDBConfig);
    }

    public List<CrawlDatum> list(String databaseName) {
        List<CrawlDatum> crawlDatumList = null;
        try {
            if (env == null) {
                open();
                Database crawldbDatabase = env.openDatabase(null, databaseName, defaultDBConfig);
                cursor = crawldbDatabase.openCursor(null, CursorConfig.DEFAULT);
                DatabaseEntry key = new DatabaseEntry();
                DatabaseEntry value = new DatabaseEntry();
                while (cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    try {
                        crawlDatumList = new ArrayList<>();
                        CrawlDatum datum = createCrawlDatum(key, value);
                        crawlDatumList.add(datum);
                        LOG.info(CrawlDatumFormater.datumToString(datum));
                    } catch (Exception ex) {
                        LOG.info("Exception when generating", ex);
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crawlDatumList;
    }

    @Override
    public void save(String databaseName, CrawlDatum datum, boolean force){
        Database database = env.openDatabase(null, databaseName, defaultDBConfig);
        DatabaseEntry key = null;
        try {
            key = strToEntry(datum.key());
            DatabaseEntry value = new DatabaseEntry();
            if (!force) {
                if (database.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    database.close();
                    return;
                }
            }
            value = strToEntry(CrawlDatumFormater.datumToJsonStr(datum));
            database.put(null, key, value);
            database.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save(String databaseName, CrawlDatums datums, boolean force) {
        Database database = env.openDatabase(null, databaseName, defaultDBConfig);
        try {
            for (int i = 0; i < datums.size(); i++) {
                CrawlDatum datum = datums.get(i);
                DatabaseEntry key = null;
                key = strToEntry(datum.key());
                DatabaseEntry value = new DatabaseEntry();
                if (!force) {
                    if (database.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                        continue;
                    }
                }
                value = strToEntry(CrawlDatumFormater.datumToJsonStr(datum));
                database.put(null, key, value);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        database.close();
    }

    @Override
    public void save(String databaseName, CrawlDatums datums) {
        save(databaseName, datums, false);
    }

    public void merge() {
        LOG.info("start merge");
        Database crawldbDatabase = env.openDatabase(null, "crawldb", defaultDBConfig);
        /*合并fetch库*/
        LOG.info("merge fetch database");
        Database fetchDatabase = env.openDatabase(null, "fetch", defaultDBConfig);
        Cursor fetchCursor = fetchDatabase.openCursor(null, null);
        DatabaseEntry key = new DatabaseEntry();
        DatabaseEntry value = new DatabaseEntry();
        while (fetchCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            crawldbDatabase.put(null, key, value);
        }
        fetchCursor.close();
        fetchDatabase.close();
        /*合并link库*/
        LOG.info("merge link database");
        Database linkDatabase = env.openDatabase(null, "link", defaultDBConfig);
        Cursor linkCursor = linkDatabase.openCursor(null, null);
        while (linkCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            if (!(crawldbDatabase.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS)) {
                crawldbDatabase.put(null, key, value);
            }
        }
        linkCursor.close();
        linkDatabase.close();
        /*合并error库*/
        LOG.info("merge error database");
        Database errorDatabase = env.openDatabase(null, "error", defaultDBConfig);
        Cursor errorCursor = errorDatabase.openCursor(null, null);
        while (errorCursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            if (!(crawldbDatabase.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS)) {
                crawldbDatabase.put(null, key, value);
            }
        }
        errorCursor.close();
        errorDatabase.close();
        LOG.info("end merge");
        crawldbDatabase.close();

        env.removeDatabase(null, "fetch");
        LOG.debug("remove fetch database");
        env.removeDatabase(null, "link");
        LOG.debug("remove link database");
        env.removeDatabase(null, "error");
        LOG.debug("remove errors database");
    }


    @Override
    public void open(){
        File dir = new File(crawlPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.setAllowCreate(true);
        env = new Environment(dir, environmentConfig);
    }

    public static CrawlDatum createCrawlDatum(DatabaseEntry key, DatabaseEntry value) {
        String datumKey = null;
        try {
            datumKey = new String(key.getData(), "utf-8");
            String valueStr = new String(value.getData(), "utf-8");
            return CrawlDatumFormater.jsonStrToDatum(datumKey, valueStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeDatumToDatabase(Database database, CrawlDatum datum) throws Exception {
        String key = datum.key();
        String value = CrawlDatumFormater.datumToJsonStr(datum);
        database.put(null, strToEntry(key), strToEntry(value));
    }


    @Override
    public void writeFetchSegment(CrawlDatum fetchDatum) throws Exception {
        writeDatumToDatabase(fetchDatabase, fetchDatum);
    }


    @Override
    public void writeParseSegment(CrawlDatums parseDatums) throws Exception {
        for (CrawlDatum datum : parseDatums) {
            writeDatumToDatabase(linkDatabase, datum);
        }
    }

    @Override
    public void writeErrorSegment(CrawlDatum errorDatum) throws Exception {
        writeDatumToDatabase(errorDatabase, errorDatum);
    }

    public CrawlDatum findFromDatabase(String key, String databaseName) {
        try {
            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
            DatabaseEntry theData = new DatabaseEntry();
            Database database = env.openDatabase(null, databaseName, defaultDBConfig);
            if (database.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                CrawlDatum datum = BerkeleyDBManager.createCrawlDatum(theKey, theData);
                return datum;
            } else {
                return null;
            }
        } catch (LockConflictException lockConflict) {
            LOG.error("从数据库" + databaseName + "中读取:" + key + "出现lock异常");
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFromDatabase(String key, String databaseName) {
        boolean success = false;
        Transaction txn = null;
        try {
            Database database = env.openDatabase(null, databaseName, defaultDBConfig);
            TransactionConfig txConfig = new TransactionConfig();
            txConfig.setSerializableIsolation(true);
            txn = env.beginTransaction(null, txConfig);
            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
            OperationStatus res = database.delete(txn, theKey);
            txn.commit();
            if (res == OperationStatus.SUCCESS) {
                LOG.info("从数据库" + database.getDatabaseName() + "中删除:" + key);
                success = true;
                return success;
            } else if (res == OperationStatus.KEYEMPTY) {
                LOG.info("没有从数据库" + database.getDatabaseName() + "中找到:" + key + "。无法删除");
            } else {
                LOG.info("删除操作失败，由于" + res.toString());
            }
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (LockConflictException lockConflict) {
            LOG.error("删除操作失败，出现lockConflict异常");


        } finally {
            if (!success) {
                if (txn != null) {
                    txn.abort();
                }
            }
        }
        return false;
    }


    @Override
    public void closeSegmentWriter() {
        if (fetchDatabase != null) {
            fetchDatabase.close();
        }
        if (linkDatabase != null) {
            linkDatabase.close();
        }
        if (errorDatabase != null) {
            errorDatabase.close();
        }
    }


    @Override
    public boolean isDBExists() {
        File dir = new File(crawlPath);
        return dir.exists();
    }

    @Override
    public void clear() {
        File dir = new File(crawlPath);
        if (dir.exists()) {
            FileUtils.deleteDir(dir);
        }
    }

    @Override
    public void close(Database database) {
        if (database != null) {
            database.close();
        }
        if (env != null) {
            env.close();
        }
    }

    @Override
    public void close() {
        if (env != null) {
            env.close();
        }
    }

    @Override
    public Generator getGenerator() {
        generator.setEnv(env);
        return generator;
    }

    public static DatabaseEntry strToEntry(String str) throws UnsupportedEncodingException {
        return new DatabaseEntry(str.getBytes("utf-8"));
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public String getCrawlPath() {
        return crawlPath;
    }

    public void setCrawlPath(String crawlPath) {
        this.crawlPath = crawlPath;
    }
}