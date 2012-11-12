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
 *  Created on : May 16, 2012 , 10:46:54 AM
 *  Author     : princehaku
 */
package net.techest.util;

import java.util.HashMap;
import java.util.regex.Matcher;

/**
 *
 * @author baizhongwei.pt
 */
public class GlobalVars {
    // 参数组,用于参数替换

    private HashMap<String, String> params = new HashMap<String, String>();

    /**
     * 得到设置的参数
     *
     * @param key
     * @return
     */
    public String getParam(String key) {
        String value = this.params.get(key);
        if (value == null) {
            value = "";
        }
        return value;
    }

    /**
     * 得到设置的参数组
     *
     * @param key
     * @return
     */
    public HashMap<String, String> getParams() {
        return this.params;
    }

    /**
     * 设置参数 之后xml中的值可以通过${key}变量进行获取
     *
     * @param key
     * @return
     */
    public void putParam(String key, String value) {
        this.params.put(key.trim(), value.trim());
    }

    /**
     * 把正则后的结果集计入到param中 对应关系 group_id => result
     *
     * @param regxpResult
     */
    public void setRegxpResult(Matcher regxpResult) {
        if (regxpResult == null) {
            return;
        }
        for (int regxpu = 0, regsize = regxpResult.groupCount();
                regxpu <= regsize; regxpu++) {
            this.putParam(regxpu + "", regxpResult.group(regxpu));
        }
    }
}
