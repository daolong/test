/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.android;

import org.sscraper.database.DatabaseHelper;
import org.sscraper.model.MovieInfo;
import org.sscraper.utils.Log;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AndroidHelper implements DatabaseHelper {
    private static String TAG = "AndroidHelper";
    
    private SqliteHelper dbHelper;
    
    public AndroidHelper() {
        dbHelper = SqliteHelper.getInstance();
    }
    
    @Override
    public synchronized long insertMovie(MovieInfo movie) {
    	long lastId = -1;
        if (dbHelper != null) {
            String sql = movie.getInsertSqlCommand();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(sql);
            
            String query = "SELECT ROWID from movies order by ROWID DESC limit 1";
            Cursor c = db.rawQuery(query, null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
                Log.d(TAG, "insertMovie = " + lastId);
                movie.setZmdbId(lastId);
            }
        }
        
        return lastId;
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
                	Log.d(TAG, "hit movie");
                    info = new MovieInfo(cr);
                }
                cr.close();
            }
        }        
        return info;
    }

}
