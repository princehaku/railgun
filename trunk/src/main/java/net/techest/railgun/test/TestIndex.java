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
 *  Created on : Apr 16, 2012 , 1:51:50 PM
 *  Author     : princehaku
 */
package net.techest.railgun.test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import net.techest.railgun.RailGunThreadPool;
import net.techest.railgun.system.AddShellException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.sample.IKAnalyzerDemo;

/**
 *
 * @author baizhongwei.pt
 */
public class TestIndex {

    public static void main(String[] args) throws IOException {

        SimpleFSDirectory ramDir = new SimpleFSDirectory(new File("index"));
        IKAnalyzer ika = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_34, ika);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(ramDir, iwc);
        Document doc = new Document();
        Field f= new Field("source", "虽然lucene没有定义一个确定的输入文档格式，但越来越多的人想到使用一个标准的中间格式作为Lucene的数据导入接口，然后其他数据，比如PDF只需要通过解析器转换成标准的中间格式就可以进行数据索引了。", Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
        doc.add(f);
        writer.addDocument(doc);
        writer.close();
    }
}