/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sscraper.utils.AppConstants;
import org.sscraper.utils.Log;

import android.database.Cursor;
import sun.net.www.http.PosterOutputStream;

public class MovieInfo {
    private static String TAG = "MovieInfo";
    
    // max actors record count. We ignore the record if exceed
    private static int MAX_ACTOR_RECORD_COUNT = 20;
    
    private Long zmdbId; // zidoo movie data base id
    private String imdbId; // imdb id
    
    private String originalSearchTitle; // original search string from client
    private String searchTitle;         // search title process by guess
    
    private String title;       // title from Internet
    private String otherTitle;  // alias if exist
    
    private String releaseDate;
    private Long duration;
    private String language;   // original language
    private String overView;
    
    private Double voteAverage;
    private Long   voteCount;
    
    private String posterImageUrl;  //  url on the Internet
    private String posterImageName; //  zmdb poster image name
    private String backDropImageName; // zmdb backdrop image name
    private String backDropImageUrl; // url on the Internet
    
    private List<String> genres;
    private List<String> directors;
    private List<Actor>  actors;
    private List<String> scriptWriters;    
    private List<String> productionCompanies;
    private List<String> spokenLanguages;
    private String source; // tmdb, m1905, douban etc.
    
    public MovieInfo(String searchTitle) {
        this.searchTitle = searchTitle;
        
        zmdbId = -1L; // means invalid
        imdbId = "";  // means invalid
        
        voteAverage = 0.0;
        voteCount = 0L;
        
        genres = new ArrayList<String>();
        directors = new ArrayList<String>();
        actors = new ArrayList<Actor>();
        scriptWriters = new ArrayList<String>();
        productionCompanies = new ArrayList<String>();
        spokenLanguages = new ArrayList<String>();
        
        originalSearchTitle = " ";
        searchTitle = " ";
        title = " ";
        otherTitle = " ";
            
        posterImageName = "poster_image.jpg";
        backDropImageName = "backdrop_image.jpg";
    }
    
    public void setZmdbId(Long zmdbId) {
        this.zmdbId = zmdbId;
    }
    
    public Long getZmdb() {
        return this.zmdbId;
    }
    
    public void setImdbId(String id) { this.imdbId = id; }
    public String getImdbId() { return this.imdbId; }
    
    public void setOriginalSearchTitle(String originalSearchTitle) { this.originalSearchTitle = originalSearchTitle; }
    
    public String getOriginalSearChTitle() { return this.originalSearchTitle; }
    
    public void setSearchTitle(String searchTitle) { this.searchTitle = searchTitle; }
    
    public String getSearChTitle() { return this.searchTitle; }
    
