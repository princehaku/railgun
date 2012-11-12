/*  Copyright 2010 princehaku
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
 *  Created on : May 1, 2012, 3:14:25 PM
 *  Author     : princehaku
 */
package net.techest.vsearch.rest;

import java.io.IOException;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.techest.railgun.index.Index;
import net.techest.railgun.net.QuestParams;
import net.techest.railgun.util.Configure;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

/**
 *
 * @author princehaku
 */
public class IndexHandler implements APIHandlerInterface {

    @Override
    public void handle(QuestParams requestParams, JSONObject responseJson) {
        if (requestParams.get("key") == null || requestParams.get("value") == null) {
            responseJson.put("errmsg", "Method Not Support");
            return;
        }
        // 初始化索引目录
        String indexdir = Configure.getSystemConfig().getString("INDEX_DIR", "indexes");
        Index index = null;
        try {
            index = new Index(indexdir, true);
        } catch (IOException ex) {
            responseJson.put("errmsg", "Index Dir读取失败" + ex.getMessage());
            return;
        }
        int offset = 0;
        if (requestParams.get("st") != null) {
            offset = Integer.parseInt(requestParams.get("st"));
        }
        int number = 15;
        if (requestParams.get("n") != null) {
            number = Integer.parseInt(requestParams.get("n"));
        }
        ArrayList<Document> search = index.search(requestParams.get("key"), requestParams.get("value"), offset, number);
        responseJson.put("totalhits", index.getTotalHits());
        JSONArray response = new JSONArray();
        for (Document doc : search) {
            JSONObject enty = new JSONObject();
            for (Fieldable fieldable : doc.getFields()) {
                enty.put(fieldable.name(), fieldable.stringValue());
            }
            response.add(enty);
        }
        // 释放index资源
        index = null;
        responseJson.put("content", response);
    }
}
