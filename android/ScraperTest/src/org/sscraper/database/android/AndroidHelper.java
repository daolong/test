/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.android;

import org.sscraper.database.DatabaseHelper;
import org.sscraper.model.MovieInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AndroidHelper implements DatabaseHelper {
    
    private SqliteHelper dbHelper;
    
    public AndroidHelper() {
        dbHelper = SqliteHelper.getInstance();
    }
    
    @Override
    public synchronized long insertMovie(MovieInfo movie) {
        if (dbHelper != null) {
            String sql = movie.getInsertSqlCmd();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(sql);
        }
        return -1;
    }

    @Override
    public MovieInfo queryMovieByOriginalTitle(String originalTitle, String year) {
        MovieInfo info = null;
        if (dbHelper != null) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT * FROM movies WHERE original_search_title like '%" + originalTitle + "%'";
            Cursor cr = db.rawQuery(sql, null);
            if (cr != null) {
                if (cr.moveToFirst()) {
                    //TODO:  maybe match several movies, check the year also 
                    info = new MovieInfo(cr);
                }
                cr.close();
            }
        }        
        return info;
    }

}
