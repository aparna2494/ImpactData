package com.cisco.bccsp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.cisco.bccsp.db.dao.GenericDataFetchDao;
import com.cisco.bccsp.model.*;
import com.cisco.bccsp.db.util.DBUtilMongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.geojson.Position;

@Path("secondlevelnodes")
public class SecondLevelNodesService {
	public static Double randInt(Double mean) {

		double rangeMin = mean-100;
		double rangeMax = mean+100;
		
	    Random rand=new Random();
	    
	    Double randomNum;
	    
	    	
	    	randomNum= rangeMin + (rangeMax - rangeMin)*rand.nextDouble();	
	    
	    

	    return randomNum;
	}
	private static Logger logger = Logger.getLogger(SecondLevelNodesService.class.getName());
	 @GET
	 @Path("/details/{node_id}/{pos_x}/{pos_y}")
	 @Produces(MediaType.APPLICATION_JSON)
	public Flow getCSCCData(@PathParam("node_id") String node_id,@PathParam("pos_x") String pos_x,@PathParam("pos_y") String pos_y)
	{
		 String result ="";
			Flow flow=new Flow();
			List<Node> node_lst=new ArrayList<Node>();
			
			MongoCollection node_pos_col = null;
			node_pos_col= DBUtilMongo.getCollectionByName("node_position");
			List<Double> x_array_in_mem=new ArrayList<Double>();
			List<Double> y_array_in_mem=new ArrayList<Double>();
			/*nodes.push({
	            "locked" : false,
	            "selected" : false,
	            "group" : "nodes",
	            "classes" : "",
	            "node_fixed" : true,
	            "grabbable" : true,
	            "position" : {
	                "y" : -100,
	                "x" : 500
	            },
	            "selectable" : true,
	            "removed" : false,
	            "data" : {
	                "kpi_data" : [ 
	                    
	                ],
	                "weight" : 1,
	                "track_id" : 1,
	                "track_name" : "Service Quotes",
	                "id" : nodeid+"1",
	                "component_name" : "sub1",
	                "parent":nodeid	
	            }
	        })*/
			
			for(int i=0;i<2;i++)
			{
				NodeModel nodemodel=new NodeModel();
				Node node= new Node();
				nodemodel.setParent(node_id);
				PositionModel position=new PositionModel();
				node.setLocked(false);
				node.setSelected(false);
				node.setGroup("nodes");
				node.setClasses("");
				
				node.setGrabbable(true);
				node.setNode_fixed(true);
				node.setLocked(false);
				node.setSelectable(true);
				
				nodemodel.setId(node_id+i);
				node.setData(nodemodel);
				int x_pos=0;
				int y_pos=0;
				while(1==1)
				{
					Double temp_x=randInt(Double.parseDouble((pos_x)));
					Double temp_y=randInt(Double.parseDouble((pos_y)));
					long count=node_pos_col.count(new BasicDBObject("x",temp_x).append("y", temp_y));
					logger.info(temp_x);
					logger.info(temp_y);
					logger.info(count);
					logger.info(x_array_in_mem.contains(temp_x));
					logger.info(y_array_in_mem.contains(temp_y));
					if(count==0 && ! x_array_in_mem.contains(temp_x) && ! y_array_in_mem.contains(temp_y))
					{
						
						position.setX(temp_x.floatValue());
						position.setY(temp_y.floatValue());
						x_array_in_mem.add(temp_x);
						y_array_in_mem.add(temp_y);
						node.setPosition(position);
						break;
					}
					
				}
				node_lst.add(node);
				
			}
			flow.setNode(node_lst);
			List<EdgeModel> edge_lst=new ArrayList<EdgeModel>();
			EdgeModel edgemodel=new EdgeModel();
			Edge edge=new Edge();
			edge.setEdge_fixed(true);
			edge.setSource(node_id+"0");
			edge.setTarget(node_id+"1");
			edge.setId(node_id+"0"+"R"+node_id+"1");
			edgemodel.setData(edge);
			edge_lst.add(edgemodel);
			flow.setEdge(edge_lst);
			/*Map<String, String> map = new HashMap<String, String>();
			map.put("targetColl","bp_flow_data");
			edges.push({
	            "data" : {
	                "source" : nodeid+"1",
	                "edge_fixed" : true,
	                "target" : nodeid+"2",
	                "id" : nodeid+"1"+"R"+nodeid+2
	            }
	        })
			*/
			
			try {
				/*String orderDataStr = Property.getDomain_url()+"dataService";
				result= (String) RestUtil.post(orderDataStr,String.class, map);*/
				
				

				
			} catch (Exception e) {
				e.printStackTrace();
			}
       
		return flow;
		
	}
}
