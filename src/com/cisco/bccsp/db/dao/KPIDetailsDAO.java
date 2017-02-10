package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.commons.lang3.time.StopWatch;
import java.util.Date;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.ApplicationList;
import com.cisco.bccsp.model.KPIModel;
import com.cisco.bccsp.model.Node;
import com.cisco.bccsp.model.NodeModel;
import com.cisco.bccsp.model.PositionModel;
import com.cisco.bccsp.model.ThresholdModel;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import static com.mongodb.client.model.Projections.*;


public class KPIDetailsDAO {
	private static Logger logger = Logger.getLogger(KPIDetailsDAO.class.getName());
	
public static String getUniqueTracks(String collectionName) {
	
	JSONArray arr = new JSONArray();
	MongoCollection col = null;
	col = DBUtilCiscoOpsMongo.getCollectionByName(collectionName);
	
	 DBObject unwind = new BasicDBObject("$unwind", "$nodes");
	 DBObject group = new BasicDBObject("$group", new BasicDBObject("_id","$nodes.data.track_name"));
	 
	 List<DBObject> pipeline = Arrays.asList(unwind,group);
	 AggregateIterable<Document> output = col.aggregate(pipeline);
	 MongoCursor<Document> iterator = output.iterator();
	 
	 while(iterator.hasNext()) {
		 Document doc = iterator.next();
		 JSONObject obj = new JSONObject();
		 
		 obj.put("track_name", doc.getString("_id"));
		 arr.add(obj);
		 
	 }
	 
	 return arr.toJSONString();
		
	}

public static String getUniqueComponents(String collectionName, String trackName) {
	
	JSONArray arr = new JSONArray();
	MongoCollection col = null;
	col = DBUtilCiscoOpsMongo.getCollectionByName(collectionName);
	
	 DBObject unwind = new BasicDBObject("$unwind", "$nodes");
	 DBObject group = new BasicDBObject("$project", new BasicDBObject("_id","$nodes.data.component_name"));
	 DBObject match =  new BasicDBObject("$match", new BasicDBObject("nodes.data.track_name", trackName)); 
	 List<DBObject> pipeline = Arrays.asList(unwind,match,group);
	 AggregateIterable<Document> output = col.aggregate(pipeline);
	 MongoCursor<Document> iterator = output.iterator();
	 
	 while(iterator.hasNext()) {
		 Document doc = iterator.next();
		 JSONObject obj = new JSONObject();
		 
		 obj.put("component_name", doc.getString("_id"));
		 arr.add(obj);
		 
	 }
	 
	 return arr.toJSONString();
		
}

public static String getUniqueKPI(String collectionName, String trackName, String componentName) {
	
	JSONArray arr = new JSONArray();
	MongoCollection col = null;
	col = DBUtilCiscoOpsMongo.getCollectionByName(collectionName);
	
	 DBObject unwind = new BasicDBObject("$unwind", "$nodes");
	 DBObject group = new BasicDBObject("$project", new BasicDBObject("_id","$nodes.data.kpi_data"));
	 DBObject match =  new BasicDBObject("$match", new BasicDBObject("nodes.data.track_name", trackName).append("nodes.data.component_name",componentName)); 
	 List<DBObject> pipeline = Arrays.asList(unwind,match,group);
	 AggregateIterable<Document> output = col.aggregate(pipeline);
	 MongoCursor<Document> iterator = output.iterator();
	 
	 while(iterator.hasNext()) {
		 Document doc = iterator.next();
		 JSONObject obj = new JSONObject();
		 
		 obj.put("kpi_data", doc.get("_id"));
		 arr.add(obj);
		 
	 }
	 
	 return arr.toJSONString();
		
}

public static String getKPIdetails(String trackName, String collectionName,String nodeid){
	JSONArray arr = new JSONArray();
	MongoCollection col = null;
	
	col = DBUtilCiscoOpsMongo.getCollectionByName(collectionName);
	MongoCollection kpiCollection = DBUtilCiscoOpsMongo.getCollectionByName("job_query_details");
	
	 DBObject unwind = new BasicDBObject("$unwind", "$nodes");
	 DBObject match=null;
	 if(nodeid!=null)
	 {
		 match = new BasicDBObject("$match", new BasicDBObject("nodes.data.track_name", trackName).append("nodes.data.id", nodeid));
	 }
	 else
	 {
		 match = new BasicDBObject("$match", new BasicDBObject("nodes.data.track_name", trackName));
	 }
	 DBObject project = new BasicDBObject("$project", new BasicDBObject("data", "$nodes.data"));
	 
	 List<DBObject> pipeline = Arrays.asList(unwind, match, project);
	 AggregateIterable<Document> output = col.aggregate(pipeline);
	 MongoCursor<Document> iterator = output.iterator();
	 
	 while(iterator.hasNext()) {
		 Document document = (Document) iterator.next().get("data");
		 String id = document.getString("id");
		 List<Document> kpis = (List<Document>) document.get("kpi_data");
		 
		 List<Document> dstKpis = new ArrayList<Document>();
		 for(Document kpi : kpis)
		 {
			 Long kpi_id = kpi.getLong("kpi_id");

			 kpi.append("details", getKPIdetailsQuery(id, kpi_id, kpiCollection));
			 
			 dstKpis.add(kpi);
		 }
		 
		 
		 document.remove("kpi_data");
		 document.append("kpi_data", dstKpis);
		 arr.add(document);

	 }


	 return arr.toJSONString();
	
}


public static Document getKPIdetailsQuery(String id, long kpi_id, MongoCollection col){
	
	Document kpiDetails = (Document)col.find(new Document("id", id).append("kpi_id", kpi_id)).projection(fields(include("query_string", "database"), excludeId())).first();
	
	
	return kpiDetails;
}


