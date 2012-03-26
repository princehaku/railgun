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

import java.util.ArrayList;
import java.util.regex.Matcher;

/**模式串处理
 * 将一个带有特殊标记的字符串处理为系统标准字符串
 * 比如DATE转换为2012-03-02这样的
 * @author baizhongwei.pt
 */
public class PatternHelper {
    /**根据既有规则进行字符串转换
     * 注意得到的结果是个数组,哪怕只有一个值返回
     * @param input
     * @param m
     * @return 
     */
    public static ArrayList<String> convertAll(String input, Matcher m) {
        ArrayList<String> strings = new ArrayList<>();
        if (m != null) {
            for (int regxpu = 0, regsize = m.groupCount();
                    regxpu <= regsize; regxpu++) {
                input = input.replaceAll("\\$" + regxpu, m.group(regxpu));
            }
        }
        // 取字符串内的{}标记 进行替换后插入到strings数组内
        // 如果没有的话 就只把input放入到数组内
        strings.add(input);
        
        return strings;
    }
}
