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
 *  Created on : May 1, 2012, 3:14:34 PM
 *  Author     : princehaku
 */
package net.techest.railgun.rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.techest.railgun.db.DBConnection;
import net.techest.railgun.db.DBException;
import net.techest.railgun.net.QuestParams;

/**
 *
 * @author princehaku
 */
public class DBHandler implements APIHandlerInterface {

    @Override
    public void handle(QuestParams requestParams, JSONObject responseJson) {
        if (requestParams.get("key") == null || requestParams.get("db") == null || requestParams.get("formName") == null
                || requestParams.get("value") == null || requestParams.get("cols") == null) {
            responseJson.put("errmsg", "Method Not Support");
            return;
        }
        
        DBConnection dbc = null;
        try {
            dbc = new DBConnection(requestParams.get("db"));
            ResultSet rs = dbc.getResultSet(requestParams.get("formName"), new String[]{requestParams.get("key")}, new String[]{requestParams.get("value")});

            JSONArray response = new JSONArray();
            while (rs.next()) {
                JSONObject enty = new JSONObject();
                String[] cols = requestParams.get("cols").split(",");
                for (String col : cols) {
                    enty.put(col, rs.getString(col));
                }
                response.add(enty);
            }

            responseJson.put("content", response);

            dbc.getConnection().close();
        }
        catch (DBException ex) {
            responseJson.put("errmsg", ex.getMessage());
            return;
        }
        catch (SQLException ex) {
            responseJson.put("errmsg", ex.getMessage());
            return;
        }
    }
}