	public static Map getNodeandKPIArrayIndexOfMongocol(MongoCollection col){
		BasicDBObject searchQuery = new BasicDBObject();
		MongoCursor cursor = col.find(searchQuery).iterator();	
    	Map<String, Integer> map = new HashMap<String, Integer>();  	
    	Document doc = null;

		while (cursor.hasNext()) {
			int node_counter =0;				
			doc = (Document) cursor.next();
			List<Document> listdocs = (List<Document>)doc.get("nodes");
				  for (  Document docs : listdocs) {
					  int kpi_counter =0;
					    //System.out.println("docs::"+docs.toString());
					    Document data_doc =  (Document) docs.get("data");					    	
					    	//System.out.println("data_doc::"+data_doc.toString());
					    	String track_name = (String)data_doc.get("track_name");
					    	String component_name = (String)data_doc.get("component_name");
					    if(!map.containsKey(track_name+component_name.toString()))
					    	map.put(track_name+component_name.toString(), node_counter);
					    			
					    			List<Document> kpi_doc = (List<Document>)data_doc.get("kpi_data");
					    			//System.out.println("Kpi_data::" + data_doc.get("kpi_data"));
					    for ( Document kpi_data : kpi_doc) {
					    	if(!map.containsKey(track_name+component_name.toString()+kpi_data.get("kpi_name").toString())){
					    		map.put(track_name+component_name+kpi_data.get("kpi_name").toString(), kpi_counter);
					    	}

					    	//System.out.println("kpi_id::" + kpi_data.get("kpi_id"));
						    //System.out.println("kpi_counter::" + kpi_counter);

						    kpi_counter++;
					    }
					    //System.out.println("node_counter:: before" + node_counter);

 				    node_counter++;
				    //System.out.println("node_counter:: after" + node_counter);

					  }
		}
		return map;
	}



public static boolean updateKPIdetails(String collectionName, String track_name, String component_name, String kpi_name, Integer critical, Integer high, Integer medium, String status){

	    MongoCollection col = null;
	  col = DBUtilCiscoOpsMongo.getCollectionByName(collectionName);
	  Map result = getNodeandKPIArrayIndexOfMongocol(col);
		Integer node_index,kpi_index;
		  node_index = (Integer)result.get(track_name+component_name);
		  kpi_index = (Integer)result.get(track_name+component_name+kpi_name);
		  BasicDBObject searchQuery = new BasicDBObject();

		  BasicDBObject updateQuery = new BasicDBObject();
		  
		  BasicDBObject generic = new BasicDBObject("nodes."+node_index+".data.kpi_data."+kpi_index+".threshold.Critical", critical);
		  generic.append("nodes."+node_index+".data.kpi_data."+kpi_index+".threshold.High", high);
		  generic.append("nodes."+node_index+".data.kpi_data."+kpi_index+".threshold.Medium", medium);
		  generic.append("nodes."+node_index+".data.kpi_data."+kpi_index+".kpi_status", status);
		  
		
	  updateQuery.append("$set", generic);
	  col.updateMany(searchQuery,updateQuery);

	  System.out.println("Done");
  	  return true;
}

public static List<Document> KPImapping(){

	final String id = "kpi_id";
	final String name = "kpi_name";
	MongoCollection col = null;
	List<Document> kpiList = new ArrayList<Document>();
	try {
		
		col = DBUtilCiscoOpsMongo.getCollectionByName("KPI_mappings");
		MongoCursor cursor = col.find().iterator();
		while(cursor.hasNext()) {
            Document dbobj =(Document) cursor.next();			
            kpiList.add(new Document(id,dbobj.get(id)).append(name, dbobj.get(name).toString()));
            

		}

	} catch (Exception e) {
		logger.error(e);
	}

	return kpiList;
}
public static Long saveKPI(String kpiname){
	     MongoCollection col = null;
	     col=DBUtilCiscoOpsMongo.getCollectionByName("KPI_mappings");
         long kpi_id = 0;
         long kpiid = 0;
        
    	 FindIterable itr =  col.find().sort(new BasicDBObject("kpi_id",-1)).limit(1);
         MongoCursor cursor=itr.iterator();
	         if(cursor.hasNext())
            	{
	             	Document  dbobkpi=(Document)cursor.next();
	            	kpi_id= (Long) dbobkpi.get("kpi_id")+1 ;
	            	kpiid= (Long)dbobkpi.get("kpi_id")+1;
		
	            }	
		Document kpiinfo= new Document("kpi_id",kpi_id).append("kpi_name", kpiname);
		col.insertOne(kpiinfo);
	
        
		
		return kpi_id;
}

public static Map getComponentId(MongoCollection col){
	BasicDBObject searchQuery = new BasicDBObject();
	MongoCursor cursor = col.find(searchQuery).iterator();	
	Map<String, String> map = new HashMap<String, String>();  	
	Document doc = null;

	while (cursor.hasNext()) {
	
		doc = (Document) cursor.next();
		List<Document> listdocs = (List<Document>)doc.get("nodes");
			  for (  Document docs : listdocs) {
				    //System.out.println("docs::"+docs.toString());
				    Document data_doc =  (Document) docs.get("data");					    	
				    	//System.out.println("data_doc::"+data_doc.toString());
				    	String track_name = (String)data_doc.get("track_name");
				    	String component_name = (String)data_doc.get("component_name");
				    	String component_id = (String)data_doc.get("id");
				    if(!map.containsKey(track_name+component_name.toString()))
				    	map.put(track_name+component_name.toString(), component_id);
				    
				    }
				
	}
	return map;
}


public static boolean createKPI(String collectionName, String track_name, String component_name, String kpi_name, Integer critical, Integer high, Integer medium, String status, String dbname,String query, Integer frequency, boolean jobupdate ){
      
	  Long kpi_id = saveKPI(kpi_name);
      MongoCollection col = null;
      col = DBUtilCiscoOpsMongo.getCollectionByName(collectionName);
      Map result = getNodeandKPIArrayIndexOfMongocol(col);
	  Integer node_index,kpi_index;
	  node_index = (Integer)result.get(track_name+component_name);
	  kpi_index = (Integer)result.get(track_name+component_name+kpi_name);
	  BasicDBObject searchQuery = new BasicDBObject();
	  BasicDBObject updateQuery = new BasicDBObject();
	  boolean status1 = true;
	  
	 	  
	  if (kpi_id!=null&&kpi_index==null){
		  Map<String,Object> thresholdMap= new HashMap<String,Object>();
		  
		    thresholdMap.put("Critical",critical);
			thresholdMap.put("High",high);
			thresholdMap.put("Medium", medium);
			
	  Document generic = new Document("threshold",thresholdMap);
	  generic.append("kpi_name", kpi_name);
	  generic.append("kpi_status", status);
	  generic.append("kpi_id", kpi_id);
	  generic.append("query", query);
	  generic.append("skms_id", 0);
	  generic.append("database", dbname);

        updateQuery.put("$push", new BasicDBObject( "nodes."+node_index+".data.kpi_data", generic ) );
        col.updateMany(searchQuery, updateQuery);
        System.out.println("Done");
       	 if(jobupdate){
          	MongoCollection coljob = null;
        	MongoCollection col1 = null;
            col1 = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data");
            coljob = DBUtilCiscoOpsMongo.getCollectionByName("job_query_details");
            Map result1 = getComponentId(col1);
            String component_id;
            component_id = (String)result1.get(track_name+component_name);
            Document genericData = new Document("kpi_id", kpi_id);
            genericData.append("kpi_name", kpi_name);
            genericData.append("status", "stopped");
            genericData.append("database", dbname);
            genericData.append("frequency", frequency);
            genericData.append("query_string", query);
            genericData.append("kpi_name", kpi_name);
            genericData.append("next_run_time", new Date());
            genericData.append("last_run_time", null);
            genericData.append("id", component_id);
            coljob.insertOne(genericData);
       	 }
	  }
	  else {
		  System.out.println("Error");
		  status1 = false;
		  
	  }
	  
	  return status1;
}

public static String getDB() {

	
	final String key = "DB_NAME";
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	JSONArray arr = new JSONArray();
	try {
		
		col = DBUtilCiscoOpsMongo.getCollectionByName("db_details");
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

public static String getJobdetails() {

	
	
	MongoCollection col = null;
	Set<String> set = new HashSet<String>();
	JSONArray arr = new JSONArray();
	try {
		
		col = DBUtilCiscoOpsMongo.getCollectionByName("job_query_details");
		MongoCursor cursor = col.find().iterator();
		while(cursor.hasNext()) {
			Document document =  (Document) cursor.next();
			JSONObject obj = new JSONObject();
            obj.put("data", document);
			arr.add(obj);

		}

	} catch (Exception e) {
		logger.error(e);
	}

	return arr.toJSONString();

}


public static boolean deleteKPI(){
    
	 //TEST DELETE FUNCTION
	
    MongoCollection col = null;
    col = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data_stage");
    Map result = getNodeandKPIArrayIndexOfMongocol(col);
	  Integer node_index,kpi_index;
	  node_index = (Integer)result.get("GTMS - Channels"+"PMC2.0");
    BasicDBObject updateQuery = new BasicDBObject();
    BasicDBObject searchQuery = new BasicDBObject();
    Map<String,Object> thresholdMap= new HashMap<String,Object>();
		    thresholdMap.put("Critical",11);
			thresholdMap.put("High",6);
			thresholdMap.put("Medium",5);
			
	  Document generic = new Document("threshold",thresholdMap);
	  generic.append("kpi_name", "TEST QUERY");
		
		
   updateQuery.put("$pull", new BasicDBObject( "nodes."+node_index+".data.kpi_data", generic ) );
   col.updateMany(searchQuery, updateQuery);

    System.out.println("Done");
     	  
	 return true;

}

public static boolean deleteJob(){
    
	 //TEST DELETE FUNCTION
	
   MongoCollection col = null;
   col = DBUtilCiscoOpsMongo.getCollectionByName("job_query_details");

  
	  Document generic = new Document("kpi_name", "TEST QUERY");
	  generic.append("kpi_id", 187);

  col.deleteOne(generic);

   System.out.println("Done");
    	  
	 return true;

}

public static boolean updateJob(String kpi_name, String component_name, String track_name, String query){
    
	
	    MongoCollection col = null;
	    MongoCollection col1 = null;
	    col = DBUtilCiscoOpsMongo.getCollectionByName("job_query_details");
	    col1 = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data");
	    Map result = getComponentId(col1);
		String component_id;
		component_id = (String)result.get(track_name+component_name);
	    BasicDBObject updateQuery = new BasicDBObject();

		  Document generic = new Document("kpi_name", kpi_name);
		  generic.append("id", component_id);
			
	   updateQuery.put("$set", new BasicDBObject("query_string", query ) );
	   col.updateOne(generic, updateQuery);

	    System.out.println("Done");
	     	  
		 return true;

}
}
