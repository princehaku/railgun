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
 *  Created on : Mar 22, 2012 , 7:31:35 PM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.util.Log4j;
import net.techest.util.MD5;
import net.techest.util.PatternHelper;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
class StoreActionNode implements ActionNode {

    public StoreActionNode() {
    }

    @Override
    public void execute(Element node, Shell bullet) {
        for (Iterator i = bullet.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            FileOutputStream fw = null;
            String savePath = node.getData().toString();
            ArrayList<String> strings = PatternHelper.convertAll(savePath, res.getRegxpResult());
            for (Iterator si = strings.iterator(); si.hasNext();) {
                savePath = (String) si.next();
                try {
                    File saveTo = new File(savePath);
                    // 如果字符串最后一个是/或者path是一个目录 并且没有存在这个目录 创建目录
                    if ((savePath.substring(savePath.length() - 1, savePath.length()).equals("/")
                            || saveTo.isDirectory()) && !saveTo.exists()) {
                        if (!saveTo.mkdirs()) {
                            Log4j.getInstance().error("Create Dir Failed [path] " + savePath);
                        }
                    }
                    if (saveTo.isDirectory()) {
                        String ext = "";
                        if (node.attribute("ext") != null) {
                            ext = node.attribute("ext").getData().toString();
                        }
                        savePath = savePath + "/" + MD5.getMD5((System.nanoTime() + "").getBytes()) + ext;
                    }
                    fw = new FileOutputStream(savePath);
                    fw.write(res.getBytes());
                    Log4j.getInstance().debug("Store Success To " + savePath);
                } catch (IOException ex) {
                    Log4j.getInstance().error("Store Error " + ex.getMessage());
                } finally {
                    try {
                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        Log4j.getInstance().error("Store Error " + ex.getMessage());
                    }
                }
            }

        }
    }
}
