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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.sql.DataSource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.db.ConnectionPool;
import net.techest.railgun.system.Resource;
import net.techest.railgun.util.Log4j;
import net.techest.railgun.util.PatternHelper;
import net.techest.util.ArrayTools;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * 数据库存储类
 *
 * @author baizhongwei.pt
 */
public class DbstoreActionNode implements ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        Properties p = new Properties();
        for (int i = 0, size = node.element("resource").attributeCount(); i < size; i++) {
            Attribute attr = node.element("resource").attribute(i);
            p.setProperty(attr.getName(), attr.getValue());
        }
        node.element("resource").detach();
        DataSource d = ConnectionPool.setupDataSource(p);
        // 数据库连接失败的话屏蔽掉异常但是打印错误
        Connection connection = null;
        try {
            connection = d.getConnection();
        }
        catch (SQLException ex) {
            Log4j.getInstance().warn("连接数据库失败 " + ex.getMessage() + d.toString());
            return;
        }
        Iterator mappings = node.elements("mapping").iterator();
        while (mappings.hasNext()) {
            Element mapping = (Element) mappings.next();
            String formName = mapping.attributeValue("form");
            ArrayList<String> colsName = new ArrayList<String>();
            ArrayList<String> colsValue = new ArrayList<String>();
            // 遍历form里面的mapping 拿到cols的名字和对应值
            if (mapping.elements("enty") == null) {
                Log4j.getInstance().warn("form 标签内没有mapping规则");
                continue;
            }
            Iterator enties = mapping.elements("enty").iterator();
            while (enties.hasNext()) {
                Element entry = (Element) enties.next();
                colsName.add(entry.elementTextTrim("name").replaceAll("\'", "\\\'").replaceAll("\"", "\\\""));
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
                    colsValueConverted.add(valueConverted.get(0).replaceAll("\\\'", "\\\\\'").replaceAll("\\\"", "\\\\\""));
                }
                String sql = "insert into `" + formName + "` (" + ArrayTools.implode(",", "`", colsName) + ") values (" + ArrayTools.implode(",", "'", colsValueConverted) + ")";
                try {
                    connection.createStatement().executeUpdate(sql);
                }
                catch (SQLException ex) {
                    Log4j.getInstance().debug(" [SQL] " + sql);
                    Log4j.getInstance().error("SQL执行失败" + ex.getMessage());
                }
            }

            mapping.detach();
            Log4j.getInstance().info("表" + formName + "存储完毕");
        }
    }
}
