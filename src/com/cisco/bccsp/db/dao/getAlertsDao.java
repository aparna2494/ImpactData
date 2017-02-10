package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class getAlertsDao {

	private static Logger logger = Logger.getLogger(getAlertsDao.class.getName());
	public static String getDocument(String user,String coll_name) {
		logger.info(coll_name);
		MongoCollection col;
		MongoCollection col_track;
		MongoCollection col_user_track;
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName(coll_name);
			col_track=DBUtilCiscoOpsMongo.getCollectionByName("track_mappings");
			col_user_track=DBUtilCiscoOpsMongo.getCollectionByName("user_track_mappings");
			MongoCursor curTrack=col_track.find(new BasicDBObject("protected","Y")).iterator();
			List<Long> protectedTracks=new ArrayList<Long>();
			List<Long> newProtectedTracks=new ArrayList<Long>();
			while(curTrack.hasNext())
			{
				Document doc=(Document) curTrack.next();
				protectedTracks.add(((Number)doc.get("track_id")).longValue());
			}
			logger.info(protectedTracks);
			
			Iterator<Long> itr_tracks=protectedTracks.iterator();
			while(itr_tracks.hasNext())
			{
				Long trackId=itr_tracks.next();
				logger.info(new BasicDBObject("user",user).append("tracksAccess.track_id",trackId));
				MongoCursor cur_user=col_user_track.find(new BasicDBObject("user",user).append("tracksAccess.track_id",trackId)).projection(new BasicDBObject("tracksAccess.$",1)).iterator();
				logger.info(cur_user.hasNext());
				if(cur_user.hasNext())
				{
					Document doc=(Document)cur_user.next();
					
					List doclst=(List) doc.get("tracksAccess");
					Document doc2=(Document) doclst.get(0);
					int levell=(Integer) doc2.get("level");
					logger.info(levell);
					if(levell==0)
					{
						logger.info("removing");
						logger.info(doc2.get("track_id"));
						newProtectedTracks.add(((Number)doc2.get("track_id")).longValue());
					}
				}
				else
				{
					newProtectedTracks.add(trackId);
				}
				
			}
			logger.info(newProtectedTracks);
			BasicDBObject doc1=null;
			BasicDBList dblist=new BasicDBList();
			
			
			Document resultObj = null;
			MongoCursor cursor = col.find(new BasicDBObject("alert_status","open").append("track_id", new BasicDBObject("$nin",newProtectedTracks))).iterator();
			Document finalResult=new Document();
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

}
