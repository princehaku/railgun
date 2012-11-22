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
 *  Created on : Mar 23, 2012 , 10:03:48 AM
 *  Author     : princehaku
 */
package net.techest.railgun.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;

/**
 * 模式串处理 将一个带有特殊标记的字符串处理为系统标准字符串 比如DATE转换为2012-03-02这样的 支持范围定义
 *
 * @author baizhongwei.pt
 */
public class PatternHelper {

    private static ArrayList<String> convertDeep(String input, ArrayList<String> strings) {
        // 取字符串内的$[xx,xx]标记 进行替换后插入到strings数组内
        Pattern p = Pattern.compile("\\$\\[(.*?)\\]");
        Matcher m = p.matcher(input);
        if (m.find()) {
            String key = m.group(1);
            String[] range = key.split(",");
            if (range.length != 2) {
            }
            int range_min = Integer.parseInt(range[0]);
            int range_max = Integer.parseInt(range[1]);
            for (int pos = range_min; pos <= range_max; pos++) {
                String inputw = input.replaceFirst("\\$\\[" + key + "\\]", pos + "");
                convertDeep(inputw, strings);
            }
        } else {
            strings.add(input);
        }
        return strings;
    }

    /**
     * 根据既有规则进行字符串转换 注意得到的结果是个数组,哪怕只有一个值返回 支持的字段 $result 当前res的内容 $date
     * yyyy-MM-dd $time HH:mm:ss $[number,number] 范围数字 $hash 资源hash值 ${key}
     * 预存的资源 上一步正则返回值${group_id}
     *
     * @param input
     * @param m
     * @return
     */
    public static ArrayList<String> convertAll(String input, Resource res, Shell shell) {
        try {
            Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
            Matcher m = p.matcher(input);
            while (m.find()) {
                String key = m.group(1);
                input = input.replaceFirst("\\$\\{" + key + "\\}", ((String) res.getParam(key)).replaceAll("\\$", "\\\\\\$"));
                m = p.matcher(input);
            }
            input = convertBase(input);
        } catch (Exception ex) {
            Log4j.getInstance().warn("转换失败" + ex.getMessage());
        }
        ArrayList<String> strings = new ArrayList();
        PatternHelper.convertDeep(input, strings);
        return strings;
    }
    /**
     * 一些基本转换
     * @param input
     * @return 
     */
    public static String convertBase(String input) {
        input = input.replaceAll("\\$timestamp", System.currentTimeMillis() + "");
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        input = input.replaceAll("\\$date", sd.format(new Date()));
        sd = new SimpleDateFormat("HH:mm:ss");
        input = input.replaceAll("\\$time", sd.format(new Date()));
        return input;
    }
}
