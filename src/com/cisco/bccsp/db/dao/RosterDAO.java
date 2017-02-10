package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.Chat;
import com.cisco.bccsp.model.Roster;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;


public class RosterDAO {
	private static Logger logger = Logger.getLogger(RosterDAO.class.getName());


	
	public static String getUniqueApplication() {
		
		final String key = "application.name";
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		JSONArray arr = new JSONArray();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
			MongoCursor<String> cursor = col.distinct(key, String.class).iterator();
			while(cursor.hasNext()) {
				String document =  (String) cursor.next();
				JSONObject obj = new JSONObject();
	            obj.put("name", document);
				arr.add(obj);

			}

		} catch (Exception e) {
			logger.error(e);
		}
	
		return arr.toJSONString();
	}
	
public static String getUniqueAGs() {
		
		final String key = "AG.name";
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		JSONArray arr = new JSONArray();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
			MongoCursor<String> cursor = col.distinct(key, String.class).iterator();
			while(cursor.hasNext()) {
				String document =  (String) cursor.next();
				JSONObject obj = new JSONObject();
	            obj.put("name", document);
				arr.add(obj);

			}

		} catch (Exception e) {
			logger.error(e);
		}
	
		return arr.toJSONString();
	}

public static List<Document> getUsersForField(String fieldName, String agName, String fromDate, String toDate) {
	List<Document> usersList = new ArrayList<Document>();
	
	MongoCollection collection = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
	
	MongoCursor<Document> resultCursor = collection.find(new Document(fieldName + ".name", agName).append("date", new Document("$gt", fromDate).append("$lt", toDate))).iterator();
	
	while(resultCursor.hasNext()) {
		Document currentDocument = resultCursor.next();
		
		Document document = (Document) currentDocument.get(fieldName);
		List<Document> listOfUsers = (List<Document>) document.get("users");
		
		usersList.add(new Document("date", currentDocument.get("date")).append("users", listOfUsers));
	}
	
	return usersList;
}


	


	public static String getRoster(Roster roster) {

		MongoCollection col = null;
		MongoCollection col1 = null;
		List<String> userIds = new ArrayList<String>();
		List<String> dates = new ArrayList<String>();
		JSONArray arr = new JSONArray();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");

			BasicDBObject query = new BasicDBObject();
			if("".equals(roster.getDate())) {
				col1 = DBUtilCiscoOpsMongo.getCollectionByName("bcc_calendar");
				BasicDBObject newQuery = new BasicDBObject();
				if(roster.getMonth() != 0)
					newQuery.put("month", (double) roster.getMonth());
				else if(roster.getWeek() != 0)
					newQuery.put("week", (double) roster.getWeek());
				FindIterable findItr1 = col1.find(newQuery);
				MongoCursor cursor1 = findItr1.iterator();
				while(cursor1.hasNext()) {
					Document document1 =  (Document) cursor1.next();
					JSONParser parser = new JSONParser();
					JSONObject obj1 = (JSONObject) parser.parse(document1.toJson());
					dates.add(obj1.get("date").toString());					
				}								
			}
				
		    query.put("AG.id", roster.getAGId());
		    if(!dates.isEmpty())
		    	query.put("date", new BasicDBObject("$in",dates));
		    else
		    	query.put("date", roster.getDate());

			FindIterable findItr = col.find(query);
			MongoCursor cursor = findItr.iterator();
			while(cursor.hasNext()) {
				JSONObject finalObj = new JSONObject();
				Document document =  (Document) cursor.next();
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(document.toJson());
			    JSONObject agObj =  (JSONObject) obj.get("AG");
			    JSONArray users = (JSONArray) agObj.get("users");
			    finalObj.put("date", obj.get("date").toString());
			    finalObj.put("users", users);
			    arr.add(finalObj);
			}

		} catch (Exception e) {
			logger.error(e);
		}

		return arr.toJSONString();
	}
	
	public static String getApplicationRoster(Roster roster) {

		MongoCollection col = null;
		MongoCollection col1 = null;
		List<String> userIds = new ArrayList<String>();
		List<String> dates = new ArrayList<String>();
		JSONArray arr = new JSONArray();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");

			BasicDBObject query = new BasicDBObject();
			if("".equals(roster.getDate())) {
				col1 = DBUtilCiscoOpsMongo.getCollectionByName("bcc_calendar");
				BasicDBObject newQuery = new BasicDBObject();
				if(roster.getMonth() != 0)
					newQuery.put("month", (double) roster.getMonth());
				else if(roster.getWeek() != 0)
					newQuery.put("week", (double) roster.getWeek());
				FindIterable findItr1 = col1.find(newQuery);
				MongoCursor cursor1 = findItr1.iterator();
				while(cursor1.hasNext()) {
					Document document1 =  (Document) cursor1.next();
					JSONParser parser = new JSONParser();
					JSONObject obj1 = (JSONObject) parser.parse(document1.toJson());
					dates.add(obj1.get("date").toString());					
				}								
			}
				
		    query.put("application.appId", roster.getAGId());
		    if(!dates.isEmpty())
		    	query.put("date", new BasicDBObject("$in",dates));
		    else
		    	query.put("date", roster.getDate());

			FindIterable findItr = col.find(query);
			MongoCursor cursor = findItr.iterator();
			while(cursor.hasNext()) {
				JSONObject finalObj = new JSONObject();
				Document document =  (Document) cursor.next();
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(document.toJson());
			    JSONObject agObj =  (JSONObject) obj.get("application");
			    JSONArray users = (JSONArray) agObj.get("users");
			    finalObj.put("date", obj.get("date").toString());
			    finalObj.put("users", users);
			    arr.add(finalObj);
			}

		} catch (Exception e) {
			logger.error(e);
		}

		return arr.toJSONString();
	}
	
