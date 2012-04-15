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

/**
 *
 * @author baizhongwei.pt
 */
public class RailGunThread extends Thread {

    RailGun railgun;
    RailGunRunningHandler handler;

    public RailGunThread(RailGun railgun, RailGunRunningHandler handler) {
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
            //ex.printStackTrace();
            if (this.handler != null) {
                this.handler.onError(this.railgun);
            }
        }
    }
}
