/**
 * Copyright 2012 Etao Inc.
 *
 * Project Name : net.techest_railgun_jar_0.3 Created on : Jan 2, 2013, 6:04:49
 * PM Author : haku
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.parser;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.StringResource;
import net.techest.railgun.util.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author haku
 */
public class DomParser{

    public static LinkedList<Resource> parse(Resource resw, String rule, String set, String returnType) {

        LinkedList<Resource> parsedRes = new LinkedList<Resource>();
        StringResource res = new StringResource(resw);
        try {
            // dom搜索 使用jsoup
            String htmlContent = res.getText();
            // --特别的 针对没有table标签的tr进行处理--
            if (res.getText().indexOf("tr") >= 0 && res.getText().indexOf("table") == -1) {
                htmlContent = "<table>" + htmlContent + "</table>";
            }
            // --特别的 针对没有html标签的进行处理--
            if (res.getText().indexOf("html") == -1) {
                htmlContent = "<html><body>" + htmlContent + "</body></html>";
            }
            Document doc = Jsoup.parse(htmlContent);
            Elements els = doc.select(rule);
            // 循环els存放为新的r节点
            for (Iterator ri = els.iterator(); ri.hasNext();) {
                org.jsoup.nodes.Element el = (org.jsoup.nodes.Element) ri.next();
                // 从原始节点复制一份出来 然后把数据填充
                Resource r = (Resource) resw.clone();
                byte[] data = el.outerHtml().getBytes(res.getCharset());
                r.putParam("bytedata", data);
                // 如果有set标记 存放到参数中
                if (set != null) {
                    String elementValue = el.outerHtml();
                    if (returnType.equals("text")) {
                        elementValue = el.text();
                    }
                    if (returnType.equals("html")) {
                        elementValue = el.html();
                    }
                    if (returnType.equals("outerhtml")) {
                        elementValue = el.outerHtml();
                    }
                    r.putParam(set, elementValue);
                }
                // 添加到解析后的列表中
                parsedRes.add(r);
            }
        } catch (IOException ex) {
            Log4j.getInstance().warn("dom方式解析失败 " + ex.getMessage() + res.getCharset());
        }
        return parsedRes;
    }
}
