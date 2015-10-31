
/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.scraper;

import org.sscraper.model.MovieInfo;

public class ScraperBase implements ScraperInterface {
    
    private String name;
    
    public ScraperBase(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    @Override
    public MovieInfo findMovie(String name, String year) {
        return null;
    }
}
