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

/**
 *
 * @author baizhongwei.pt
 */
public class Main {

    public static void main(String[] argvs) throws IOException {
//        Document doc = Jsoup.parse(new URL("http://www.zhaokunyao.com/"),3000);
//        Elements d = doc.select("#content div");
        HttpClient hc = new HttpClient("http://www.baidu.com");

        while (1 == 1) {
            long sttime = System.currentTimeMillis();
            try {
                byte[] c = hc.exec();
                System.out.println(new String(c, "GBK"));
            }
            catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Done " + (System.currentTimeMillis() - sttime));
        }
    }
}
