/**  
 *  Copyright (C) 2015 dl@zidoo.tv
 */
package org.sscraper.database.mysql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionInterface {
    public Connection getConnection() throws SQLException;
}
