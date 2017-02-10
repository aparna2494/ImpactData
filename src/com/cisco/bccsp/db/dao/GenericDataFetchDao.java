package com.cisco.bccsp.db.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.parser.JSONParser;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.CpeDetails;
import com.mongodb.*;
import com.mongodb.client.*;
import com.cisco.bccsp.db.util.GenericType;

public class GenericDataFetchDao {
	private static Logger logger = Logger.getLogger(GenericDataFetchDao.class.getName());
	public static String getDocument(String coll_name,Object...objects )
	{
		
		logger.info(coll_name);
		MongoCollection col;
		
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName(coll_name);
			
			BasicDBObject doc1=null;
			BasicDBList dblist=new BasicDBList();
			BasicDBObject queryObj = new BasicDBObject();
			for (int i = 0; i < objects.length; i = i + 2) {
				queryObj.append(String.valueOf(objects[i]), objects[i + 1]);
			}
			
			
			Document resultObj = null;
			MongoCursor cursor = col.find(queryObj).iterator();
			
			while (cursor.hasNext()) {
				
				resultObj = (Document) cursor.next();
				
				dblist.add(resultObj);
			}
			

			return dblist.toString();
			
			
			

		} catch (Exception e) {
			logger.error(e);
		}
		return null;
		
	}

	/*public static Map<String, Object> getFlowData(String whoIsIt,String collection_name) {
		// TODO Auto-generated method stub
		MongoCollection col;
		MongoCollection col_track;
		MongoCollection col_users;
		List<Long> tracks=new ArrayList<Long>();
		Map<String,Object> resultmap = new HashMap<String,Object>();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName(collection_name);
			col_track = DBUtilCiscoOpsMongo.getCollectionByName("track_mappings");
			col_users=DBUtilCiscoOpsMongo.getCollectionByName("user_track_mappings");
			MongoCursor cursor=col_track.find(new BasicDBObject()).iterator();
			long AdminUser=col_users.count(new BasicDBObject().append("user", whoIsIt).append("admin",true));
			while(cursor.hasNext())
			{
				logger.info("inside iterator");
				Document doc=(Document) cursor.next();
				logger.info(doc.get("protected").toString());
				if(doc.get("protected").toString().equalsIgnoreCase("N"))
				{
					logger.info("in if");
				tracks.add((long)doc.get("track_id"));
				}
				else if(AdminUser>0)
				{
					logger.info("in else if");
					tracks.add((long)doc.get("track_id"));
				}
				else
				{
					logger.info("in else");
					MongoCursor userCur=col_users.find(new BasicDBObject().append("user",whoIsIt).append("trackAccess.track_id", doc.get("track_id")).append("trackAccess.level", new BasicDBObject("$gt",0))).iterator();
					if(userCur.hasNext())
					{
						logger.info("inside elseif");
						tracks.add((long)doc.get("track_id"));
						
					}
				}
				
			}
			logger.info(tracks);
			BasicDBObject p1=new BasicDBObject().append("$unwind", "$nodes");
			BasicDBObject p2=new BasicDBObject().append("$match",new BasicDBObject("nodes.data.track_id",new BasicDBObject("$in",tracks)));
			List<BasicDBObject> p=new ArrayList<BasicDBObject>();
			p.add(p1);
			p.add(p2);
			MongoCursor aggCur=col.aggregate(p).iterator();
			BasicDBList nodes=new BasicDBList();
			List<String> nodeIds=new ArrayList<String>();
			
			while(aggCur.hasNext())
			{
				Document docagg=(Document)aggCur.next();
				JSONParser parser = new JSONParser();
				
				nodes.add(docagg.get("nodes"));
				
			}
			logger.info(nodes);
			MongoCursor curr1 = col.find().iterator();
			
			while (curr1.hasNext()) {
				
				resultObj = (Document) cursor.next();
				
				dblist.add(resultObj);
			}
			if(curr1.hasNext())
			{
				Document doc=(Document) curr1.next();
				logger.info(doc);
				resultmap.put("edges",doc.get("edges").toString());
			}
			resultmap.put("nodes",nodes.toString());
			//logger.info(resultmap);
			return resultmap;
			
			
			

		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}*/
}
