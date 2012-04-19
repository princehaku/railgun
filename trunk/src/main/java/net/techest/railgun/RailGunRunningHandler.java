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
 *  Created on : Apr 15, 2012 , 3:44:30 PM
 *  Author     : princehaku
 */
package net.techest.railgun;

import java.util.ArrayList;
import net.techest.railgun.util.Log4j;

/**
 *
 * @author baizhongwei.pt
 */
public class RailGunRunningHandler {

    private ArrayList<RailGun> appendingRemoval;
    final byte[] lock = new byte[]{};

    RailGunRunningHandler(ArrayList<RailGun> railguns) {
        this.appendingRemoval = railguns;
    }

    void onComplete(RailGun railgun) {
        synchronized (lock) {
            long runtime = System.currentTimeMillis() - railgun.getLastRunTime();
            Log4j.getInstance().info("完成 RailGun " + railgun.getShell().getName() + " 运行时间 [" + runtime + " ms] ");
            railgun.setNextRunTime(System.currentTimeMillis() + railgun.getShell().getReloadTime());
            if (railgun.getShell().getReloadTime() == -1) {
                Log4j.getInstance().info("Railgun 一次性任务 " + railgun.getShell().getName());
                appendingRemoval.add(railgun);
            }
        }
    }

    synchronized void onError(RailGun railgun,Exception ex) {
        synchronized (lock) {
            appendingRemoval.add(railgun);
            Log4j.getInstance().error("RailGun " + railgun.getShell().getName() + " 发生致命错误，强行终止 " +ex.getMessage());
        }
    }
}
