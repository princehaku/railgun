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
 *  Created on : 2010-11-16, 23:19:19
 *  Author     : princehaku
 */
package net.techest.railgun.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import net.techest.railgun.util.Log4j;
import net.techest.util.StringTools;
import org.apache.commons.collections.map.UnmodifiableMap;

/**
 * HTTP连接类 带cookie 可以使用GET和POST 注意: 非线程安全
 *
 * @author princehaku
 */
public class HttpClient implements Client {

    public enum REQ_TYPE {
        POST, GET
    };
    private HttpURLConnection httpConn = null;
    private URL turl;
    private REQ_TYPE requestType;
    private String postString;
    private Map<String, List<String>> responseHeader;
    private String responseMessage;
    Cookies cookies = new Cookies();
    boolean cookieEnable = true;
    QuestParams requestParam = new QuestParams();
    QuestParams postParams = new QuestParams();
    private int connectTimeOut = 10000;
    private int responseTimerOut = 30000;
    // 页面编码
    String charset = "auto";

    @Override
    public URL getUrl() {
        return this.turl;
    }

    /**
     * 是否启用cookie特性
     *
     */
    public void enableCookie(boolean cookieEnable) {
        this.cookieEnable = cookieEnable;
    }

    /**
     * 设置请求的url
     *
     * @param url
     * @throws MalformedURLException
     */
    @Override
    public void setUrl(String url) {
        try {
            turl = new URL(url);
            Log4j.getInstance().debug("URL SET :" + url);
        }
        catch (MalformedURLException e) {
            turl = null;
            Log4j.getInstance().error("错误的URL格式" + e.getMessage());
            return;
        }
    }

    public HttpClient(String url) {
        try {
            turl = new URL(url);
            Log4j.getInstance().debug("URL SET :" + url);
        }
        catch (MalformedURLException e) {
            Log4j.getInstance().error("错误的URL格式" + e.getMessage());
        }
    }

    /**
     * 得到当前编码
     *
     * @return
     */
    public String getCharset() {
        return this.charset;
    }

    /**
     * 设置请求类型
     *
     * @param req_type
     */
    public void setRequestType(REQ_TYPE req_type) {
        this.requestType = req_type;
    }

    public HttpClient() {
    }

    /**
     * 清空cookie
     *
     */
    public void clearCookie() {
        cookies.clear();
    }

    public void setCookie(String key, String value) {
        cookies.put(key, value);
    }

    public void setCookie(Cookies cookies) {
        this.cookies = cookies;
    }

    public String getCookieString() {
        return cookies.toString();
    }

    public String getRequestType() {
        if (requestType == null) {
            requestType = REQ_TYPE.GET;
        }
        return requestType.toString();
    }

    /**
     * 得到post串
     *
     * @return
     */
    public String getPostString() {
        if (!postString.equals("")) {
            postString = postString + "&";
        }
        postString += this.postParams.toString();
        return postString;
    }

    public void setPostString(String postString) {
        this.setRequestType(REQ_TYPE.POST);
        this.postString = postString;
    }

    public void setRequestProperty(String key, String value) {
        requestParam.put(key, value);
    }

    public void setPostProperty(String key, String value) {
        postParams.put(key, value);
    }

    /**
     * 得到所有cookie
     *
     * @return
     */
    public Cookies getCookies() {
        return cookies;
    }
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*[\"]?([^\\s;\"]*)");

    /**
     *
     * @param charset 页面编码 可以指定auto然后会自动从Content-Type猜测
     */
    @Override
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 得到页面返回body的string
     *
     * @return
     * @throws Exception
     */
    @Override
    public String getBodyString() throws Exception {
        byte[] result = this.exec();
        String resultString = new String(result, charset);
        return resultString;
    }

