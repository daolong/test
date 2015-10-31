/**  
 *  Copyright (C) 2015 dl@zidoso.tv
 */
package com.zidoo.scrapertest;

import org.sscraper.ScraperProcess;
import org.sscraper.database.android.SqliteHelper;
import org.sscraper.model.MovieInfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private static String TAG = "Scraper.MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // initialize data base
        SqliteHelper.createHelper(getApplicationContext());
        
        new Thread(new Runnable() {            
            @Override
            public void run() {
                MovieInfo info = new ScraperProcess().findMovie4Local("夏洛特烦恼", "2015"); 
                if (info != null) {
                    Log.d(TAG, info.toJsonString());
                } else {
                    Log.d(TAG, "Can not find movie");
                }
            }
        }).start();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // close database
        SqliteHelper helper = SqliteHelper.getInstance();
        if (helper != null) {
            helper.close();
        }
    }
}
