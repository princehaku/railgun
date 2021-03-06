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
 *  Created on : Mar 23, 2012 , 8:12:14 PM
 *  Author     : princehaku
 */
package net.techest.railgun.jit;

import net.techest.railgun.system.Shell;

/**
 * 处理器 可以对资源节点进行操作
 *
 * @author baizhongwei.pt
 */
public interface Filter {

    public void process(Shell shell);
}
