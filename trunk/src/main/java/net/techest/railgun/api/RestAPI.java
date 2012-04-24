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
package net.techest.railgun.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.index.Index;
import net.techest.railgun.net.QuestParams;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import org.apache.lucene.document.Document;

/**
 * QPS:3000
 *
 * @author baizhongwei.pt
 */
public class RestAPI {

    public static void main(String[] args) throws IOException {
        // 初始化索引目录
        String indexdir = Configure.getSystemConfig().getString("INDEX_DIR");
        if (indexdir == null) {
            indexdir = "indexes";
        }
        Index index = new Index(indexdir);
        HttpServer hs = HttpServer.create(new InetSocketAddress(Configure.getSystemConfig().getInt("REST_PORT", 9090)), 0);
        hs.createContext("/api", new MyHandler(index));
        hs.setExecutor(null);
        hs.start();
    }

    private static class MyHandler implements HttpHandler {

        Index index;
        HttpExchange httpEx;

        public MyHandler(Index index) {
            this.index = index;
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            try {
                he.getRequestBody();
                this.httpEx = he;
                he.getResponseHeaders().add("X-Powered-By", "RailGun");
                he.getResponseHeaders().add("Content-Type", "text/xml;charset=utf-8");
                // 解析url
                String query = he.getRequestURI().getQuery();
                String[] querySeg = query.split("&");
                QuestParams requestParams = new QuestParams();
                for (String segment : querySeg) {
                    String[] cols = segment.split("=");
                    String key = cols[0];
                    String value = cols[1];
                    requestParams.put(key, value);
                }
                if (requestParams.get("field") == null || requestParams.get("text") == null) {
                    this.Output(500, "Not Support");
                    return;
                }
                ArrayList<Document> search = index.search(requestParams.get("field"), requestParams.get("text"), 0, 10);
                String response = search.toString();
                this.Output(200, response);
            }
            catch (Exception ex) {
                Log4j.getInstance().error("API Error " + ex.getMessage());
                this.Output(500, ex.getMessage());
            }
            finally {
                he.close();
            }
        }

        public void Output(int code, String response) throws IOException {
            httpEx.sendResponseHeaders(code, response.getBytes("utf-8").length);
            OutputStream os = httpEx.getResponseBody();
            for (byte byet : response.getBytes("utf-8")) {
                os.write(byet);
            }
            os.close();
        }
    }
}
