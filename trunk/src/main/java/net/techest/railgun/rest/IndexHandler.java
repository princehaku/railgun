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
package net.techest.railgun.rest;

import java.io.IOException;
import java.util.ArrayList;
import net.sf.json.JSONObject;
import net.techest.railgun.index.Index;
import net.techest.railgun.net.QuestParams;
import net.techest.railgun.util.Configure;
import org.apache.lucene.document.Document;

/**
 *
 * @author princehaku
 */
public class IndexHandler implements APIHandlerInterface {

    @Override
    public void handle(QuestParams requestParams, JSONObject responseJson) {
        if (requestParams.get("field") == null || requestParams.get("text") == null) {
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
        ArrayList<Document> search = index.search(requestParams.get("field"), requestParams.get("text"), 0, 10);
        String response = search.toString();
        index = null;
        responseJson.put("content", response);
    }
}
