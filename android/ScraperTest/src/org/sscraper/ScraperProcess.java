/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */

package org.sscraper;

import java.util.ArrayList;
import java.util.List;

import org.sscraper.database.DatabaseHelper;
import org.sscraper.database.android.AndroidHelper;
import org.sscraper.database.mysql.MysqlHelper;
import org.sscraper.model.MovieInfo;
import org.sscraper.network.HttpUtils;
import org.sscraper.scraper.*;

public class ScraperProcess {
    static final String TAG = "Scraper.Process";

    private List<ScraperBase> mScrapers;
    DatabaseHelper helper;
    
    public ScraperProcess() {
        registerScrpaer(new DoubanScraper());
        registerScrpaer((ScraperBase)new TmdbScraper());
        //registerScrpaer(new M1905Scraper());
    }
    
    private void registerScrpaer(ScraperBase scraper) {
        if (mScrapers == null) {
            mScrapers = new ArrayList<ScraperBase>();
        }
        
        mScrapers.add(scraper);
    }
    
    
    public MovieInfo queryMovie(String title, String year) {
        String queryUtf8 = HttpUtils.decodeHttpParam(title, "UTF-8"); 
        // TODO guess the movie title
        
        // query from data base first
        MovieInfo movie = helper.queryMovieByOriginalTitle(queryUtf8, year);
        
        if (movie == null) {     
            // not found in data base, scraper from Internet
            for (int i = 0; i < mScrapers.size(); i++) {
                movie = mScrapers.get(i).findMovie(title, year);
                if (movie != null) {
                    movie.setOriginalSearchTitle(queryUtf8);
                    // add to data base
                    helper.insertMovie(movie);
                    break;
                }
            }
        }
        
        return movie;
    }
    
    public MovieInfo findMovie4Local(String title, String year) {
        helper = new AndroidHelper();
        return queryMovie(title, year);
    }
    
    public String findMovie4Server(String title, String year) {
       helper = new MysqlHelper();
       MovieInfo movie = queryMovie(title, year);
       if (movie != null) {
           Response res = new Response(Status.OK);
           res.addMovie(movie);
           return res.toJsonString(); 
       }
       
       return (new Response(Status.NOT_FOUND)).toJsonString();
       
    }   
   
}
