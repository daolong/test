package com.zidoo.scrapertest;

import org.sscraper.ScraperProcess;
import org.sscraper.database.android.AndroidHelper;
import org.sscraper.database.android.SqliteHelper;
import org.sscraper.model.MovieInfo;
import org.sscraper.network.HttpUtils;
import org.sscraper.utils.Log;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {
    final static String TAG = "ScraperTest.MainActivity";
    
    private final String SERVER_ADDR = "http://192.168.11.204:8085";
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	    // initialize data base
	    SqliteHelper.createHelper(getApplicationContext());

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
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // 通过http请求zidoo服务器获取影片信息
        startServerTest();
    	
        //通过 搜刮接口 获取影片信息， 
        //先从本地数据库查询影片， 查到立即返回， 没有查到则调用搜刮器去网络上搜
        //搜索到影片后先保存到本地数据库， 然后返回数据
        startLocalTest();
        
        // 从本地数据库查询影片
        startDatabaseTest();
    }

    
    private void startDatabaseTest() {
    	Log.d(TAG, "startDatabaseTest");
    	
    	// 必须先执行 SqliteHelper.createHelper(getApplicationContext());
    	// 这里我们在onCreate 中已经执行
    	AndroidHelper helper = new AndroidHelper();
    	MovieInfo info = helper.queryMovieByOriginalTitle("夏洛特烦恼", "2015");
    	if (info != null) {
            Log.d(TAG, "db find moive : " + info.toJsonString());
        } else {
            Log.d(TAG, "db can not find movie");
        } 
		
	}


    void startLocalTest() {
    	Log.d(TAG, "startLocalTest");
        new Thread(new Runnable() {            
            @Override
            public void run() {           	
                MovieInfo info = new ScraperProcess().findMovie4Local("夏洛特烦恼", "2015"); 
                if (info != null) {
                    Log.d(TAG, "local find moive : " + info.toJsonString());
                } else {
                    Log.d(TAG, "local can not find movie");
                }                               
            }
        }).start();
    }

    
    void startServerTest() {
    	Log.d(TAG, "startServerTest");
        new Thread(new Runnable() {            
            @Override
            public void run() {            	
            	String response = HttpUtils.httpGet(SERVER_ADDR + "/search/movie?api_key=57983e31fb435df4df77afb854740ea9&query=夏洛特烦恼&year=2015");
                if (response != null) {
                	Log.d(TAG, "server response = " + response);
                } else {
                    Log.d(TAG, "server can not find movie");
                }
                
                
            }
        }).start();    	
    	
    }
}