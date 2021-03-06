
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.jit.Filter;
import net.techest.railgun.system.Resource;
import net.techest.railgun.system.Shell;
import net.techest.railgun.system.StringResource;
import net.techest.util.MD5;
import net.techest.util.SHA;
import net.techest.util.StringTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SomaFilter implements Filter {

    public void process(Resource res1) {
        StringResource res = new StringResource(res1);
        Document doc = Jsoup.parse("<body><table>" + res.getText() + "</table></body>");
        // 标题
        String title = doc.select("h2 a").html();
        res.putParam("title", title);
        // 目录
        String cat = doc.select(".category a").html();
        res.putParam("cat", cat);
        // 下载链接
        String downloadLink = doc.select(".magnet a").attr("href");
        res.putParam("links", downloadLink);
        // 大小
        String size = "";
        String info = doc.select(".info").html();
        size = StringTools.findMc(info, "([\\d\\.]*[MGK]B)", 0);
        res.putParam("size", size);
        // 页面url
        String surl = doc.select("h2 a").attr("href");
        res.putParam("surl", surl);
        
        // 全文索引
        res.putParam("index", title + downloadLink);
        
        // hash
        byte[] hashBytes = (cat + title + downloadLink).getBytes();
        String hash = MD5.getMD5(hashBytes) + SHA.getSHA1(hashBytes);
        res.putParam("hash", hash);
        res.putParam("result", cat + title + downloadLink);
    }
    
    @Override
    public void process(Shell shell) {
        for (Iterator i = shell.getResources().iterator(); i.hasNext();) {
            Resource res = (Resource) i.next();
            this.process(res);
        }
    }
}