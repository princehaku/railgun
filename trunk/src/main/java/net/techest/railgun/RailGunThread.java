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
 *  Created on : Mar 30, 2012 , 4:01:05 PM
 *  Author     : princehaku
 */
package net.techest.railgun;

import net.techest.railgun.system.AddShellException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import net.techest.railgun.system.Shell;
import net.techest.util.Log4j;

/**
 *
 * @author baizhongwei.pt
 */
public class RailGunThread extends Thread {

    /**
     * 单例模式
     *
     */
    private static class holder {

        static RailGunThread instance = new RailGunThread();
    }

    public static RailGunThread getInstance() {
        return holder.instance;
    }
    private ArrayList<RailGun> railguns = new ArrayList();

    /**
     * 通过xml装载一个炮弹
     *
     * @param xmlpath
     *
     */
    public synchronized void addShellXml(String xmlpath) throws AddShellException {
        try {
            File inputXml = new File(xmlpath);
            Shell shell = new Shell();
            RailGun railgun = new RailGun(inputXml, shell);
            railguns.add(railgun);
            if (this.getState().equals(Thread.State.NEW)) {
                this.start();
            }
        } catch (Exception ex) {
            // 出错了.不把shell添加到节点域
            Log4j.getInstance().error("AddShell Failed Ex: " + ex.getMessage());
            throw new AddShellException(ex);
        }
    }

    /**
     * 执行所有装载的炮弹
     *
     */
    private void execAll() {
        RailGun railgun = null;
        for (Iterator<RailGun> t = railguns.iterator(); t.hasNext();) {
            try {
                railgun = (RailGun) t.next();
                if (railgun.getNextRunTime() <= System.currentTimeMillis()) {
                    Log4j.getInstance().info("运行 RailGun " + railgun.getShell().getName());
                    railgun.fire();
                    Log4j.getInstance().info("完成 RailGun " + railgun.getShell().getName());
                    railgun.setLastRunTime(System.currentTimeMillis());
                    railgun.setNextRunTime(System.currentTimeMillis() + railgun.getShell().getReloadTime());
                }
            } catch (Exception ex) {
                // 执行失败移除该单个railgun
                Log4j.getInstance().error("RailGun " + railgun.getShell().getName() + " 发生致命错误，强行终止");
                t.remove();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            this.execAll();
        }
    }
}
