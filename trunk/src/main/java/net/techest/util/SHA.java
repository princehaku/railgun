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
 *  Created on : Apr 6, 2012 , 3:48:47 PM
 *  Author     : princehaku
 */
package net.techest.util;

/**
 *
 * @author baizhongwei.pt
 */
public class SHA {

    public static String getSHA1(byte[] source) {
        String s = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
            md.update(source);
            byte tmp[] = md.digest();
            s = StringTools.bin2hex(tmp);
        } catch (Exception e) {
            
        }
        return s;
    }
}
