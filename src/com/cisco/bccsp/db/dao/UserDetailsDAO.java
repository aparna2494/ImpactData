package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.joda.time.DateTime;

import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.AutoComplete;
import com.cisco.bccsp.model.UserDetails;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class UserDetailsDAO {
	private static Logger logger = Logger.getLogger(UserDetailsDAO.class.getName());
	public static UserDetails getUserDetails(String userId) {
		
		ArrayList<UserDetails> userdetailList = new ArrayList<UserDetails>();
		UserDetails userdetail = null;
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		try {
			col = DBUtilMongo.getCollectionByName("incident_details");

			BasicDBObject doc1=null;
			BasicDBObject doc2=null;
			BasicDBObject doc3=null;
			BasicDBObject doc4=null;
			
			BasicDBList dblist=new BasicDBList();
			dblist.add("Pending");
			dblist.add("Assigned");
			dblist.add("In Progress");
			
			
			doc1=new BasicDBObject("customer",userId).append("status",new BasicDBObject("$in",dblist));
			doc2=new BasicDBObject("customer",userId);
			doc3=new BasicDBObject("customer",userId).append("status",new BasicDBObject("$in",dblist)).append("cet_value", 1);
			doc4=new BasicDBObject("customer",userId).append("cet_value", 1);
			long open_count=col.count(doc1);
			userdetail=new UserDetails();
			
			userdetail.setUserOpenCasesCount(open_count);
			long total_count=col.count(doc2);
			
			userdetail.setUserTotalCasesCount(total_count);
			long open_escalation_count=col.count(doc3);
			userdetail.setUserOpenEscalationsCount(open_escalation_count);
			
			long total_escalation_count=col.count(doc4);
			
			userdetail.setUserTotalEscalationsCount(total_escalation_count);
			FindIterable finditr=col.find(doc2);
			MongoCursor cursor=finditr.iterator();
			
			if(cursor.hasNext())
			{
				
				Document doc_obj=(Document)cursor.next();
				
				if(doc_obj.get("region_fullname")!=null)
				{
					userdetail.setUserLocation(doc_obj.get("region_fullname").toString());
				}
				userdetail.setUserFullName(doc_obj.get("full_name").toString());
				userdetail.setUserJobTitle(doc_obj.get("job_title").toString());
			}
			
			
			
			  


		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return userdetail;
	}
	
}
