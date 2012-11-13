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
 *  Created on : Mar 21, 2012 , 6:40:26 PM
 *  Author     : princehaku
 */
package net.techest.railgun.system;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Matcher;
import net.techest.railgun.util.Log4j;

/**
 * 资源类 表示节点处理类 基本的数据类型是byte[] 附加byte[]数据的charset 所以可以转换成字符串或者作二进制操作
 *
 * @author baizhongwei.pt
 */
public class Resource implements Cloneable {
    // 参数组,用于参数替换
    private HashMap<String, Object> params = new HashMap<String, Object>();

    /**
     * 得到设置的参数
     *
     * @param key
     * @return
     */
    public Object getParam(String key) {
        Object value = this.params.get(key);
        return value;
    }

    /**
     * 得到设置的参数组
     *
     * @param key
     * @return
     */
    public HashMap<String, Object> getParams() {
        return this.params;
    }

    /**
     * 设置参数 之后xml中的值可以通过${key}变量进行获取
     *
     * @param key
     * @return
     */
    public void putParam(String key, Object value) {
         this.params.put(key.trim(), value);
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

    public Resource() {
    }

    @Override
    public Object clone() {
        try {
            Resource r = (Resource) super.clone();
            r.params = (HashMap<String, Object>) this.params.clone();
            return r;
        } catch (Exception e) {
            System.out.println("Cloning not allowed.");
            return this;
        }
    }
}
