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

import java.util.Iterator;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
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
        for (Iterator i = bullet.getResource().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            System.out.println(res.toString());
        }

    }
}
