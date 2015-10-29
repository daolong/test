package src.JsoupTest;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


public class DoubanMovie {
    public static void main(String[] args) throws IOException {
        System.out.println("length = " + args.length);
        if (args.length < 1) {
            System.out.println("usage: java -cp jsoup-1.8.3.jar src.JsoupTest.DoubanMovie file");
            return;
        }
        
        String filename = args[0];
        
        File file = new File(filename);
        
        Document doc = Jsoup.parse(file, "UTF-8");
        if (doc == null) {
            System.out.println("parse file fail");
            return;
        }
        
        // information
        Element infoElement = doc.getElementById("info");
        if (infoElement != null) {
            Elements spans = infoElement.getElementsByTag("span");
            System.out.println("span count = " + spans.size());
            for (Element e : spans) {
                String className = e.attr("class");
                System.out.println("class = " + className);
                if (className != null && !className.isEmpty() && className.equals("pl")) {
                    System.out.println("text : " + e.text());
                    System.out.println("nextSibling : " + e.nextSibling());
                    if (e.text().startsWith("IMDb")) {
                        System.out.println("match imdb info");
                        Element imdbInfo = e.nextElementSibling();
                        if (imdbInfo != null) {
                            System.out.println("imdb url = " + imdbInfo.attr("href"));
                            System.out.println("imdb id = " + imdbInfo.text());
                        } else {
                            System.out.println("can not find imdb info");
                        }
                    }
                } 
                
            }
            
        } else {
            System.out.println("No info id");
        }
        
        
        // overview
        Elements overview = doc.select("[class=indent][id=link-report]");
        if (overview != null && overview.size() > 0) {
            Element span = overview.first();
            System.out.println("Overview = " + span.text());
        } else {
            System.out.println("Can not find over view");
        }
    }
}