    public void setTitle(String title) {
        this.title = title;        
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setOtherTitle(String title) {
        this.otherTitle = title;        
    }
    
    public String getOtherTitle() {
        return this.otherTitle;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public String getReleaseDate() {
        return this.releaseDate;
    }
    
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    
    public Long getDuration()  {
        return this.duration;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getLanguage() {
        return this.language;
    }
    
    public void setOverView(String overView) {
        this.overView = overView;
    }
    
    public String getOverView() {
        return this.overView;
    }
    
    public void setVoteAverage(Double voteAverage) { this.voteAverage = voteAverage; }    
    public Double getVoteAverage() { return this.voteAverage; }
    
    public void setVoteCount(Long voteCount) { this.voteCount = voteCount; }
    public Long getVoteCount() { return this.voteCount; }
    
    public void setPostorImageUrl(String url) {
        this.posterImageUrl = url;
    }
    
    public String getPosterImageUrl() {
        return this.posterImageUrl;
    }
    
    public void setBackDropImageUrl(String url) {
        this.backDropImageUrl = url;
    }
    
    public String getBackDropImageUrl() {
        return this.backDropImageUrl;
    }
    
    public void addGenre(String genre) { this.genres.add(genre);}
    public List<String> getGenres() { return this.genres; }
    
    public void addDirector(String director) {
        this.directors.add(director);
    }
    
    public List<String> getDirectors() {
        return this.directors;
    }
    
    public void addActor(Actor actor) {
        this.actors.add(actor);
    }
    
    public List<Actor> getActors() {
        return this.actors;
    }
    
    public void addSriptWriter(String scriptWriter) {
        this.scriptWriters.add(scriptWriter);
    }
    
    public void addProductionCompanies(String company) { this.productionCompanies.add(company);}
    public List<String> getProductionCompanies() { return this.productionCompanies; }
    
    public void addSpokenLanguage(String language) { this.spokenLanguages.add(language);}
    public List<String> getSpokenLanguages() { return this.spokenLanguages; }
      
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getSource() {
        return this.source;
    }
    
    /**
     * Serialize MovieInfo to json string
     * @return
     */
    public String toJsonString() {
        String json = "{\"id\":\"" + this.zmdbId + "\", \"imdb_id\":\"" + imdbId + "\", \"title\":\"" + this.title + "\", \"other_title\":\"" + this.otherTitle + "\", \"release_date\":\"" + 
                this.releaseDate + "\", \"duration\":" + this.duration + ", \"original_language\":\"" + this.language + "\",\"image_base_url\":\"" + AppConstants.IMAGE_BASE_URL + 
                "\", \"poster_image_name\":\"" + this.posterImageName + "\", \"poster_image_url\":\"" + this.posterImageUrl  + "\", backdrop_image_name\":\"" + backDropImageName + 
                "\", \"backdrop_image_url\":\"" + backDropImageUrl + "\", \"vote_average\":" + voteAverage + ", \"vote_count\":" + voteCount +
                ",\"over_view\":\"" + this.overView + "\", "; 
        
        json += "\"genres\":[";
        int i = 0;        
        int size = genres.size();
        for (i = 0; i < size; i++) {
            if (i == size - 1) {
                json += "{\"name\":\"" + genres.get(i) + "\"}";
            } else {
                json += "{\"name\":\"" + genres.get(i) + "\"},";
            }
        }
        json += "],";
        
        json += "\"directors\":[";
        size = directors.size();
        for (i = 0; i < size; i++) {
            if (i == size - 1) {
                json += "{\"name\":\"" + directors.get(i) + "\"}";
            } else {
                json += "{\"name\":\"" + directors.get(i) + "\"},";
            }
        }
        json += "],";
        
        json += "\"actors\":[";
        size = actors.size();
        Actor actor;
        for (i = 0; i < size; i++) {
            actor = actors.get(i);
            if (i == size - 1) {
                json += "{\"name\":\"" + actor.getName() + "\", \"role\":\"" + actor.getRole() + "\"}";
            } else {
                json += "{\"name\":\"" + actor.getName() + "\", \"role\":\"" + actor.getRole() + "\"},";
            }
        }
        json += "],";
        
        json += "\"production_companies\":[";
        size = productionCompanies.size();
        for (i = 0; i < size; i++) {
            if (i == size - 1) {
                json += "{\"name\":\"" + productionCompanies.get(i) + "\"}";
            } else {
                json += "{\"name\":\"" + productionCompanies.get(i) + "\"},";
            }
        }
        json += "],";
        
        json += "\"spoken_languages\":[";
        size = spokenLanguages.size();
        for (i = 0; i < size; i++) {
            if (i == size - 1) {
                json += "{\"name\":\"" + spokenLanguages.get(i) + "\"}";
            } else {
                json += "{\"name\":\"" + spokenLanguages.get(i) + "\"},";
            }
        }
        json += "]}";
        
        return json;
    }

    /**
     * Get create 'movies' table sql command string
     * @return
     */
    public static String getMysqlCreateTableCommand() {
        return "CREATE TABLE IF NOT EXISTS movies(id BIGINT PRIMARY KEY AUTO_INCREMENT, " + 
               "imdb_id VARCHAR(32), " + 
               "original_search_title VARCHAR(512), " +
                "search_title VARCHAR(512), " + 
                "title VARCHAR(512), " + 
                "other_title VARCHAR(512), " + 
                "release_date VARCHAR(32), " + 
                "duration BIGINT, " +
                "language VARCHAR(10), " + 
                "overview VARCHAR(1024), " + 
                "vote_average DOUBLE, " + 
                "vote_count   BIGINT, " +
                "poster_image_url VARCHAR(512), " + 
                "poster_image_name VARCHAR(64), " + 
                "backdrop_image_url VARCHAR(512), " +
                "backdrop_image_name VARCHAR(64), " + 
                "genres VARCHAR(512), " +
                "directors VARCHAR(512), " + 
                "actors VARCHAR(1024), " + 
                "script_writers VARCHAR(512), " + 
                "production_companies VARCHAR(512), " + 
                "spoken_languages VARCHAR(256), " +
                "source VARCHAR(10)) ENGINE=InnoDB DEFAULT CHARSET=utf8";        
    }
    
    public static String getSqliteCreateTableCommand() {
        return "CREATE TABLE IF NOT EXISTS movies(id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "imdb_id VARCHAR(32), " + 
                "original_search_title VARCHAR(512), " +
                 "search_title VARCHAR(512), " + 
                 "title VARCHAR(512), " + 
                 "other_title VARCHAR(512), " + 
                 "release_date VARCHAR(32), " + 
                 "duration INTEGER, " +
                 "language VARCHAR(10), " + 
                 "overview VARCHAR(1024), " + 
                 "vote_average DOUBLE, " + 
                 "vote_count INTEGER, " +
                 "poster_image_url VARCHAR(512), " + 
                 "poster_image_name VARCHAR(64), " + 
                 "backdrop_image_url VARCHAR(512), " +
                 "backdrop_image_name VARCHAR(64), " + 
                 "genres VARCHAR(512), " +
                 "directors VARCHAR(512), " + 
                 "actors VARCHAR(1024), " + 
                 "script_writers VARCHAR(512), " + 
                 "production_companies VARCHAR(512), " + 
                 "spoken_languages VARCHAR(256), " +
                 "source VARCHAR(10))";
    }
    
    /**
     * HACK: not use now
     * Get insert movie to data base sql command string for prepare statement
     * @return
     */
    public String getInsertSqlCmd() {
        String sql = "INSERT INTO movies (original_search_title,search_title,title,other_title," +
                "release_date,duration,language,overview,vote_average,poster_image_url,poster_image_name," + 
                "directors,actors,script_writers,source) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return sql;
    }
    
    private String preprocess(String str) {
        return str.replace("\'", "\\\'");
    }
    
    /**
     * Get insert movie to data base sql command string
     * @return
     */
    public String getInsertSqlCommand() {
        String sql = "INSERT INTO movies (imdb_id, original_search_title,search_title,title,other_title," +
                "release_date,duration,language,overview,vote_average,vote_count,poster_image_url,poster_image_name," + 
                "backdrop_image_url,backdrop_image_name,genres,directors,actors,script_writers,production_companies,spoken_languages,source)" + 
                " values ('" + imdbId + "','" + preprocess(originalSearchTitle) + "','" + preprocess(searchTitle) + "','" + preprocess(title) + "','" + 
                preprocess(otherTitle) + "','" + releaseDate + "'," + duration + ",'" + language + "','" + preprocess(overView) + "',"  +  voteAverage + "," + voteCount + ",'" +  
                posterImageUrl + "', '" + posterImageName + "','" + backDropImageUrl + "','" + backDropImageName + "','";
        
        String str = "";
        if (genres.size() > 0) {            
            for (int i = 0; i < genres.size(); i++) {
                str += (String)genres.get(i);
                if (i != genres.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }  
        } else {
            str = " ";
        }
        sql = sql + preprocess(str) + "','";
        
        str = "";
        if (directors.size() > 0) {            
            for (int i = 0; i < directors.size(); i++) {
                str += (String)directors.get(i);
                if (i != directors.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }  
        } else {
            str = " ";
        }
        sql = sql + preprocess(str) + "','";
        
        str = "";
        int count = actors.size(); 
        if (count > 0) {
            // name1<role1>:name2<role2>:...
            int real_count = (count  > MAX_ACTOR_RECORD_COUNT) ? MAX_ACTOR_RECORD_COUNT : count;
            for (int i = 0; i < real_count; i++) {
                str += actors.get(i).getName() + "<" + actors.get(i).getRole() + ">";
                if (i != real_count - 1) {
                    str += ":"; // separate by ':'
                }
            }
        } else {
            str = " ";
        } 
        sql = sql + preprocess(str) + "','";
        
        str = "";
        if (scriptWriters.size() > 0) {
            for (int i = 0; i < scriptWriters.size(); i++) {
                str += scriptWriters.get(i);
                if (i != scriptWriters.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }
        } else {
            str = " ";
        }
        sql = sql + preprocess(str) + "','";
        
        str = "";
        if (productionCompanies.size() > 0) {
            for (int i = 0; i < productionCompanies.size(); i++) {
                str += productionCompanies.get(i);
                if (i != productionCompanies.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }
        } else {
            str = " ";
        }
        sql = sql + preprocess(str) + "','";
        
        str = "";
        if (spokenLanguages.size() > 0) {
            for (int i = 0; i < spokenLanguages.size(); i++) {
                str += spokenLanguages.get(i);
                if (i != spokenLanguages.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }
        } else {
            str = " ";
        }
        sql = sql + preprocess(str) + "','";
        
        sql = sql + source + "')";
        
        return sql;  
    }
    
    /**
     * HACK: not use now
     * Set record to statement
     * @param pstmt
     * @throws SQLException
     */
    public void setToStatement(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, originalSearchTitle);
        pstmt.setString(2, searchTitle);
        pstmt.setString(3, title);
        pstmt.setString(4, otherTitle);
        pstmt.setString(5, releaseDate);
        pstmt.setLong(6, duration);
        pstmt.setString(7, language);
        pstmt.setString(8, overView);
        pstmt.setDouble(9, voteAverage);
        pstmt.setString(10, posterImageUrl);
        pstmt.setString(11, posterImageName);
        if (directors.size() > 0) {
            String str = "";
            for (int i = 0; i < directors.size(); i++) {
                str += (String)directors.get(i);
                if (i != directors.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }
            pstmt.setString(12, str);
        } else {
            pstmt.setString(12, "");
        }
        
        if (actors.size() > 0) {
            // name1<role1>:name2<role2>:...
            String str = "";
            for (int i = 0; i < actors.size(); i++) {
                str += actors.get(i).getName() + "<" + actors.get(i).getRole() + ">";
                if (i != actors.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }
            pstmt.setString(13, str);
        } else {
            pstmt.setString(13, "");
        }
        
        if (scriptWriters.size() > 0) {
            String str = "";
            for (int i = 0; i < scriptWriters.size(); i++) {
                str += scriptWriters.get(i);
                if (i != scriptWriters.size() - 1) {
                    str += ":"; // separate by ':'
                }
            }
            pstmt.setString(14, str);
        } else {
            pstmt.setString(14, "");
        }
        
        pstmt.setString(15, source);
    }
    
    
    private void fillGenres(String Str) {
        if (!Str.isEmpty() && !Str.equals(" ")) {
            String[] strArray = Str.split(":");
            if (strArray != null && strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    genres.add(strArray[i]);
                }
            } else {
                genres.add(Str);
            }
        }
    }
    
    private void fillDirectors(String Str) {
        if (!Str.isEmpty() && !Str.equals(" ")) {
            String[] strArray = Str.split(":");
            if (strArray != null && strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    directors.add(strArray[i]);
                }
            } else {
                directors.add(Str);
            }
        }
    }
    
    private void fillActors(String Str) {
        if (!Str.isEmpty() && !Str.equals(" ")) {
            // name1<role1>:name2<role2>:...
            String[] strArray = Str.split(":");
            if (strArray != null && strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    int len = strArray[i].length();
                    int j = strArray[i].indexOf('<');
                    if (j > 0) { 
                        String name = strArray[i].substring(0, j);
                        String role = strArray[i].substring(j + 1, len - 1);
                        //Log.d(TAG, "name(" + name + "), role(" + role + ")");
                        actors.add(new Actor(name, role));
                    } else {
                        Log.d(TAG, "actor string forma error : " + Str);
                    }
                }
            } else {
                int len = Str.length();
                int j = Str.indexOf('<');
                String name = Str.substring(0, j);
                String role = Str.substring(j + 1, len - 1);
                //Log.d(TAG, "name(" + name + "), role(" + role + ")");
                actors.add(new Actor(name, role));
            }
        }
    }
    
    private void fillScriptWriter(String Str) {
    	if (!Str.isEmpty() && !Str.equals(" ")) {
            String[] strArray = Str.split(":");
            if (strArray != null && strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    scriptWriters.add(strArray[i]);
                }
            } else {
                scriptWriters.add(Str);
            }
        }
    }
    
    private void fillProductionCompanies(String Str) {
    	if (!Str.isEmpty() && !Str.equals(" ")) {
            String[] strArray = Str.split(":");
            if (strArray != null && strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    productionCompanies.add(strArray[i]);
                }
            } else {
                productionCompanies.add(Str);
            }
        }
    }
    
    private void fillSpokenLanguage(String Str) {
    	if (!Str.isEmpty() && !Str.equals(" ")) {
            String[] strArray = Str.split(":");
            if (strArray != null && strArray.length > 0) {
                for (int i = 0; i < strArray.length; i++) {
                    spokenLanguages.add(strArray[i]);
                }
            } else {
                spokenLanguages.add(Str);
            }
        }
    }
    
    /**
     * Build movie information from Mysql data base result
     * @param rs
     * @throws SQLException
     */
    public MovieInfo(ResultSet rs) throws SQLException {        
        genres = new ArrayList<String>();
        directors = new ArrayList<String>();
        actors = new ArrayList<Actor>();
        scriptWriters = new ArrayList<String>();
        productionCompanies = new ArrayList<String>();
        spokenLanguages = new ArrayList<String>();
        
        zmdbId = rs.getLong("id");
        imdbId = rs.getString("imdb_id");
        originalSearchTitle = rs.getString("original_search_title");
        searchTitle = rs.getString("search_title");
        title = rs.getString("title");
        otherTitle= rs.getString("other_title");
        releaseDate = rs.getString("release_date");
        duration = rs.getLong("duration");
        language = rs.getString("language");
        overView = rs.getString("overview");
        voteAverage = rs.getDouble("vote_average");
        voteCount = rs.getLong("vote_count");
        posterImageUrl = rs.getString("poster_image_url");
        posterImageName = rs.getString("poster_image_name");
        backDropImageUrl = rs.getString("backdrop_image_url");
        backDropImageName = rs.getString("backdrop_image_name");
        
        String Str = rs.getString("genres");
        fillGenres(Str);
        
        Str = rs.getString("directors");
        fillDirectors(Str);
        
        Str = rs.getString("actors");
        fillActors(Str);
        
        Str = rs.getString("script_writers");
        fillScriptWriter(Str);
        
        Str = rs.getString("production_companies");
        fillProductionCompanies(Str);
        
        Str = rs.getString("spoken_languages");
        fillSpokenLanguage(Str);
        
        source = rs.getString("source");
    }

    /**
     * Build movie information from Sqlite data base result
     * @param cr
     */
    public MovieInfo(Cursor cr) {
        genres = new ArrayList<String>();
        directors = new ArrayList<String>();
        actors = new ArrayList<Actor>();
        scriptWriters = new ArrayList<String>();
        productionCompanies = new ArrayList<String>();
        spokenLanguages = new ArrayList<String>();
        
        zmdbId = cr.getLong(cr.getColumnIndex("id"));
        imdbId = cr.getString(cr.getColumnIndex("imdb_id"));
        originalSearchTitle = cr.getString(cr.getColumnIndex("original_search_title"));
        searchTitle = cr.getString(cr.getColumnIndex("search_title"));
        title = cr.getString(cr.getColumnIndex("title"));
        otherTitle= cr.getString(cr.getColumnIndex("other_title"));
        releaseDate = cr.getString(cr.getColumnIndex("release_date"));
        duration = cr.getLong(cr.getColumnIndex("duration"));
        language = cr.getString(cr.getColumnIndex("language"));
        overView = cr.getString(cr.getColumnIndex("overview"));
        voteAverage = cr.getDouble(cr.getColumnIndex("vote_average"));
        voteCount = cr.getLong(cr.getColumnIndex("vote_count"));
        posterImageUrl = cr.getString(cr.getColumnIndex("poster_image_url"));
        posterImageName = cr.getString(cr.getColumnIndex("poster_image_name"));
        backDropImageUrl = cr.getString(cr.getColumnIndex("backdrop_image_url"));
        backDropImageName = cr.getString(cr.getColumnIndex("backdrop_image_name"));
        
        String Str = cr.getString(cr.getColumnIndex("genres"));
        fillGenres(Str);
        
        Str = cr.getString(cr.getColumnIndex("directors"));
        fillDirectors(Str);
        
        Str = cr.getString(cr.getColumnIndex("actors"));
        fillActors(Str);
        
        Str = cr.getString(cr.getColumnIndex("script_writers"));
        fillScriptWriter(Str);
        
        Str = cr.getString(cr.getColumnIndex("production_companies"));
        fillProductionCompanies(Str);
        
        Str = cr.getString(cr.getColumnIndex("spoken_languages"));
        fillSpokenLanguage(Str);
        
        source = cr.getString(cr.getColumnIndex("source"));
    }
}
