/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;


public class DbConnection implements ConnectionInterface {
    protected static DataSource mDataSource;
      
    /**
     * Get connect status
     * 
     * @return
     * @throws SQLException
     */
    public Map<String, Integer> getDataSourceStatus()
            throws SQLException {
        BasicDataSource bds = (BasicDataSource) mDataSource;
        Map<String, Integer> map = new HashMap<String, Integer>(2);
        map.put("active_number", bds.getNumActive());
        map.put("idle_number", bds.getNumIdle());
        return map;
    }
    
    /**
     * Shutdown data source connection
     * 
     * @throws SQLException
     */
    public void shutdownDataSource() throws SQLException {
        BasicDataSource bds = (BasicDataSource) mDataSource;
        bds.close();
    }
    
    /**
     * Close connection
     * @param conn
     * @param pstmt
     * @param rs
     */
    public void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != pstmt) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } 
    
    /**
     * Close connection
     * @param conn
     * @param stmt
     * @param rs
     */
    public void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } 
    
    /**
     * Close connection
     * @param conn
     * @param pstmt
     */
    public void closeResources(Connection conn, PreparedStatement pstmt) {  
        if (null != pstmt) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
    } 
    
    /**
     * Close connection
     * @param conn
     * @param stmt
     */
    public void closeResources(Connection conn, Statement stmt) {  
        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    } 
}
