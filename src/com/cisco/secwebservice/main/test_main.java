package com.cisco.secwebservice.main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.cisco.bccsp.db.dao.FlowDetailsDAO;
import com.cisco.bccsp.db.util.BCCException;
import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Path("neo4jService")
public class test_main {

	 @Context HttpServletRequest servletRequest;  
 	
 	JSONObject track_obj =new JSONObject();
 	
 	MongoCollection coll_track=null;
 	MongoCollection coll_neo=null;
 	
 	private static Logger logger = Logger.getLogger(test_main.class.getName());
    public test_main() {
    	
    	
    }
    

	/**
	 * Get a list of nodes, edges array
	 * @param 
	 * @return String
	 * @throws BCCException 
	 */
    private String incrementString(String id)
    {
    	
		int id_int=Integer.parseInt(id);
		id_int++;
		String new_id=new Integer(id_int).toString();
		String zeros="";
		for (int i=new_id.length();i<id.length();i++)
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
		FindIterable find_itr=coll_neo.find().sort(new BasicDBObject().append("track_id", -1));
		
		MongoCursor cur=find_itr.iterator();
		long track_id=0;
		if(cur.hasNext())
		{
			Document doc=(Document)cur.next();
			track_id=(Long) doc.get("track_id");
			
		}
		
		return track_id;
		
	}
	private Document buildNodeObject(String node_id,String code,String name,Long track_id,String track_name)
	{
		
		
		Document node_obj = new Document();
		node_obj.put("group", "nodes");
		node_obj.put("grabbable", true);
		node_obj.put("selected", true);
		node_obj.put("classes", null);
		node_obj.put("node_fixed", true);
		JSONObject position =new JSONObject();
		position.put("x", null);
		position.put("y", null);
		node_obj.put("locked", false);
		node_obj.put("selectable", true);
		node_obj.put("removed", null);
		JSONObject data_obj=new JSONObject();
		
		data_obj.put("weight", new Long(1));
		data_obj.put("kpi_data", new JSONArray());
		data_obj.put("track_id", track_id);
		data_obj.put("track_name", track_name);
		data_obj.put("component_code", code);
		data_obj.put("component_name", name);
		data_obj.put("id", node_id);
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
		data.put("to",to_code_id);
		edge_obj.put("data", data);
		return edge_obj;
	}
	private String getGenericDataFlow(String track_name,Long track_id,String node_id){
		
		String response_str =null;
		
		String neo4j_reqStr = "{\"statements\":[{\"statement\":\"MATCH (from:PROCESS_FLOW_STATE) where from.track='"+track_name+"' OPTIONAL MATCH (from:PROCESS_FLOW_STATE)-[r]->(to:PROCESS_FLOW_STATE) where to.track='"+track_name+"' return from.code as `from_code`,from.name  as `from_name`,to.code as `to_code`,to.name as `to_name`,collect(Type(r)) AS `relationships`;\",\"resultDataContents\":[\"row\",\"graph\"],\"includeStats\":true}]}";
		//String neo4j_reqStr = "";
		List<JSONObject> nodesList = new ArrayList<JSONObject>();
		List<JSONObject> edgesList = new ArrayList<JSONObject>();
		List<String> processedNodesList = new ArrayList<String>();
		List<String> processedEdgesList = new ArrayList<String>();
		Map<String,String> code_id_mapping=new HashMap<String, String>();
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
				logger.info(from_code);
				logger.info(from_name);
				logger.info(to_code);
				logger.info(to_name);
				logger.info(relationships);
				if(from_code!=null && !(processedNodesList.contains(from_code))){
					node_id=incrementString(node_id);
					code_id_mapping.put(from_code, node_id);
					processedNodesList.add(from_code);
					//nodesList.add(buildNodeObject(node_id,from_code,from_name,track_id,track_name));
					coll_neo.updateOne(new BasicDBObject(),new BasicDBObject("$addToSet",new BasicDBObject("nodes",buildNodeObject(node_id,from_code,from_name,track_id,track_name))));
					//coll_neo.insertOne(buildNodeObject(node_id,from_code,from_name,track_id,track_name));
					
				}
				if(to_code!=null && !(processedNodesList.contains(to_code))){
					node_id=incrementString(node_id);
					code_id_mapping.put(to_code, node_id);
					processedNodesList.add(to_code);
					//nodesList.add(buildNodeObject(node_id,to_code,to_name,track_id,track_name));
					coll_neo.updateOne(new BasicDBObject(),new BasicDBObject("$addToSet",new BasicDBObject("nodes",buildNodeObject(node_id,to_code,to_name,track_id,track_name))));
					//coll_neo.insertOne(buildNodeObject(node_id,to_code,to_name,track_id,track_name));
					
				}
				if(from_code!=null && to_code!=null && !(processedEdgesList.contains(code_id_mapping.get(from_code)+"R"+code_id_mapping.get(to_code))))
						{
							processedEdgesList.add(code_id_mapping.get(from_code)+"R"+code_id_mapping.get(to_code));
							//edgesList.add(buildEdgeObject(code_id_mapping.get(from_code),code_id_mapping.get(to_code)));
							coll_neo.updateOne(new BasicDBObject(),new BasicDBObject("$addToSet",new BasicDBObject("edges",buildEdgeObject(code_id_mapping.get(from_code),code_id_mapping.get(to_code)))));
						}
				}
			logger.info("########################################################");
			logger.info(nodesList);
			
			
				
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
			
			System.out.println("Neo4j Response txt"+response.getBody());

			//String result = getDataFlow(response.getBody());
			String result = response.getBody();

			System.out.println(result);
			return result;
			}
		catch (Exception e) {
			returnObj.put("response", e.getStackTrace());
			return returnObj.toString();
		}
		
	}
	
	
	@GET
	@Path("/getbpflow")
	@Produces(MediaType.APPLICATION_JSON)
	public String getServiceGenericDataFlow(){
		
		coll_track = DBUtilCiscoOpsMongo.getCollectionByName("track_mappings_neo");
    	
    	coll_neo = DBUtilCiscoOpsMongo.getCollectionByName("neo_bp_flow_data_new");
    	
    	FindIterable find_itr=coll_track.find(new BasicDBObject().append("track_name", "HR IT"));
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
    	}
    	
    	logger.info(getGenericDataFlow("HR IT",track_id,getMaxNodeId()));
		//System.out.println(getGenericDataFlow("HR IT",new Long(0)));
		return "success";

	

}
}
