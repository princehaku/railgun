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
 *  Created on : Apr 9, 2012 , 10:39:11 AM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import java.util.LinkedList;
import net.techest.railgun.net.HttpClient;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Log4j;
import org.dom4j.Element;

/**
 * 这个节点用于定义一系列操作，可以进行操作初始化
 *
 * @author baizhongwei.pt
 */
public class StairActionNode extends ActionNode {

    @Override
    public Shell execute(Element node, Shell shell) throws Exception {
        // stair可以进行资源节点克隆
        if (node.attribute("fork") != null && node.attribute("fork").getData().toString().equals("true")) {
            Log4j.getInstance().info("Shell Cloned");
            Shell newshell = (Shell) shell.clone();
            shell = newshell;
        }
        // 是否进行资源清理
        if (node.attribute("reset") != null && node.attribute("reset").getData().toString().equals("true")) {
            Log4j.getInstance().info("Stair Reset");
            LinkedList<Resource> res = new LinkedList();
            res.add(new Resource());
            shell.setResources(res);
            shell.setClient(new HttpClient());
        }

        return shell;
    }
}
