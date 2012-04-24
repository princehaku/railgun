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
 *  Created on : Apr 14, 2012 , 2:57:40 PM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import java.util.ArrayList;
import java.util.Iterator;
import net.techest.railgun.index.Index;
import net.techest.railgun.system.Shell;
import net.techest.railgun.system.Resource;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import net.techest.railgun.util.PatternHelper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.dom4j.Element;

/**
 * 数据库存储类
 *
 * @author baizhongwei.pt
 */
public class IndexActionNode extends ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        // 初始化索引目录
        String indexdir = Configure.getSystemConfig().getString("INDEX_DIR");
        if (indexdir == null) {
            indexdir = "indexes";
        }
        Index index = null;
        try {
            index = new Index(indexdir, false);
        } catch (Exception ex) {
            throw new ActionException(ex.getMessage());
        }
        // data标签
        Element data = node.element("data");

        ArrayList<String> colsName = new ArrayList<String>();
        ArrayList<String> colsValue = new ArrayList<String>();

        if (data.elements("enty") == null) {
            throw new ActionException("data标签内没有enty规则");
        }
        // 保存定位 用于Consist的offset计算
        Iterator enties = data.elements("enty").iterator();
        while (enties.hasNext()) {
            Element entry = (Element) enties.next();
            colsName.add(entry.elementTextTrim("name"));
            colsValue.add(entry.elementTextTrim("content"));
        }
        // 遍历资源
        // 按资源加入到索引中
        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            String consist = data.attributeValue("consist");
            if (consist != null) {
                int pos = colsName.indexOf(consist);
                if (pos == -1) {
                    throw new ActionException("Consist字段名不存在");
                }
                // 内容获取
                ArrayList<String> valueConverted = PatternHelper.convertAll(colsValue.get(pos), res, shell);
                String content = valueConverted.get(0);
                if (index.existed(consist, content)) {
                    Log4j.getInstance().debug("Index : Consist已存在 跳过存入");
                    continue;
                }
            }

            // 加入索引
            enties = data.elements("enty").iterator();
            Document doc = new Document();
            // 字段加入
            while (enties.hasNext()) {
                Element entry = (Element) enties.next();
                Field f = null;
                String type = entry.elementTextTrim("type");
                // 内容获取
                ArrayList<String> valueConverted = PatternHelper.convertAll(entry.elementTextTrim("content"), res, shell);
                String content = valueConverted.get(0);
                if (type == null) {
                    type = "text";
                }
                if (type.equals("fulltext")) {
                    f = new Field(entry.elementTextTrim("name"), content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
                }
                if (type.equals("index")) {
                    f = new Field(entry.elementTextTrim("name"), content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.NO);
                }
                if (type.equals("text")) {
                    f = new Field(entry.elementTextTrim("name"), content, Field.Store.YES, Field.Index.NO, Field.TermVector.NO);
                }
                doc.add(f);
            }
            index.addToRam(doc);
            Log4j.getInstance().debug("Index 存入成功");
        }
        index.applyToDisk();
        data.detach();
        Log4j.getInstance().info("Index 存入完成");
    }
}
