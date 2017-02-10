package com.cisco.bccsp.db.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.AutoComplete;
import com.cisco.bccsp.model.IncidentDetails;
import com.cisco.bccsp.model.UserDetails;
import com.cisco.bccsp.model.Chat;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;

public class ChatDao {
	private static Logger logger = Logger.getLogger(ChatDao.class.getName());
	public static String startChat(Chat chat) {
		
		MongoCollection col = null;
		JSONObject finalObj = new JSONObject();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_chat");
			
			Document modifiedObject = new Document();
			modifiedObject.append("$set", new BasicDBObject().append("chatActive", chat.getChatActive()).append("initUser", chat.getInitUser()));
			
			Document searchQuery = new Document().append("chatSubjectId", chat.getChatSubjectId());
			
			List<String> currentUsers = new ArrayList<String>();

			if(chat.getText() != null && !chat.getText().isEmpty()) {
				Document messageDetail = new Document();
				messageDetail.put("createdTime", new Date());
				if(chat.getFrom() != null && !chat.getFrom().isEmpty())
					messageDetail.put("from", chat.getFrom());
				if(chat.getAppId() != null && !chat.getAppId().isEmpty())
					messageDetail.put("appId", chat.getAppId());
				messageDetail.put("event", chat.getEvent());
				messageDetail.put("text", chat.getText());
				if(chat.getUser() != null && !chat.getUser().isEmpty())
					messageDetail.put("user", chat.getUser());
				if(chat.getUserType() != null && !chat.getUserType().isEmpty())
					messageDetail.put("userType", chat.getUserType());
				messageDetail.put("chatId", chat.getChatId());
				if(chat.getUserList() != null && !chat.getUserList().isEmpty())
					messageDetail.put("usersInvolved", chat.getUserList());
				
				modifiedObject.append("$push", new Document().append("messages", messageDetail));	
			}
			
			if(chat.getUserList() != null && !chat.getUserList().isEmpty()) {
				modifiedObject.put("$addToSet", new Document().append("users", new BasicDBObject("$each", chat.getUserList())));
				}
			col.updateOne(searchQuery, modifiedObject, new UpdateOptions().upsert(true));
			
			finalObj.put("chatSubjectId", chat.getChatSubjectId());
			finalObj.put("response", "success");
			
		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return finalObj.toJSONString();
	}
	
public static String updateChat(Chat chat) {
		
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		JSONObject responseObj = new JSONObject();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_chat");
			
			Document modifiedObject = new Document();
			modifiedObject.append("$set", new BasicDBObject().append("chatActive", chat.getChatActive()));
			
			Document searchQuery = new Document().append("chatSubjectId", chat.getChatSubjectId());
			List<String> currentUsers = new ArrayList<String>();

			if(chat.getText() != null && !chat.getText().isEmpty()) {
				Document messageDetail = new Document();
				messageDetail.put("createdTime", new Date());
				if(chat.getFrom() != null && !chat.getFrom().isEmpty())
					messageDetail.put("from", chat.getFrom());
				if(chat.getAppId() != null && !chat.getAppId().isEmpty())
					messageDetail.put("appId", chat.getAppId());
				messageDetail.put("event", chat.getEvent());
				messageDetail.put("text", chat.getText());
				if(chat.getUser() != null && !chat.getUser().isEmpty())
					messageDetail.put("user", chat.getUser());
				if(chat.getUserType() != null && !chat.getUserType().isEmpty())
					messageDetail.put("userType", chat.getUserType());
				messageDetail.put("chatId", chat.getChatId());
				if(chat.getUserList() != null && !chat.getUserList().isEmpty())
					messageDetail.put("usersInvolved", chat.getUserList());
				
				modifiedObject.put("$push", new Document().append("messages", messageDetail));
			}
			
			if(chat.getUserList() != null && !chat.getUserList().isEmpty()) {
				modifiedObject.put("$addToSet", new Document().append("users", new BasicDBObject("$each", chat.getUserList())));
				}
			col.updateOne(searchQuery, modifiedObject);
			responseObj.put("response", "success");
				
			
		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return responseObj.toJSONString();
	}

public static String getChat(String chatId) {
	
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	JSONObject result = new JSONObject();
	JSONObject response = new JSONObject();
	try {
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_chat");
		
		Document searchQuery = new Document().append("_id", new ObjectId(chatId));
		FindIterable findItr = col.find(searchQuery);
		MongoCursor cursor = findItr.iterator();
		while(cursor.hasNext()) {
			Document document =  (Document) cursor.next();
			JSONParser parser = new JSONParser();
			result = (JSONObject) parser.parse(document.toJson());
		}	
		response.put("result", result);
		response.put("response", "success");
		
	} catch (Exception e) {
		logger.error(e);
		response.put("response", "error: chat not found");
	}

	finally {		
		//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
	}
	
	return response.toJSONString();
}

public static String getActiveChatsByUser(String userId) {
	
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
	JSONObject response = new JSONObject();
	try {
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_chat");
		List<String> list = new ArrayList<String>();
		list.add(userId);
		
		BasicDBObject query = new BasicDBObject();
		query.put("users", new BasicDBObject("$in", list));
		query.put("chatActive", "Y");
		
		FindIterable findItr = col.find(query);
		MongoCursor cursor = findItr.iterator();
		while(cursor.hasNext()) {
			Document document =  (Document) cursor.next();
			JSONParser parser = new JSONParser();
			arr.add((JSONObject) parser.parse(document.toJson()));
		}
		response.put("result", arr);
		response.put("response", "success");
		
	} catch (Exception e) {
		logger.error(e);
		response.put("response", "error: chat not found" );
	}

	finally {		
		//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
	}
	
	return response.toJSONString();
}

public static String getActiveChats() {
	
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
	JSONObject response = new JSONObject();
	try {
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_chat");
		
		BasicDBObject query = new BasicDBObject();
		query.put("chatActive", "Y");
		
		FindIterable findItr = col.find(query);
		MongoCursor cursor = findItr.iterator();
		while(cursor.hasNext()) {
			Document document =  (Document) cursor.next();
			JSONParser parser = new JSONParser();
			arr.add((JSONObject) parser.parse(document.toJson()));
		}	
		response.put("result", arr);
		response.put("response", "success");
		
	} catch (Exception e) {
		logger.error(e);
		response.put("response", "error: chat not found");
	}

	finally {		
		//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
	}
	
	return response.toJSONString();
}

