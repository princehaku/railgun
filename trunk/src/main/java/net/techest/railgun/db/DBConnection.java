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
 *  Created on : Apr 24, 2012 , 9:52:10 AM
 *  Author     : princehaku
 */
package net.techest.railgun.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sql.DataSource;
import net.techest.railgun.action.ActionException;
import net.techest.railgun.util.Log4j;
import net.techest.util.ArrayTools;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class DBConnection {

    Connection connection = null;

    public DBConnection(String sourceName) throws DBException {

        DataSource d = ConnectionPool.getSystemPool().getFromPool(sourceName);
        // 数据库连接失败的话屏蔽掉异常但是打印错误
        this.connection = null;
        try {
            this.connection = d.getConnection();
            Log4j.getInstance().info("连接数据库成功 " + sourceName);
        }
        catch (SQLException ex) {
            throw new DBException("数据库连接失败" + ex.getMessage() + sourceName);
        }
    }

    public boolean existed(String formName, String[] keys, String[] values) throws DBException {
        String con = "";
        for (int i = 0; i < keys.length; i++) {
            con += " and `" + keys[i] + "` = ? ";
        }
        String sql = "select * from `" + formName + "` where 1=1 " + con + " limit 1;";
        Log4j.getInstance().debug("[SQL] " + sql);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }
            ResultSet rs = statement.executeQuery();
            // 如果存在记录,跳过
            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException ex) {
            throw new DBException("检验Consist失败 " + ex.getMessage());
        }

        return false;
    }

    public int insert(String formName, ArrayList<String> colNames, ArrayList<String> values) throws DBException {

        ArrayList<String> colsAll = new ArrayList<String>();
        Iterator enties = values.iterator();
        while (enties.hasNext()) {
            enties.next();
            colsAll.add("?");
        }
        String sql = "insert into `" + formName + "` (" + ArrayTools.implode(",", "`", colNames)
                + ") values (" + ArrayTools.implode(",", "", colsAll) + ")";
        
        Log4j.getInstance().debug("[SQL] " + sql);
        
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // 替换index
            int idx = 0;
            for (int idxParam = idx, size = values.size(); idxParam < size; idxParam++) {
                statement.setString(idxParam + 1, values.get(idxParam));
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            Log4j.getInstance().warn("表" + formName + "可能没有主键");
        }
        catch (Exception ex) {
            throw new DBException("DB存储失败 " + ex.getMessage());
        }
        return -1;
    }
}
