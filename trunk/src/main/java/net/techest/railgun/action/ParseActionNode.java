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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.dom4j.Element;
import org.jsoup.select.Elements;

/**
 * parse节点处理类
 *
 * @author baizhongwei.pt
 */
class ParseActionNode extends ActionNode {

    public ParseActionNode() {
    }

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        if (node.attribute("method") == null || node.element("rule") == null) {
            throw new ActionException("ParseNode 需要rule属性");
        }
        String rule = node.element("rule").getData().toString().trim();
        node.element("rule").detach();

        Log4j.getInstance().info("当前资源节点内有" + shell.getResources().size());
        LinkedList<Resource> resnew = new LinkedList<Resource>();
        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            if (node.attribute("method").getData().toString().equals("dom")) {
                try {
                    // dom搜索 使用jsoup
                    Document doc = Jsoup.parse(new ByteArrayInputStream(res.getBytes()), res.getCharset(), "");
                    Elements els = doc.select(rule);
                    //循环els存放为新的r节点
                    for (Iterator ri = els.iterator(); ri.hasNext();) {
                        org.jsoup.nodes.Element el = (org.jsoup.nodes.Element) ri.next();
                        Resource r = (Resource) res.clone();
                        r.setBytes(el.outerHtml().getBytes(res.getCharset()));
                        resnew.add(r);
                    }
                } catch (IOException ex) {
                    Log4j.getInstance().warn("dom方式解析失败 " + ex.getMessage() + res.getCharset());
                }
            }
            if (node.attribute("method").getData().toString().equals("regxp")) {
                // 正则搜索
                Pattern ptn = Pattern.compile(rule);
                Matcher m = ptn.matcher(res.toString());
                if (m.find()) {
                    try {
                        Resource r = (Resource) res.clone();
                        r.setBytes(m.group(0).getBytes(res.getCharset()));
                        r.setRegxpResult(m);
                        resnew.add(r);
                    } catch (UnsupportedEncodingException ex) {
                        Log4j.getInstance().error("不支持的编码 " + ex.getMessage() + res.getCharset());
                    }
                } else {
                    Log4j.getInstance().warn("regxp方式解析失败 规则" + rule + "没有被匹配");
                }
            }
        }

        Log4j.getInstance().info("处理后节点内有" + resnew.size());
        shell.setResources(resnew);
    }
}
