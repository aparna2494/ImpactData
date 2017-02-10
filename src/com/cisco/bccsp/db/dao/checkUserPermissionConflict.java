package com.cisco.bccsp.db.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.ApplicationList;
import com.cisco.bccsp.model.GroupDetail;

import com.cisco.bccsp.model.groupMemberLDAP;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class checkUserPermissionConflict {
	private static Logger logger = Logger.getLogger(checkUserPermissionConflict.class.getName());
	static Map<String, String> successmap= new HashMap<String, String>();
	public static Boolean verify(String userID,groupMemberLDAP grpper) {
		String result=null;
		MongoCollection col = null;
	
		BasicDBObject doc2=null;
		Boolean conflict_flag=false;
		try {

	logger.info("inside the save method of groupMemebers");
	
	
		
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_users");
		FindIterable finditr=col.find(new Document("userId",userID));
		
		MongoCursor cursor=finditr.iterator();
		if(cursor.hasNext())
		{
			Document dbobj =(Document) cursor.next();
			if(grpper.getChatWithIncident()!=dbobj.get("ChatWithIncident")){
				conflict_flag=true;
			}
			else if(grpper.getChatWithoutIncident()!=dbobj.get("ChatWithoutIncident")){
				conflict_flag=true;
			}
			else if(grpper.getCallSEC()!=dbobj.get("CallSEC")){
				conflict_flag=true;
			}
			else if(grpper.getCreateCase()!=dbobj.get("CreateCase")){
				conflict_flag=true;
			}
		}
		
		
	
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		successmap.put("result","success");
		return conflict_flag;
	}
}
