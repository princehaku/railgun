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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import net.techest.railgun.net.Cookies;
import net.techest.railgun.net.HttpClient;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Log4j;
import net.techest.railgun.util.PatternGroup;
import org.dom4j.Element;

/**
 * fetch节点处理类
 *
 * @author baizhongwei.pt
 */
public class FetchActionNode extends ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        HttpClient client = (HttpClient) shell.getClient();
        shell.setClient(client);
        // url字段是必须的
        if (node.element("url") == null) {
            Log4j.getInstance().error("FetchNode Need An Url Parameter");
            throw new ActionException("FetchNode Need An Url Parameter");
        }
        PatternGroup pg = new PatternGroup();
        String url = node.elementTextTrim("url");
        pg.addNewString("url", url, canPattern(node.element("url")));
        // Method设置
        if (node.element("method") != null) {
            String requestMethod = node.element("method").getData().toString();
            Log4j.getInstance().info("RequestMethod " + requestMethod);
            if (requestMethod.toLowerCase().equals("post")) {
                client.setRequestType(HttpClient.REQ_TYPE.POST);
            }
            node.element("method").detach();
        }
        // 获取并设置body节点内容
        if (node.element("content") != null) {
            pg.addNewString("content", node.elementTextTrim("content"), canPattern(node.element("content")));
            node.element("content").detach();
        }
        // 编码定制
        if (node.element("charset") != null) {
            pg.addNewString("charset", node.elementTextTrim("charset"), canPattern(node.element("charset")));
            node.element("charset").detach();
        } else {
            pg.addNewString("charset", "auto", false);
        }
        // 超时设置
        if (node.element("timeout") != null) {
            int timemillons = Integer.parseInt(node.element("timeout").getData().toString());
            client.setResponseTimeOut(timemillons);
            client.setConnectTimeOut(timemillons);
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
        Cookies cookie = new Cookies();
        // 组合cookie参数
        if (node.element("cookies") != null) {
            // 如果有cookie-string,优先解析
            if (node.element("cookies").element("cookie-string") != null) {
                pg.addNewString("cookie-string", node.element("cookies").elementTextTrim("cookie-string"), canPattern(node.element("cookies").element("cookie-string")));
                cookie.fromString(node.element("cookies").elementTextTrim("cookie-string"));
            }
            List params = node.element("cookies").elements("cookie");
            for (Iterator i = params.iterator(); i.hasNext();) {
                Element e = (Element) i.next();
                if (e.element("key") == null || e.element("value") == null) {
                    Log4j.getInstance().warn("Cookie need both set key and value");
                    continue;
                }
                String key = e.element("key").getData().toString();
                String value = e.element("value").getData().toString();
                cookie.put(key, value);
            }
            client.setCookie(cookie);
            Log4j.getInstance().debug("Cookie Setted " + cookie.toString());
            node.element("cookies").detach();
        }

        LinkedList<Resource> resnew = new LinkedList();
        // url格式转换
        Log4j.getInstance().debug("Source Url : " + url);
        node.element("url").detach();
        // 循环资源节点
        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            String newurl = url;
            ArrayList<HashMap<String, String>> pgs = pg.convert();
            // 循环匹配组
            for (int pgi = 0, pgsize = pgs.size(); pgi < pgsize; pgi++) {
                HashMap<String, String> hash = pgs.get(pgi);
                newurl = hash.get("url");
                // url合法性检测
                try {
                    new URL(newurl);
                    // 如果不在baseUrl范围内 不抓取
                    if (!( shell.getBaseUrl().equals("*") || newurl.indexOf(shell.getBaseUrl()) != -1 )) {
                        Log4j.getInstance().warn("URI " + newurl + " Doesn't Match BaseURL");
                        continue;
                    }
                }
                catch (MalformedURLException ex) {
                    Log4j.getInstance().warn("URI " + ex.getMessage());
                }
                // 重设字段并发送请求
                try {
                    client.setUrl(newurl);
                    // 如果设置了POST方式,填充content
                    if (client.getRequestType().toString().equals(HttpClient.REQ_TYPE.POST.toString())) {
                        client.setPostString(hash.get("content"));
                    }
                    // 重设编码
                    client.setCharset(hash.get("charset"));
                    // 重设cookie
                    if (hash.get("cookie-string") != null) {
                        cookie.fromString(hash.get("cookie-string"));
                        client.setCookie(cookie);
                    }
                    byte[] result = client.exec();
                    Resource newResNode = new Resource(result, client.getCharset());
                    newResNode.putParam("url", newurl);
                    newResNode.putParam("cookie", client.getCookieString());
                    resnew.add(newResNode);
                }
                catch (Exception ex) {
                    Log4j.getInstance().warn("Fetch Error " + ex.getMessage());
                }
            }
            shell.setResources(resnew);
        }
    }
}
