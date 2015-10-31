/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sscraper.database.DatabaseHelper;
import org.sscraper.model.MovieInfo;
import org.sscraper.utils.Log;

public class MysqlHelper implements DatabaseHelper {
    private static String TAG = "DatabaseHelper";
    
    private DatabaseManager mDbManager;
    private DbConnection mDbConnection;
    
    public MysqlHelper() {
        mDbManager = DatabaseManager.getInstance();
        mDbConnection =  mDbManager.getDbConnection("mysql");
        createMoviesTable();
    }
    
    /**
     * Get mysql connection
     * @return
     * @throws SQLException 
     */
    private Connection getMysqlConnection() throws SQLException {
        if (mDbConnection != null) {
            return mDbConnection.getConnection();
        }
        
        return null;
    }
    
    
    private void createMoviesTable() {
        Connection conn = null;
        Statement stmt = null;
        String sql = MovieInfo.getMysqlCreateTableCommand();
        Log.d(TAG, "create table sql : " + sql);
        try {
            conn = getMysqlConnection();
            if (conn == null)
                return;
            stmt = conn.createStatement();
            //executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            int result = stmt.executeUpdate(sql);        
            Log.d(TAG, "create table result = " + result);
        } catch (Exception e) {
            Log.printStackTrace(e);
        } 
        finally {
            if (mDbConnection != null) {
                mDbConnection.closeResources(conn, stmt);
            } 
        }
    }
    
    public long insertMovie(MovieInfo movie) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long id = -1;
        String sql = movie.getInsertSqlCommand();
        Log.d(TAG, "insert movie sql : " + sql);
        try {
            conn = getMysqlConnection();
            if (conn == null)
                return id;
            
            
            pstmt = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //movie.setToStatement(pstmt);
            int ret = pstmt.executeUpdate();
            Log.d(TAG, "execute insert ret = " + ret);
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
                Log.d(TAG, "movie db id  = " + id);
                movie.setZmdbId(id);
            } else {
                // throw an exception from here
                Log.d(TAG, "insert movie fail");
            }
            
            /*
            Statement stmt = conn.createStatement();
            int ret = stmt.executeUpdate(sql);            
            Log.d(TAG, "execute insert ret = " + ret);
            stmt.close();
            */
        } catch (Exception e) {
            Log.printStackTrace(e);
        } 
        finally {            
            if (mDbConnection != null) {
                mDbConnection.closeResources(conn, pstmt, rs);
            }
        }
        
        
        return id;
    }
    
    public MovieInfo queryMovieByOriginalTitle(String originalTitle, String year) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM movies WHERE original_search_title like '%" + originalTitle + "%'";
        try {
            conn = getMysqlConnection();
            if (conn == null)
                return null;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql); 
            int i = 0;
            while (rs.next()) {
                Log.d(TAG, "hit " + i++);
                return new MovieInfo(rs);
            }
            
            if (i == 0) {
                Log.d(TAG, "query movie <" + originalTitle + "> no result");
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        } finally {            
            if (mDbConnection != null) {
                mDbConnection.closeResources(conn, stmt, rs);
            }
        }
        
        return null;
    }
}
