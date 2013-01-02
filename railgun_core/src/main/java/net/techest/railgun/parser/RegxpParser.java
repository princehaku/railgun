/**
 * Copyright 2012 Etao Inc.
 *
 * Project Name : net.techest_railgun_jar_0.3 Created on : Jan 2, 2013, 6:17:40
 * PM Author : haku
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.techest.railgun.parser;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.StringResource;
import net.techest.railgun.util.Log4j;

/**
 *
 * @author haku
 */
public class RegxpParser {

    public static LinkedList<Resource> parse(Resource resw, String rule, String set, String returnType) {

        LinkedList<Resource> parsedRes = new LinkedList<Resource>();
        StringResource res = new StringResource(resw);
        // 正则搜索
        Pattern ptn = Pattern.compile(rule);
        Matcher m = ptn.matcher(res.toString());
        if (m.find()) {
            try {
                // 从原始节点复制一份出来 然后把数据填充
                Resource r = (Resource) resw.clone();
                r.putParam("bytedata", m.group(0).getBytes(res.getCharset()));
                r.setRegxpResult(m);
                // 如果有set标记 存放到set中
                if (set != null) {
                    if (m.find()) {
                        String e = m.group(0);
                        r.putParam(set, e);
                    }
                }
                parsedRes.add(r);
            } catch (UnsupportedEncodingException ex) {
                Log4j.getInstance().error("不支持的编码 " + ex.getMessage() + res.getCharset());
            }
        } else {
            Log4j.getInstance().warn("regxp方式解析失败 规则" + rule + "没有被匹配");
        }

        return parsedRes;
    }
}
