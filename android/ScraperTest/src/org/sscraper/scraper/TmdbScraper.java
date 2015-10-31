/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.scraper;


import java.util.ArrayList;
import java.util.List;

import org.sscraper.Status;
import org.sscraper.model.Actor;
import org.sscraper.model.Genre;
import org.sscraper.model.MovieInfo;
import org.sscraper.model.NameItem;
import org.sscraper.model.SearchResult;
import org.sscraper.model.tmdb.Casts;
import org.sscraper.model.tmdb.TmdbConfig;
import org.sscraper.model.tmdb.TmdbMovie;
import org.sscraper.model.tmdb.TmdbSearchResult;
import org.sscraper.network.HttpUtils;
import org.sscraper.utils.AppConstants;
import org.sscraper.utils.Log;

public class TmdbScraper extends ScraperBase {
    private final static String NAME = "TMDB";
   
    public TmdbScraper() {
        super(NAME);
    }

    public MovieInfo findMovie(String name, String year) {
        String nameUtf8 =  HttpUtils.decodeHttpParam(name, "UTF-8");
        Log.d(NAME, "find movie : " + nameUtf8);
        
        SearchResult search = searchMovie(name, year);
        if (search == null) {
            Log.d(NAME, "can not find information of <" + nameUtf8 + ">");
            return null;
        }
        
        MovieInfo info = new MovieInfo(nameUtf8);
        /*
        byte[] bytes;
        try {
            bytes = search.getTitle().getBytes();
            for (int i = 0 ; i < bytes.length; i++) {
                Log.d(NAME, String.format("title byte[%d] : 0x%x", i, bytes[i]));
            }
            
            String newTitel = new String(bytes, "iso8859-1");
            bytes = newTitel.getBytes("iso8859-1");
            for (int i = 0 ; i < bytes.length; i++) {
                Log.d(NAME, String.format("new title byte[%d] : 0x%x", i, bytes[i]));
            }
            
        } catch (Exception e) {}
        */
        
        info.setTitle(search.getTitle());
        info.setOtherTitle(search.getOriginalTitle()); //别名
        info.setReleaseDate(search.getReleaseDate());
        info.setDuration(0L);
        info.setLanguage(search.getOriginalLanguage());
        info.setOverView(search.getOverView());
        info.setVoteAverage(search.getVoteAverage());
        info.setVoteCount(search.getVoteCount());
        
        TmdbMovie movie = getMovieDetail(search.getId());
        if (movie != null) {
            int i = 0;
            // Genre
            List<Genre> genres = movie.getGenres();
            if (genres != null && genres.size() > 0) {
                for (i = 0; i < genres.size(); i++) {
                    info.addGenre(genres.get(i).getName());
                }
            }
            // Production companies
            List<NameItem> companies = movie.getProductionCompanies();
            if (companies != null && companies.size() > 0) {
                for (i = 0; i < companies.size(); i++) {
                    info.addProductionCompanies(companies.get(i).getName());
                }
            }
            
            // Spoken language
            List<NameItem> langaues = movie.getSpokenLanguages();
            if (langaues != null && langaues.size() > 0) {
                for (i = 0; i < langaues.size(); i++) {
                    info.addSpokenLanguage(langaues.get(i).getName());
                }
            }
            
            info.setImdbId(movie.getImdbId());
            info.setDuration(movie.getRuntime());
        }
        
        // get casts, director, script writer ...
        Casts cast = getCasts(search.getId());
        if (cast != null) {
            ArrayList<Casts.Cast> casts = cast.getCasts();
            if (casts != null && casts.size() > 0) {
                for (int i = 0; i < casts.size(); i++) {
                    Actor actor = new Actor(casts.get(i).getName(), casts.get(i).getCharacter());
                    info.addActor(actor);
                }
            }
            
            ArrayList<Casts.Crew> crews = cast.getCrews();
            if (crews != null && crews.size() > 0) {
                for (int i = 0; i < crews.size(); i++) {
                    if (crews.get(i).getJob().equals("Director")) {
                        info.addDirector(crews.get(i).getName());
                    }
                }
            }
        }
        
        // get poster image url
        TmdbConfig config = getConfig();        
        if (config != null) {
            String base_url = config.getImages().getBase_url();
            if (base_url != null) {
                String url;
                if (config.getImages().getPoster_sizes().size() > 0) {   
                    int last = config.getImages().getPoster_sizes().size() - 1;
                    url = base_url + config.getImages().getPoster_sizes().get(last) + search.getPosterPath();         
                } else {
                    url = base_url + "original" + search.getPosterPath();
                }
                info.setPostorImageUrl(url);
                
                if (config.getImages().getBackdrop_sizes().size() > 0) {   
                    int last = config.getImages().getBackdrop_sizes().size() - 1;
                    url = base_url + config.getImages().getBackdrop_sizes().get(last) + search.getBackdropPath();         
                } else {
                    url = base_url + "original" + search.getBackdropPath();
                }
                info.setBackDropImageUrl(url);
            }            
        } 
        
        info.setSource(NAME);
        
        return info;
    }

