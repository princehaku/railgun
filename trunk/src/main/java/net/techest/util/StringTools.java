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
 *  Created on : 2010-11-17, 14:22:57
 *  Author     : princehaku
 */
package net.techest.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author princehaku
 */
public class StringTools {

    /**
     * 正则匹配,返回模式串里面第pos个模式匹配的字符串 如果没有 返回空字符串
     *
     * @param SourceString
     * @param RegxString
     * @param pos
     * @return
     */
    public static String findMc(String SourceString, String RegxString, int pos) {
        Pattern pn = Pattern.compile(RegxString);
        Matcher mc = pn.matcher(SourceString);
        if (mc.find() && mc.groupCount() > pos) {
            return mc.group(pos);
        }
        return "";
    }

    /**
     * 字符串前补0
     *
     */
    public static String plusZero(String s, int length) {
        if (s.length() < length) {
            return "0" + plusZero(s, length - 1);
        } else {
            return s;
        }
    }

    /**
     * 将字节转换成字符串
     *
     * @param source
     * @param encode 编码
     * @return
     */
    public static String byteToString(byte[] source, String encode) {
        String returnString = "";
        try {
            returnString = new String(source, encode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return returnString;
    }

    /**
     * 用来截取两个字符串包含中间的字符串
     *
     * @param source
     * @param st
     * @param ed
     * @return
     */
    public static String cutString(String source, String st, String ed) {

        int apos = source.indexOf(st);
        // 从第一个位置开始找下一个/>
        int bpos = source.indexOf(ed, apos + st.length());

        if (apos == -1 || bpos == -1) {
            return "";
        } else {
            return source.substring(apos + st.length(), bpos);
        }


    }

    /**
     * 最小长度切割字符串
     *
     * @param source
     * @param length
     * @return
     */
    public static String subMinStr(String source, int st, int length) {
        if (source.length() > st + length) {
            return source.substring(st, st + length);
        } else {
            return source;
        }
    }

    /**
     * 反向过滤html字符
     *
     * @param s
     * @return
     */
    public static String reverseEscapeHTML(String s) {
        s = s.replaceAll("&amp;", "&");
        s = s.replaceAll("&lt;", "<");
        s = s.replaceAll("&gt;", ">");
        s = s.replaceAll("&quot;", "\"");
        s = s.replaceAll("&apos;", "'");
        s = s.replaceAll("&nbsp;", " ");
        return s;
    }

    /**
     * 去掉html里面的的标签
     *
     * @param content
     * @return
     */
    public static String removeHTMLtags(String content) {
        //去掉/head以前的所有内容
        String value = null;
        int end = content.indexOf("</head>");
        if (end > 9) {
            content = content.substring(end + 7, content.length());
        }

        //去掉所有的标签
        Pattern p = Pattern.compile("(<[^>]*?>)");
        Matcher m = p.matcher(content);
        while (m.find()) {
            value = m.group(0);
            content = content.replace(value, "");
        }

        //缩减连续的空格
        content = content.replaceAll("\\s*|\t|\r|\n", "");
        //缩减特殊字符串
        content = reverseEscapeHTML(content);

        return content;
    }

    public static ByteBuffer readToByteBuffer(InputStream inStream, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        int read;
        while (true) {
            read = inStream.read(buffer);
            if (read == -1) {
                break;
            }
            outStream.write(buffer, 0, read);
        }
        ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
        return byteData;
    }

    public static String bin2hex(byte[] bin) {
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0;                                // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = bin[i];                 // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
        }
        return new String(str);
    }

    /**
     * 把字符串中的' " 转换为\' \"
     *
     * @param input
     * @return
     */
    public static String addSlashes(String input) {
        if (input == null) {
            return null;
        }
        String output = input.replaceAll("\"", "\\\\\"");
        output = output.replaceAll("\'", "\\\\\'");
        return output;
    }
}
