package com.cisco.bccsp.db.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.groupMemberLDAP;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class getUserPermissionDAO {
	private static Logger logger = Logger.getLogger(getUserPermissionDAO.class.getName());
	
	public static groupMemberLDAP getDetails(String userID) {
		String result=null;
		MongoCollection col = null;
		groupMemberLDAP grpmemper=new groupMemberLDAP();
		BasicDBObject doc2=null;
		Boolean conflict_flag=false;
		try {

	logger.info("inside the get method of user permission");
	
	
		
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_users");
		FindIterable finditr=col.find(new Document("userId",userID));
		logger.info(userID);
		logger.info(col.count(new Document("userId",userID)));
		MongoCursor cursor=finditr.iterator();
		if(cursor.hasNext())
		{
			
			Document dbobj =(Document) cursor.next();
			
			grpmemper.setChatWithIncident((Boolean) dbobj.get("ChatWithIncident"));
				grpmemper.setChatWithoutIncident((Boolean)dbobj.get("ChatWithoutIncident"));
				
			grpmemper.setCallSEC((Boolean)dbobj.get("CallSEC"));
				grpmemper.setCreateCase((Boolean)dbobj.get("CreateCase"));
				grpmemper.setConflict(false);
				
		}
		else
		{
			grpmemper.setConflict(true);
		}
		
		
	
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		
		return grpmemper;
	}
}
