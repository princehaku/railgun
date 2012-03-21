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
 *  Created on : Mar 20, 2012 , 10:35:44 AM
 *  Author     : princehaku
 */
package net.techest.railgun;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.net.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author baizhongwei.pt
 */
public class JsoupTest {

    public static void main(String[] argvs) throws IOException {
//        Document doc = Jsoup.parse(new URL("http://www.zhaokunyao.com/"),3000);
//        Elements d = doc.select("#content div");
          HttpClient hc = new HttpClient("http://www.baidu.com");

            long sttime = System.currentTimeMillis();
            try {
                String result = hc.get();
                Document doc = Jsoup.parse(result);
                Elements d = doc.select("#content div");
                System.out.println(d.outerHtml());
            }
            catch (Exception ex) {
                Logger.getLogger(JsoupTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Done " + (System.currentTimeMillis() - sttime));
    }
}
