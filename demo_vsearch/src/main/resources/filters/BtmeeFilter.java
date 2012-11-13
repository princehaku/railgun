
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BtmeeFilter implements Filter {

    public void process(Resource res1) {
        StringResource res = new StringResource(res1);
        Document doc = Jsoup.parse("<body><table>" + res.getText() + "</table></body>");
        // 标题
        String title = doc.select(".name a").html();
        res.putParam("title", title);
        // 目录
        String cat = doc.select(".cat a").html();
        res.putParam("cat", cat);
        // 下载链接
        String downloadLink = doc.select(".dow .magDown").attr("href");
        downloadLink += "[|||]" + doc.select(".dow .ed2kDown").attr("ed2k");
        res.putParam("links", downloadLink);
        // 大小
        String size = "";
        if (doc.select(".seed").size() > 0) {
            size = doc.select(".seed").get(0).html();
        }
        res.putParam("size", size);
        // 页面url
        String surl = doc.select(".magTitle a").attr("href");
        res.putParam("surl", "http://btmee.com" + surl);

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