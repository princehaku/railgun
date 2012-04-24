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
 *  Created on : Apr 15, 2012 , 4:13:07 PM
 *  Author     : princehaku
 */
package net.techest.railgun;

import net.techest.railgun.util.Log4j;

/**
 *
 * @author baizhongwei.pt
 */
public class RailGunThread extends Thread {

    RailGun railgun;
    RailGunFinishHandler handler;
    
    /**是否是对应某railgun的线程
     * 
     * @param railgun
     * @return 
     */
    public boolean isForYou(RailGun railgun) {
        return this.railgun.equals(railgun);
    }
    

    public RailGunThread(RailGun railgun, RailGunFinishHandler handler) {
        this.railgun = railgun;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            this.railgun.fire();
            if (this.handler != null) {
                this.handler.onComplete(this.railgun);
            }
        }
        catch (Exception ex) {
            if (this.handler != null) {
                this.handler.onError(this.railgun, ex);
            }
        }
    }
}
