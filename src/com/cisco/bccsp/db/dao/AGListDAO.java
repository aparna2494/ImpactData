package com.cisco.bccsp.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.bson.Document;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.BasicDBObject;

public class AGListDAO {
	
	private static Logger logger = Logger
			.getLogger(AGListDAO.class.getName());

public static String getAssignedGroup() {

	
		final String key = "assignedGroup";
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		JSONArray arr = new JSONArray();
		try {
			
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_assigned_groups");
			MongoCursor<String> cursor = col.distinct(key, String.class).iterator();
			while(cursor.hasNext()) {
				String document =  (String) cursor.next();
				JSONObject obj = new JSONObject();
	            obj.put(key, document);
				arr.add(obj);

			}

		} catch (Exception e) {
			logger.error(e);
		}
	
		return arr.toJSONString();
	}
public static void submitRoster(){
	
}
};