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

public class DBUtilMongo {
	  private static Logger logger = Logger.getLogger(DBUtilMongo.class.getName());
		
	    private static DBUtilMongo dbutil = null;
	    private static MongoDatabase db=null;
	    
	    public DBUtilMongo() {
	        
	    MongoCollection   col = null;
	        
	        try {
	            
	            // Load the DB JNDI datasource            
	            Context initContext = new InitialContext();
	            Context envContext = (Context) initContext.lookup("java:/comp/env");
	            db= (MongoDatabase)  envContext.lookup("jdbc/mongo");
	           
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
	    
	    public static DBUtilMongo getInstance() {
	        
	        if (dbutil == null) {
	            
	            synchronized (DBUtilMongo.class) {
	                
	                if (dbutil == null) {
	                    dbutil = new DBUtilMongo();
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
	       logger.info("*************************************");
	      logger.info(col.count());
	        } catch (Exception ex) {
	        	logger.error("Did NOT get a DB Connection", ex);
	        }
	        
	        return col;
	    }
	    
	  
	    

	    
}
