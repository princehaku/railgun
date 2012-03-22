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
 *  Created on : Mar 22, 2012 , 2:04:26 PM
 *  Author     : princehaku
 */

package net.techest.railgun.net;

import java.net.URL;

/**
 *
 * @author baizhongwei.pt
 */
public interface Client extends Cloneable {

    Object clone();

    /**
     *
     * @param url 提交地址
     */
    byte[] exec() throws Exception;

    /**
     * 得到页面返回body的string
     *
     * @return
     * @throws Exception
     */
    String getBodyString() throws Exception;

    URL getUrl();

    /**
     *
     * @param charset 页面编码
     * 可以指定auto然后会自动从Content-Type猜测
     */
    void setCharset(String charset);

    void setResponseTimeOut(int i);

    /**
     * 设置请求的url
     *
     * @param url
     * @throws MalformedURLException
     */
    void setUrl(String url);

}
