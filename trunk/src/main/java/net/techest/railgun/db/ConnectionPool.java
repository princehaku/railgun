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
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import net.techest.util.MD5;
import org.apache.commons.dbcp.*;
import org.dom4j.Attribute;

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

    private BasicDataSource setupDataSource(String dbPoolName) {
        BasicDataSource dataSource = null;
        Properties p = new Properties();
        p.setProperty("driverClassName",  Configure.getSystemConfig().getString(dbPoolName+"_DRIVER","com.mysql.jdbc.Driver"));
        p.setProperty("url", Configure.getSystemConfig().getString(dbPoolName+"_URL"));
        p.setProperty("username", Configure.getSystemConfig().getString(dbPoolName+"_USERNAME"));
        p.setProperty("password", Configure.getSystemConfig().getString(dbPoolName+"_PASSWORD"));
        p.setProperty("maxActive", Configure.getSystemConfig().getString(dbPoolName+"_MAXACTIVE","30"));
        p.setProperty("maxWait", Configure.getSystemConfig().getString(dbPoolName+"_MAXWAIT","5000"));
        p.setProperty("maxIdle", Configure.getSystemConfig().getString(dbPoolName+"_MAXIDLE","5"));
        
        try {
            dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
            Log4j.getInstance().error("Pool 初始化完成 " + p.toString());
            pools.put(MD5.getMD5(dbPoolName.getBytes()), dataSource);
        }
        catch (Exception ex) {
            Log4j.getInstance().error("Pool 初始化失败 " + ex.getMessage());
        }
        return dataSource;
    }

    public BasicDataSource getFromPool(String dbPoolName) {
        BasicDataSource dataSource = null;
        String key = MD5.getMD5(dbPoolName.getBytes());
        if (pools.containsKey(key)) {
            dataSource = pools.get(key);
        } else {
            dataSource = this.setupDataSource(dbPoolName);
        }
        return dataSource;
    }
}
