import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.system.Handler;
import net.techest.railgun.system.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BtmeeHandler implements Handler {

    @Override
    public void process(Resource res) {
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
        res.putParam("surl","http://btmee.com" + surl);
        
        // 全文索引
        res.putParam("index", title + downloadLink);
        try {
            res.setBytes((cat + title + downloadLink).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BtmeeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}