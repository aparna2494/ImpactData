package com.cisco.bccsp.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.Neo4jSNodeDetailModelv2;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Neo4jDAO {
	List<Document> nodesList = new ArrayList<Document>();
	List<Document> edgesList = new ArrayList<Document>();
	List<String> processedNodesList = new ArrayList<String>();
	List<String> processedEdgesList = new ArrayList<String>();
	Map<String,String> code_id_mapping=new HashMap<String, String>();
	String parent=null;
	 private String incrementString(String id)
	    {
	    	
			int id_int=Integer.parseInt(id);
			id_int++;
			String new_id=new Integer(id_int).toString();
			String zeros="";
			for (int i=new_id.length();i<4;i++)
			{
				zeros+="0";
			}
			new_id=zeros.concat(new_id);
			return new_id;
	    }
		private String getMaxNodeId()
		{
			BasicDBObject p1=new BasicDBObject();
			p1.append("$unwind", "$nodes");
			BasicDBObject p2=new BasicDBObject();
			p2.append("$project", new BasicDBObject("nodes",1));
			BasicDBObject p3=new BasicDBObject();
			p3.append("$sort", new BasicDBObject("nodes.data.id",-1));
			BasicDBObject p4=new BasicDBObject();
			p4.append("$limit", 1);
			List<BasicDBObject> pipeline=new ArrayList<BasicDBObject>();
			pipeline.add(p1);
			pipeline.add(p2);
			pipeline.add(p3);
			pipeline.add(p4);
			AggregateIterable agg_itr=coll_neo.aggregate(pipeline);
			MongoCursor cur=agg_itr.iterator();
			String id="";
			if(cur.hasNext())
			{
				
				Document dbobj=(Document) cur.next();
				Document nodes=(Document) dbobj.get("nodes");
				Document data=(Document) nodes.get("data");
				id=(String) data.get("id");
				
				
			}
			
			return id;
			
		}
		
		private Long getMaxTrackId()
		{
			BasicDBObject p1=new BasicDBObject();
			p1.append("$unwind", "$nodes");
			BasicDBObject p2=new BasicDBObject();
			p2.append("$project", new BasicDBObject("nodes",1));
			BasicDBObject p3=new BasicDBObject();
			p3.append("$sort", new BasicDBObject("nodes.data.id",-1));
			BasicDBObject p4=new BasicDBObject();
			p4.append("$limit", 1);
			List<BasicDBObject> pipeline=new ArrayList<BasicDBObject>();
			pipeline.add(p1);
			pipeline.add(p2);
			pipeline.add(p3);
			pipeline.add(p4);
			FindIterable find_itr=coll_track.find().sort(new BasicDBObject().append("track_id", -1));
			
			MongoCursor cur=find_itr.iterator();
			long track_id=0;
			if(cur.hasNext())
			{
				Document doc=(Document)cur.next();
				logger.info(doc);
				track_id=(Long) doc.get("track_id");
				
			}
			
			return track_id;
			
		}
		private Document buildNodeObject(int node_counter,String node_id,String code,String name,Long track_id,String track_name,String parent,String masterCode)
		{
			
			
			Document node_obj = new Document();
			node_obj.put("group", "nodes");
			node_obj.put("grabbable", true);
			node_obj.put("selected", false);
			
			node_obj.put("node_fixed", true);
			JSONObject position =new JSONObject();
			position.put("x", 48+50*node_counter);
			position.put("y", 870);
			
			node_obj.put("locked", false);
			node_obj.put("selectable", true);
			node_obj.put("removed", null);
			JSONObject data_obj=new JSONObject();
			if (parent!=null)
			{
				data_obj.put("parent",parent);
				
			}
			else
			{
				
				data_obj.put("parentNode","yes");
			}
			
				node_obj.put("position", position);
			
			data_obj.put("weight", new Long(1));
			data_obj.put("kpi_data", new JSONArray());
			data_obj.put("track_id", track_id);
			data_obj.put("track_name", track_name);
			data_obj.put("component_code", code);
			data_obj.put("component_name", name);
			data_obj.put("id", node_id);
			data_obj.put("masterid", masterCode);
			data_obj.put("neo_node_on_ui","yes");
			node_obj.put("data", data_obj);
			
			return node_obj;
			
		}
		private Document buildEdgeObject(String from_code_id,String to_code_id)
		{
			Document edge_obj = new Document();
			Document data = new Document();
			data.put("edge_fixed",true);
			data.put("id",from_code_id+"R"+to_code_id);
			data.put("source",from_code_id);
			data.put("target",to_code_id);
			edge_obj.put("data", data);
			return edge_obj;
		}
		
		
		private String getGenericDataFlow(String track_name,Long track_id,String node_id,int node_counter){
			
			String response_str =null;
			String neo4j_reqStr="";
			
			neo4j_reqStr="{\"statements\":[{\"statement\":\"MATCH (from:PROCESS_FLOW_STATE) where from.track='"+track_name+"' OPTIONAL MATCH (from:PROCESS_FLOW_STATE)-[r]->(to:PROCESS_FLOW_STATE) where to.track='"+track_name+"' return from.code as `from_code`,from.name  as `from_name`,to.code as `to_code`,to.name as `to_name`,collect(Type(r)) AS `relationships`,from.sub_process_flow,to.sub_process_flow,from.parent_node,to.parent_node;\",\"resultDataContents\":[\"row\",\"graph\"],\"includeStats\":true}]}";
			
			
			//String neo4j_reqStr = "";
			
			
			
			response_str = callneo4jService(neo4j_reqStr);
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			JSONArray results_array=new JSONArray();
		
			
			
	 		JSONParser parser = new JSONParser();
	 		try {
				obj= (JSONObject)parser.parse(response_str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		
			results_array=(JSONArray) obj.get("results");
			Iterator result_array_itr=results_array.iterator();
			if(result_array_itr.hasNext())
			{
				JSONObject res =(JSONObject)result_array_itr.next();
				JSONArray data_array=new JSONArray();
				data_array=(JSONArray) res.get("data");
				
				Iterator data_array_itr=data_array.iterator();
				if(data_array_itr.hasNext())
				{
					nodesList.add(buildNodeObject(node_counter,parent,"","",track_id,track_name,null,"null"));
				}
				while(data_array_itr.hasNext())
				{				
					JSONObject data =(JSONObject)data_array_itr.next();
					JSONArray row=new JSONArray();
					row=(JSONArray) data.get("row");
					
					String from_code=(String) row.get(0);
					String from_name=(String) row.get(1);
					String to_code=(String) row.get(2);
					String to_name=(String) row.get(3);
					List<String> relationships=(List<String>) row.get(4);
					String from_sub_process_flow=(String) row.get(5);
					String to_sub_process_flow=(String) row.get(6);
					String from_parent_node=(String) row.get(7);
					String to_parent_node=(String) row.get(8);
					String masterCode="null";
					if(from_code!=null && !(processedNodesList.contains(from_code))){
						node_id=incrementString(node_id);
						
						node_counter++;
						code_id_mapping.put(from_code, node_id);
						processedNodesList.add(from_code);
						
						if(from_parent_node!=null)
							masterCode=from_parent_node;
						nodesList.add(buildNodeObject(node_counter,node_id,from_code,from_name,track_id,track_name,parent,masterCode));
						
						//coll_neo.updateOne(new BasicDBObject(),new BasicDBObject("$addToSet",new BasicDBObject("nodes",buildNodeObject(node_id,from_code,from_name,track_id,track_name))));
						//coll_neo.insertOne(buildNodeObject(node_id,from_code,from_name,track_id,track_name));
						
					}
					if(to_code!=null && !(processedNodesList.contains(to_code))){
						node_id=incrementString(node_id);
						node_counter++;
						code_id_mapping.put(to_code, node_id);
						processedNodesList.add(to_code);
						if(to_parent_node!=null)
							masterCode=to_parent_node;
						nodesList.add(buildNodeObject(node_counter,node_id,to_code,to_name,track_id,track_name,parent,masterCode));
						
						//coll_neo.updateOne(new BasicDBObject(),new BasicDBObject("$addToSet",new BasicDBObject("nodes",buildNodeObject(node_id,to_code,to_name,track_id,track_name))));
						//coll_neo.insertOne(buildNodeObject(node_id,to_code,to_name,track_id,track_name));
						
					}
					
					if(from_code!=null && to_code!=null && code_id_mapping.get(from_code)!=null &&code_id_mapping.get(to_code)!=null  && !(processedEdgesList.contains(code_id_mapping.get(from_code)+"R"+code_id_mapping.get(to_code))))
							{
								processedEdgesList.add(code_id_mapping.get(from_code)+"R"+code_id_mapping.get(to_code));
								edgesList.add(buildEdgeObject(code_id_mapping.get(from_code),code_id_mapping.get(to_code)));
								//coll_neo.updateOne(new BasicDBObject(),new BasicDBObject("$addToSet",new BasicDBObject("edges",buildEdgeObject(code_id_mapping.get(from_code),code_id_mapping.get(to_code)))));
							}
					}
				
				
				
					
				}
				
				
				JSONObject final_map = new JSONObject();
				final_map.put("nodes", nodesList);
				final_map.put("edges", edgesList);
				
				response_str = final_map.toString();
				return response_str;
			}
			

		private String callneo4jService(String neo4j_request){
			JSONObject returnObj = new JSONObject();
			try {
				String jsonReqString = "";
				
				
				jsonReqString = neo4j_request;
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Basic bmVvNGo6YWRtaW4=");
				headers.add("Content-Type", "application/json");
				HttpEntity<String> request = new HttpEntity<String>(jsonReqString, headers);
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = restTemplate.exchange(
				        "http://itds-script-02:7474/db/data/transaction/commit", HttpMethod.POST, request, String.class);
				
				

				//String result = getDataFlow(response.getBody());
				String result = response.getBody();

				return result;
				}
			catch (Exception e) {
				returnObj.put("response", e.getStackTrace());
				return returnObj.toString();
			}
			
		}
		
	MongoCollection coll_track=null;
 	MongoCollection coll_neo=null;
 	
 	private static Logger logger = Logger.getLogger(Neo4jDAO.class.getName());
	public  String getFlow(String track_name,String counter) {
		
		coll_track = DBUtilCiscoOpsMongo.getCollectionByName("track_mappings");
    	
    	coll_neo = DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_neo_data_stage");
    	
    	
    	
    	long trackRecCount=coll_neo.count(new BasicDBObject().append("nodes.data.track_name", track_name));
    	
    	if(trackRecCount!=0)
    	{
    		JSONObject obj = new JSONObject();
    		obj.put("error","Track Already Configured.Please Contact Dev Team for making Changes");
    		return obj.toString();
    	}
    	
    	FindIterable find_itr=coll_track.find(new BasicDBObject().append("track_name", track_name));
    	MongoCursor cur=find_itr.iterator();
    	Long track_id=(long) 0;
    	if(cur.hasNext())
    	{
    		Document doc=(Document) cur.next();
    		track_id=(Long) doc.get("track_id");
    		
    	}
    	else
    	{
    		track_id=getMaxTrackId()+1;
    		logger.info("instering track with id "+track_id+" and track_name "+track_name);
    		List<String> tracklst=new ArrayList<String>();
    		tracklst.add(track_name);
    		coll_track.insertOne(new Document().append("track_name", tracklst).append("track_id",track_id).append("spark_room_name", "")
    				.append("spark_room_id", "").append("active_flag", "N").append("protected", "N"));
    	}
    	
    	
		String maxnodeid_incoll=getMaxNodeId();
		Integer nodeIdInt=(Integer.parseInt(getMaxNodeId())+Integer.parseInt(counter));
		
		parent=(new Integer(9999-Integer.parseInt(counter))).toString();
		logger.info("parent= "+parent);
		
		String result=getGenericDataFlow(track_name,track_id,nodeIdInt.toString(),Integer.parseInt(counter));
		
		return result;
	}
	public Neo4jSNodeDetailModelv2 getNodeDetails(String component_code) {
		String response_str =null;
		String neo4j_reqStr="";
		
		neo4j_reqStr="{\"statements\":[{\"statement\":\"MATCH (sd:SERVICE_DOMAIN)-[r7]->(sc:SERVICE_CATEGORY)-[r6]->(s:SERVICE)-[r1]->(so:SERVICE_OFFERING)-[r2]->(n:APPLICATION)-[r3]->(p:PROCESS)<-[sp:STATE_TO_PROCESS]-(ps:PROCESS_FLOW_STATE) where   ps.code='"+component_code+"'   and p.service_offering=so.name OPTIONAL MATCH (p:PROCESS)-[pcp:PROCESS_TO_JOB]->(cp:JOB) OPTIONAL MATCH (s:SERVICE)-[r4:IT_SERVICE_OWNER]->(p1:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[rm_r:RESPONSIBLE_MANAGER]->(rm:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sm_r:SUPPORT_MANAGER]->(sm:PEOPLE) OPTIONAL MATCH (sc:SERVICE_CATEGORY)-[se_r:SERVICE_EXECUTIVE]->(se:PEOPLE) OPTIONAL MATCH (a:APPLICATION)-[sdir_r:SUPPORT_DIRECTOR]->(sdir:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sc_r:SUPPORT_CONTACT]->(scon:PEOPLE)  OPTIONAL MATCH (ps:PROCESS_FLOW_STATE)-[ds_r:PROCESS_FLOW_DEPENDENCY]->(ds:PROCESS_FLOW_STATE) return  ps.name,sd.name AS `Service Domain`,sc.name AS `Service Category`,s.name AS `Service`,so.name as `Service Offering`,n.name as `Application`,collect(distinct {code:cp.code,name:cp.name}) as `Job`,collect (distinct {code:NULL,name:NULL}) as `JVM name`, p1.name as `IT service owner`,rm.name as `Responsible Manager`, sm.name as `Support Manager` ,se.name as `Service Executive`,collect(distinct {cecId:scon.cecId,name:scon.name}) as `Support Contact`,collect(distinct {code:ds.code,name:ds.name,flow:ds.flow}) as `Dependant State`,sdir.name as `Support Director` UNION MATCH (sd:SERVICE_DOMAIN)-[r7]->(sc:SERVICE_CATEGORY)-[r6]->(s:SERVICE)-[r1]->(so:SERVICE_OFFERING)-[r2]->(n:APPLICATION)-[r3]->(jvm:JVM_INSTANCES)<-[sp:PROCESS_FLOW_DEPENDENCY]-(ps:PROCESS_FLOW_STATE) where   ps.code='"+component_code+"'     OPTIONAL MATCH (s:SERVICE)-[r4:IT_SERVICE_OWNER]->(p1:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[rm_r:RESPONSIBLE_MANAGER]->(rm:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sm_r:SUPPORT_MANAGER]->(sm:PEOPLE) OPTIONAL MATCH (sc:SERVICE_CATEGORY)-[se_r:SERVICE_EXECUTIVE]->(se:PEOPLE) OPTIONAL MATCH (a:APPLICATION)-[sdir_r:SUPPORT_DIRECTOR]->(sdir:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sc_r:SUPPORT_CONTACT]->(scon:PEOPLE)  OPTIONAL MATCH (ps:PROCESS_FLOW_STATE)-[ds_r:PROCESS_FLOW_DEPENDENCY]->(ds:PROCESS_FLOW_STATE) return  ps.name,sd.name AS `Service Domain`,sc.name AS `Service Category`,s.name AS `Service`,so.name as `Service Offering`,n.name as `Application`,collect(distinct {code:null,name:null}) as `Job`,collect (distinct {code:jvm.code,name:jvm.name}) as `JVM name`,p1.name as `IT service owner`,rm.name as `Responsible Manager`, sm.name as `Support Manager` ,se.name as `Service Executive`,collect(distinct {cecId:scon.cecId,name:scon.name}) as `Support Contact`,collect(distinct {code:ds.code,name:ds.name,flow:ds.flow}) as `Dependant State`,sdir.name as `Support Director`;\",\"resultDataContents\":[\"row\",\"graph\"],\"includeStats\":true}]}";
		
		logger.info(neo4j_reqStr);
		//String neo4j_reqStr = "";
		
		
		
		response_str = callneo4jService(neo4j_reqStr);
		JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();
		JSONArray results_array=new JSONArray();
		
		Neo4jSNodeDetailModelv2 nodeDetails=new Neo4jSNodeDetailModelv2();
		
		
		List<Map<String,String>> jobList=new ArrayList<Map<String,String>>();
		List<Map<String,String>> jvmList=new ArrayList<Map<String,String>>();
		List<Map<String,String>> supportContactList=new ArrayList<Map<String,String>>();
		List<Map<String,String>> depStateList=new ArrayList<Map<String,String>>();
		JSONParser parser = new JSONParser();
 		try {
			obj= (JSONObject)parser.parse(response_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
		results_array=(JSONArray) obj.get("results");
		Iterator result_array_itr=results_array.iterator();
		if(result_array_itr.hasNext())
		{
			JSONObject res =(JSONObject)result_array_itr.next();
			JSONArray data_array=new JSONArray();
			data_array=(JSONArray) res.get("data");
			
			Iterator data_array_itr=data_array.iterator();
			
			while(data_array_itr.hasNext())
			{				
				JSONObject data =(JSONObject)data_array_itr.next();
				JSONArray row=new JSONArray();
				row=(JSONArray) data.get("row");
				nodeDetails.setPsName(row.get(0).toString());
				
				nodeDetails.setServiceDomain(row.get(1).toString());
				
				nodeDetails.setServiceCategory(row.get(2).toString());
				
				nodeDetails.setService(row.get(3).toString());
				
				nodeDetails.setServiceOffering(row.get(4).toString());
				
				nodeDetails.setApplication(row.get(5).toString());
				
				JSONArray jobArray=new JSONArray();
				jobArray=(JSONArray) row.get(6);
				Iterator itr_jobarray=jobArray.iterator();
				while(itr_jobarray.hasNext())
				{
					Map <String,String> job=new HashMap<String,String>();
					
					JSONObject jobObj=(JSONObject) itr_jobarray.next();
					if(jobObj.get("code")!=null)
					{
						job.put("code",jobObj.get("code").toString());
						job.put("name",jobObj.get("name").toString());
						if(!jobList.contains(job))
						{
							jobList.add(job);	
						}
						
					}
				}
				
				nodeDetails.setJob(jobList);
				
				
				JSONArray jvmArray=new JSONArray();
				jvmArray=(JSONArray) row.get(7);
				
				Iterator itr_jvmarray=jvmArray.iterator();
				while(itr_jvmarray.hasNext())
				{
					
					Map <String,String> jvm=new HashMap<String,String>();
					JSONObject jvmObj=(JSONObject) itr_jvmarray.next();
				
					if(jvmObj.get("code")!=null)
					{
						jvm.put("code",jvmObj.get("code").toString());
						jvm.put("name",jvmObj.get("name").toString());
						if(!jvmList.contains(jvm))
						{
							jvmList.add(jvm);
						}
						
					}
				}
				
				
				nodeDetails.setJvm(jvmList);
				
				
				nodeDetails.setItServiceOwner(row.get(8).toString());
				
				nodeDetails.setResponsibleManager(row.get(9).toString());
				
				nodeDetails.setSupportManager(row.get(10).toString());
				
				nodeDetails.setServiceExecutive(row.get(11).toString());
				
				JSONArray supportContactArray=new JSONArray();
				supportContactArray=(JSONArray) row.get(12);
				Iterator itr_supportContactArray=supportContactArray.iterator();
				while(itr_supportContactArray.hasNext())
				{
					Map <String,String> supportContact=new HashMap<String,String>();
					
					JSONObject supportContactObj=(JSONObject) itr_supportContactArray.next();
					if(supportContactObj.get("cecId")!=null)
					{
						supportContact.put("cecId",supportContactObj.get("cecId").toString());
						supportContact.put("name",supportContactObj.get("name").toString());
						if(!supportContactList.contains(supportContact))
						{
							supportContactList.add(supportContact);	
						}
						
					}
				}
				nodeDetails.setSupportContact(supportContactList);
				
				
				JSONArray depStateArray=new JSONArray();
				depStateArray=(JSONArray) row.get(13);
				Iterator itr_depStateArray=depStateArray.iterator();
				while(itr_depStateArray.hasNext())
				{
					Map <String,String> depState=new HashMap<String,String>();
					
					JSONObject depStateObj=(JSONObject) itr_depStateArray.next();
					if(depStateObj.get("code")!=null)
					{
						
						depState.put("code",depStateObj.get("code").toString());
						depState.put("name",depStateObj.get("name").toString());
						depState.put("flow",depStateObj.get("flow").toString());
						if(!depStateList.contains(depState))
						{
							depStateList.add(depState);
						}
						
					}
				}
				nodeDetails.setDependantState(depStateList);
				
				nodeDetails.setSupportDirector(row.get(14).toString());
				
			}
		}
	
		
		return nodeDetails;
		// TODO Auto-generated method stub
		
	}

}
