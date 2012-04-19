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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.techest.railgun.system.Shell;
import net.techest.railgun.system.Resource;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import net.techest.railgun.util.PatternHelper;
import net.techest.util.StringTools;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.dom4j.Element;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.query.IKQueryExpressionParser;

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
        SimpleFSDirectory fsDir = new SimpleFSDirectory(new File(indexdir));
        RAMDirectory ramDir = new RAMDirectory();
        if (Configure.getSystemConfig().getString("LOADTOMEM", "false").toLowerCase().equals("true")) {
            ramDir = new RAMDirectory(fsDir);
        }
        IKAnalyzer ika = new IKAnalyzer();
        IndexWriterConfig fsIwc = new IndexWriterConfig(Version.LUCENE_34, ika);
        fsIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        fsIwc.setRAMBufferSizeMB(16.0);
        IndexWriterConfig ramIwc = new IndexWriterConfig(Version.LUCENE_34, ika);
        ramIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        ramIwc.setRAMBufferSizeMB(256.0);
        IndexWriter ramWriter = new IndexWriter(ramDir, ramIwc);
        IndexWriter fsWriter = new IndexWriter(fsDir, fsIwc);
        IndexSearcher fsIs = null;
        IndexSearcher ramIs = null;
        try {
            fsIs = new IndexSearcher(fsDir, true);
        }
        catch (IOException ex) {
        }
        try {
            ramIs = new IndexSearcher(ramDir, true);
        }
        catch (IOException ex) {
        }
        // data标签
        Element data = node.element("data");
        
        ArrayList<String> colsName = new ArrayList<String>();
        ArrayList<String> colsValue = new ArrayList<String>();
        // 遍历form里面的data 拿到cols的名字和对应值
        if (data.elements("enty") == null) {
            Log4j.getInstance().warn("form 标签内没有data规则");
            return;
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
                    Log4j.getInstance().error("Consist字段名不存在");
                }
                // 内容获取
                ArrayList<String> valueConverted = PatternHelper.convertAll(colsValue.get(pos), res, shell);
                String content = valueConverted.get(0);
                Query query = new TermQuery(new Term(consist, content));
                // 先从内存索引读.如果没有从文件索引读
                if (ramIs != null) {
                    TopDocs tops = ramIs.search(query, 1);
                    if (tops.scoreDocs.length > 0) {
                        Log4j.getInstance().debug("内存Index命中 " + tops.scoreDocs.length);
                        Log4j.getInstance().debug("Index : Consist已存在 跳过存入");
                        continue;
                    }
                }
                if (fsIs != null) {
                    TopDocs tops = fsIs.search(query, 1);
                    if (tops.scoreDocs.length > 0) {
                        Log4j.getInstance().debug("文件Index命中 " + tops.scoreDocs.length);
                        Log4j.getInstance().debug("Index : Consist已存在 跳过存入");
                        continue;
                    }
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
            ramWriter.addDocument(doc);
            ramWriter.commit();
            ramIs = new IndexSearcher(ramDir, true);
            Log4j.getInstance().debug("Index 存入成功");
        }
        ramWriter.close();
        // 合并内存index到文件
        fsWriter.addIndexes(ramDir);
        // 优化索引
        fsWriter.optimize();
        fsWriter.close();
        data.detach();
    }
}
