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
 *  Created on : Apr 24, 2012 , 10:23:08 AM
 *  Author     : princehaku
 */
package net.techest.railgun.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 全文索引系统
 *
 * @author baizhongwei.pt
 */
public class Index {

    IndexWriter ramWriter;
    IndexWriter fsWriter;
    IndexSearcher ramIs;
    IndexSearcher fsIs;
    RAMDirectory ramDir;
    SimpleFSDirectory fsDir;
    private int totalHits = 0;

    /**
     *
     * @param indexdir
     * @param readMode
     * @throws IOException
     */
    public Index(String indexdir, boolean readMode) throws IOException {
        fsDir = new SimpleFSDirectory(new File(indexdir));
        // 读取模式下只需要一个文件
        if (readMode) {
            fsIs = new IndexSearcher(fsDir, true);
            return;
        }
        ramDir = new RAMDirectory();
        if (Configure.getSystemConfig().getString("LOADTOMEM", "false").toLowerCase().equals("true")) {
            ramDir = new RAMDirectory(fsDir);
        }
        IKAnalyzer ika = new IKAnalyzer();
        IndexWriterConfig fsIwc = new IndexWriterConfig(Version.LUCENE_34, ika);
        fsIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        fsIwc.setWriteLockTimeout(-1);
        fsIwc.setRAMBufferSizeMB(Configure.getSystemConfig().getInt("INDEX_FS_BUFFER", 16));
        IndexWriterConfig ramIwc = new IndexWriterConfig(Version.LUCENE_34, ika);
        ramIwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        ramIwc.setWriteLockTimeout(-1);
        ramIwc.setRAMBufferSizeMB(Configure.getSystemConfig().getInt("INDEX_RAM_BUFFER", 128));
        ramWriter = new IndexWriter(ramDir, ramIwc);
        fsWriter = new IndexWriter(fsDir, fsIwc);
        fsIs = null;
        ramIs = null;
        try {
            fsIs = new IndexSearcher(fsDir, true);
        } catch (IOException ex) {
        }
        try {
            ramIs = new IndexSearcher(ramDir, true);
        } catch (IOException ex) {
        }
    }

    /**
     * 判断应用的fieldName=value是否已经存在 先从内存索引读.如果没有从文件索引读
     *
     * @param fieldName
     * @param value
     * @return
     */
    public boolean existed(String fieldName, String value) {

        Query query = new TermQuery(new Term(fieldName, value));
        // 先从内存索引读.如果没有从文件索引读
        if (ramIs != null) {
            try {
                TopDocs tops = ramIs.search(query, 1);
                if (tops.scoreDocs.length > 0) {
                    Log4j.getInstance().debug("内存Index命中 " + tops.scoreDocs.length);
                    return true;
                }
            } catch (IOException ex) {
                Log4j.getInstance().warn("内存Index搜索错误" + ex.getMessage());
            }
        }
        if (fsIs != null) {
            try {
                TopDocs tops = fsIs.search(query, 1);
                if (tops.scoreDocs.length > 0) {
                    Log4j.getInstance().debug("文件Index命中 " + tops.scoreDocs.length);
                    return true;
                }
            } catch (IOException ex) {
                Log4j.getInstance().warn("文件Index搜索错误" + ex.getMessage());
            }
        }

        return false;
    }

    /**
     * 添加doc到内存索引 并重置内存读取器
     *
     * @param doc
     */
    public void addToRam(Document doc) {
        try {
            ramWriter.addDocument(doc);
            ramWriter.commit();
            ramIs = new IndexSearcher(ramDir, true);
        } catch (CorruptIndexException ex) {
            Log4j.getInstance().error(ex.getMessage());
        } catch (IOException ex) {
            Log4j.getInstance().error(ex.getMessage());
        }
    }

    /**
     * 合并mem和fs的索引
     *
     */
    public void applyToDisk() {
        try {
            ramWriter.close();
            // 合并内存index到文件 
            fsWriter.addIndexes(ramDir);
            // 优化索引
            fsWriter.optimize();
            fsWriter.close();
        } catch (CorruptIndexException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 得到总命中数
     *
     * @return
     */
    public int getTotalHits() {
        return totalHits;
    }

    /**
     * 从文件索引中查找结果
     *
     * @param searchField
     * @param value
     * @param offset
     * @param number
     * @return
     */
    public ArrayList<Document> search(String searchField, String value, int offset, int number) {
        ArrayList<Document> docs = new ArrayList();
        if (fsIs == null) {
            return docs;
        }
        try {
            IKAnalyzer analyzer = new IKAnalyzer();
            QueryParser qp = new QueryParser(Version.LUCENE_34, searchField, analyzer);
            qp.setDefaultOperator(QueryParser.OR_OPERATOR);
            Query query = qp.parse(value);
            TopDocs tops = fsIs.search(query, offset + number);
            this.totalHits = tops.totalHits;
            if (tops.totalHits < offset) {
                return docs;
            }
            int max = offset + number;
            if (tops.totalHits < offset + number) {
                max = tops.totalHits;
            }
            ScoreDoc[] scoreDoc = tops.scoreDocs;
            // 加到里面去
            for (int i = offset; i < max; i++) {
                docs.add(fsIs.doc(scoreDoc[i].doc));
            }
        } catch (ParseException ex) {
            Log4j.getInstance().error("[INDEX]" + ex.getMessage());
        } catch (IOException ex) {
            Log4j.getInstance().error("[INDEX]" + ex.getMessage());
        }

        return docs;
    }
}
