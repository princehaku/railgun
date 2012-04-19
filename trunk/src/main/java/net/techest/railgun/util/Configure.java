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
 *  Created on : 2010-12-21, 16:19:30
 *  Author     : princehaku
 */
package net.techest.railgun.util;

import java.io.*;
import java.util.Properties;

/**
 * 配置文件读取和写入类
 *
 * @author princehaku
 */
public class Configure {

    Properties propertie;
    String filePath = "";

    private static ClassLoader getClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null) {
            loader = Configure.class.getClassLoader();
        }
        return loader;
    }

    /**
     * 单例模式
     *
     */
    private static class holder {

        static Configure instance = new Configure("system.propertie");
    }

    public static Configure getSystemConfig() {
        return Configure.holder.instance;
    }

    /**
     * 默认构造函数 需要优先读取jar包内配置文件
     *
     * @param filePath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Configure(String filePath) {

        propertie = new Properties();

        InputStream inputFile = null;

        try {
            inputFile = getClassLoader().getResourceAsStream(filePath);

            if (inputFile == null) {
                inputFile = new FileInputStream(filePath);
                if (inputFile == null) {
                    throw new FileNotFoundException(filePath);
                }
            }
            propertie.load(inputFile);

            this.filePath = filePath;

        }
        catch (FileNotFoundException ex) {
            Log4j.getInstance().error(this.getClass().getName() + "配置文件" + filePath + "不存在 " + ex.getMessage());
            return;
        }
        catch (IOException ex) {
            Log4j.getInstance().error(this.getClass().getName() + "配置文件" + filePath + "读取失败 " + ex.getMessage());
            return;

        }
    }

    /**
     * 得到字符串型值
     *
     * @param key
     * @return String
     */
    public String getString(String key) {
        String res = propertie.getProperty(key);
        if (res == null) {
            Log4j.getInstance().error(this.getClass().getName() + "属性" + key + "获取失败，请检查配置文件");
        }
        return res;
    }

    /**
     * 得到整型值
     *
     * @param key
     * @return int
     */
    public int getInt(String key) {
        int r = Integer.parseInt(propertie.getProperty(key));
        if (propertie.getProperty(key) == null) {
            Log4j.getInstance().error(this.getClass().getName() + "属性" + key + "获取失败，请检查配置文件");
        }
        return r;
    }


    /**
     * 得到字符串型值，如果不存在，返回defaultValue
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        String res = propertie.getProperty(key);
        if (res == null) {
            res = defaultValue;
        }
        return res;
    }

    /**
     * 得到整型值，如果不存在，返回defaultValue
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        int r = Integer.parseInt(propertie.getProperty(key));
        if (propertie.getProperty(key) == null) {
            r = defaultValue;
        }
        return r;
    }
    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        propertie.setProperty(key, value);
    }

    /**
     * 保存配置文件到指定位置
     *
     * 为空的话会覆盖原来的文件
     *
     * @param filePath
     */
    public void saveFile(String filePath) {
        try {
            if (filePath == null || filePath.equals("")) {
                filePath = this.filePath;
            }
            FileOutputStream outputFile = new FileOutputStream(filePath);
            propertie.store(outputFile, "Configure File");
            outputFile.close();
        }
        catch (FileNotFoundException ex) {
            Log4j.getInstance().error(this.getClass().getName() + "配置文件保存失败" + ex.getMessage());
        }
        catch (IOException ex) {
            Log4j.getInstance().error(this.getClass().getName() + "配置文件保存失败" + ex.getMessage());
        }
    }
}
