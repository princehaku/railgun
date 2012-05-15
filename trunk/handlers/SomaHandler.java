import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.system.Handler;
import net.techest.railgun.system.Resource;
import net.techest.util.StringTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SomaHandler implements Handler {

    @Override
    public void process(Resource res) {
        Document doc = Jsoup.parse("<body><table>" + res.getText() + "</table></body>");
        String cat = doc.select(".category a").html();
        res.putParam("cat", cat);
        String title = doc.select("h2 a").html();
        res.putParam("title", title);
        String downloadLink = doc.select(".magnet a").outerHtml();
        res.putParam("links", downloadLink);
        String size = "";
        String info = doc.select(".info").html();
        size = StringTools.findMc(info, "([\\d\\.]*MB)", 0);
        res.putParam("size", size);
        res.putParam("index", title + downloadLink);
        
        try {
            res.setBytes((cat + title + downloadLink).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SomaHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}