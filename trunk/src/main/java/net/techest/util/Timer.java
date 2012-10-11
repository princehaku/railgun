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
 *  Author     : princehaku
 */
package net.techest.util;

public class Timer {

    private long interval;
    private long sysTime;

    public void setInterval(long milliontime) {
        this.interval = milliontime;
        this.sysTime = System.currentTimeMillis();
    }

    public boolean isTimeUp() {
        return (System.currentTimeMillis() - this.sysTime > this.interval);
    }
}
