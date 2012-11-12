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
 *  Created on : May 15, 2012 , 11:05:35 AM
 *  Author     : princehaku
 */
package net.techest.railgun.index;

/**
 * @deprecated @author baizhongwei.pt
 */
public class IndexFactory {

    /**
     * @deprecated
     */
    public static Index getIndex(String indexdir, boolean readOnly) throws IndexException {
        Index index = null;
        try {
            // 如果是以读写形式打开的 需要加锁
            if (readOnly == false) {
            }
            index = new Index(indexdir, readOnly);
        } catch (Exception ex) {
            throw new IndexException(ex.getMessage());
        }
        return index;
    }
}
