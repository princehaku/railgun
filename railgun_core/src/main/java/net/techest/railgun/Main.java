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
 *  Created on : Apr 1, 2012 , 2:37:37 PM
 *  Author     : princehaku
 */
package net.techest.railgun;

import net.techest.railgun.rest.APIServer;
import net.techest.railgun.thread.RailGunThreadPool;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;

/**
 *
 * @author baizhongwei.pt
 */
public class Main {

    public static void main(String[] argvs) {
        if (Configure.getSystemConfig().getString("REST_ENABLE", "true").equals("true")) {
            Log4j.getInstance().info("API 尝试启动");
            APIServer.getInstance().start();
        } else {
            Log4j.getInstance().info("API 不启动" + Configure.getSystemConfig().getString("REST_ENABLE", "true"));
        }
        Log4j.getInstance().info("RailGun开始运行...");
        RailGunThreadPool.getInstance().start();
    }
}
