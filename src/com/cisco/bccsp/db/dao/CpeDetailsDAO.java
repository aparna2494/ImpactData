package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilMongo;
import com.cisco.bccsp.model.AutoComplete;
import com.cisco.bccsp.model.CpeDetails;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CpeDetailsDAO {
	private static Logger logger = Logger.getLogger(CpeDetailsDAO.class.getName());
	public static List<CpeDetails> getCpeDetails(String entity,String category) {
		logger.info(entity);
		logger.info(category);
		ArrayList<CpeDetails> cpedetailList = new ArrayList<CpeDetails>();
		CpeDetails cpedetail = null;
		MongoCollection col = null;
		Set<String> set = new HashSet<String>();
		try {
			col = DBUtilMongo.getCollection();

			BasicDBObject doc1=null;
			
			if(category.equalsIgnoreCase("incident_number"))
			{
				 doc1=new BasicDBObject("incident_number", entity.toUpperCase());
			}
			
			else if(category.equalsIgnoreCase("customer"))
			{
				doc1=new BasicDBObject("customer", entity.toLowerCase());
			}
			
			
			
			FindIterable cur= col.find(doc1).limit(1);
			MongoCursor c1=cur.iterator();
			
			  while(c1.hasNext())
			  {
				  Document  dbobj=(Document) c1.next();
				  //DBObject dbobj=(DBObject) c1.next();
	        	
				  cpedetail = new CpeDetails();
				  cpedetail.setCountry((String)dbobj.get("region_fullname"));
				  cpedetail.setCustomercec((String)dbobj.get("customer"));
				  cpedetail.setCustomername((String)dbobj.get("full_name"));
				  cpedetail.setDescription((String)dbobj.get("description"));
				  cpedetail.setIncidentNumber((String)dbobj.get("incident_number"));
				  cpedetail.setJob_title((String)dbobj.get("job_title"));
				  
				 
	           
	            
	            
				  cpedetailList.add(cpedetail);
	        	
	        }


		} catch (Exception e) {
			logger.error(e);
		}

		finally {		
			//DBUtilCaseImpact.releaseConnectionResources(conn, rs, stmt);
		}
		
		return cpedetailList;
	}
}
