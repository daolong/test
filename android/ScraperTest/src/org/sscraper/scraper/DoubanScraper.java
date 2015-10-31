/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */

package org.sscraper.scraper;

import java.util.ArrayList;
import java.util.List;

import org.sscraper.Status;
import org.sscraper.model.Actor;
import org.sscraper.model.MovieInfo;
import org.sscraper.model.NameItem;
import org.sscraper.model.douban.DoubanMovie;
import org.sscraper.model.douban.DoubanSearchResult;
import org.sscraper.model.douban.DoubanSearchResult.Cast;
import org.sscraper.model.douban.DoubanSearchResult.Subject;
import org.sscraper.network.HttpUtils;
import org.sscraper.utils.AppConstants;
import org.sscraper.utils.Log;

import com.sun.org.apache.xml.internal.utils.NSInfo;


public class DoubanScraper extends ScraperBase {
    private final static String NAME = "DOUBAN";

    public DoubanScraper() {
        super(NAME);
    }
    
    public MovieInfo findMovie(String name, String year) {
        String nameUtf8 =  HttpUtils.decodeHttpParam(name, "UTF-8");
        Log.d(NAME, "find movie : " + nameUtf8);
        
        DoubanSearchResult search = searchMovie(name, year);
        Subject finalSubject = null;
        
        if (search != null) {
            ArrayList<Subject> subjects = search.getSubjects();
            if (subjects != null && subjects.size() > 0) {
                ArrayList<Subject> matchNames = new ArrayList<Subject>();  
               
                
                for (int i = 0; i < subjects.size(); i++) {
                    Subject s = subjects.get(i);
                    // exact match name only
                    if (nameUtf8.equals(s.getTitle())) {
                        matchNames.add(s);
                    }
                }
                
                if (matchNames.size() > 0) {
                    if (year != null) {
                        for (int i = 0; i < matchNames.size(); i++) {
                            if (year.equals(matchNames.get(i).getYear())) {
                                finalSubject = matchNames.get(i);
                                break;
                            }
                        }                        
                    } else {
                        finalSubject = matchNames.get(0); // get the first one
                    }
                }
            }
        }
        
        
        if (finalSubject != null) {
            Subject s = finalSubject;
            DoubanMovie movie = getMovieDetail(s.getAlt());
            if (movie == null) {
                // HACK:
                // we can not find the detail of movie, maybe the html structure was changed.
                // we should update the parse method. 
                // And we drop this movie to let scraper to find from other source.
                Log.e(NAME, "Please Check html update!!!!");
                return null;
            }
            
            MovieInfo info = new MovieInfo(nameUtf8);
        
            info.setImdbId(movie.getImdbId());
            info.setTitle(s.getTitle());
            info.setOtherTitle(movie.getOtherTitle());
            info.setReleaseDate(movie.getReleaseDate());
            info.setDuration(movie.getDuration());
            info.setLanguage(movie.getArea());
            info.setOverView(movie.getOverview());
            info.setVoteAverage(s.getRating().getAverage());
            info.setVoteCount((long) s.getRating().getStars());
            info.setPostorImageUrl(s.getImages().getLarge());
            info.setBackDropImageUrl(null); // no backdrop from douban
            
            int i = 0;
            // genre            
            List<NameItem> genres = movie.getGenres(); // get from detail first
            if (genres == null || genres.size() == 0) {
                // if detail can not find genre, get from search 
                genres = s.getGenres();
            }
            
            if (genres != null && genres.size() > 0) {
                for (i = 0; i < genres.size(); i++) {
                    info.addGenre(genres.get(i).getName());
                }
            }
            
            // director
            // find from detail first, then from search
            List<NameItem> directors = movie.getDirectors();
            if (directors != null && directors.size() > 0) {
                for (i = 0; i < directors.size(); i++) {
                    info.addDirector(directors.get(i).getName());
                }
            } else {
                List<Cast> _directors = s.getDirectors();
                if (_directors != null && _directors.size() > 0) {
                    for (i = 0; i < _directors.size(); i++) {
                        info.addDirector(_directors.get(i).getName());
                    }
                }
            }
            
            // actor
            List<NameItem> actors = movie.getActors();
            if (actors != null && actors.size() > 0) {
                for (i = 0; i < actors.size(); i++) {
                    info.addActor(new Actor(actors.get(i).getName(), ""));
                }
            } else {
                List<Cast> _actors = s.getCasts();
                if (_actors != null &&  _actors.size() > 0) {
                    Actor actor;
                    for (i = 0; i < _actors.size(); i++) {
                        actor = new Actor(_actors.get(i).getName(), "");
                        info.addActor(actor);
                    }
                }
            }

            List<NameItem> writers = movie.getScriptWriter();
            if (writers != null && writers.size() > 0) {
                for (i = 0; i < writers.size(); i++) {
                    info.addSriptWriter(writers.get(i).getName());
                }
            }
            
            // FIXME : production companies
            
            // spoken language
            info.addSpokenLanguage(movie.getSpokenLanguage());
            
            info.setSource(NAME);
            return info;            
        }
        
        return null;
    }

    private DoubanSearchResult searchMovie(String name, String year) {
        String url = AppConstants.DOUBAN_SEARCH_URL + "q=" + name;
        String response = HttpUtils.httpGet(url);
        if (response != null) {
            DoubanSearchResult search = new DoubanSearchResult();
            int ret = search.parseJson(response);
            if (ret == Status.OK) 
                return search;
            else {
                Log.d(NAME, "searchMovie : parserJson fail or no result");
                return null;
            }            
        } else {
            Log.d(NAME, "searchMovie : HttpGet " + url + " fail!");
            return null;
        }
    }
    
    
    private DoubanMovie getMovieDetail(String url) {
        if (url == null || url.isEmpty())
            return null;
        
        String response = HttpUtils.httpGet(url);
        if (response != null) {
            DoubanMovie movie = new DoubanMovie();
            int ret = movie.parseHtml(response);
            if (ret == Status.OK) 
                return movie;
            else {
                Log.d(NAME, "getMovieDetail : parserHtml error, ret = " + ret);
                return null;
            }            
        } else {
            Log.d(NAME, "getMovieDetail : HttpGet " + url + " fail!");
            return null;
        }
    }

}