package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.resources.ConversionAPI;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class AlertDetailsDAO {
	
	private static Logger logger = Logger.getLogger(AlertDetailsDAO.class.getName());
	public static String savecomment(Map<String,String> data,String coll) {
		String result=null;
		MongoCollection col = null;
		BasicDBObject doc2=null;
		try {

	logger.info("inside the save comments"+data.get("alertID"));
	List<BasicDBObject> alertIssues = null;
	
		col = DBUtilCiscoOpsMongo.getCollectionByName(coll);
		doc2=new BasicDBObject("alert_id",Long.parseLong(data.get("alertID")));
		FindIterable finditr=col.find(doc2);
		MongoCursor cursor=finditr.iterator();
		BasicDBObject commentObj = new BasicDBObject();
		commentObj.append("TIME",Long.parseLong(data.get("time")) );
		commentObj.append("COMMENT", data.get("comments"));
		commentObj.append("REACH", data.get("reach"));
		logger.error("here with comments");
	if(cursor.hasNext())
		{

		Document doc_obj=(Document)cursor.next();
		logger.error(doc_obj.get("alert_comments"));
		Object alertObj = doc_obj.get("alert_comments");
		if (alertObj != null && !alertObj.equals("")) {
			logger.error("earlier not null");
			alertIssues = (List<BasicDBObject>) alertObj;
			alertIssues.add(commentObj);
		} else {
			logger.error("earlier null");
			alertIssues = new ArrayList<BasicDBObject>();
			alertIssues.add(commentObj);
			logger.error(alertIssues);
			doc_obj.append("alert_comments", alertIssues);
			
		}
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("alert_id", Long.parseLong(data.get("alertID")));
		
		col.updateOne(searchQuery,
                new BasicDBObject("$set",doc_obj));
		logger.error("comment saved!");
		BasicDBList basicDBList = new BasicDBList();
		basicDBList.add(commentObj);
		result = ConversionAPI.convertDBList2JSONStatic(basicDBList);
		logger.error("comment result"+result);

		}
	else{
		result="No data available";
	}
		
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		return result;
	}
	public static void assignAlert(Map<String,String> data,String collectionname) {
		String result=null;
		MongoCollection col = null;
		BasicDBObject doc1=null;
		BasicDBObject doc2=null;
		try {
			logger.info("inside the AlertDetailsDAO for assigning the alert"+data);
			col = DBUtilCiscoOpsMongo.getCollectionByName(collectionname);
			doc2=new BasicDBObject("alert_id",Long.parseLong(data.get("alertID")));
			doc1=new BasicDBObject("alert_assignee",data.get("userName"));
			doc1.append("assigned_by",data.get("assignedby"));
			col.updateOne(doc2,
		                new BasicDBObject("$set",doc1));
		}
		catch(Exception e)
		{
			logger.info("failure with"+e );
		}
		}

	public static void closeAlert(String alertID) {
	MongoCollection col = null;
	BasicDBObject doc2=null;
	try {

logger.info("inside the AlertDetailsDAO");
	col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_alerts_sec");
	doc2=new BasicDBObject("alert_status","closed");
	
	col.updateOne(new BasicDBObject("alert_id", Long.parseLong(alertID)),
                new BasicDBObject("$set",doc2));

	}
	catch (Exception e) {
	logger.error(e);
	}
	}
}