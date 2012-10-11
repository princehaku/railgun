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
 *  Created on : Mar 22, 2012 , 10:15:07 AM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import java.util.LinkedList;
import net.techest.railgun.net.Client;
import net.techest.railgun.net.HttpClient;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import org.dom4j.Element;

/**
 * 根节点处理类 进行炮弹的初始化
 *
 * @author baizhongwei.pt
 */
public class ShellActionNode extends ActionNode {

    @Override
    public Shell execute(Element node, Shell shell) throws Exception {
        // name字段是必须的
        if (node.element("name") == null) {
            throw new ActionException("请设置railgun的Name标签");
        }
        Element nameNode = node.element("name");
        shell.setName(nameNode.getData().toString());
        nameNode.detach();
        // description字段不是必须的
        if (node.element("description") != null) {
            Element despNode = node.element("description");
            shell.setDescription(despNode.getData().toString());
            despNode.detach();
        }
        // reloadtime字段不是必须的
        if (node.element("reloadtime") != null) {
            Element reloadNode = node.element("reloadtime");
            shell.setReloadTime(Long.parseLong(reloadNode.getData().toString()));
            reloadNode.detach();
        }
        // baseurl字段不是必须的
        if (node.element("baseurl") != null) {
            Element baseurlNode = node.element("baseurl");
            shell.setBaseUrl(baseurlNode.getData().toString());
            baseurlNode.detach();
        }
        // 连接器初始化 如果没有默认使用httpclient
        Client hc = new HttpClient();
        if (node.element("client") == null) {
            // Log4j.getInstance().info("Your Shell Has No Gun To Lauther");
        }
        shell.setClient(hc);
        LinkedList<Resource> res = new LinkedList();
        res.add(new Resource());
        shell.setResources(res);
        return shell;
    }
}
