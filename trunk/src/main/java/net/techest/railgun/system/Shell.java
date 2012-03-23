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
 *
 * @author baizhongwei.pt
 */
public class Shell implements Cloneable{
    String name;
    String description;
    String baseUrl;
    // 连接器 (枪  要有枪才能发子弹撒~)
    Client client;
    // 资源类
    LinkedList<Resource> resources;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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
            // call clone in Object.
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Cloning not allowed.");
            return this;
        }
    }
}
