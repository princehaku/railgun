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

import java.util.Iterator;
import net.techest.railgun.system.Handler;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.util.Log4j;
import org.dom4j.Element;

/**
 *
 * @author baizhongwei.pt
 */
public class HandlerActionNode  implements ActionNode {

    @Override
    public void execute(Element node, Shell shell) throws Exception {
        
        // 执行过滤器
        if (node.getData() != null) {
            try {
                Handler filter = (Handler) Class.forName(node.getData().toString()).newInstance();
                Log4j.getInstance().info("Apply Handler " + node.getData().toString());
                for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
                    Resource res = (Resource) i.next();
                    filter.process(res);
                }
            } catch (Exception ex) {
                Log4j.getInstance().error(ex.getMessage());
                throw new Exception("执行自定义操作失败" + ex.getMessage());
            }
        }
    }

}
