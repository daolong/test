/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper;

import java.util.ArrayList;
import java.util.List;

import org.sscraper.model.MovieInfo;

public class Response {
    
    private int status;
    private List<MovieInfo> movies;
    
    public Response(int status) {
        this.status = status;
    }
    
    public void addMovie(MovieInfo movie) {
        if (movies == null) {
            movies = new ArrayList<MovieInfo>();
        }
        
        movies.add(movie);
    }
    
    public String toJsonString() {
        if (this.status == Status.OK) {
            if (movies.size() > 0) {
                // return the first one now
                String json = "{\"status\":" + Status.OK + ", \"total_results\":" + movies.size() + ", \"results\":[";
                for (int i = 0; i < movies.size(); i++) {
                    json += movies.get(i).toJsonString(); 
                    if (i != movies.size() - 1)
                        json += ",";
                }
                json += "]}";
                
                return json;
            } else {
                this.status = Status.NOT_FOUND;
            }
        } 
        
        String jason = "{\"status\":" + this.status + ", ";
        String decribtion;
        switch (this.status) {
        case Status.BAD_PARAM:
            decribtion = "bad request parameter";
            break;
        case Status.AUTH_FAIL:
            decribtion = "permission denied";
        case Status.OUT_OF_DATE:
            decribtion = "authorization out of date";
        default:
            decribtion = "can not find movie information";
        }
        
        jason += "\"decribtion\":\"" + decribtion + "\"}";
        
        return jason;
        
    }
}
