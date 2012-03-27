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
import java.util.LinkedList;
import java.util.regex.Matcher;
import net.techest.util.Log4j;

/**
 * 资源类 表示节点处理类 基本的数据类型是byte[] 附加byte[]数据的charset 所以可以转换成字符串或者作二进制操作
 *
 * @author baizhongwei.pt
 */
public class Resource implements Cloneable {

    private byte[] bytes;
    private String charset = "";
    private Matcher regxpResult = null;

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

    public Matcher getRegxpResult() {
        return regxpResult;
    }

    public void setRegxpResult(Matcher regxpResult) {
        this.regxpResult = regxpResult;
    }
    
    public Resource() {
         this.bytes=new byte[1];
    }
    
    /**根据资源设定的字符集转换成字符串
     * 
     * @return 
     */
    @Override
    public String toString(){
        String result = "";
        try {
            result =  new String(this.bytes, this.charset);
        } catch (UnsupportedEncodingException ex) {
           Log4j.getInstance().error("Resource " + ex.getMessage());
        }
        return result;
    }
    
    @Override
    public Object clone() {
        try {
            // call clone in Object.
            return super.clone();
        } catch (Exception e) {
            System.out.println("Cloning not allowed.");
            return this;
        }
    }
}
