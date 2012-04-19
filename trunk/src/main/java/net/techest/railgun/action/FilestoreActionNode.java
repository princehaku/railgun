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
 *  Created on : Apr 14, 2012 , 2:18:10 PM
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
import net.techest.railgun.util.PatternHelper;
import net.techest.railgun.util.Log4j;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class FilestoreActionNode extends ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        // 参数path是必须的
        if (node.element("path") == null) {
            Log4j.getInstance().error("FetchNode Need An Url Parameter");
            throw new ActionException("FilestoreNode 需要参数 path");
        }

        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            FileOutputStream fw = null;
            String savePath = node.element("path").getData().toString();
            byte[] data = res.getBytes();
            // 如果数据节点存在，替换数据节点
            if (node.element("data") != null) {
                ArrayList<String> content = PatternHelper.convertAll(node.element("data").getData().toString(), res, shell);
                data = content.get(0).getBytes();
            }

            ArrayList<String> strings = PatternHelper.convertAll(savePath, res, shell);
            for (Iterator si = strings.iterator(); si.hasNext();) {
                savePath = (String) si.next();
                try {
                    File saveTo = new File(savePath);
                    fw = new FileOutputStream(savePath);
                    fw.write(data);
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
            res.putParam("savePath", savePath);

        }
        // 解除节点关联
        if (node.element("path") != null) {
            node.element("path").detach();
        }
        if (node.element("data") != null) {
            node.element("data").detach();
        }
    }
}
