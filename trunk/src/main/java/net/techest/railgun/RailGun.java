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
 *  Created on : Apr 6, 2012 , 4:29:02 PM
 *  Author     : princehaku
 */
package net.techest.railgun;

import java.io.File;
import java.util.Iterator;
import net.techest.railgun.action.ActionNode;
import net.techest.railgun.action.ActionNodeFactory;
import net.techest.railgun.system.Filter;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.util.Log4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author baizhongwei.pt
 */
public class RailGun {

    File doc;
    Shell shell;
    private long lastRunTime;
    private long nextRunTime;

    public RailGun(File doc, Shell shell) {
        this.doc = doc;
        this.shell = shell;
    }

    public long getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(long lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public long getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(long nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public Shell getShell() {
        return shell;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    /**
     * 执行所有装载的炮弹
     *
     */
    public void fire() throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(this.doc);
        Element root = document.getRootElement();
        this.applyAction(root, this.shell);
    }

    /**
     * XML 节点处理
     *
     * @param e
     * @param shell
     */
    private void applyAction(Element e, Shell shell) throws Exception {
        // 处理当前节点
        ActionNode action = ActionNodeFactory.getNodeAction(e.getName());
        if (action == null) {
            Log4j.getInstance().error("No Such Action " + e.getName());
            throw new Exception("Action 不存在");
        }
        Log4j.getInstance().info("Execute Action " + e.getName());
        // 进行资源节点克隆
        if (e.attribute("fork") != null && e.attribute("fork").getData().toString().equals("true")) {
            Log4j.getInstance().info("Shell Cloned");
            Shell newshell = (Shell) shell.clone();
            shell = newshell;
        }
        // 执行
        ActionNodeFactory.executeAction(action, e, shell);

        for (Iterator i = e.elementIterator(); i.hasNext();) {
            Element childe = (Element) i.next();
            // 处理子节点 递归
            applyAction(childe, shell);
        }
    }
}
