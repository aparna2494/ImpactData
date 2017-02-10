package com.cisco.bccsp.db.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.AutoComplete;

import com.cisco.bccsp.model.Timeline;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class TimelineActivityDAO {
	private static Logger logger = Logger.getLogger(TimelineActivityDAO.class.getName());
	public static List<Timeline> getTimelineDetails(String userId) {
		
		ArrayList<Timeline> timelineList = new ArrayList<Timeline>();
		Timeline timelinedetail = null;
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		try {
			col = DBUtilMongo.getCollectionByName("user_details");
			
			BasicDBObject doc1=null;
			
			
			
			
			doc1=new BasicDBObject("user",userId);
			FindIterable finditr=col.find(doc1);
			MongoCursor cursor=finditr.iterator();
			if(cursor.hasNext())
			{
				Document  dbobj=(Document)cursor.next();
				List incident_list=(List)dbobj.get("incident_details");
				Iterator ilitr=incident_list.iterator();
				while(ilitr.hasNext())
				{
					Document  inobj=(Document)ilitr.next();
					timelinedetail=new Timeline();
					timelinedetail.setActivity("Incident Creation");
					timelinedetail.setCustomClass("glyphicon glyphicon-file font-blue-steel");
					timelinedetail.setActivityID(inobj.getString("incident_number"));
					timelinedetail.setActivityStatus(inobj.getString("status"));
					Long a=Long.parseLong(inobj.get("reported_time").toString());
					Long b=new Long(1000);
					Long c =a*b;
					
					Date reported_date=new Date(Long.parseLong((c.toString()).toString()));
					
					DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					timelinedetail.setActivitytime(format.format(reported_date));
					
					timelineList.add(timelinedetail);
				}
				
				
				List escalation_list=(List)dbobj.get("escalation_details");
				Iterator elitr=escalation_list.iterator();
				while(elitr.hasNext())
				{
					Document  inobj=(Document)elitr.next();
					timelinedetail=new Timeline();
					timelinedetail.setActivity("Escalation");
					timelinedetail.setCustomClass("glyphicon glyphicon-warning-sign font-red-intense");
					timelinedetail.setActivityID(inobj.getString("incident_number"));
					timelinedetail.setActivityStatus(inobj.getString("escalation_status"));
					
					//Date reported_date=new Date(Long.parseLong(inobj.get("escalated_date").toString()));
					
					DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					DateFormat format2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
					Date reported_date=format2.parse(inobj.get("escalated_date").toString());
					
					timelinedetail.setActivitytime(format.format(reported_date).toString());
					
					timelineList.add(timelinedetail);
				}
				
				
			}
			


		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return timelineList;
	}
}
