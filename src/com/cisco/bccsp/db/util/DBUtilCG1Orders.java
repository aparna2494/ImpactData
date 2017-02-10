/**
 * Singleton for core database interactions
 *
 * @author jhasting
 */

package com.cisco.bccsp.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class DBUtilCG1Orders {
    
    private static Logger logger = Logger.getLogger(DBUtilCG1Orders.class.getName());
	
    private static DBUtilCG1Orders dbutil = null;
    private static DataSource ds = null;
    
    
    
    public DBUtilCG1Orders() {
        
        Connection conn = null;
        
        try {
            
            // Load the DB JNDI datasource            
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/cg1orders");
            
            // Force a connection so that pool is kickstarted!
            // Don't know why but tomcat doesn't create on startup
            conn = getConnection();
            
            if (conn == null) {
            	logger.error("Unable to establish connections, please check your JNDI resources");                            	
            } else {
                logger.info("--> Database connection pool established <--");                            	            	
            }
            
        } catch (NamingException ex) {
        	logger.error("Unable to get Datasource for JDBC connections", ex);
        } finally {
            releaseConnectionResources(conn, null, null);
        }
        
    }
    
    
    
    public static DBUtilCG1Orders getInstance() {
        
        if (dbutil == null) {
            
            synchronized (DBUtilCG1Orders.class) {
                
                if (dbutil == null) {
                    dbutil = new DBUtilCG1Orders();
                }
                
            }
            
        }
        
        return dbutil;
    }
    
    
    
    public static Connection getConnection() { 
        Connection conn = null;
        
        try {
            conn = ds.getConnection();
        } catch (Exception ex) {
        	logger.error("Did NOT get a DB Connection", ex);
        }
        
        return conn;
    }
    
    
	public static Timestamp getCurrentTimestamp(Connection conn) throws SQLException {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Timestamp currentTimestamp = null;

		String dateqry = 
              "SELECT SYSDATE THE_DATE " 
		    + "FROM DUAL ";

		stmt = conn.prepareStatement(dateqry);

		rs = stmt.executeQuery();

		if (rs.next()) {
			currentTimestamp = rs.getTimestamp("THE_DATE");
		}
		
		releaseConnectionResources(null, rs, stmt);
		
		return currentTimestamp;

	}   
    
    public static void releaseConnectionResources(Connection conn, ResultSet rs, Statement stmt) {
        
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            	logger.error("ResultSet not closed", e);
            }
            rs = null;
        }
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            	logger.error("Statement not closed", e);                
            }
            stmt = null;
        }
        
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            	logger.error("Connection not closed", e);                
            }
            conn = null;
        }

    }  
    

    
    
    
}
