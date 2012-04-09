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
 *  Created on : Apr 9, 2012 , 6:51:44 PM
 *  Author     : princehaku
 */


package net.techest.railgun.test;

import net.techest.railgun.RailGunThread;
import net.techest.railgun.system.AddShellException;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.util.PatternHelper;

/**
 *
 * @author baizhongwei.pt
 */
public class TestPattern {


    public static void main(String[] argvs) throws AddShellException {
            PatternHelper.convertAll("http://3haku.net/$[1,20]-$[2,30]", new Resource(), new Shell());
    }
}