	public static String getAGUsersAvailability(IncidentDetails incident) {
	
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		String result = null;
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_ag_users");
		
			BasicDBObject query = new BasicDBObject("AG", incident.getAG());
			FindIterable findItr = col.find(query);
			MongoCursor cursor = findItr.iterator();
			while(cursor.hasNext()) {
				Document document =  (Document) cursor.next();
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(document.toJson());
			    JSONArray agObj =  (JSONArray) obj.get("users");
			    logger.info(agObj);
			    result = agObj.toJSONString();
			}

		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
	
		return result;
	}

	public static List<String> getRosterAvailability(IncidentDetails incident) {

		MongoCollection col = null;
		List<String> userIds = new ArrayList<String>();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");

			BasicDBObject query = new BasicDBObject();
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).toDate());
		    logger.info(date);
			
		    query.put("AG.name", incident.getAG());
			query.put("date", date);

			FindIterable findItr = col.find(query);
			MongoCursor cursor = findItr.iterator();
			while(cursor.hasNext()) {
				logger.info("cursor is not empty");
				Document document =  (Document) cursor.next();
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(document.toJson());
			    JSONObject agObj =  (JSONObject) obj.get("AG");
			    JSONArray users = (JSONArray) agObj.get("users");
			    for (int i = 0; i < users.size(); i++) {
			    	JSONObject jsonObj = (JSONObject) users.get(i);
			    	if(Math.round((Double) jsonObj.get("shiftStartTime")) < new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).getMinuteOfDay() && Math.round((Double) jsonObj.get("shiftEndTime")) > new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).getMinuteOfDay()) {
			    		  logger.info("startTime: "+ jsonObj.get("shiftStartTime"));
			    		  logger.info("endTime: "+ jsonObj.get("shiftEndTime"));
			    		  if(!userIds.contains(jsonObj.get("name").toString()))
			    			  userIds.add(jsonObj.get("name").toString());
			    	  }
			    }
			}

		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}

		return userIds;
	}
	
	public static String getApplicationAvailability(IncidentDetails incident) {
		
		MongoCollection col = null;
		JSONArray finalArr = new JSONArray();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
			
			BasicDBObject query = new BasicDBObject();
			if(incident.getAppId() != 0)
				query.put("application.appId", incident.getAppId());
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).toDate());
		    
			
			query.put("date", date);
			
			FindIterable findItr = col.find(query);
			MongoCursor cursor = findItr.iterator();
			while(cursor.hasNext()) {
				List<String> userIds = new ArrayList<String>();
				JSONObject finalObj = new JSONObject();
				logger.info("cursor is not empty");
				Document document =  (Document) cursor.next();
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(document.toJson());
			    JSONObject agObj =  (JSONObject) obj.get("application");
			    finalObj.put("appName",agObj.get("name"));
			    JSONArray users = (JSONArray) agObj.get("users");
			    for (int i = 0; i < users.size(); i++) {
			    	JSONObject jsonObj = new JSONObject();
			    	  jsonObj = (JSONObject) users.get(i);
			    	  if(Math.round((Double) jsonObj.get("shiftStartTime")) < new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).getMinuteOfDay() && Math.round((Double) jsonObj.get("shiftEndTime")) > new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).getMinuteOfDay()) {
			    		 
			    		  if(!userIds.contains(jsonObj.get("name").toString()))
			    			  userIds.add(jsonObj.get("name").toString());
			    	  }
			    	}
			    finalObj.put("userIds", userIds);
			    finalArr.add(finalObj);
			}

		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}

		return finalArr.toJSONString();
	}

}
