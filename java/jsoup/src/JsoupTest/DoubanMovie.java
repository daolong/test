package src.JsoupTest;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


public class DoubanMovie {
    
    private static String TAG = "DoubanMovie";
    
    private static class Log {
        public static void d(String tag, String msg) {
            System.out.println(tag + " : " + msg);
        }
    }
    
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
            Log.d(TAG, "parse file fail");
            return;
        }
        
        // information
        Element infoElement = doc.getElementById("info");
        if (infoElement != null) {
            Elements spans = infoElement.getElementsByTag("span");
            if (spans != null && spans.size() > 0) {
                //Log.d(TAG, "span count = " + spans.size());                
                String className, property;
                for (Element e : spans) {
                    // obtain genre, release date, duration
                    property = e.attr("property");
                    if (property != null && !property.isEmpty()) {
                        //Log.d(TAG, "property = " + property);
                        if (property.equals("v:genre")) {
                            Log.d(TAG, "hit genre : " + e.text());
                        } else if (property.equals("v:initialReleaseDate")) {
                            Log.d(TAG, "hit release date : " + e.text());
                        } else if (property.equals("v:runtime")) {
                            Log.d(TAG, "hit runtime : " + e.attr("content"));
                        }
                    }

                    className = e.attr("class");
                    if (className != null && !className.isEmpty()) {
                        //Log.d(TAG, "class = " + className);
                        if (className.equals("attrs")) { 
                            // obtain director,script writer, actor
                            Elements es = e.getElementsByTag("a");
                            String rel;
                            if (es != null && es.size() > 0) {
                                for (Element ee : es) {
                                    rel = ee.attr("rel");
                                    if (rel != null) {
                                        if (rel.equals("v:directedBy")) {
                                            Log.d(TAG, "hit director : " + ee.text());
                                        } else if (rel.equals("v:starring")) {
                                            Log.d(TAG, "hit actor : " + ee.text());
                                        } else {
                                            Log.d(TAG, "script writer? : " + ee.text());
                                        }
                                    } else {
                                        Log.d(TAG, "class attrs no rel attr");
                                    }
                                }    
                            }

                        } else if (className.equals("pl")) {
                            // obtain production area/country, spoken language, other title, imdb info
                            String content = e.text();
                            //Log.d(TAG, "content : " + e.text());
                            if (content != null && !content.isEmpty()) {
                                if (content.equals("制片国家/地区:")) {
                                    Log.d(TAG, "hit area : " + e.nextSibling());
                                } else if (content.equals("语言:")) {
                                    Log.d(TAG, "hit language : " + e.nextSibling());
                                } else if (content.equals("又名:")) {
                                    Log.d(TAG, "hit other title : " + e.nextSibling());
                                } else if (content.equals("IMDb链接:")) {
                                    Log.d(TAG, "hit imdb info");
                                    Element imdbInfo = e.nextElementSibling();
                                    if (imdbInfo != null) {
                                        Log.d(TAG, "imdb url = "
                                                + imdbInfo.attr("href"));
                                        Log.d(TAG, "imdb id = " + imdbInfo.text());
                                    } else {
                                        Log.d(TAG, "can not find imdb info");
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                Log.d(TAG, "No span in info div");
            }            
        } else {
            Log.d(TAG, "No info id");
        }
        
        
        // overview
        Elements overview = doc.select("[class=indent][id=link-report]");
        if (overview != null && overview.size() > 0) {
            Element span = overview.first();
            Log.d(TAG, "Overview = " + span.text());
        } else {
            Log.d(TAG, "Can not find over view");
        }
    }
}