    private SearchResult searchMovie(String name, String year) {
        String url;
        if (year != null)
            url = AppConstants.TMDB_SEARCH_URL + "api_key=" + AppConstants.TMDB_API_KEY + "&query=" + name + "&year=" + year + "&language=zh";
        else 
            url = AppConstants.TMDB_SEARCH_URL + "api_key=" + AppConstants.TMDB_API_KEY + "&query=" + name + "&language=zh";
        
        String response = HttpUtils.httpGet(url);
        if (response != null) {
            SearchResult search = new TmdbSearchResult();
            int ret = search.parseJson(response);
            if (ret == Status.OK) 
                return search;
            else {
                Log.d(NAME, "searchMovie : parserJson fail");
                return null;
            }            
        } else {
            Log.d(NAME, "searchMovie : HttpGet " + url + " fail!");
            return null;
        }
    }
    
    /**
     * get movie detail information
     * @param id
     * @return
     */
    private TmdbMovie getMovieDetail(Long id) {
        String url = AppConstants.ZMDB_INFO_URL + id + "?api_key=" + AppConstants.TMDB_API_KEY + "&language=zh";
        String response = HttpUtils.httpGet(url);
        if (response != null) {
            TmdbMovie movie = new TmdbMovie();
            if (movie.parseJson(response) == Status.OK) {
                return movie;
            } else {
                Log.d(NAME, "getMovieDetail : parse json fail");
            }
        } else {
            Log.d(NAME, "getMovieDetail : no response!");
        }
        
        return null;
    }
    
    /**
     * Get actors & directors ...
     * @param id The movie data base id 
     * @return
     */
    private Casts getCasts(Long id) {
        String url = AppConstants.ZMDB_INFO_URL + id + "/casts?api_key=" + AppConstants.TMDB_API_KEY + "&language=zh"; 
        String response = HttpUtils.httpGet(url);
        if (response != null) {
            Casts casts = new Casts();
            if (casts.parseJson(response) == Status.OK) {
                return casts;
            } else {
                Log.d(NAME, "getCasts : parse json fail");
            }
        } else {
            Log.d(NAME, "getCasts : no response!");
        }
        
        return null;
    }
    
    private TmdbConfig getConfig() {
        String url = AppConstants.TMDB_CONFIG_URL + "api_key=" + AppConstants.TMDB_API_KEY;
        String response = HttpUtils.httpGet(url);
        if (response != null) {
            TmdbConfig config = new TmdbConfig();
            if (config.parseJson(response) == Status.OK) {
                return config;
            } else {
                Log.d(NAME, "getConfig : parse json fail");
            }
        } else {
            Log.d(NAME, "getConfig : no response!");
        }
        
        return null;
    }
}