public static String updateRoster(Roster roster) {
		
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		JSONObject responseObj = new JSONObject();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
			
			Document modifiedObject = new Document();
			
			Document searchQuery = new Document().append("AG.id", roster.getAGId()).append("date", roster.getDate());
			List<String> currentUsers = new ArrayList<String>();
			

			Document pull = new Document("$pull", new Document("AG.users", new Document("name", roster.getUserId())));

			col.updateOne(searchQuery,  pull);

			Document users = new Document();
			users.put("name", roster.getUserId());
			users.put("shiftStartTime", roster.getStartShiftTime());
			users.put("shiftEndTime", roster.getEndShiftTime());
			users.put("shiftId", roster.getShiftId());
				
			modifiedObject.put("$addToSet", new Document().append("AG.users", users));

			col.updateOne(searchQuery, modifiedObject, new UpdateOptions().upsert(true));
			responseObj.put("response", "success");
				
			
		} catch (Exception e) {
			logger.error(e);
		}
		
		return responseObj.toJSONString();
	}

public static String removeRoster(Roster roster) {
	
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	JSONObject responseObj = new JSONObject();
	try {
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
		
		Document searchQuery = new Document().append("AG.id", roster.getAGId()).append("date", roster.getDate());
		
		Document pull = new Document("$pull", new Document("AG.users", new Document("name", roster.getUserId())));

		col.updateOne(searchQuery,  pull);
		responseObj.put("response", "success");
			
		
	} catch (Exception e) {
		logger.error(e);
	}
	
	return responseObj.toJSONString();
}

public static boolean insertRoster(String s)
{
	MongoCollection collection = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
	logger.error(s);
	Document doc = Document.parse(s);
	collection.insertOne(doc);
	
	logger.error(doc.get("_id").toString());
	
	if(doc.get("_id") != null)
		return true;
	else
		return false;
}

public static String getDates(String fieldname,String trackname) {
	
	final String key = "date";
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	JSONArray arr = new JSONArray();
	BasicDBObject filter = new BasicDBObject();
	filter.put( fieldname+".name", trackname );
	try {
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
		MongoCursor<String> cursor = col.distinct(key,filter, String.class).iterator();
		while(cursor.hasNext()) {
			String document =  (String) cursor.next();
			JSONObject obj = new JSONObject();
			arr.add(document);

		}

	} catch (Exception e) {
		logger.error(e);
	}

	return arr.toJSONString();
}

public static List<Document> getUsers(String fieldName, String agName, String Date) {
	List<Document> usersList = new ArrayList<Document>();
	
	MongoCollection collection = DBUtilCiscoOpsMongo.getCollectionByName("bcc_roster_schedule");
	
	MongoCursor<Document> resultCursor = collection.find(new Document(fieldName + ".name", agName).append("date", Date)).iterator();
	
	while(resultCursor.hasNext()) {
		Document currentDocument = resultCursor.next();
		
		Document document = (Document) currentDocument.get(fieldName);
		usersList = (List<Document>) document.get("users");

	}
	
	return usersList;
}

}
