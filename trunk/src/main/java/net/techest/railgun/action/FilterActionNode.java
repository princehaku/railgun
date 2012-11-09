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
 *  Created on : Apr 9, 2012 , 11:22:50 AM
 *  Author     : princehaku
 */
package net.techest.railgun.action;

import net.techest.railgun.jit.FiltersInvoker;
import net.techest.railgun.system.Shell;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class FilterActionNode extends ActionNode {

    @Override
    public Shell execute(Element node, Shell shell) throws Exception {
        // 动态编译和执行脚本
        if (node.getData() == null) {
            throw new ActionException("请填入Filter类名，然后放置在filters文件夹内");
        }
        String className = node.getTextTrim();
        new FiltersInvoker().invoke(shell, className, className);
        return shell;
    }
}
