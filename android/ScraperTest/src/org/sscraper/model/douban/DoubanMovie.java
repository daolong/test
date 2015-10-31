/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.model.douban;

import java.util.ArrayList;

import org.sscraper.Status;
import org.sscraper.model.NameItem;
import org.sscraper.utils.Log;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DoubanMovie {
    private static String TAG = "DoubanMovie";
    private static boolean LOGD = false;
    
    private ArrayList<NameItem> directors = new ArrayList<NameItem>();
    private ArrayList<NameItem> script_writers = new ArrayList<NameItem>();
    private ArrayList<NameItem> actors = new ArrayList<NameItem>();
    private ArrayList<NameItem> genres = new ArrayList<NameItem>();
    
    private String area;
    private String spoken_language;
    private String release_date;
    private long  duration;
    private String other_title;
    private String imdb_link;
    private String imdb_id;
    
    private String over_view;
    
    public ArrayList<NameItem> getDirectors() { return this.directors; }
    public void addDirector(NameItem director) { this.directors.add(director); }
    
    public ArrayList<NameItem> getScriptWriter() { return this.script_writers; }
    public void addScriptWriter(NameItem script_writer) { this.script_writers.add(script_writer); }
    
    public ArrayList<NameItem> getActors() { return this.actors; }
    public void addActor(NameItem actor) { this.script_writers.add(actor); }
    
    public ArrayList<NameItem> getGenres() { return this.genres; }
    public void addGenre(NameItem genre) { this.genres.add(genre); }
    
    public void setArea(String area) { this.area = area; }
    public String getArea() { return this.area; }
    
    public void setSpokenLanguage(String spoken) { this.spoken_language = spoken; }
    public String getSpokenLanguage() { return this.spoken_language; }
    
    public void setReleaseDate(String release_date) { this.release_date = release_date; }
    public String getReleaseDate() { return this.release_date; }
    
    public void setDuration(long duration) { this.duration = duration; }
    public long getDuration() { return this.duration; }
    
    public void setOtherTitle(String other_title) { this.other_title = other_title; }
    public String getOtherTitle() { return this.other_title; }
    
    public void setImdbLink(String imdb_link) { this.imdb_link = imdb_link; }
    public String getImdbLink() { return this.imdb_link; }
    
    public void setImdbId(String imdb_id) { this.imdb_id = imdb_id; }
    public String getImdbId() { return this.imdb_id; }
    
    public String getOverview() { return this.over_view; }
    
    public int parseHtml(String htmlString) {
        
        Document doc = Jsoup.parse(htmlString);
        if (doc == null) {
            return Status.FORMAT_ERROR;
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
                            if (LOGD) Log.d(TAG, "hit genre : " + e.text());
                            addGenre(new NameItem(e.text()));
                        } else if (property.equals("v:initialReleaseDate")) {
                            if (LOGD) Log.d(TAG, "hit release date : " + e.text());
                            release_date = e.text();
                        } else if (property.equals("v:runtime")) {
                            if (LOGD) Log.d(TAG, "hit runtime : " + e.attr("content"));
                            try {
                                int runtime = Integer.parseInt(e.attr("content"));
                                duration = (long)runtime;
                            } catch (NumberFormatException exc) {
                                duration = -1;
                            }
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
                                            if (LOGD) Log.d(TAG, "hit director : " + ee.text());
                                            addDirector(new NameItem(ee.text()));
                                        } else if (rel.equals("v:starring")) {
                                            if (LOGD) Log.d(TAG, "hit actor : " + ee.text());
                                            addActor(new NameItem(ee.text()));
                                        } else {
                                            if (LOGD) Log.d(TAG, "script writer? : " + ee.text());
                                            addScriptWriter(new NameItem(ee.text()));
                                        }
                                    } else {
                                        if (LOGD) Log.d(TAG, "class attrs no rel attr");
                                    }
                                }    
                            }

                        } else if (className.equals("pl")) {
                            // obtain production area/country, spoken language, other title, imdb info
                            String content = e.text();
                            //Log.d(TAG, "content : " + e.text());
                            if (content != null && !content.isEmpty()) {
                                if (content.equals("制片国家/地区:")) {
                                    if (LOGD) Log.d(TAG, "hit area : " + e.nextSibling());
                                    area = e.nextSibling().toString();
                                } else if (content.equals("语言:")) {
                                    if (LOGD) Log.d(TAG, "hit language : " + e.nextSibling());
                                    spoken_language = e.nextSibling().toString();
                                } else if (content.equals("又名:")) {
                                    if (LOGD) Log.d(TAG, "hit other title : " + e.nextSibling());
                                    other_title = e.nextSibling().toString();
                                } else if (content.equals("IMDb链接:")) {
                                    //Log.d(TAG, "hit imdb info");
                                    Element imdbInfo = e.nextElementSibling();
                                    if (imdbInfo != null) {
                                        if (LOGD) {
                                            Log.d(TAG, "imdb url = " + imdbInfo.attr("href"));
                                            Log.d(TAG, "imdb id = " + imdbInfo.text());
                                        }
                                        imdb_link = imdbInfo.attr("href");
                                        imdb_id = imdbInfo.text();
                                    } else {
                                        if (LOGD) Log.d(TAG, "can not find imdb info");
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
            return Status.FORMAT_ERROR;
        }
        
        
        // overview
        Elements overview = doc.select("[class=indent][id=link-report]");
        if (overview != null && overview.size() > 0) {
            Element span = overview.first();
            if (LOGD) Log.d(TAG, "Overview = " + span.text()); 
            over_view = span.text();
        } else {
            Log.d(TAG, "Can not find over view");
        }
        
        return Status.OK;
    }
    
}
