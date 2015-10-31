/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.utils;

public class AppConstants {

    // Application Version 
    public final static String VERSION = "1.0.0";
    
    private final static boolean SERVER = true;
    
    // the movie data base 
    public final static String TMDB_API_KEY = "57983e31fb435df4df77afb854740ea9";
    public final static String TMDB_SEARCH_URL = "http://api.tmdb.org/3/search/movie?";
    public final static String TMDB_CONFIG_URL = "http://api.tmdb.org/3/configuration?";
    public final static String ZMDB_INFO_URL = "http://api.tmdb.org/3/movie/";
    
    // dou ban data base
    public final static String DOUBAN_SEARCH_URL = "http://api.douban.com/v2/movie/search?";
    
    // mysql server address
    public final static String MYSQL_SERVER = "192.168.11.204:3306";
    
    // image base url, use by poster, backdrop 
    public final static String IMAGE_BASE_URL = "http://192.168.11.127:8085/download/sscraper/images/"; 
}
