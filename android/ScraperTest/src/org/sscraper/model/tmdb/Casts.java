/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.model.tmdb;

import java.util.ArrayList;

import org.sscraper.Status;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Casts {
    
    private ArrayList<Cast> mCasts;
    private ArrayList<Crew> mCrews;
    
    public ArrayList<Cast> getCasts() { return mCasts; }
    
    public ArrayList<Crew> getCrews() { return mCrews; }
    
    
    public static class Cast {
        private String character;
        private String name;
        
        public void setCharacter(String character) { this.character  = character; }
        
        public String getCharacter() { return this.character; }
        
        public void setName(String name) { this.name = name; }
        
        public String getName() { return this.name; }
    }
    
    public static class Crew {
        private String job;
        private String name;
        
        public void setJob(String job) { this.job = job; }
        
        public String getJob() { return this.job; }
        
        public void setName(String name) { this.name = name; }
        
        public String getName() { return this.name; }
    }
    
    /*
    {"id":362682,"cast":[
    {"cast_id":2,"character":"Xia Luo","credit_id":"561297309251416b5600169d","id":1519026,"name":"Teng Shen","order":1,"profile_path":null},
    {"cast_id":3,"character":"Ma Dong Mei","credit_id":"561297469251414feb00b8e1","id":1519027,"name":"Mary","order":2,"profile_path":null},
    {"cast_id":4,"character":"Qiuya","credit_id":"561297bbc3a368681f013fd4","id":1519029,"name":"Zixuan Wang","order":3,"profile_path":null},
    {"cast_id":5,"character":"","credit_id":"561297dbc3a3686810015c79","id":1519030,"name":"Zheng Yin","order":4,"profile_path":null},
    {"cast_id":6,"character":"","credit_id":"561297f5925141478d00bee8","id":1519031,"name":"Lun Ai","order":5,"profile_path":null}
    ],
    "crew":[
    {"credit_id":"561296dac3a368681f013fbc","department":"Directing","id":1519023,"job":"Director","name":"Fei Yan","profile_path":null},
    {"credit_id":"561296fcc3a3686810015c57","department":"Directing","id":1519024,"job":"Director","name":"Damo Peng","profile_path":null}]}
    */   
    public int parseJson(String jsonString) {
        JSONObject jb = JSONObject.fromObject(jsonString);
        if (jb == null)
            return Status.NOT_FOUND;
        
        JSONArray jcast = jb.getJSONArray("cast");
        if (jcast == null)
            return Status.NOT_FOUND;
        
        if (mCasts == null)
            mCasts = new ArrayList<Casts.Cast>();
        
        for (int i = 0; i < jcast.size(); i++) {
            Cast cast = new Cast();
            cast.setCharacter(jcast.getJSONObject(i).getString("character"));
            cast.setName(jcast.getJSONObject(i).getString("name"));
            
            mCasts.add(cast);
        }
        
        JSONArray jcrew = jb.getJSONArray("crew");
        if (jcrew != null) {        
            if (mCrews == null) 
                mCrews = new ArrayList<Casts.Crew>();
            
            for (int i = 0; i < jcrew.size(); i++) {
                 Crew crew = new Crew();
                 crew.setJob(jcrew.getJSONObject(i).getString("job"));
                 crew.setName(jcrew.getJSONObject(i).getString("name"));
                 
                 mCrews.add(crew);
            }
        }
        
        return Status.OK;
    }
}
