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

import java.io.IOException;
import java.io.StringReader;
import net.techest.railgun.RailGunThreadPool;
import net.techest.railgun.system.AddShellException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
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

    public static void main(String[] args) {

        RAMDirectory ramDir = new RAMDirectory();
        IKAnalyzer ika = new IKAnalyzer();
        
        
        IKSegmenter seg = null;
        String text = "苏轼不是圣人，他最可贵的地方是在痛苦彷徨挣扎中，"
                + "总能把自己的心灵置于更广阔的天地中，如同《赤壁后赋》中横飞而去的老鹤，"
                + "戛戛于星空夜月，长河大江之上，澄明清澈，皎然不滓。"
                + "苏轼是一个善于苦中找乐的人，这种乐观与真趣帮他度过了不少难关。"
                + "画家陈丹青说鲁迅是一个有趣的人，我想，拿来说苏东坡一样也行。";

        StringReader reader = new StringReader(text);
        seg = new IKSegmenter(reader, true);
        Lexeme lex = null;
        try {
            while (( lex = seg.next() ) != null) {
                System.out.print(lex.getLexemeText() + "|");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}