package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.Edge;
import com.cisco.bccsp.model.KPIModel;
import com.cisco.bccsp.model.Node;
import com.cisco.bccsp.model.NodeModel;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class NodeDetailsDAO {
	private static Logger logger = Logger.getLogger(NodeDetailsDAO.class.getName());
	static Map<String, String> successmap= new HashMap<String, String>();
	public static String fetchmaxID(){
		String result= null;
		MongoCollection col = null;
		col = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data_stage");
		
		 DBObject unwind = new BasicDBObject("$unwind", "$nodes");
		 DBObject sort = new BasicDBObject("$sort", new BasicDBObject("nodes.data.id",-1));
		 DBObject limit = new BasicDBObject("$limit",1);
		List<DBObject> pipeline = Arrays.asList(unwind,sort,limit);
		//pipeline.add(sort);
		//pipeline.add(limit);
		 AggregateIterable<Document> output = col.aggregate(pipeline);
		 if(output.iterator().hasNext()){
			 Document node=   (Document) output.iterator().next().get("nodes");
			/* if(node.iterator().hasNext()){
			 logger.info(node.iterator().next().get("data"));
			 }*/
			Document data = (Document) node.get("data");
			 return (String) data.get("id");
			//return result;
		 }
		 /*output.forEach(new Block<DBObject>() {
			    @Override
			    public void apply(final DBObject document) {
			        logger.info(document);
			    }
			});
		*/
	       
		return result;
	}
	public static Map<String,String> savenodedetails(Node node) {
		String result=null;
		MongoCollection col = null;
		MongoCollection col1 = null;
		MongoCollection col2 = null;
		MongoCollection col3 = null;
		BasicDBObject doc2=null;
		BasicDBObject doc3=null;
		long track_id = 0;
		long kpi_id = 0;
		try {

	
	List<Document> KPILst;
	
	
	List<Map> KPIMapList = new ArrayList<Map>();
	
		col = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data");
		col1 = DBUtilCiscoOpsMongo.getCollectionByName("track_mappings");
		col2=DBUtilCiscoOpsMongo.getCollectionByName("KPI_mappings");
		col3=DBUtilCiscoOpsMongo.getCollectionByName("job_query_details");
		List<KPIModel> KPIList= node.getData().getKpi_data();
		Iterator<KPIModel> KPIIterator = KPIList.iterator();
		while (KPIIterator.hasNext()) {
			
			KPIModel KPInew= KPIIterator.next();
			Map<String,Object> kpiMap= new HashMap<String,Object>();
			Map<String,Object> thresholdMap= new HashMap<String,Object>();
			Object kpiid = 0;
			kpiMap.put("kpi_name", KPInew.getKpi_name());
			
			doc2=new BasicDBObject("kpi_name", KPInew.getKpi_name());
			FindIterable finditr=col2.find(doc2);
			MongoCursor cursor=finditr.iterator();
			if(cursor.hasNext())
			{
				Document  dbobj=(Document)cursor.next();
				kpiMap.put("kpi_id",dbobj.get("kpi_id") );
				kpiid= dbobj.get("kpi_id");
			}
			
				
			else{
				FindIterable itr2 =  col2.find().sort(new BasicDBObject("kpi_id",-1)).limit(1);
				MongoCursor cursor2=itr2.iterator();
				if(cursor2.hasNext())
				{
					Document  dbobkpi=(Document)cursor2.next();
					kpi_id= (Long) dbobkpi.get("kpi_id")+1 ;
					kpiid= (Long)dbobkpi.get("kpi_id")+1;
					
				}
				
					Document kpiinfo= new Document("kpi_id",kpi_id).append("kpi_name",KPInew.getKpi_name());//change
					col2.insertOne(kpiinfo);
					kpiMap.put("kpi_id",kpi_id);//change
					kpiid = kpi_id;
				
			}
			Document kpiMapForQuery= new Document("database", KPInew.getDatabase())
			.append("kpi_id", kpiid)
			.append("kpi_name", KPInew.getKpi_name())
			.append("query_string", KPInew.getQuery())
			.append("id",node.getData().getId());
			col3.insertOne(kpiMapForQuery);
			kpiMap.put("kpi_status","Y");
			kpiMap.put("skms_id", Long.parseLong(KPInew.getSkms_id()));
			thresholdMap.put("Critical", KPInew.getThreshold().getCritical());
			thresholdMap.put("High",KPInew.getThreshold().getHigh());
			thresholdMap.put("Medium",KPInew.getThreshold().getMedium());
			kpiMap.put("threshold",thresholdMap);
			
			KPIMapList.add(kpiMap);
			
			}
		
		doc3=new BasicDBObject("track_name", node.getData().getTrack_name());
		FindIterable finditr=col1.find(doc3);
		MongoCursor cursor=finditr.iterator();
		if(cursor.hasNext())
		{
			Document  dbobj=(Document)cursor.next();
			track_id= (Long) dbobj.get("track_id") ;
		
		}
		else{
			FindIterable itr1 =  col1.find().sort(new BasicDBObject("track_id",-1)).limit(1);
			MongoCursor cursor1=itr1.iterator();
			if(cursor1.hasNext())
			{
				Document  dbob=(Document)cursor1.next();
				track_id= (Long) dbob.get("track_id") +1;
				
			
			}
			Document trackData= new Document("track_id",track_id).append("track_name",node.getData().getTrack_name());//change
			col1.insertOne(trackData);
			
		}
		
		Document Data = new Document("id",node.getData().getId()).append("component_name",node.getData().getComponent_name()).append("weight",node.getData().getWeight())
		.append("track_name",node.getData().getTrack_name())
		.append("track_id",track_id)
		.append("kpi_data", KPIMapList);
		Document pos= new Document("x",node.getPosition().getX()).append("y", node.getPosition().getY());
		Document NodeData= new Document("data",Data).append("position",pos);
		
		Document query = new Document("_id", new Document("$exists", true));
        Document update = new Document();
        update.put("$push", new BasicDBObject( "nodes", NodeData ) );
         
        col.updateMany(query, update);
		//update.put( "$push", new BasicDBObject( "addresses", addressSpec ) );
	//col.insertOne(NodeData);	
		
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		successmap.put("result","success");
		return successmap;
	}
	public static Map<String, String> saveedgedetails(Edge edge) {
		MongoCollection col = null;
		col = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data_stage");
		BasicDBObject doc= null;
		doc=new BasicDBObject("id", edge.getId())
		.append("source",edge.getSource())
		.append("target",edge.getTarget());
		//Document query = new Document("_id", 100);
		Document query = new Document("_id", new Document("$exists", true));
        Document update = new Document();
        Document doc2=new Document("data",doc);
        update.put("$push", new BasicDBObject( "edges", doc2 ) );
         
        col.updateMany(query, update);

		
		return null;
	}
	public static Map<String, String> savekpidetails(NodeModel node) {
		MongoCollection col = null;
		MongoCollection col1 = null;
		MongoCollection col2 = null;
		MongoCollection col3 = null;
		col = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data_stage");
		col1 = DBUtilCiscoOpsMongo.getCollectionByName("track_mappings");
		col2=DBUtilCiscoOpsMongo.getCollectionByName("KPI_mappings");
		col3=DBUtilCiscoOpsMongo.getCollectionByName("job_query_details");
		BasicDBObject doc2= null;
		long track_id = 0;
		long kpi_id = 0;
		try {
	List<Document> KPILst;
	
	List<KPIModel> KPIList= node.getKpi_data();
		Iterator<KPIModel> KPIIterator = KPIList.iterator();
		while (KPIIterator.hasNext()) {
			
			KPIModel KPInew= KPIIterator.next();
			Map<String,Object> thresholdMap= new HashMap<String,Object>();
			Object kpiid = 0;
			doc2=new BasicDBObject("kpi_name", KPInew.getKpi_name());
			FindIterable finditr=col2.find(doc2);
			MongoCursor cursor=finditr.iterator();
			if(cursor.hasNext())
			{
				Document  dbobj=(Document)cursor.next();
				kpiid= dbobj.get("kpi_id");
			}
			
				
			else{
				FindIterable itr2 =  col2.find().sort(new BasicDBObject("kpi_id",-1)).limit(1);
				MongoCursor cursor2=itr2.iterator();
				if(cursor2.hasNext())
				{
					Document  dbobkpi=(Document)cursor2.next();
					kpi_id= (Long) dbobkpi.get("kpi_id")+1 ;
					kpiid= (Long)dbobkpi.get("kpi_id")+1;
					
				}
				
					Document kpiinfo= new Document("kpi_id",kpi_id).append("kpi_name",KPInew.getKpi_name());
					col2.insertOne(kpiinfo);
					kpiid = kpi_id;
				
			}
			logger.info(KPInew.getNextRunTime());
			logger.info(new Date(KPInew.getNextRunTime()));
			Document kpiMapForQuery= new Document("database", KPInew.getDatabase())
			.append("kpi_id", kpiid)
			.append("kpi_name", KPInew.getKpi_name())
			.append("query_string", KPInew.getQuery())
			.append("id",node.getId())
			.append("next_run_time",new Date(KPInew.getNextRunTime()))
			.append("frequency",KPInew.getFrequency())
			.append("status", "stopped");
			col3.insertOne(kpiMapForQuery);
			
			
			thresholdMap.put("Critical",KPInew.getThreshold().getCritical());
			thresholdMap.put("High",KPInew.getThreshold().getHigh());
			thresholdMap.put("Medium", KPInew.getThreshold().getMedium());
			
			Document kpiDocument = new Document("kpi_id",kpiid)
			.append("kpi_name",  KPInew.getKpi_name())
			.append("kpi_status", "Y")
			.append("skms_id", Long.parseLong(KPInew.getSkms_id()))
			.append("threshold",thresholdMap);
			Document query = new Document("nodes.data.id",node.getId());
	        Document update = new Document();
	        update.put("$push", new BasicDBObject( "nodes.$.data.kpi_data", kpiDocument ) );
	        col.updateMany(query, update);
			}
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		successmap.put("result","success");
		return successmap;
	}
	public static String getSKMSid(String kpi_id, String flowComponentid){
		String result= null;
		long kpi_ids= Long.parseLong(kpi_id);
		MongoCollection col = null;
		col = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data");

		 DBObject unwind = new BasicDBObject("$unwind", "$nodes");
		 DBObject match = new BasicDBObject("nodes.data.id",flowComponentid);
		 DBObject matchDoc = new BasicDBObject("$match",match);
		 DBObject unwinds = new BasicDBObject("$unwind", "$nodes.data.kpi_data");
		 DBObject matches = new BasicDBObject("nodes.data.kpi_data.kpi_id",kpi_ids);
		 DBObject matchDocs = new BasicDBObject("$match",matches);
		
		List<DBObject> pipeline = Arrays.asList(unwind,matchDoc,unwinds,matchDocs);
		//List<DBObject> pipeline = Arrays.asList(unwind,matchDoc);
		//pipeline.add(sort);
		//pipeline.add(limit);
		 AggregateIterable<Document> output = col.aggregate(pipeline);
		 if(output.iterator().hasNext()){
			 Document node=   (Document) output.iterator().next().get("nodes");
			/* if(node.iterator().hasNext()){
			 logger.info(node.iterator().next().get("data"));
			 }*/
			Document data = (Document) node.get("data");
			Document kpidata = (Document) data.get("kpi_data");
			result = kpidata.get("skms_id").toString();
		 }

		return result;
	}
	public static Map<String, String> updatenodedetails(Node node) {
		// TODO Auto-generated method stub
		MongoCollection col = null;
		try
		{
			col = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_data_test");
			FindIterable find_itr=col.find();
			MongoCursor cursor=find_itr.iterator();
			
			while(cursor.hasNext())
			{
				
				Document node_cur=(Document)cursor.next();
				List node_lst= (List)node_cur.get("nodes");
				Iterator nodes_itr=node_lst.iterator();
				List new_node_lst=new ArrayList();
				
						Document update_document=new Document();
						while(nodes_itr.hasNext())
						{
							Document node_temp=(Document)nodes_itr.next();
							logger.info(((Document)node_temp.get("data")).get("id"));
							if(((Document)node_temp.get("data")).get("id").toString().equals("0007"))
							{
								
								
								new_node_lst.add(node);
								logger.info(new_node_lst);
								
							}
							else
							{
								new_node_lst.add(node_temp);
							}
							
						}
						//logger.info(new_node_lst);
				
			}
			
			
		}
		catch(Exception e)
		{
			logger.info(e);
		}
		
		return null;
	}
}
