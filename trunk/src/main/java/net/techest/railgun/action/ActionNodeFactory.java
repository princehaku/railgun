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
import net.techest.util.Log4j;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class ActionNodeFactory {

    public static ActionNode getNodeAction(String name) {
        ActionNode n = null;
        switch (name) {
            case "shell":
                n = new RootActionNode();
                break;
            case "fetch":
                n = new FetchActionNode();
                break;
            case "parse":
                n = new ParseActionNode();
                break;
        }
        return n;
    }

    public static void executeAction(ActionNode action, Element eone, Shell shell) {
        if (action == null) {
            Log4j.getInstance().error("No Action Found");
            return;
        }
        // 进行资源节点克隆
        if (eone.attribute("fork") != null && eone.attribute("fork").getData().toString().equals("true")) {
            Shell newshell = (Shell) shell.clone();
            Log4j.getInstance().info("Shell Cloned");
            action.execute(eone, newshell);
        } else {
            action.execute(eone, shell);
        }
    }
}
