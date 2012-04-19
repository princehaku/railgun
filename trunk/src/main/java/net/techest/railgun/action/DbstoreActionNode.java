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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import javax.sql.DataSource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.db.ConnectionPool;
import net.techest.railgun.system.Resource;
import net.techest.railgun.util.Log4j;
import net.techest.railgun.util.PatternHelper;
import net.techest.util.ArrayTools;
import net.techest.util.StringTools;
import org.dom4j.Attribute;
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
        DataSource d = ConnectionPool.getSystemPool().getFromPool(source);
        // 数据库连接失败的话屏蔽掉异常但是打印错误
        Connection connection = null;
        try {
            connection = d.getConnection();
            Log4j.getInstance().warn("连接数据库成功 " + source);
        }
        catch (SQLException ex) {
            Log4j.getInstance().warn("连接数据库失败 " + ex.getMessage() + source);
            if (node.element("mapping") != null) {
                node.element("mapping").detach();
            }
            return;
        }
        Iterator mappings = node.elements("mapping").iterator();
        while (mappings.hasNext()) {
            Element mapping = (Element) mappings.next();
            String formName = mapping.attributeValue("form");
            String consist = mapping.attributeValue("consist");
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
                colsName.add(StringTools.addSlashes(entry.elementTextTrim("name")));
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
                    colsValueConverted.add(StringTools.addSlashes(valueConverted.get(0)));
                }
                // 如果consist不为空.对数据进行插入效验
                if (consist != null) {
                    int pos = colsName.indexOf(consist);
                    if (pos == -1) {
                        Log4j.getInstance().error("Consist字段名不存在");
                    }
                    String sql = "select * from `" + formName + "` where `" + StringTools.addSlashes(consist)
                            + "` = '" + colsValueConverted.get(pos) + "' limit 1;";
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery(sql);
                        // 如果存在记录,跳过
                        if (rs.next()) {
                            Log4j.getInstance().debug("DB : Consist已存在 跳过存入");
                            continue;
                        }
                    }
                    catch (SQLException ex) {
                        Log4j.getInstance().debug(" [SQL] " + sql);
                        Log4j.getInstance().error("检验Consist失败 " + ex.getMessage());
                        continue;
                    }
                }

                String sql = "insert into `" + formName + "` (" + ArrayTools.implode(",", "`", colsName)
                        + ") values (" + ArrayTools.implode(",", "'", colsValueConverted) + ")";
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        res.putParam("id", rs.getInt(1) + "");
                    }
                    Log4j.getInstance().debug("存入表 " + formName + " 成功");
                }
                catch (SQLException ex) {
                    Log4j.getInstance().debug(" [SQL] " + sql);
                    Log4j.getInstance().error("DB存储失败 " + ex.getMessage());
                }
            }

            mapping.detach();
        }
    }
}