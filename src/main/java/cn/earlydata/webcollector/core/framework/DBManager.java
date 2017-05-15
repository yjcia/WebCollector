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
package cn.earlydata.webcollector.core.framework;

import cn.earlydata.webcollector.plugin.berkeley.SegmentWriter;
import cn.earlydata.webcollector.model.CrawlDatum;
import cn.earlydata.webcollector.model.CrawlDatums;
import com.sleepycat.je.Database;

import java.util.List;

public interface DBManager extends SegmentWriter {

    boolean isDBExists();

    void clear();

    Generator getGenerator();

    void open();

    void close(Database database);

    void close();

    List<CrawlDatum> list(String databaseName);

    void save(String databaseName,CrawlDatum datum, boolean force);

    void save(String databaseName,CrawlDatums datums, boolean force);

    void save(String databaseName,CrawlDatums datums);

    void merge();

    CrawlDatum findFromDatabase(String key, String databaseName);

    boolean deleteFromDatabase(String key, String databaseName);


}
