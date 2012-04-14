/*  Copyright 2010 princehaku
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
 *  Created on : 2011-8-31, 下午6:41:28
 *  Author     : princehaku
 */
package net.techest.railgun.net;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.util.Log4j;

/**
 * Cookie类 方便把cookie组合成字符串
 *
 * @author princehaku
 *
 */
public class Cookies extends HashMap<String, String> {

    @Override
    public java.lang.String toString() {
        Iterator<Entry<String, String>> ir = this.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (ir.hasNext()) {
            try {
                Entry<String, String> obj = ir.next();
                sb.append(URLEncoder.encode(obj.getKey().trim(), "utf8"));
                sb.append("=");
                sb.append(URLEncoder.encode(obj.getValue().trim(), "utf8"));
                if (ir.hasNext()) {
                    sb.append(";");
                }
            } catch (UnsupportedEncodingException ex) {
                // 指定了utf8编码 但是有的linux居然还是不支持的 = =
                Log4j.getInstance().error(ex.getMessage());
                sb = new StringBuilder("");
            }
        }
        return sb.toString();
    }

    public Iterator<Entry<String, String>> getIterator() {
        return this.entrySet().iterator();
    }

    /**
     * 从cookie字符串解析
     *
     * cna=DlvsBzUz4gICAXEdAHlIDV5l; l=princehaku::1333174802501::01; ;
     *
     * @param cookieString
     */
    public void fromString(String cookieString) {
        String[] cookies = cookieString.split(";");
        for (int i = 0, size = cookies.length; i < size; i++) {
            try {
                String[] cookieenty = cookies[i].split("=");
                if (cookieenty.length != 2) {
                    Log4j.getInstance().error("CookieString Error " + cookies[i]);
                    continue;
                }
                String key = URLDecoder.decode(cookieenty[0], "utf8");
                String value = URLDecoder.decode(cookieenty[1], "utf8");
                this.put(key.trim(), value.trim());
            } catch (UnsupportedEncodingException ex) {
                Log4j.getInstance().error(ex.getMessage());
            }
        }
    }
}
