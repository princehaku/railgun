
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.techest.railgun.system.Handler;
import net.techest.railgun.system.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class OabtHandler implements Handler {

    @Override
    public void process(Resource res) {
        Document doc = Jsoup.parse("<body><table>" + res.getText() + "</table></body>");
        String cat = doc.select(".cat a").html();
        res.putParam("cat", cat);
        String title = doc.select(".name a").html();
        res.putParam("title", title);
        String downloadLink = doc.select(".dow a").outerHtml();
        res.putParam("links", downloadLink);
        String size = "";
        if (doc.select(".seed").size() > 0) {
            size = doc.select(".seed").get(0).html();
        }
        res.putParam("size", size);
        res.putParam("index", title + downloadLink);
        
        try {
            res.setBytes((cat + title + downloadLink).getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OabtHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}