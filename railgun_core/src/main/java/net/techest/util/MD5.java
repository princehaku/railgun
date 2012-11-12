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
 *  Created on : 2010-12-9, 15:58:46
 *  Author     : princehaku
 */
package net.techest.util;

/**
 *
 * @author princehaku
 */
public class MD5 {

    public static String getMD5(byte[] source) {
        String s = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            // MD5 的计算结果是一个 128 位的长整数，
            byte tmp[] = md.digest();
            s = StringTools.bin2hex(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
