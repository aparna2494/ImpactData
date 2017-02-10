package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONObject;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.ApplicationList;
import com.cisco.bccsp.model.GroupDetail;
import com.cisco.bccsp.model.groupMemberLDAP;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class GroupDetailsDAO {
	private static Logger logger = Logger.getLogger(GroupDetailsDAO.class.getName());
	
	
	public static List<String> getGroupNames() {
		String result=null;
		MongoCollection col = null;
		List<String> groupList=  new ArrayList<String>();
		try {

	logger.info("inside the fetch method of groupMemebers");
	
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_clients_group ");
		FindIterable finditr=col.find();
		
		MongoCursor cursor=finditr.iterator();
		while(cursor.hasNext())
		{
			
			Document dbobj =(Document) cursor.next();
			
			groupList.add((String)dbobj.get("group_name"));
			
		}
		
		
	
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		return groupList;
	}
	
	
	
	public static String deleteGroupName(String group_name) {
		String result=null;
		MongoCollection col = null;
		
		try {

	logger.info("inside the fetch method of groupMemebers");
	
	
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_clients_group ");
		
		col.deleteOne(new Document("group_name",group_name));
		
		
		result="success";
	
		}
		catch(Exception e)
		{
			logger.info(e);
			result="error";
		}
		return result;
	}
	
	
	
	
	public static List<GroupDetail> getGroupInfo(String WhoIsIt) {
		String result=null;
		MongoCollection col = null;
		List<GroupDetail> groupList=  new ArrayList<GroupDetail>();
		try {

	logger.info("inside the fetch method of groupMemebers");
	
	
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_clients_group ");
		FindIterable finditr=col.find(new Document("group_admin",WhoIsIt));
		
		MongoCursor cursor=finditr.iterator();
		while(cursor.hasNext())
		{
			GroupDetail grpdet=new GroupDetail();
			Document dbobj =(Document) cursor.next();
			BasicDBList temp=new BasicDBList();
			
			grpdet.setGroupName((String)dbobj.get("group_name"));
			
			grpdet.setMemberCount(((ArrayList) dbobj.get("group_members")).size());
			groupList.add(grpdet);
			logger.info(groupList);
			logger.info(grpdet);
		}
		
		
	
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		return groupList;
	}
	public static GroupDetail getGroupDetail(String groupName) {
		String result=null;
		MongoCollection col = null;
		GroupDetail grpdet=new GroupDetail();
		try {

	logger.info("inside the fetch method of groupMemebers");
	
	
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_clients_group ");
		FindIterable finditr=col.find(new Document("group_name",groupName));
		
		MongoCursor cursor=finditr.iterator();
		while(cursor.hasNext())
		{
			
			Document dbobj =(Document) cursor.next();
			BasicDBList temp=new BasicDBList();
			
			grpdet.setGroupName((String)dbobj.get("group_name"));
			grpdet.setApplicationList((List<ApplicationList>) dbobj.get("applications"));
			grpdet.setGroupAdmin((String)dbobj.get("group_admin"));
			grpdet.setGroupID((Double) dbobj.get("group_id"));
			grpdet.setGroupMember((List<groupMemberLDAP>) dbobj.get("group_members"));
			grpdet.setGroupPermission((List<groupMemberLDAP>) dbobj.get("group_permission"));
			grpdet.setMailerAD_name((String) dbobj.get("mailerAD_name"));
			List<String> remedy_names=new ArrayList<String>();
			List remedyObject=(List) dbobj.get("remedy_groups");
			Iterator remedyObject_itr=remedyObject.iterator();
			while(remedyObject_itr.hasNext())
			{
				Document obj=(Document)remedyObject_itr.next();
				
				remedy_names.add(obj.get("AG_name").toString());
			}
			
			grpdet.setRemedy_AG(remedy_names);
			grpdet.setSync_type((String)dbobj.getString("sync_type"));
			grpdet.setMemberCount(((ArrayList) dbobj.get("group_members")).size());
			
		}
		
		logger.info(grpdet);
	
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		return grpdet;
	}
/*	public static GroupDetail getGroupDetailApp(String application) {
		String result=null;
		MongoCollection col = null;
		GroupDetail grpdet=new GroupDetail();
		try {

	logger.info("inside the fetch method of groupMemebers");
	
	
		col = DBUtilCiscoOpsMongo.getCollectionByName("bcc_clients_group ");
		FindIterable finditr=col.find(new Document("application.id",application));
		
		MongoCursor cursor=finditr.iterator();
		while(cursor.hasNext())
		{
			
			Document dbobj =(Document) cursor.next();
			BasicDBList temp=new BasicDBList();
			
			grpdet.setGroupName((String)dbobj.get("group_name"));
			grpdet.setApplicationList((List<ApplicationList>) dbobj.get("applications"));
			grpdet.setGroupAdmin((String)dbobj.get("group_admin"));
			grpdet.setGroupID((Double) dbobj.get("group_id"));
			grpdet.setGroupMember((List<groupMemberLDAP>) dbobj.get("group_members"));
			grpdet.setGroupPermission((List<groupMemberLDAP>) dbobj.get("group_permission"));
			grpdet.setMailerAD_name((String) dbobj.get("mailerAD_name"));
			List<String> remedy_names=new ArrayList<String>();
			List remedyObject=(List) dbobj.get("remedy_groups");
			Iterator remedyObject_itr=remedyObject.iterator();
			while(remedyObject_itr.hasNext())
			{
				Document obj=(Document)remedyObject_itr.next();
				
				remedy_names.add(obj.get("AG_name").toString());
			}
			
			grpdet.setRemedy_AG(remedy_names);
			grpdet.setSync_type((String)dbobj.getString("sync_type"));
			grpdet.setMemberCount(((ArrayList) dbobj.get("group_members")).size());
			
		}
		
		logger.info(grpdet);
	
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		return grpdet;
	}*/
}