    /**
     *
     * @param url 提交地址
     */
    @Override
    public byte[] exec() throws Exception {
        if (turl == null) {
            throw new Exception("错误的URL格式");
        }
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        byte[] bufferCache;

        try {
            httpConn = (HttpURLConnection) turl.openConnection();
            httpConn.setConnectTimeout(this.connectTimeOut);
            httpConn.setReadTimeout(this.responseTimerOut);
            if (getRequestType().equals(REQ_TYPE.GET.toString())) {
                httpConn.setRequestMethod("GET");
            }
            if (getRequestType().equals(REQ_TYPE.POST.toString())) {
                httpConn.setRequestMethod("POST");
            }
            httpConn.setRequestProperty("Host", turl.getHost());
            httpConn.setRequestProperty("User-Agent",
                    "railgun");
            httpConn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpConn.setRequestProperty("Accept-Encoding", "gzip");
            httpConn.setRequestProperty("Accept-Language", "en,zh-cn,zh;q=0.5");
            httpConn.setRequestProperty("Accept-Charset",
                    "utf8,gbk,GB2312;q=0.7,*;q=0.7");
            // 发送cookie
            String cookieString = cookies.toString();
            if (!( cookieString.equals("") )) {
                httpConn.setRequestProperty("Cookie", cookieString);
            }
            httpConn.setRequestProperty("Keep-Alive", "off");
            httpConn.setRequestProperty("Cache-Control", "max-age=0");
            // 吧requestParam里面的信息写入
            Iterator<Entry<String, String>> ir = this.requestParam.getIterator();
            while (ir.hasNext()) {
                Entry<String, String> obj = ir.next();
                httpConn.setRequestProperty(obj.getKey(), obj.getValue());
            }

            // 传递POST值
            if (this.requestType.equals(REQ_TYPE.POST)) {
                httpConn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                httpConn.setRequestProperty("Content-Length",
                        String.valueOf(getPostString().length()));
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);
                OutputStreamWriter out = new OutputStreamWriter(
                        httpConn.getOutputStream());
                out.write(getPostString());
                out.close();
            }
            // 读取响应正文
            if (httpConn.getResponseCode() != 200) {
                throw new IOException("响应出错 代码[" + httpConn.getResponseCode() + "]");
            }
            // 放到响应header去
            this.responseHeader = httpConn.getHeaderFields();
            // 放到响应message中
            this.responseMessage = httpConn.getHeaderFieldKey(0);

            if (this.cookieEnable && httpConn.getHeaderField("Set-Cookie") != null) {
                List<String> newCookies = httpConn.getHeaderFields().get("Set-Cookie");
                if (newCookies != null) {
                    Iterator<String> nit = newCookies.iterator();
                    while (nit.hasNext()) {
                        String set_Cookie = nit.next();
                        String cookiestring = set_Cookie.substring(0, set_Cookie.indexOf(";"));
                        String cookiekey = cookiestring.substring(0, cookiestring.indexOf('='));
                        String cookievalue = cookiestring.substring(cookiestring.indexOf('=') + 1, cookiestring.length());
                        cookies.put(cookiekey, cookievalue);
                    }
                }
            }

            // 读取响应正文
            int contentLength = httpConn.getContentLength();

            if (contentLength > 0) {
                bufferCache = new byte[contentLength];
            } else {
                bufferCache = new byte[1024];
            }
            InputStream uurl = httpConn.getInputStream();
            // gzip支持
            if (httpConn.getHeaderField("Content-Encoding") != null && httpConn.getHeaderField("Content-Encoding").equalsIgnoreCase("gzip")) {
                uurl = new GZIPInputStream(uurl);
            }
            int length;
            while (( length = uurl.read(bufferCache) ) > 0) {
                content.write(bufferCache, 0, length);
            }

            // Content-Type: text/html; charset=UTF-8
            if (charset.equals("auto")) {
                if (responseHeader.get("Content-Type") != null) {
                    Matcher m = charsetPattern.matcher(responseHeader.get("Content-Type").get(0).toString());
                    if (m.find()) {
                        charset = m.group(1).trim().toUpperCase();
                        Log4j.getInstance().debug("Get Page Encode From Response");
                    }
                }
            }
            // 从返回值中猜测编码
            if (charset.equals("auto")) {
                String headArea = StringTools.subMinStr(content.toString("utf8"), 0, 2048).toLowerCase();
                Matcher m = charsetPattern.matcher(headArea);
                if (m.find()) {
                    charset = m.group(1).trim().toUpperCase();
                    Log4j.getInstance().debug("Get Page Encode From Meta");
                }
            }

            if (charset.equals("auto")) {
                charset = "utf8";
            }
            Log4j.getInstance().debug("Page Encode : " + charset);
        }
        catch (Exception e) {
            if (httpConn != null) {
                httpConn.disconnect();
            }
            throw e;
        }
        httpConn.disconnect();
        return content.toByteArray();
    }

    public Map<String, List<String>> getResponseHeader() {
        return this.responseHeader;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    @Override
    public Object clone() {
        try {
            HttpClient c= (HttpClient) super.clone();
            c.setCookie((Cookies)this.cookies.clone());
            c.postParams = (QuestParams) this.postParams.clone();
            c.requestParam = (QuestParams) this.requestParam.clone();
            return c;
        }
        catch (CloneNotSupportedException e) {
            System.out.println("Cloning not allowed.");
            return this;
        }
    }

    /**
     * 设置响应超时时间 默认30000毫秒
     *
     * @param timemillons
     */
    @Override
    public void setResponseTimeOut(int timemillons) {
        this.responseTimerOut = timemillons;
    }

    /**
     * 设置连接超时时间 默认10000毫秒
     *
     * @param timemillons
     */
    @Override
    public void setConnectTimeOut(int timemillons) {
        this.connectTimeOut = timemillons;
    }
}
