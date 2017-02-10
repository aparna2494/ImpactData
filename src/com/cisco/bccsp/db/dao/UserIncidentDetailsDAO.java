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
import com.cisco.bccsp.model.IncidentDetails;
import com.cisco.bccsp.model.UserDetails;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class UserIncidentDetailsDAO {
	private static Logger logger = Logger.getLogger(UserIncidentDetailsDAO.class.getName());
	public static List<IncidentDetails> getUserIncidentDetails(String userId) {
		
		ArrayList<IncidentDetails> incidentdetailList = new ArrayList<IncidentDetails>();
		IncidentDetails incidentdetail = null;
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
					incidentdetail=new IncidentDetails();
					incidentdetail.setAssignee(inobj.getString("assignee"));
					incidentdetail.setAG(inobj.getString("AG"));
					incidentdetail.setStatus(inobj.getString("status"));
					incidentdetail.setIncidentNumber(inobj.getString("incident_number"));
					
					Long a=Long.parseLong(inobj.get("reported_time").toString());
					Long b=new Long(1000);
					Long c =a*b;
					
					Date reported_date=new Date(Long.parseLong((c.toString()).toString()));
					
					DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					a=Long.parseLong(inobj.get("resolved_time").toString());
					b=new Long(1000);
					c =a*b;
					if(c!=0)
					{
						Date resolved_date=new Date(Long.parseLong(c.toString()));
						incidentdetail.setResolved_date(format.format(resolved_date));
					}
					else
					{
						incidentdetail.setResolved_date("-");
					}
					incidentdetail.setReported_date(format.format(reported_date));
					incidentdetail.setCet_value(inobj.get("cet_value").toString());
					incidentdetailList.add(incidentdetail);
				}
			}
			


		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return incidentdetailList;
	}
}
