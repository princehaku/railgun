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
 *  Created on : Mar 21, 2012 , 3:58:20 PM
 *  Author     : princehaku
 */
package net.techest.railgun.test;

import java.io.File;
import java.util.Iterator;
import net.techest.util.Log4j;
import net.techest.railgun.action.ActionNodeFactory;
import net.techest.railgun.action.ActionNode;
import net.techest.railgun.system.Shell;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author baizhongwei.pt
 */
public class ParseXmlTest {

    public static void main(String[] argvs) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        File inputXml = new File("src/main/java/testtask1.xml");
        Document document = saxReader.read(inputXml);
        Element root = document.getRootElement();
        Shell shell = new Shell();
        ParseXmlTest.applyAction(root, shell);
    }

    public static void applyAction(Element e, Shell shell) {
        // 处理当前节点
        ActionNode action = ActionNodeFactory.getNodeAction(e.getName());
        if (action == null) {
            Log4j.getInstance().error("No Such Action " + e.getName());
            //return;
        } else {
            Log4j.getInstance().debug("Execute Action " + e.getName());
            ActionNodeFactory.executeAction(action, e, shell);
        }
        for (Iterator i = e.elementIterator(); i.hasNext();) {
            Element childe = (Element) i.next();
            // 处理子节点 递归
            applyAction(childe, shell);
        }
    }
}