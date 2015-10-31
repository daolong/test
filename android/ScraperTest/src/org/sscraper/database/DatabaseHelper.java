/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database;

import org.sscraper.model.MovieInfo;

public interface DatabaseHelper {
    
    /**
     * Insert movie into data base
     * 
     * @param movie The movie information data structure
     * @return The data base id if success, -1 if fail
     */
    public long insertMovie(MovieInfo movie);
    
    
    /**
     * Query movie by original search title from client/application
     * 
     * @param originalTitle Then Original search title
     * @param year Release year of the movie, maybe null 
     * @return The movie if success, null if not found
     */
    public MovieInfo queryMovieByOriginalTitle(String originalTitle, String year);
}
