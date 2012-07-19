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
 *  Created on : May 17, 2012 , 10:15:08 AM
 *  Author     : princehaku
 */
package net.techest.railgun.test;

import net.techest.railgun.action.ActionNode;
import net.techest.railgun.action.ActionNodeFactory;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Log4j;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;

/**
 *
 * @author baizhongwei.pt
 */
public class TestClassReferer {

    public static void main(String[] argvs) throws Exception {
        // 处理当前节点
        ActionNode action = ActionNodeFactory.getNodeAction("stair");
        if (action == null) {
            Log4j.getInstance().error("No Such Action stair");
            throw new Exception("节点名  非法");
        }
        Shell shell = new Shell();
        Element e = new DOMElement("a");
        Log4j.getInstance().info("Execute Action stair");
        // 执行
        executeAction(action, e, shell);
        Log4j.getInstance().info("Execute Action Done " + shell.getBaseUrl());
    }

    private static void executeAction(ActionNode action, Element e, Shell shell) {
        Log4j.getInstance().info(shell.hashCode());
        shell.setBaseUrl("aaa");
        Shell wshell = new Shell();
        wshell.setBaseUrl("wwwww");
        shell = wshell;
    }
}
