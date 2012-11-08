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
 *  Created on : Apr 9, 2012 , 11:22:50 AM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import net.techest.railgun.system.Handler;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import net.techest.util.MD5;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class HandlerActionNode extends ActionNode {

    /**
     * 脚本文件hash
     *
     */
    private static HashMap<String, String> fileHashes = new HashMap();

    @Override
    public Shell execute(Element node, Shell shell) throws Exception {
        // 动态编译和执行脚本
        if (node.getData() == null) {
            throw new ActionException("请输入Handler类名");
        }
        String className = node.getTextTrim();
        String scriptDirString = Configure.getSystemConfig().getString("HANDLER_DIR", "handlers");
        File scriptDir = new File(scriptDirString);

        String filePath = scriptDir.getAbsolutePath() + "/" + className + ".java";
        File file = new File(filePath);
        URL[] urls = new URL[]{new URL("file://" + scriptDir.getAbsolutePath() + "/")};
        Log4j.getInstance().info("脚本目录  file:/" + scriptDir.getAbsolutePath() + "/");
        URLClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
        Handler handler = null;
        // 直接加载一次
        boolean loaded = false;
        // 不在hash里的有更新的文件则重新编译
        String newHash = MD5.getMD5((file.getName() + file.length() + file.lastModified()).getBytes());
        if (HandlerActionNode.fileHashes.containsKey(file.getName()) && HandlerActionNode.fileHashes.get(file.getName()).equals(newHash)) {
            try {
                handler = (Handler) ((classLoader.loadClass(className)).newInstance());
                loaded = true;
            } catch (Exception ex) {
                Log4j.getInstance().info(className + "加载失败，尝试动态编译。" + ex.getMessage());
            }
        }
        // 加载失败就编译一次
        try {
            if (loaded == false) {
                JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
                Log4j.getInstance().info("Compile: " + filePath);
                if (javac == null) {
                    throw new ActionException("javac无法获取");
                }
                int status = javac.run(null, null, null, "-d", scriptDir.getAbsolutePath(), filePath);
                if (status != 0) {
                    throw new ActionException("编译script失败" + status);
                }
                handler = (Handler) ((classLoader.loadClass(className)).newInstance());
                loaded = true;
            }
        } catch (Exception ex) {
            throw new ActionException(className + "类动态加载失败" + ex.getMessage());
        }
        HandlerActionNode.fileHashes.put(file.getName(), newHash);
        Log4j.getInstance().info("Apply Handler " + className);
        try {
            for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
                Resource res = (Resource) i.next();
                handler.process(res);
            }
        } catch (Exception ex) {
            Log4j.getInstance().error("自定义类执行失败" + ex.getMessage());
        }
        return shell;
    }
}
