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
package net.techest.railgun.thread;

import net.techest.railgun.system.AddShellException;
import java.io.File;
import java.util.*;
import net.techest.railgun.RailGun;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import net.techest.util.MD5;

/**
 *
 * @author baizhongwei.pt
 */
public class RailGunThreadPool extends TimerTask {

    /**
     * 当前运行中的railguns线程
     *
     */
    private LinkedList<RailGunThread> railgunThreads = new LinkedList<RailGunThread>();
    /**
     * 当前运行中的railguns
     *
     */
    private ArrayList<RailGun> railguns = new ArrayList();
    /**
     * 待移除的railgun节点
     *
     */
    private ArrayList<RailGun> appendingRemoval = new ArrayList();
    /**
     * 目录内文件hash
     *
     */
    private HashMap<String, String> fileHashes = new HashMap();
    /**
     * railgun运行处理器 负责任务完成，失败的消息处理
     */
    private RailGunFinishHandler handler = new RailGunFinishHandler(appendingRemoval);

    private static class holder {

        static Timer timer = new Timer();
        static RailGunThreadPool instance = new RailGunThreadPool();
    }

    /**
     * 得到唯一实例
     *
     */
    public static RailGunThreadPool getInstance() {
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
            railgun.setHandler(this.handler);
            railguns.add(railgun);
        }
        catch (Exception ex) {
            // 出错了.不把shell添加到节点域
            Log4j.getInstance().error("AddShell Failed Ex: " + ex.getMessage());
            railguns.remove(railgun);
            throw new AddShellException(ex);
        }
    }

    public void start() {
        Log4j.getInstance().info("RailGun开始运行...");
        holder.timer.schedule(this, 0, 500);
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
        Log4j.getInstance().debug("SITE XML 总量" + railguns.size());
        // 从待移除列表获取并移除
        for (Iterator<RailGun> t = appendingRemoval.iterator(); t.hasNext();) {
            railgun = (RailGun) t.next();
            // 从任务进程中获取并销毁
            for (Iterator<RailGunThread> rt = railgunThreads.iterator(); rt.hasNext();) {
                RailGunThread rgt = rt.next();
                if (rgt.isForYou(railgun)) {
                    rt.remove();
                    rgt = null;
                }
            }
            railguns.remove(railgun);
            t.remove();
        }
        for (Iterator<RailGun> t = railguns.iterator(); t.hasNext();) {
            railgun = (RailGun) t.next();
            if (railgun.getNextRunTime() <= System.currentTimeMillis()) {
                Log4j.getInstance().info("运行 RailGun " + railgun.getShell().getName());
                // 设置不再运行，线程结束时会自动重写并让其运行
                railgun.setLastRunTime(System.currentTimeMillis());
                railgun.setNextRunTime(Long.MAX_VALUE);
                RailGunThread rt = new RailGunThread(railgun, handler);
                railgunThreads.add(rt);
                rt.start();
            }
        }
    }

    /**
     * 检测目录中xml文件状态并添加到队列中
     *
     */
    public void checkAndAdd() {
        // 提取目录文件hash
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
                String newHash = MD5.getMD5(( file.getName() + file.length() + file.lastModified() ).getBytes());
                if (fileHashes.get(file.getName()) == null || !fileHashes.get(file.getName()).equals(newHash)) {
                    try {
                        Log4j.getInstance().info(file.getName() + " 有更新");
                        // 如果railgun里面已经存在railgun 标记为已变更
                        boolean isRunning = false;
                        for (Iterator<RailGun> t = railguns.iterator(); t.hasNext();) {
                            RailGun railgun = (RailGun) t.next();
                            if (railgun.getFileName().equals(file.getName())) {
                                Log4j.getInstance().info(file.getName() + " 加入更新队列");
                                railgun.setReload(true);
                                for (Iterator<RailGunThread> rt = railgunThreads.iterator(); rt.hasNext();) {
                                    RailGunThread rgt = rt.next();
                                    if (rgt.isForYou(railgun)) {
                                        isRunning = true;
                                    }
                                }
                                // 如果没有正在运行的进程，从运行库中删掉更新的，并且更新下次运行时间
                                if (isRunning = false) {
                                    railgun.setNextRunTime(System.currentTimeMillis());
                                    appendingRemoval.add(railgun);
                                }
                                isRunning = true;
                            }
                        }
                        // 全新的就全新加入新的xml
                        if (isRunning == false) {
                            this.addShellXml(file.getAbsolutePath());
                        }
                        fileHashes.put(file.getName(), newHash);
                    }
                    catch (AddShellException ex) {
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        // 检测目录
        this.checkAndAdd();
        // 执行所有
        this.execAll();
    }
}
