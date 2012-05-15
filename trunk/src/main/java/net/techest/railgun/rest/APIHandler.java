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
 *  Created on : Apr 26, 2012 , 9:02:44 AM
 *  Author     : princehaku
 */
package net.techest.railgun.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import net.sf.json.JSONObject;
import net.techest.railgun.net.QuestParams;

/**
 *
 * @author baizhongwei.pt
 */
class APIHandler implements HttpHandler {

    HttpExchange httpEx;

    public APIHandler() {
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        JSONObject responseJson = new JSONObject();
        responseJson.put("status", 0);
        try {
            he.getRequestBody();
            this.httpEx = he;
            he.getResponseHeaders().add("X-Powered-By", "RailGun");
            he.getResponseHeaders().add("Content-Type", "application/javascript;charset=utf-8");
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
            String source = requestParams.get("s", "db");
            if (source.equals("index")) {
                new IndexHandler().handle(requestParams, responseJson);
            } else {
                new DBHandler().handle(requestParams, responseJson);
            }
            responseJson.put("status", 1);
            this.OutputJson(responseJson);
        } catch (Exception ex) {
            responseJson.put("errmsg", ex.getMessage());
            this.OutputJson(responseJson);
        } finally {
            he.close();
        }
    }

    public void OutputJson(JSONObject result) throws IOException {
        byte[] resultbytes = result.toString().getBytes("utf-8");
        httpEx.sendResponseHeaders(200, resultbytes.length);
        OutputStream os = httpEx.getResponseBody();
        for (byte byet : resultbytes) {
            os.write(byet);
        }
        os.close();
    }
}
