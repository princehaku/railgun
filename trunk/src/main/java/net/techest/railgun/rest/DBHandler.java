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

import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            DBConnection dbc = new DBConnection(requestParams.get("db"));
        } catch (DBException ex) {
            responseJson.put("errmsg", ex.getMessage());
        }
    }

}
