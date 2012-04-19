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
    // 字节流

    private byte[] bytes;
    // 字节流的编码方式
    private String charset = null;
    // 参数组,用于参数替换
    private HashMap<String, String> params = new HashMap<String, String>();

    public Resource(byte[] bytes, String charset) {
        this.bytes = bytes;
        this.charset = charset;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

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

    public Resource() {
        this.bytes = new byte[1];
        this.charset = "utf8";
    }
    /**
     * @see getText()
     * @return 
     */
    @Override
    public String toString() {
        return this.getText();
    }

    /**
     * 根据资源设定的字符集转换成字符串
     *
     * @return
     */
    public String getText() {
        String result = "";
        try {
            result = new String(this.bytes, this.charset);
        } catch (UnsupportedEncodingException ex) {
            Log4j.getInstance().error("Resource " + ex.getMessage());
        }
        return result;
    }
    
    @Override
    public Object clone() {
        try {
            Resource r= (Resource) super.clone();
            r.params = (HashMap<String, String>) this.params.clone();
            return r;
        } catch (Exception e) {
            System.out.println("Cloning not allowed.");
            return this;
        }
    }
}
