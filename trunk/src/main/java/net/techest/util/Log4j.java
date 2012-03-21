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
 *  Created on : 2010-11-17, 8:21:36
 *  Author     : princehaku
 */

package net.techest.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
/**日志类
 *
 * @author princehaku
 */
public class Log4j {

    private static class InstanceHolder {

        final static Log4j instance = new Log4j();
    }
    /**得到单例
     * 
     * @return 
     */
    public static Logger getInstance() {
        return InstanceHolder.instance.getLogger();
    }

    private Logger logger;

    public Logger getLogger(){
        
        if(logger==null){
            
            BasicConfigurator.configure();
        }

        logger = Logger.getLogger("");
        
        return logger;
    }
    
}
