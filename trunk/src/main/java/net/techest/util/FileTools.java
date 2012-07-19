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
 *  Created on : 2011-4-17, 9:59:53
 *  Author     : princehaku
 */
package net.techest.util;

import net.techest.railgun.util.Log4j;
import java.io.*;

/**
 *
 * @author princehaku
 */
public class FileTools {
    /**读取一个文件的全部内容
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String getContent(String filePath){
        FileReader fr=null;
        String strContent = "";
        String line = "";
        try {
            fr = new FileReader(filePath);
        } catch (FileNotFoundException ex) {
            Log4j.getInstance().error("FileTools"+ex.getMessage());
        }
        BufferedReader br = new BufferedReader(fr);

        try {
            while ((line = br.readLine()) != null) {
                strContent += line;
            }
        } catch (IOException ex) {
            Log4j.getInstance().error("FileTools"+ex.getMessage());
        }

        return strContent;
    }

    /**读取一个文件的全部内容
     *
     * @param filePath
     * @param encode
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String getContent(String filePath,String enCode) throws UnsupportedEncodingException{
        InputStreamReader read =null;
        String strContent = "";
        String line = "";
        try {
            read   =  new InputStreamReader (new FileInputStream(filePath),enCode);
        } catch (FileNotFoundException ex) {
            Log4j.getInstance().error("FileTools"+ex.getMessage());
        }

        BufferedReader br = new BufferedReader(read);

        try {
            while ((line = br.readLine()) != null) {
                strContent += line;
            }
        } catch (IOException ex) {
            Log4j.getInstance().error("FileTools"+ex.getMessage());
        }

        return strContent;
    }

    /**得到一个文件的后缀名
     *
     * @param File file
     * @return
     */
    public static String getFileExt(File file) {
        String   filename   =   file.getName();
        int   index   =   filename.lastIndexOf( ".");
        if(index   <0   ||   index==filename.length()-1)
            return   "";
        return   filename.substring(index+1).toLowerCase();
    }
}
