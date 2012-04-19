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
import java.util.Iterator;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import net.techest.railgun.system.Handler;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class HandlerActionNode extends ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        // 动态编译和执行脚本
        if (node.getData() == null) {
            throw new ActionException("请输入Handler类名");
        }
        String className = node.getTextTrim();
        String scriptDirString = Configure.getSystemConfig().getString("HANDLER_DIR");
        File scriptDir = new File(scriptDirString);
        URL[] urls = new URL[]{new URL("file:/" + scriptDir.getAbsolutePath() + "/")};
        URLClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
        Handler handler = null;
        // 直接加载一次
        boolean loaded = false;
        try {
            handler = (Handler) ( ( classLoader.loadClass(className) ).newInstance() );
            loaded = true;
        }
        catch (Exception ex) {
            Log4j.getInstance().info(className + "加载失败，尝试动态编译。" + ex.getMessage());
        }
        // 加载失败就编译一次
        try {
            if (loaded == false) {
                JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
                Log4j.getInstance().info("Compile: " + scriptDir.getAbsolutePath() + "/" + className + ".java");
                int status = javac.run(null, null, null, "-d", scriptDir.getAbsolutePath(), scriptDir.getAbsolutePath() + "/" + className + ".java");
                if (status != 0) {
                    throw new ActionException("编译script失败" + status);
                }
                handler = (Handler) ( ( classLoader.loadClass(className) ).newInstance() );
                loaded = true;
            }
        }
        catch (Exception ex) {
            throw new ActionException(className + "动态加载失败" + ex.getMessage());
        }

        Log4j.getInstance().info("Apply Handler " + className);
        try {
            for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
                Resource res = (Resource) i.next();
                handler.process(res);
            }
        }
        catch (Exception ex) {
            throw new ActionException("执行自定义操作失败" + ex.getMessage());
        }
    }
}