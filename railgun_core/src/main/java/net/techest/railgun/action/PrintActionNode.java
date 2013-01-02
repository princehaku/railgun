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

import java.util.Calendar;
import java.util.Iterator;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import org.dom4j.Element;

/**
 * 打印resource信息
 *
 * @author baizhongwei.pt
 */
class PrintActionNode extends ActionNode {

    public PrintActionNode() {
    }

    @Override
    public Shell execute(Element node, Shell shell) throws Exception {
        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            for (String key : res.getParams().keySet()) {
                System.out.println("[key]" + key);
                System.out.println("[value]" + res.getParams().get(key));
            }
            System.out.println("[content]" + res.toString());
        }
        return shell;
    }
}
