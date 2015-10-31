/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.android;

import org.sscraper.model.MovieInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String TAG = "StreamVideo.DBHelper";
    
    private static final String DB_NAME = "movies.db";
    private static SqliteHelper sInstance = null;
    
    private SQLiteDatabase mDb;
    
    public SqliteHelper(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }
    
    public static SqliteHelper createHelper(Context context) {
        if (sInstance == null) {
            sInstance = new SqliteHelper(context, DB_NAME, null, 1);
        }
        
        return sInstance;
    }
    
    public static SqliteHelper getInstance() {
        return sInstance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = MovieInfo.getSqliteCreateTableCommand();
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
    
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        mDb = db;
    }
    
    public void close() {
        if (mDb != null) {
            try {
                mDb.close();
                mDb = null;
            } catch (Exception e) {}
        }
    }

}
