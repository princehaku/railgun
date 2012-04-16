package net.techest.railgun.db;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import java.util.HashMap;
import javax.sql.DataSource;
import java.util.Properties;
import net.techest.railgun.util.Log4j;
import net.techest.util.MD5;
import org.apache.commons.dbcp.*;

public class ConnectionPool {

    /**
     * 单例模式
     *
     */
    private static class holder {

        static ConnectionPool instance = new ConnectionPool();
    }

    public static ConnectionPool getSystemPool() {
        return ConnectionPool.holder.instance;
    }
    private HashMap<String, BasicDataSource> pools = new HashMap<String, BasicDataSource>();

    private BasicDataSource setupDataSource(Properties p) {
        BasicDataSource dataSource = null;
        try {
            dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
            pools.put(MD5.getMD5(p.getProperty("url").getBytes()), dataSource);
        }
        catch (Exception ex) {
            Log4j.getInstance().error("Pool 初始化失败 " + ex.getMessage());
        }
        return dataSource;
    }

    public BasicDataSource getFromPool(Properties p) {
        BasicDataSource dataSource = null;
        String key = MD5.getMD5(p.getProperty("url").getBytes());
        if (pools.containsKey(key)) {
            dataSource = pools.get(key);
        } else {
            dataSource = this.setupDataSource(p);
        }
        return dataSource;
    }
}
