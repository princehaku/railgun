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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.techest.railgun.net.Cookies;
import net.techest.railgun.net.HttpClient;
import net.techest.railgun.system.ActionException;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.PatternHelper;
import net.techest.railgun.util.Log4j;
import org.dom4j.Element;

/**
 * fetch节点处理类
 *
 * @author baizhongwei.pt
 */
public class FetchActionNode implements ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        HttpClient client = (HttpClient) shell.getClient();
        shell.setClient(client);
        // url字段是必须的
        if (node.element("url") == null) {
            Log4j.getInstance().error("FetchNode Need An Url Parameter");
            throw new ActionException("FetchNode Need An Url Parameter");
        }
        String url = node.element("url").getData().toString().trim();
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
        String content = null;
        if (node.element("content") != null) {
            content = node.element("content").getData().toString();
            client.setPostString(content.trim());
            node.element("content").detach();
        }
        String charset = "auto";
        // 编码定制
        if (node.element("charset") != null) {
            charset = node.element("charset").getData().toString();
            client.setCharset(charset);
            node.element("charset").detach();
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
        // 组合cookie参数
        if (node.element("cookies") != null) {
            Cookies cookie = new Cookies();
            // 如果有cookie-string,优先解析
            if (node.element("cookies").element("cookie-string") != null) {
                cookie.fromString(node.element("cookies").element("cookie-string").getData().toString());
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
        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            try {
                String newurl = url;
                ArrayList<String> urlPatterns = PatternHelper.convertAll(newurl, res, shell);
                for (Iterator si = urlPatterns.iterator(); si.hasNext();) {
                    newurl = (String) si.next();
                    URL uri;
                    try {
                        uri = new URL(newurl);
                        // 如果不在baseUrl范围内 不抓取
                        if (!(shell.getBaseUrl().equals("*") || newurl.indexOf(shell.getBaseUrl()) != -1)) {
                            Log4j.getInstance().warn("URI " + newurl + " Doesn't Match BaseURL");
                            continue;
                        }
                    } catch (MalformedURLException ex) {
                        Log4j.getInstance().warn("URI " + ex.getMessage());
                    }
                    client.setUrl(newurl);
                    ArrayList<String> contentPatterns = new ArrayList<String>();
                    // 如果设置了POST方式.使用patternHelp对content进行处理
                    if (client.getRequestType().toString().equals(HttpClient.REQ_TYPE.POST.toString()) && content != null) {
                        contentPatterns = PatternHelper.convertAll(content.trim(), res, shell);
                    } else {
                        contentPatterns.add("");
                    }
                    for (Iterator sc = contentPatterns.iterator(); sc.hasNext();) {
                        String postcontent = (String) sc.next();
                        if (!postcontent.equals("")) {
                            client.setPostString(postcontent);
                        }
                        // 重设编码
                        client.setCharset(charset);
                        byte[] result = client.exec();
                        Resource newResNode = new Resource(result, client.getCharset());
                        newResNode.setUrl(newurl);
                        resnew.add(newResNode);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log4j.getInstance().error("Fetch Error " + ex.getMessage());
            }
        }
        shell.setResources(resnew);
    }
}
