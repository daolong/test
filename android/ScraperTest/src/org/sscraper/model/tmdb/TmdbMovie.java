/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.model.tmdb;

import java.util.ArrayList;
import java.util.List;

import org.sscraper.Status;
import org.sscraper.model.Genre;
import org.sscraper.model.NameItem;
import org.sscraper.model.tmdb.TmdbConfig.Images;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TmdbMovie {
    private Long tmdbId;
    private Boolean adult;
    private String backdropPath;
    private Long budget;
    private String flattenedGenres;
    private String homepage;
    private String imdbId;
    private String originalTitle;
    private String overview;
    private Double popularity;
    private String posterPath;
    private String flattenedProductionCompanies;
    private String releaseDate;
    private Long revenue;
    private Long runtime;
    private String status;
    private String tagline;
    private String title;
    private Double voteAverage;
    private Long voteCount;
    private String trailer;
    
    private List<Genre> genres = new ArrayList<Genre>();
    private List<NameItem> productionCompanies = new ArrayList<NameItem>();
    private List<NameItem> spokenLanguages = new ArrayList<NameItem>();
    
    public Long getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Long tmdbId) {
        this.tmdbId = tmdbId;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getFlattenedGenres() {
        return flattenedGenres;
    }

    public void setFlattenedGenres(String flattenedGenres) {
        this.flattenedGenres = flattenedGenres;
    }

    public String getFlattenedProductionCompanies() {
        return flattenedProductionCompanies;
    }

    public void setFlattenedProductionCompanies(String flattenedProductionCompanies) {
        this.flattenedProductionCompanies = flattenedProductionCompanies;
    }
    
    public List<NameItem> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<NameItem> companies) {
        this.productionCompanies = companies;
    }

    public List<NameItem> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<NameItem> languages) {
        this.spokenLanguages = languages;
    }
    
    /**
     * HACK: we only need genres, imdb_id, production_companiess, spoken_languages, runtime just now
     * @param jsonString
     * @return
     */
    public int parseJson(String jsonString) {
        /*
        {"adult":false,"backdrop_path":"/lurZ6PAlfZYGSanrI6V9P7UcCLY.jpg","belongs_to_collection":null,"budget":0,"genres":[{"id":10749,"name":"爱情"},
        {"id":35,"name":"喜剧"}],"homepage":"","id":362682,"imdb_id":"tt5061814","original_language":"zh","original_title":"夏洛特烦恼","overview":"　　昔日校花秋雅（王智 饰）的婚礼正在隆重举行，
                        学生时代暗恋秋雅的夏洛（沈腾 饰）看着周围事业成功的老同学，心中泛起酸味，借着七分醉意大闹婚礼现场，甚至惹得妻子马冬梅（马丽 饰）现场发飙，
                        而他发泄过后却在马桶上睡着了。梦里他重回校园，追求到他心爱的女孩、让失望的母亲重展笑颜、甚至成为无所不能的流行乐坛巨星……  　　
                        醉生梦死中，他发现身边人都在利用自己，只有马冬梅是最值得珍惜的……"
        ,"popularity":0.377389,"poster_path":"/e5anoE2AUPXGLOh2r1MPirECegq.jpg",
        "production_companies":[{"name":"Beijing Mahua Funage Company","id":64858}],
        "production_countries":[{"iso_3166_1":"CN","name":"China"}],"release_date":"2015-09-30","revenue":0,"runtime":104,
        "spoken_languages":[{"iso_639_1":"zh","name":"普通话"}],
        "status":"Released","tagline":"","title":"夏洛特烦恼","video":false,"vote_average":7.5,"vote_count":4}
        */
        
        JSONObject jb = JSONObject.fromObject(jsonString);
        if (jb == null)
            return Status.NOT_FOUND;

        
        JSONArray ja = jb.getJSONArray("genres");
        if (ja != null) {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject gb = ja.getJSONObject(i);
                Genre genre = new Genre();
                genre.setId(gb.getLong("id"));
                genre.setName(gb.getString("name"));
                genres.add(genre);
            }
        }
        
        ja = jb.getJSONArray("production_companies");
        if (ja != null) {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject gb = ja.getJSONObject(i);
                NameItem item = new NameItem();
                item.setName(gb.getString("name"));
                productionCompanies.add(item);
            }
        }
        
        ja = jb.getJSONArray("spoken_languages");
        if (ja != null) {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject gb = ja.getJSONObject(i);
                NameItem item = new NameItem();
                item.setName(gb.getString("name"));
                spokenLanguages.add(item);
            }
        }
        
        imdbId = jb.getString("imdb_id");
        runtime = jb.getLong("runtime");
        
        return Status.OK;
    }
}
