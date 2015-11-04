/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JbdcConnection extends DbConnection {

    private String mysql_url;
    private String mysql_user;
    private String mysql_passwd;
    private String mysql_driverClass;
    
    public JbdcConnection(String url, String user, String passwd, String driverClass) {
        mysql_url = url;
        mysql_user = user;
        mysql_passwd = passwd;
        mysql_driverClass = driverClass;
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(mysql_driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
        String url = mysql_url + "?user=" + mysql_user + "&password=" + mysql_passwd + "&useUnicode=true&characterEncoding=UTF8";
        
        return DriverManager.getConnection(url);                
    }
}
