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
 *  Created on : Mar 22, 2012 , 9:51:53 AM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import net.techest.railgun.system.Shell;
import net.techest.railgun.util.Log4j;
import org.dom4j.Element;

/**
 * ActionNode自动装载工厂
 *
 * @author baizhongwei.pt
 */
public class ActionNodeFactory {

    public static ActionNode getNodeAction(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase();
        ActionNode n = null;
        try {
            n = (ActionNode) Class.forName("net.techest.railgun.action." + name + "ActionNode").newInstance();
        } catch (Exception ex) {
            Log4j.getInstance().warn(ex.getMessage());
        }
        return n;
    }

    /**
     * execute an actionNode
     *
     * @param action
     * @param element
     * @param shell
     * @return
     * @throws Exception
     */
    public static Shell executeAction(ActionNode action, Element element, Shell shell) throws Exception {
        return action.execute(element, shell);
    }
}
