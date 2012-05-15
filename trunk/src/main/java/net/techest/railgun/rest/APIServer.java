/*  Copyright 2012 princehaku
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Project Name : railgun
 *  Created on : Apr 23, 2012 , 9:10:16 AM
 *  Author     : princehaku
 */
package net.techest.railgun.rest;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;

/**
 *
 * @author baizhongwei.pt
 */
public class APIServer {

    APIServer() {
    }

    private static class holder {
        static APIServer holder = new APIServer();
    };

    public static APIServer getInstance() {
        return holder.holder;
    }

    public void start() {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(Configure.getSystemConfig().getInt("REST_PORT", 9090));
            HttpServer hs = HttpServer.create(inetSocketAddress, 0);
            hs.createContext("/api", new APIHandler());
            hs.setExecutor(null);
            hs.start();
            Log4j.getInstance().info("API启动完成 监听" + inetSocketAddress.toString());
        } catch (IOException ex) {
            Log4j.getInstance().error("API初始化失败" + ex.getMessage());
        }
    }
}
