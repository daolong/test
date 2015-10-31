/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.model.tmdb;

import org.sscraper.Status;
import org.sscraper.model.SearchResult;
import org.sscraper.utils.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TmdbSearchResult extends SearchResult {
    
    public  int parseJson(String jsonString) {
        Log.d("TmdbSearchResult", "parseJson : "  + jsonString);
        
        JSONObject jb = null;
        try {
            jb = JSONObject.fromObject(jsonString);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
       
        if (jb == null)
            return Status.NOT_FOUND;
        
        int total = jb.getInt("total_results");
        if (total == 0) {
            return Status.NOT_FOUND;
        }
        
        // FIXME: we get the first result now       
        JSONArray ja = jb.getJSONArray("results");
        if (ja == null  || ja.isEmpty())
            return Status.NOT_FOUND;
        
        id = ja.getJSONObject(0).getLong("id");
        title = ja.getJSONObject(0).getString("title");
        originalLanguage = ja.getJSONObject(0).getString("original_language");
        originalTitle = ja.getJSONObject(0).getString("original_title");
        overView = ja.getJSONObject(0).getString("overview");
        releaseDate = ja.getJSONObject(0).getString("release_date");
        posterPath = ja.getJSONObject(0).getString("poster_path");
        backdropPath = ja.getJSONObject(0).getString("backdrop_path");
        voteAverage = ja.getJSONObject(0).getDouble("vote_average");
        voteCount = ja.getJSONObject(0).getLong("vote_count");
        
        return Status.OK;
    }
}
