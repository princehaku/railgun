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
 *  Created on : Mar 23, 2012 , 8:14:30 PM
 *  Author     : princehaku
 */
package net.techest.railgun.test;

import net.techest.util.StringTools;

/**
 *
 * @author baizhongwei.pt
 */
public class TestFilter {

    public static void main(String[] argvs) {
        String a = "<tbody onmouseover=\"rowOver(this)\" onmouseout=\"rowOut(this)\">  <tr>   <td class=\"cat\"><a href=\"/?cid=1\" class=\"sbule\">美剧</a></td>   <td class=\"name magTitle\" flag=\"down\" rel=\"100\"><input type=\"checkbox\" /><a href=\"show.php?tid=35595\" rel=\"35595\" target=\"_blank\">广告狂人.Mad.Men.S05E03.Chi_Eng.HDTVrip.624X352-YYeTs人人影视.rmvb【Auto】</a></td>   <td class=\"dow\"><a href=\"javascript:void();\" class=\"qvodView\" onclick=\"return false;\"></a><a href=\"magnet:?xt=urn:btih:1bd36b26fe82b9cff2bf23cfe002309469d8b11a&amp;tr.0=http://tracker.openbittorrent.com/announce&amp;tr.1=udp://tracker.openbittorrent.com:80/announce&amp;tr.2=http://tracker.thepiratebay.org/announce&amp;tr.3=http://tracker.publicbt.com/announce&amp;tr.4=http://tracker.prq.to/announce&amp;tr.5=http://torrent-download.to:5869/announce\" class=\"magDown\" onclick=\"ajax_downCount(35595)\">磁力下载</a><a ed2k=\"ed2k://|file|%E5%B9%BF%E5%91%8A%E7%8B%82%E4%BA%BA.Mad.Men.S05E03.Chi_Eng.HDTVrip.624X352-YYeTs%E4%BA%BA%E4%BA%BA%E5%BD%B1%E8%A7%86.rmvb|200728651|0b2afd30f275b0c8671c4c61861ca110|h=gq4rpbvfxbltjzklfccemfvkp4hyy7ht|/\" class=\"ed2kDown\"></a>   <script language=\"javascript\">var thunder_url = \"ed2k://|file|%E5%B9%BF%E5%91%8A%E7%8B%82%E4%BA%BA.Mad.Men.S05E03.Chi_Eng.HDTVrip.624X352-YYeTs%E4%BA%BA%E4%BA%BA%E5%BD%B1%E8%A7%86.rmvb|200728651|0b2afd30f275b0c8671c4c61861ca110|h=gq4rpbvfxbltjzklfccemfvkp4hyy7ht|/\";var thunder_pid = \"37361\";var restitle = \"\";document.write('<a class=\"thunder\" href=\"#\" title=\"\" thunderHref=\"' + ThunderEncode(thunder_url) + '\" thunderPid=\"' + thunder_pid + '\" thunderResTitle=\"' + restitle + '\" onClick=\"return OnDownloadClick_Simple(this,2,4)\" oncontextmenu=\"ThunderNetwork_SetHref(this)\"></a> ');   </script></td>   <td class=\"time\">13:28</td>   <td class=\"seed\">191.4 MB</td>   <td class=\"seed\">52</td>  </tr> </tbody>";

        System.out.print(StringTools.addSlashes(a));
    }
}
