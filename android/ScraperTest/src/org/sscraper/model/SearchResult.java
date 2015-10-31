/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */

package org.sscraper.model;

import org.sscraper.Status;

public class SearchResult {
    
    protected Long id;
    protected String title;
    protected String originalLanguage;
    protected String originalTitle;
    protected String overView;
    protected String releaseDate;
    protected String posterPath; // poster image url
    protected String backdropPath;
    protected Double voteAverage;
    protected Long   voteCount;
    
    
    public Long getId() { return this.id; }
    
    public String getTitle() {return this.title; }
    
    public String getOriginalTitle() {return this.originalTitle; }
    
    public String getOriginalLanguage() {return this.originalLanguage; }
    
    public String getOverView() { return this.overView; }
    
    public String getReleaseDate() { return this.releaseDate; } 
    
    public String getPosterPath() { return this.posterPath; }
    
    public String getBackdropPath() { return this.backdropPath; }
    
    public Double getVoteAverage() { return this.voteAverage; }
    
    public Long   getVoteCount() { return  this.voteCount; }   
    
    public  int parseJson(String jsonString) { return Status.OK; }
 }
    
