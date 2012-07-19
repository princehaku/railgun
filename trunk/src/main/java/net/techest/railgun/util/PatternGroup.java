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
 *  Created on : Apr 15, 2012 , 11:51:06 AM
 *  Author     : princehaku
 */
package net.techest.railgun.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;

/**
 * 模式组.用于多批量替换 ("url", "uu$[1,2]", true); ("cookie", "aa$[3,4]", true); ("set",
 * "ss$[5,6]", true);  conver后转换为ArrayList<HashMap<String, String>>
 *
 * @author baizhongwei.pt
 */
public class PatternGroup {

    Resource res;
    
    Shell shell;
    
    private HashMap<String, ArrayList<String>> patterns = new HashMap<String, ArrayList<String>>();
    
    private ArrayList<HashMap<String, String>> converted = new ArrayList<HashMap<String, String>>();
    
    public PatternGroup(Resource res, Shell shell){
        this.res = res;
        this.shell = shell;
    }

    public void setRes(Resource res) {
        this.res = res;
    }
    
    public void setShell(Shell shell) {
        this.shell = shell;
    }
    /**添加新字符组到HashMap中
     * 
     * @param keyName
     * @param input
     * @param convert 
     */
    public void addNewString(String keyName, String input, boolean convert) {
        ArrayList<String> convertAll = new ArrayList<String>();

        if (convert) {
            convertAll = PatternHelper.convertAll(input, this.res, this.shell);
        } else {
            convertAll.add(input);
        }

        this.patterns.put(keyName, convertAll);
    }
    /**得到转换后的结果
     * 
     * @return ArrayList<HashMap<String, String>>
     */
    public ArrayList<HashMap<String, String>> convert() {
        converted = new ArrayList<HashMap<String, String>>();
        ArrayList<String[]> vr = new ArrayList<String[]>();
        this.convertDeep(patterns, null, vr);
        return converted;
    }
    /**递归处理
     * 
     * @param patterns
     * @param keyName
     * @param vr 
     */
    private void convertDeep(HashMap<String, ArrayList<String>> patterns, String keyName, ArrayList<String[]> vr) {
        Iterator pi = patterns.keySet().iterator();
        if (!pi.hasNext()) {
            HashMap<String, String> keySet = new HashMap<String, String>();
            for (int i = 0, size = vr.size(); i < size; i++) {
                String[] s = vr.get(i);
                keySet.put(s[0], s[1]);
            }
            converted.add(keySet);
            return;
        }
        while (pi.hasNext()) {
            String key = (String) pi.next();
            ArrayList<String> sa = patterns.get(key);
            Iterator si = sa.iterator();
            HashMap<String, ArrayList<String>> patternsLeft = (HashMap<String, ArrayList<String>>) patterns.clone();
            patternsLeft.remove(key);
            while (si.hasNext()) {
                ArrayList<String[]> vrc = (ArrayList<String[]>) vr.clone();
                String[] s = new String[]{key, (String) si.next()};
                vrc.add(s);
                convertDeep(patternsLeft, key, vrc);
            }
            break;
        }
    }
}
