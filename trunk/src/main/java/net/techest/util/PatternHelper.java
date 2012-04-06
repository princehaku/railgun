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
package net.techest.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;

/**
 * 模式串处理 将一个带有特殊标记的字符串处理为系统标准字符串 比如DATE转换为2012-03-02这样的
 *
 * @author baizhongwei.pt
 */
public class PatternHelper {

    /**
     * 根据既有规则进行字符串转换 注意得到的结果是个数组,哪怕只有一个值返回 支持的字段说明 $result 当前res的内容 $date
     * yyyy-MM-dd $time HH:mm:ss 范围数字$[number,number] 预存的资源${key}
     * 上一步正则返回值${group_id}
     *
     * @param input
     * @param m
     * @return
     */
    public static ArrayList<String> convertAll(String input, Resource res, Shell shell) {
        ArrayList<String> strings = new ArrayList();
        Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher m = p.matcher(input);
        while (m.find()) {
            String key = m.group(1);
            input = input.replaceFirst("\\$\\{" + key + "\\}", res.getParam(key));
            m = p.matcher(input);
        }

        input = input.replaceAll("\\$result", res.toString());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        input = input.replaceAll("\\$date", sd.format(new Date()));
        sd = new SimpleDateFormat("HH:mm:ss");
        input = input.replaceAll("\\$time", sd.format(new Date()));
        // 取字符串内的$[xx,xx]标记 进行替换后插入到strings数组内
        // 如果没有的话 就只把input放入到数组内
        strings.add(input);

        return strings;
    }
}
