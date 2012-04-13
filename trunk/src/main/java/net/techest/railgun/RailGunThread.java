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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import net.techest.railgun.system.Shell;
import net.techest.util.Configure;
import net.techest.util.Log4j;
import net.techest.util.MD5;

/**
 *
 * @author baizhongwei.pt
 */
public class RailGunThread extends TimerTask {

    private ArrayList<RailGun> railguns = new ArrayList();
    private HashMap<String, String> fileHashs = new HashMap();

    /**
     * 单例模式
     *
     */
    private static class holder {

        static Timer timer = new Timer();
        static RailGunThread instance = new RailGunThread();
    }

    public static RailGunThread getInstance() {
        return holder.instance;
    }

    /**
     * 通过xml装载一个炮弹
     *
     * @param xmlpath
     *
     */
    public synchronized void addShellXml(String xmlpath) throws AddShellException {
        RailGun railgun = null;
        try {
            File inputXml = new File(xmlpath);
            Shell shell = new Shell();
            railgun = new RailGun(inputXml, shell);
            railguns.add(railgun);
        } catch (Exception ex) {
            // 出错了.不把shell添加到节点域
            Log4j.getInstance().error("AddShell Failed Ex: " + ex.getMessage());
            railguns.remove(railgun);
            throw new AddShellException(ex);
        }
    }

    public void start() {
        Log4j.getInstance().info("RailGun开始运行...");
        holder.timer.schedule(this, 500, 500);
    }

    public void stop() {
        holder.timer.cancel();
    }

    /**
     * 执行所有装载的炮弹
     *
     */
    private void execAll() {
        RailGun railgun = null;
        Log4j.getInstance().debug("RailGun 总量"+railguns.size());
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
                t.remove();
                ex.printStackTrace();
                Log4j.getInstance().error("RailGun " + railgun.getShell().getName() + " 发生致命错误，强行终止");
            }
        }
    }

    public void checkAndAdd() {
        // 提取目录hash
        String sitesDir = Configure.getSystemConfig().getString("XML_DIR");
        File f = new File(sitesDir);
        if (!f.exists()) {
            f.mkdirs();
        }
        File[] files = f.listFiles();
        for (int i = 0, size = files.length; i < size; i++) {
            File file = files[i];
            String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if (prefix.toLowerCase().equals("xml")) {
                String newHash = MD5.getMD5((file.getName() + file.length()).getBytes());
                if (fileHashs.get(file.getName()) == null || !fileHashs.get(file.getName()).equals(newHash)) {
                    try {
                        Log4j.getInstance().info(file.getName() + "加入");
                        fileHashs.put(file.getName(), newHash);
                        this.addShellXml(file.getAbsolutePath());
                    } catch (AddShellException ex) {
                    }
                }
            }
        }
    }

    @Override
    public void run() {

        this.checkAndAdd();
        // 执行所有
        this.execAll();
    }
}
