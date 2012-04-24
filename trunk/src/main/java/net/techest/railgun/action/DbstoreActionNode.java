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
 *  Created on : Apr 14, 2012 , 2:57:40 PM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import net.techest.railgun.db.DBException;
import java.util.ArrayList;
import java.util.Iterator;
import net.techest.railgun.system.Shell;
import net.techest.railgun.db.DBConnection;
import net.techest.railgun.system.Resource;
import net.techest.railgun.util.Log4j;
import net.techest.railgun.util.PatternHelper;
import org.dom4j.Element;

/**
 * 数据库存储类
 *
 * @author baizhongwei.pt
 */
public class DbstoreActionNode extends ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        if (node.attribute("source") == null) {
            throw new ActionException("错误的数据库节点标记");
        }
        String source = node.attributeValue("source").toUpperCase();
        DBConnection connection = null;
        try {
            connection = new DBConnection(source);
        }
        catch (DBException ex) {
            if (node.elements("mapping") != null) {
                node.elements("mapping").clear();
            }
            throw new ActionException(ex.getMessage());
        }
        Iterator mappings = node.elements("mapping").iterator();
        while (mappings.hasNext()) {
            Element mapping = (Element) mappings.next();
            String formName = mapping.attributeValue("form");
            String consist = mapping.attributeValue("consist");
            ArrayList<String> colNames = new ArrayList<String>();
            ArrayList<String> colsValue = new ArrayList<String>();
            // 遍历form里面的mapping 拿到cols的名字和对应值
            if (mapping.elements("enty") == null) {
                throw new ActionException("form 标签内没有mapping规则");
            }
            Iterator enties = mapping.elements("enty").iterator();
            while (enties.hasNext()) {
                Element entry = (Element) enties.next();
                colNames.add(entry.elementTextTrim("name"));
                colsValue.add(entry.elementTextTrim("content"));
            }
            // 按资源加入到数据库
            for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
                Resource res = (Resource) i.next();
                // 组装content
                ArrayList<String> colsValueConverted = new ArrayList<String>();
                Iterator<String> valueIterator = colsValue.iterator();
                while (valueIterator.hasNext()) {
                    ArrayList<String> valueConverted = PatternHelper.convertAll(valueIterator.next(), res, shell);
                    // 只加入第一个 忽略$[xx,xx]规则
                    colsValueConverted.add(valueConverted.get(0));
                }
                // 如果consist不为空.对数据进行插入效验
                if (consist != null) {
                    int pos = colNames.indexOf(consist);
                    if (pos == -1) {
                        Log4j.getInstance().error("Consist字段名不存在");
                    }
                    try {
                        String keys[] = {consist};
                        String values[] = {colsValueConverted.get(pos)};
                        if (connection.existed(formName, keys, values)) {
                            Log4j.getInstance().debug("DB : Consist已存在 跳过存入");
                            continue;
                        }
                    }
                    catch (DBException ex) {
                        Log4j.getInstance().error(ex.getMessage());
                    }
                }

                try {
                    int id = connection.insert(formName, colNames, colsValueConverted);
                    if (id > 0) {
                        res.putParam("id", +id + "");
                        Log4j.getInstance().debug("[ID] " + id + " 存入表 " + formName + " 成功");
                    }
                }
                catch (DBException ex) {
                    Log4j.getInstance().error(ex.getMessage());
                }

            }
            Log4j.getInstance().info("DB " + formName + " 存入完成");
            mapping.detach();
        }
    }
}
