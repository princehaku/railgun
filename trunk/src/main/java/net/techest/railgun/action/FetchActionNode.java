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

import java.util.Iterator;
import java.util.List;
import net.techest.railgun.net.HttpClient;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.util.Log4j;
import org.dom4j.Element;

/**
 * fetch节点处理类
 *
 * @author baizhongwei.pt
 */
public class FetchActionNode implements ActionNode {

    @Override
    public void execute(Element node, Shell bullet) {
        HttpClient client = (HttpClient) bullet.getClient();
        // url字段是必须的
        if (node.element("url") == null) {
            Log4j.getInstance().error("FetchNode Need An Url Parameter");
            return;
        }
        client.setUrl(node.element("url").getData().toString());
        node.element("url").detach();
        // 编码定制
        if (node.element("charset") != null) {
            String charset = node.element("charset").getData().toString();
            client.setCharset(charset);
            node.element("charset").detach();
        }
        // 超时设置
        if (node.element("timeout") != null) {
            int timemillons = Integer.parseInt(node.element("timeout").getData().toString());
            client.setResponseTimeOut(timemillons);
            node.element("timeout").detach();
        }
        // 是否启用cookie
        if (node.element("cookie") != null) {
            boolean cookieEnable = node.element("cookie").getData().toString().equals("true");
            client.enableCookie(cookieEnable);
            node.element("cookie").detach();
        }
        // 组合param字段作为参数
        if (node.element("params") != null) {
            List params = node.element("params").elements("param");
            for (Iterator i = params.iterator(); i.hasNext();) {
                Element e = (Element) i.next();
                if (e.element("key") == null || e.element("value") == null) {
                    Log4j.getInstance().warn("Param need both set key and value");
                    continue;
                }
                String key = e.element("key").getData().toString();
                String value = e.element("value").getData().toString();
                client.setRequestProperty(key, value);
            }
            node.element("params").detach();
        }
        // 组合cookie参数
        if (node.element("cookies") != null) {
            List params = node.element("cookies").elements("cookie");
            for (Iterator i = params.iterator(); i.hasNext();) {
                Element e = (Element) i.next();
                if (e.element("key") == null || e.element("value") == null) {
                    Log4j.getInstance().warn("Cookie need both set key and value");
                    continue;
                }
                String key = e.element("key").getData().toString();
                String value = e.element("value").getData().toString();
                client.setCookie(key, value);
            }
            node.element("cookies").detach();
        }

        bullet.setClient(client);


        Resource resnew = new Resource();
        if (bullet.getResource() == null) {
            try {
                byte[] result = client.exec();
                resnew.add(new Resource(result, client.getCharset()));
            } catch (Exception ex) {
                Log4j.getInstance().error("Fetch Error " + ex.getMessage());
            }
        } else {
            for (Iterator i = bullet.getResource().iterator(); i.hasNext();i.next()) {
                try {
                    byte[] result = client.exec();
                    resnew.add(new Resource(result, client.getCharset()));
                } catch (Exception ex) {
                    Log4j.getInstance().error("Fetch Error " + ex.getMessage());
                }
            }
        }
        bullet.setResource(resnew);
    }
}
