package com.cisco.bccsp.db.util;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;




import org.apache.log4j.Logger;
import org.bson.Document;




import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBUtilCiscoOpsMongo {
	  private static Logger logger = Logger.getLogger(DBUtilCiscoOpsMongo.class.getName());
		
	    private static DBUtilCiscoOpsMongo dbutil = null;
	    private static MongoDatabase db=null;
	    
	    public DBUtilCiscoOpsMongo(String env) {
	        
	    MongoCollection   col = null;
	        
	        try {
	            
	            // Load the DB JNDI datasource             
	            Context initContext = new InitialContext();
	            Context envContext = (Context) initContext.lookup("java:/comp/env");
	            if(env.equals("dev"))
	            	db = (MongoDatabase) envContext.lookup("jdbc/ciscoopsmongo");
	            else if(env.equals("stage"))
	            	db = (MongoDatabase) envContext.lookup("jdbc/ciscoopsmongo");
	            else if(env.equals("prod"))
	            	db = (MongoDatabase) envContext.lookup("jdbc/ciscoopsmongoprod");
	           
	            // Force a connection so that pool is kickstarted!
	            // Don't know why but tomcat doesn't create on startup
	            col = getCollection();
	            
	            if (col == null) {
	            	logger.error("Unable to establish connections, please check your JNDI resources");                            	
	            } else {
	                logger.info("--> Database connection pool established <--");                            	            	
	            }
	            
	        } catch (NamingException ex) {
	        	logger.error("Unable to get Datasource for JDBC connections", ex);
	        } finally {
	            
	        }
	        
	    }
	    
	    public static DBUtilCiscoOpsMongo getInstance(String env) {
	        
	        if (dbutil == null) {
	            
	            synchronized (DBUtilCiscoOpsMongo.class) {
	                
	                if (dbutil == null) {
	                    dbutil = new DBUtilCiscoOpsMongo(env);
	                }
	                
	            }
	            
	        }
	        
	        return dbutil;
	    }
	    
	    
	    public static MongoCollection getCollection() { 
	    	MongoCollection   col = null;
	        
	        try {
	            col = (MongoCollection) db.getCollection("user_details");
	       logger.info("*************************************");
	      logger.info(col.count());
	        } catch (Exception ex) {
	        	logger.error("Did NOT get a DB Connection", ex);
	        }
	        
	        return col;
	    }
	    
	    public static MongoCollection getCollectionByName(String collection) { 
	    	MongoCollection   col = null;
	        
	        try {
	            col = (MongoCollection) db.getCollection(collection);
	       logger.info("vm-gsqs-cisco-ops");
	      logger.info(col.count());
	        } catch (Exception ex) {
	        	logger.error("Did NOT get a DB Connection", ex);
	        }
	        
	        return col;
	    }
	    
	  
	    

	    
}
