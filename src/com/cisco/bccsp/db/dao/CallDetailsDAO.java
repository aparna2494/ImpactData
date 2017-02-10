package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.CallDetails;
import com.cisco.bccsp.resources.ConversionAPI;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CallDetailsDAO {
	private static Logger logger = Logger.getLogger(CallDetailsDAO.class.getName());
	static Map<String, String> successmap= new HashMap<String, String>();
	public static Map<String,String> savecalldetails(CallDetails calldetails) {
		String result=null;
		MongoCollection col = null;
		BasicDBObject doc2=null;
		try {

	logger.info("inside the save method of calldetails"+calldetails.getCallUserID());
	
	
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_call_details");
		doc2=new BasicDBObject("IncidentNumber",calldetails.getIncidentNumber());
		FindIterable finditr=col.find(doc2);
		MongoCursor cursor=finditr.iterator();
		Document callDetailsObj = new Document("CallUserID",calldetails.getCallUserID()).append("affectedUserID",calldetails.getAffectedUserID()).append("IncidentNumber",calldetails.getIncidentNumber())
		.append("Reason",calldetails.getReason())
		.append("Incident Exist",calldetails.getIncidentExist())
		.append("resolution", calldetails.getResolution() )
		.append("startTime", calldetails.getStartTime() )
		.append("endTime",calldetails.getEndTime()  )
		.append("createdUser",calldetails.getCreatedUser()  )
		.append("createdDate",calldetails.getCreatedDate()  );
	if(cursor.hasNext())
		{

		
		logger.info("document already exists");
		
		}
	else{
		logger.info("in here");
		col.insertOne(callDetailsObj);	
		logger.info("inserted");
	}
		
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		successmap.put("result","success");
		return successmap;
	}
}
