package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.IncidentDetails;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class ClosedIncidentDetailsDAO {
	private static Logger logger = Logger.getLogger(ClosedIncidentDetailsDAO.class.getName());
	public static List<IncidentDetails> getClosedIncidentsDetails(String cpe_name) {
		logger.info(cpe_name);
		
		ArrayList<IncidentDetails> incidentdetailList = new ArrayList<IncidentDetails>();
		IncidentDetails incidentdetail = null;
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		try {
			col = DBUtilMongo.getCollection();

			BasicDBList list1=new BasicDBList();
			list1.add("Resolved");
			list1.add("Closed");
			
			BasicDBObject doc1=new BasicDBObject("$in", list1);
			
			BasicDBObject doc2=new BasicDBObject("full_name",cpe_name).append("status", doc1);
			
			
		
			long epoche=(long)System.currentTimeMillis()/1000;
			
			
			
			FindIterable cur= col.find(doc2);
			MongoCursor c1=cur.iterator();
			
			  while(c1.hasNext())
			  {
				  Document  dbobj=(Document) c1.next();
				  //DBObject dbobj=(DBObject) c1.next();
				  Long long_obj=Long.parseLong(dbobj.get("reported_time").toString())  ;
				  long_obj=(epoche-long_obj)/86400;
				  
				  incidentdetail = new IncidentDetails();
				  incidentdetail.setAge(long_obj.toString());
				  incidentdetail.setCet_value(dbobj.get("cet_value").toString());
				  incidentdetail.setDescription((String)dbobj.get("description"));
				  incidentdetail.setIncidentNotes((String)dbobj.get("incident_notes"));
				  incidentdetail.setIncidentNumber((String)dbobj.get("incident_number"));
				  incidentdetail.setAssignee((String)dbobj.get("assignee"));
	           
	            
	            
				  incidentdetailList.add(incidentdetail);
	        	
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

