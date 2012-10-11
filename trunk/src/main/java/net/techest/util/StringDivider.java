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
 *  Author     : princehaku
 */
package net.techest.util;

import java.text.ParseException;

public class StringDivider {

    private String left;
    private String right;

    public StringDivider(String string, String delim) throws ParseException {
        int pos = string.indexOf(delim);
        if (pos == -1) {
            throw new ParseException("无法分隔", 0);
        }
        this.left = (string.substring(0, pos));
        this.right = string.substring(pos + delim.length(), string.length());
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }
}
