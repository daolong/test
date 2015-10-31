/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.mysql;

import java.util.HashMap;
import java.util.Properties;

import org.sscraper.utils.AppConstants;

public class DatabaseManager {

    private static DatabaseManager mInstance = null;

    private HashMap<String, Object> mConnectionsMap;

    private String  mysql_url = "jdbc:mysql://" + AppConstants.MYSQL_SERVER + "/moviedatabase";
    private String  mysql_user = "sscraper";
    private String  mysql_passwd = "superscraper";
    private String  mysql_driver =  "com.mysql.jdbc.Driver";
    private String  mysql_maxActive = "30";
    private String  mysql_maxIdle = "10";
    private String  mysql_maxWait = "36000";
    private String  mysql_removeAbandoned = "false";
    private String  mysql_removeAbandonedTimeout = "120";
    private String  mysql_testOnBorrow = "true";
    private String  mysql_logAbandoned = "true";
    
    public static DatabaseManager getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseManager();
        }

        return mInstance;
    }

    public DatabaseManager() {
        // TODO : load local database by configure
        
        
        //DbConnection dbConn = createDbConnectionByProperities(); 
        DbConnection dbConn = createDbConnection();
        
        if (mConnectionsMap == null) {
            mConnectionsMap = new HashMap<String, Object>();
        }
        
        mConnectionsMap.put("mysql", dbConn);        
    }
    
    private DbConnection createDbConnection() {
        /*
        DbcpConnection dbConn = new DbcpConnection(mysql_url, mysql_user, mysql_passwd, mysql_driver,
                5, 30, 10, 36000, 1);
        */
        
        JbdcConnection dbConn = new JbdcConnection(mysql_url, mysql_user, mysql_passwd, mysql_driver);
        
        return dbConn;
    }
    
    private DbcpConnection createDbConnectionByProperities() {
        Properties p = new Properties();    
        p.setProperty("driverClassName", mysql_driver);    
        p.setProperty("url", mysql_url);    
        p.setProperty("username", mysql_user);    
        p.setProperty("password", mysql_passwd);    
        p.setProperty("maxActive", mysql_maxActive);    
        p.setProperty("maxIdle", mysql_maxIdle);    
        p.setProperty("maxWait", mysql_maxWait);    
        p.setProperty("removeAbandoned", mysql_removeAbandoned);    
        p.setProperty("removeAbandonedTimeout", mysql_removeAbandonedTimeout);    
        p.setProperty("testOnBorrow", mysql_testOnBorrow);    
        p.setProperty("logAbandoned", mysql_logAbandoned);  
        
        return new DbcpConnection(p);
    }
    
    
    public DbConnection getDbConnection(String name) {
        return (DbConnection) mConnectionsMap.get(name);
    }
}
