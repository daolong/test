package org.sscraper.scraper;

import org.sscraper.model.MovieInfo;

public interface ScraperInterface {

    /**
     * Find movie from the Internet
     * @param name Movie title, use for search parameter
     * @param year Movie release Year
     * @return The movie information if found, null if not found
     */
    public MovieInfo findMovie(String name, String year);

    
}
