package com.cisco.bccsp.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.AutoComplete;
import com.cisco.bccsp.model.searchIndex;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;


public class searchIndexListDAO {
	private static Logger logger = Logger.getLogger(searchIndexListDAO.class.getName());


public static List<searchIndex> getList() {

	ArrayList<searchIndex> searchindexlist = new ArrayList<searchIndex>();
	searchIndex searchindex = null;
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	try {
		col = DBUtilCiscoOpsMongo.getCollectionByName("searchIndex");

		
		FindIterable cur= col.find();
		MongoCursor c1=cur.iterator();
		
		  while(c1.hasNext())
		  {
			  Document  dbobj=(Document) c1.next();
			  //DBObject dbobj=(DBObject) c1.next();
        	
			  searchindex = new searchIndex();
			  searchindex.setCategory(dbobj.getString("type").toString());
			  searchindex.setLabel(dbobj.getString("search_string").toString());
			  searchindex.setType(dbobj.getString("search_id").toString());
			 
			  
           
            
            
            searchindexlist.add(searchindex);
        	
        }


	} catch (Exception e) {
		logger.error(e);
	}

	finally {		
		//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
	}
	
	return searchindexlist;
}
}


