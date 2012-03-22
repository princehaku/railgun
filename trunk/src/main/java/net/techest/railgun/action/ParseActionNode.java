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
 *  Created on : Mar 22, 2012 , 4:47:06 PM
 *  Author     : princehaku
 */


package net.techest.railgun.action;

import java.util.Iterator;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.test.JsoupTest;
import net.techest.util.Log4j;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**parse节点处理类
 *
 * @author baizhongwei.pt
 */
class ParseActionNode implements ActionNode {

    public ParseActionNode() {
        
    }

    @Override
    public void execute(Element node, Shell bullet) {
        if (node.attribute("method") == null || node.attribute("rule") == null) {
            Log4j.getInstance().error("ParseNode Need method and rule param to work");
            return;
        }
        String rule = node.attribute("rule").getData().toString();
        for(Iterator i = bullet.getResource().iterator();i.hasNext();) {
            Resource res = (Resource) i.next();
            System.out.println(res.toString());
            if (node.attribute("method").getData().toString().equals("dom")) {
                Document doc = Jsoup.parse(res.toString());
                Elements els = doc.select(rule);
                //循环els存放为新的r节点
                for(Iterator ri = els.iterator();ri.hasNext();) {
                    Elements el = (Elements) ri.next();
                    Resource r = new Resource(el.outerHtml().getBytes(), res.getCharset());
                    res.add(r);
                }
            }
            if (node.attribute("method").getData().toString().equals("regxp")) {

            }
        }
    }

}
