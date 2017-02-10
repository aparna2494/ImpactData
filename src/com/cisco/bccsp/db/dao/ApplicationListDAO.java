package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.joda.time.DateTime;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.ApplicationList;
import com.cisco.bccsp.model.User;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class ApplicationListDAO {
	private static Logger logger = Logger
			.getLogger(ApplicationListDAO.class.getName());

	public static List<ApplicationList> getapplicationlist() {

		
		MongoCollection col = null;
		List<ApplicationList> applist=new ArrayList<ApplicationList>();
		try {
			col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_application_list");
			logger.info(col.count());
			
			FindIterable findItr = col.find();
			MongoCursor cursor = findItr.iterator();
			while(cursor.hasNext()) {
				logger.info("cursor is not empty");
				Document document =  (Document) cursor.next();
				ApplicationList app = new ApplicationList();
				app.setId(document.get("id").toString());
				app.setLabel(document.get("application_name").toString());
				applist.add(app);
			}
			
		} catch (Exception e) {
			logger.error("Not able to query Resource", e);
		}
		
		return applist;
	}
}
