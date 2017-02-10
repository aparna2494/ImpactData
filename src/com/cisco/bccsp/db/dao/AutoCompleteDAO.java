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

import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.AutoComplete;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;


public class AutoCompleteDAO {
	private static Logger logger = Logger.getLogger(AutoCompleteDAO.class.getName());


public static List<AutoComplete> getIncidents() {

	ArrayList<AutoComplete> autocompleteList = new ArrayList<AutoComplete>();
	AutoComplete autocomplete = null;
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	try {
		col = DBUtilMongo.getCollection();

		BasicDBList list1=new BasicDBList();
		list1.add("Assigned");
		list1.add("Pending");
		list1.add("In Progress");
		BasicDBObject doc1=new BasicDBObject("$in", list1);
		
		BasicDBObject doc2=new BasicDBObject("status", doc1);
		
		
		FindIterable cur= col.find(doc2);
		MongoCursor c1=cur.iterator();
		
		  while(c1.hasNext())
		  {
			  Document  dbobj=(Document) c1.next();
			  //DBObject dbobj=(DBObject) c1.next();
        	
			  autocomplete = new AutoComplete();

			  autocomplete.setIncidentNumber((String)dbobj.get("incident_number"));
			  if(set.contains((String)dbobj.get("customer")))
			  {
				  autocomplete.setCustomername("");
			  }
			  else
			  {
			  autocomplete.setCustomername((String)dbobj.get("customer"));
			  set.add((String)dbobj.get("customer"));
			  }
           
            
            
            autocompleteList.add(autocomplete);
        	
        }


	} catch (Exception e) {
		logger.error(e);
	}

	finally {		
		//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
	}
	logger.info(autocompleteList);
	return autocompleteList;
}
}


