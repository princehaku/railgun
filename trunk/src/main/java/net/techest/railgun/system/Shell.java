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
 *  Created on : Mar 22, 2012 , 1:48:34 PM
 *  Author     : princehaku
 */
package net.techest.railgun.system;

import java.util.LinkedList;
import net.techest.railgun.net.Client;

/**
 * 炮弹类 定义一次抓取任务
 *
 * @author baizhongwei.pt
 */
public class Shell implements Cloneable {

    String name;
    String description;
    String baseUrl;
    // 再次被运行的时间间隔,如果是-1,只运行一次
    long reloadTime = -1;
    // 最大运行次数
    int maxReloadTime = -1;
    // 连接器
    Client client;
    // 资源类
    LinkedList<Resource> resources;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getMaxReloadTime() {
        return maxReloadTime;
    }

    public void setMaxReloadTime(int maxReloadTime) {
        this.maxReloadTime = maxReloadTime;
    }

    public long getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(long reloadTime) {
        this.reloadTime = reloadTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LinkedList<Resource> getResources() {
        return resources;
    }

    public void setResources(LinkedList<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public Object clone() {
        try {
            Shell s = (Shell) super.clone();
            s.setClient((Client)this.client.clone());
            s.setResources((LinkedList<Resource>)this.resources.clone());
            return s;
        } catch (CloneNotSupportedException e) {
            System.out.println("Cloning not allowed.");
            return this;
        }
    }
}
