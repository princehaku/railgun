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
 *  Created on : Apr 14, 2012 , 7:31:17 PM
 *  Author     : princehaku
 */
package net.techest.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author baizhongwei.pt
 */
public class ArrayTools {

    /**
     * 用环绕符和glue连接数组
     *
     * @param glue
     * @param surrund
     * @param list
     * @return
     */
    public static String implode(String glue, String surround, ArrayList<String> list) {
        Iterator<String> iterator = list.iterator();
        StringBuilder sb = new StringBuilder("");
        while (iterator.hasNext()) {
            sb.append(surround);
            sb.append(iterator.next());
            sb.append(surround);
            if (iterator.hasNext()) {
                sb.append(glue);
            }
        }
        return sb.toString();
    }
}
