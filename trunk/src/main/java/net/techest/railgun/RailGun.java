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
 *  Created on : Mar 30, 2012 , 4:01:05 PM
 *  Author     : princehaku
 */
package net.techest.railgun;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import net.techest.railgun.action.ActionNode;
import net.techest.railgun.action.ActionNodeFactory;
import net.techest.railgun.system.Filter;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.util.Log4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author baizhongwei.pt
 */
public class RailGun {

    private ArrayList<Document> shellXmls = new ArrayList();
    private int reloadTime = -1;

    /**
     * 通过xml装载一个炮弹
     *
     * @param xmlpath
     * @throws DocumentException
     */
    public void loadShellXml(String xmlpath) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        File inputXml = new File(xmlpath);
        Document document = saxReader.read(inputXml);
        shellXmls.add(document);
    }

    /**
     * 执行所有装载的炮弹
     *
     */
    public void fire() {
        Document document = null;
        for (Iterator<Document> t = shellXmls.iterator(); t.hasNext();) {
            document = (Document) t.next();
            Element root = document.getRootElement();
            Shell shell = new Shell();
            this.applyAction(root, shell);
        }
    }

    /**
     * XML 节点处理
     *
     * @param e
     * @param shell
     */
    public void applyAction(Element e, Shell shell) {
        // 处理当前节点
        ActionNode action = ActionNodeFactory.getNodeAction(e.getName());
        if (action == null) {
            Log4j.getInstance().error("No Such Action " + e.getName());
            return;
        }
        Log4j.getInstance().info("Execute Action " + e.getName());
        // 进行资源节点克隆
        if (e.attribute("fork") != null && e.attribute("fork").getData().toString().equals("true")) {
            Log4j.getInstance().info("Shell Cloned");
            Shell newshell = (Shell) shell.clone();
            shell = newshell;
        }
        // 执行前置过滤器
        if (e.attribute("filter-before") != null) {
            try {
                Filter filter = (Filter) Class.forName(e.attribute("filter-before").getValue()).newInstance();
                Log4j.getInstance().info("Apply Filter " + e.attribute("filter-before").getValue());
                for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
                    Resource res = (Resource) i.next();
                    filter.filter(res);
                }
            } catch (Exception ex) {
                Log4j.getInstance().error(ex.getMessage());
            }
        }
        // 执行节点方法
        ActionNodeFactory.executeAction(action, e, shell);
        // 执行后置过滤器
        if (e.attribute("filter-after") != null) {
            try {
                Filter filter = (Filter) Class.forName(e.attribute("filter-after").getValue()).newInstance();
                Log4j.getInstance().info("Apply Filter " + e.attribute("filter-after").getValue());
                for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
                    Resource res = (Resource) i.next();
                    filter.filter(res);
                }
            } catch (Exception ex) {
                Log4j.getInstance().error(ex.getMessage());
            }
        }
        if (!e.getName().equals("shell") && (e.attribute("fork") == null || !e.attribute("fork").getData().toString().equals("true")) && !e.elements().isEmpty()) {
            Log4j.getInstance().warn(e.getName() + "节点警告 只有fork后才能递归子节点" + e.elements().size());
        }
        for (Iterator i = e.elementIterator(); i.hasNext();) {
            Element childe = (Element) i.next();
            // 处理子节点 递归
            applyAction(childe, shell);
        }
    }
}
