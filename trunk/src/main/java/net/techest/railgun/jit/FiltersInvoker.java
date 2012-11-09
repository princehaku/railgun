/**  
 * Copyright 2012 Etao Inc.
 *
 *  Project Name : net.techest_railgun_jar_0.2
 *  Created on : Nov 9, 2012, 2:43:27 PM
 *  Author     : haku
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.techest.railgun.jit;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Configure;
import net.techest.railgun.util.Log4j;
import net.techest.util.MD5;

/**
 *
 * @author haku
 */
public class FiltersInvoker {

    /**
     * 脚本文件hash
     *
     */
    private static HashMap<String, String> fileHashes = new HashMap();
    
    public void invoke(Shell shell,String className, String methodName) throws JitException {
        String scriptDirString = Configure.getSystemConfig().getString("FILTERS_DIR", "filters");
        File scriptDir = new File(scriptDirString);
        scriptDir.mkdirs();

        String filePath = scriptDir.getAbsolutePath() + "/" + className + ".java";
        File file = new File(filePath);
        URL[] urls;
        try {
            urls = new URL[]{new URL("file://" + scriptDir.getAbsolutePath() + "/")};
        } catch (MalformedURLException ex) {
            throw new JitException("脚本目录  file:/" + scriptDir.getAbsolutePath() + "/ 错误");
        }
        Log4j.getInstance().info("脚本目录  file:/" + scriptDir.getAbsolutePath() + "/");
        URLClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
        Filter filter = null;
        // 直接加载一次
        boolean loaded = false;
        // 不在hash里的有更新的文件则重新编译
        String newHash = MD5.getMD5((file.getName() + file.length() + file.lastModified()).getBytes());
        if (FiltersInvoker.fileHashes.containsKey(file.getName()) && FiltersInvoker.fileHashes.get(file.getName()).equals(newHash)) {
            try {
                filter = (Filter) ((classLoader.loadClass(className)).newInstance());
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
                    throw new JitException("javac无法获取");
                }
                int status = javac.run(null, null, null, "-d", scriptDir.getAbsolutePath(), filePath);
                if (status != 0) {
                    throw new JitException("编译script失败" + status);
                }
                filter = (Filter) ((classLoader.loadClass(className)).newInstance());
                loaded = true;
            }
        } catch (Exception ex) {
            throw new JitException(className + "类动态加载失败" + ex.getMessage());
        }
        FiltersInvoker.fileHashes.put(file.getName(), newHash);
        Log4j.getInstance().info("Apply Handler " + className);
        try {
            filter.process(shell);
        } catch (Exception ex) {
            Log4j.getInstance().error("自定义类执行失败" + ex.getMessage());
            throw new JitException(className + "自定义类执行失败" + ex.getMessage());
        }
    }
}
