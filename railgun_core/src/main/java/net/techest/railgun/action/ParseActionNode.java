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
import java.util.LinkedList;
import net.techest.railgun.parser.DomParser;
import net.techest.railgun.parser.RegxpParser;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Log4j;
import org.dom4j.Element;

/**
 * parse节点处理类
 *
 * @author baizhongwei.pt
 */
class ParseActionNode extends ActionNode {

    public ParseActionNode() {
    }

    @Override
    public Shell execute(Element node, Shell shell) throws Exception {
        if (node.attribute("method") == null || node.element("rule") == null) {
            throw new ActionException("ParseNode 需要rule属性");
        }
        String rule = node.elementTextTrim("rule");
        node.element("rule").detach();
        String returnType = "html";
        if (node.element("returnType") != null) {
            returnType = node.elementTextTrim("returnType");
            node.element("returnType").detach();
        }
        String set = node.elementTextTrim("set");
        if (node.element("set") != null) {
            node.element("set").detach();
        }

        Log4j.getInstance().info("当前资源节点内有" + shell.getResources().size());
        LinkedList<Resource> resnew = new LinkedList<Resource>();
        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            if (node.attributeValue("method").equals("dom")) {
                resnew = DomParser.parse(res, rule, set, returnType);
            }
            if (node.attributeValue("method").equals("regxp")) {
                resnew = RegxpParser.parse(res, rule, set, returnType);
            }
        }
        // 把新处理好的放进主的
        Log4j.getInstance().info("处理后节点内有" + resnew.size());
        shell.setResources(resnew);

        return shell;
    }
}
