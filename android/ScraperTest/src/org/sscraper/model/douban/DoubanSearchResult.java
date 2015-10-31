/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.model.douban;

import java.util.ArrayList;
import java.util.List;

import org.sscraper.Status;
import org.sscraper.model.NameItem;
import org.sscraper.utils.Log;

import com.mysql.fabric.xmlrpc.base.Array;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DoubanSearchResult {
    private static String TAG = "DoubanSearchResult";
    
    private int count;
    private int start;
    private int total;
    
    private ArrayList<Subject> subjects = new ArrayList<Subject>();
    
    public void setCount(int count) { this.count = count; }
    public int getCount() { return this.count; }
    
    public void setStart(int start) { this.start = start; }
    public int getStart() { return this.start; }
    
    public void setTotal(int total) { this.total = total; }
    public int getTotal() { return this.total; }  
    
    public ArrayList<Subject> getSubjects() { return this.subjects; }
    public void addSubject(Subject subject) { subjects.add(subject); }
    
    public static class Subject {
        
        private Rating rating;
        private ArrayList<NameItem> genres = new ArrayList<NameItem>();
        private int collect_count;
        private List<Cast> casts = new ArrayList<Cast>();
        private String title;
        private String original_title;
        private String subtype;
        private ArrayList<Cast> directors = new ArrayList<Cast>();
        private String year;
        private Avatars images;
        private String alt;
        private String id;
 
        public void setRating(Rating rating) { this.rating = rating; }
        public Rating getRating() { return this.rating; }
        
        public ArrayList<NameItem> getGenres() { return this.genres; }
        public void addGenre(NameItem genre) { this.genres.add(genre); }
        
        public void setCollectCount(int collect_count) { this.collect_count = collect_count; }
        public int getCollectCount() { return this.collect_count; }
        
        public ArrayList<Cast> getCasts() { return (ArrayList<Cast>) this.casts; }
        public void addCast(Cast cast) { this.casts.add(cast); }
        
        public void setTitle(String title) { this.title = title; }
        public String getTitle() { return this.title; }
        
        public void setOriginalTitle(String title) { this.original_title = title; }
        public String getOriginalTitle() { return this.original_title; }
        
        public void setSubtype (String subtype) { this.subtype = subtype; }
        public String getSubtype() { return this.subtype; }
        
        public ArrayList<Cast> getDirectors() { return this.directors; }
        public void addDirector(Cast director) { this.directors.add(director); }
        
        public void setYear(String year) { this.year = year; }
        public String getYear() { return this.year; }
        
        public void setImages(Avatars images) { this.images = images; }
        public Avatars getImages() { return this.images; }
        
        public void setAlt(String alt) { this.alt = alt; }
        public String getAlt() { return this.alt; }
        
        public void setId(String id) { this.id = id; }
        public String getId() { return this.id; }
        
        public int fromJsonObject(JSONObject jb) {
            if (jb == null || jb.isEmpty())
                return Status.NOT_FOUND;

            rating = new Rating();
            rating.fromJsonOject(jb.getJSONObject("rating"));
            
            JSONArray jgenres = jb.getJSONArray("genres");
            int i = 0;
            int count = 0;
            if (jgenres != null) {
                count = jgenres.size();
            
                for (i = 0; i < count; i++) {
                    NameItem genre = new NameItem();
                    genre.setName(jgenres.getString(i));
                    genres.add(genre);
                }
            }
            
            
            JSONArray jcasts = jb.getJSONArray("casts");
            if (jcasts != null) {
                count = jcasts.size();
                for (i = 0; i < count; i++) {
                    Cast cast = new Cast();
                    if (cast.fromJsonObject(jcasts.getJSONObject(i)) == Status.OK)
                        addCast(cast);
                }
            }
            
            title = jb.getString("title");
            original_title = jb.getString("original_title");
            subtype = jb.getString("subtype");
            
            JSONArray jdirectors = jb.getJSONArray("directors");
            if (jdirectors != null) {
                count = jdirectors.size();
                for (i = 0; i < count; i++) {
                    Cast director = new Cast();
                    if (director.fromJsonObject(jdirectors.getJSONObject(i)) == Status.OK) {
                        addDirector(director);
                    }
                }
            }
            
            year = jb.getString("year");
            JSONObject jimage = jb.getJSONObject("images");
            if (jimage != null) {
                images = new Avatars();
                images.fromJsonObject(jimage);
            }
            
            alt = jb.getString("alt");
            id = jb.getString("id");
            
            return Status.OK;
        }
        
    }
    
    public static class Rating {
        private int max;
        private Double average;
        private int stars; // how many people vote this
        private int min;
        
        public Double getAverage() { return this.average; }
        public int getStars() { return this.stars; }
        
        public int fromJsonOject(JSONObject jb) {
            if (jb == null || jb.isEmpty())
                return Status.NOT_FOUND;
            
            max = jb.getInt("max");
            average = jb.getDouble("average");
            stars = jb.getInt("stars");
            min = jb.getInt("min");
            
            return Status.OK;
        } 
    }
    
    public static class Avatars {
        private String small;
        private String large;
        private String medium;
        
        public String getLarge() { return large; }
        
        public int fromJsonObject(JSONObject jb) {
            if (jb == null || jb.isEmpty())
                return Status.NOT_FOUND;
            
            small = jb.getString("small");
            large = jb.getString("large");
            medium = jb.getString("medium");
            
            return Status.OK;
        }
    }
    
    public static class Cast {
        private Avatars avatars;
        private String alt;
        private String id;
        private String name;
        
        public String getName() { return name; }
        
        public int fromJsonObject(JSONObject jb) {
            if (jb == null || jb.isEmpty())
                return Status.NOT_FOUND;
            
            JSONObject javatars = jb.getJSONObject("avatars");
            avatars = new Avatars();
            avatars.fromJsonObject(javatars);
            
            alt = jb.getString("alt");
            id = jb.getString("id");
            name = jb.getString("name");
            
            return Status.OK;
        }
    }
    
    
    public int parseJson(String jsonString) {
        jsonString = jsonString.replace("\\/\\/", "//");
        jsonString = jsonString.replace("\\/", "/");
        Log.d(TAG, "parseJson : "  + jsonString);
        
        JSONObject jb = null;
        try {
            jb = JSONObject.fromObject(jsonString);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
       
        if (jb == null)
            return Status.NOT_FOUND;
        
        count = jb.getInt("count");
        start = jb.getInt("start");
        total = jb.getInt("total");
        if (total == 0)
            return Status.NOT_FOUND;
        
        JSONArray jaSubject = jb.getJSONArray("subjects");
        if (jaSubject == null || jaSubject.isEmpty()) {
            return Status.NOT_FOUND;
        }
        
        for (int i = 0; i < jaSubject.size(); i++) {
            Subject subject = new Subject();
            if (subject.fromJsonObject(jaSubject.getJSONObject(i)) == Status.OK)
                addSubject(subject);
        }        
        
        return Status.OK;
    }
}
