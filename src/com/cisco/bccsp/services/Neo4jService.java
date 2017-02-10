package com.cisco.bccsp.services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.cisco.bccsp.db.dao.Neo4jDAO;
import com.cisco.bccsp.model.Neo4jSNodeDetailModel;
import com.cisco.bccsp.model.Neo4jSNodeDetailModelv2;
import com.cisco.bccsp.model.applicationStatement;
import com.cisco.bccsp.util.Neo4jRestUtil;


@Path("neo4j")
public class Neo4jService {
	private static Logger logger = Logger.getLogger(
			Neo4jService.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public Neo4jService() {
		
	}

	
	@GET
	@Path("/job/{node}")
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Neo4jSNodeDetailModel> getJobeList(@PathParam("node") String node)
	{
		Map<String, applicationStatement> map = new HashMap<String, applicationStatement>();
		applicationStatement obj=new applicationStatement();
		logger.info("MATCH (sd:SERVICE_DOMAIN)-[r7]->(sc:SERVICE_CATEGORY)-[r6]->(s:SERVICE)-[r1]->(so:SERVICE_OFFERING)-[r2]->(n:APPLICATION)-[r3]->(p:PROCESS)<-[sp:STATE_TO_PROCESS]-(state:ORDER_PROCESS_FLOW_STATE) where state.code=`"+node+"` and p.service_offering=so.name OPTIONAL MATCH (p:PROCESS)-[pcp:PROCESS_TO_CP]->(cp:CONCURRENT_PROGRAM) OPTIONAL MATCH (s:SERVICE)-[r4:IT_SERVICE_OWNER]->(p1:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[rm_r:RESPONSIBLE_MANAGER]->(rm:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sm_r:SUPPORT_MANAGER]->(sm:PEOPLE) OPTIONAL MATCH (sc:SERVICE_CATEGORY)-[se_r:SERVICE_EXECUTIVE]->(se:PEOPLE) OPTIONAL MATCH (n:APPLICaATION)-[sc_r:SUPPORT_CONTACT]->(scon:PEOPLE) return  sd.name AS `Service Domain`,sc.name AS `Service Category`,s.name AS `Service`,so.name as `Service Offering`,n.name as `Application`,collect(cp.name) as `Concurrent Program name`,p1.name as `IT service owner`,rm.name as `Responsible Manager`,sm.name as `Support Manager` ,se.name as `Service Executive`,scon.name as `Support Contact`");
		obj.setStatement("MATCH (sd:SERVICE_DOMAIN)-[r7]->(sc:SERVICE_CATEGORY)-[r6]->(s:SERVICE)-[r1]->(so:SERVICE_OFFERING)-[r2]->(n:APPLICATION)-[r3]->(p:PROCESS)<-[sp:STATE_TO_PROCESS]-(state:ORDER_PROCESS_FLOW_STATE) where state.code='"+node+"' and p.service_offering=so.name OPTIONAL MATCH (p:PROCESS)-[pcp:PROCESS_TO_CP]->(cp:CONCURRENT_PROGRAM) OPTIONAL MATCH (s:SERVICE)-[r4:IT_SERVICE_OWNER]->(p1:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[rm_r:RESPONSIBLE_MANAGER]->(rm:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sm_r:SUPPORT_MANAGER]->(sm:PEOPLE) OPTIONAL MATCH (sc:SERVICE_CATEGORY)-[se_r:SERVICE_EXECUTIVE]->(se:PEOPLE) OPTIONAL MATCH (n:APPLICaATION)-[sc_r:SUPPORT_CONTACT]->(scon:PEOPLE) return  sd.name AS `Service Domain`,sc.name AS `Service Category`,s.name AS `Service`,so.name as `Service Offering`,n.name as `Application`,collect(cp.name) as `Concurrent Program name`,p1.name as `IT service owner`,rm.name as `Responsible Manager`,sm.name as `Support Manager` ,se.name as `Service Executive`,scon.name as `Support Contact`");
		List<Neo4jSNodeDetailModel> result_list=null;
		List<String> lst=new ArrayList<String>();
		lst.add("row");
		lst.add("graph");
		obj.setResultDataContents(lst);
		obj.setIncludeStats(true);
		map.put("statements", obj);
		
		logger.info(map.toString());
		String url="http://itds-script-02:7474/db/data/transaction";
		try {
			result_list= Neo4jRestUtil.getJobDetails(url,String.class, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(e);
			e.printStackTrace();
		}
		 return result_list;
      
		
		
	}
	
	
		
		@GET
		@Path("/jvm/{node}")
		@Produces(MediaType.APPLICATION_JSON)
		
		public List<Neo4jSNodeDetailModel> getJVMList(@PathParam("node") String node)
		{
			Map<String, applicationStatement> map = new HashMap<String, applicationStatement>();
			applicationStatement obj=new applicationStatement();
			
			obj.setStatement("MATCH (sd:SERVICE_DOMAIN)-[r7]->(sc:SERVICE_CATEGORY)-[r6]->(s:SERVICE)-[r1]->(so:SERVICE_OFFERING)-[r2]->(n:APPLICATION)-[r3]->(jvm:JVM_INSTANCES)<-[pfd:ORDER_PROCESS_FLOW_DEPENDENCY]-(state:ORDER_PROCESS_FLOW_STATE) where state.code='"+node+"' OPTIONAL MATCH (s:SERVICE)-[r4:IT_SERVICE_OWNER]->(p1:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[rm_r:RESPONSIBLE_MANAGER]->(rm:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sm_r:SUPPORT_MANAGER]->(sm:PEOPLE) OPTIONAL MATCH (sc:SERVICE_CATEGORY)-[se_r:SERVICE_EXECUTIVE]->(se:PEOPLE) OPTIONAL MATCH (n:APPLICATION)-[sc_r:SUPPORT_CONTACT]->(scon:PEOPLE) return sd.name AS `Service Domain`,sc.name AS `Service Category`,s.name AS `Service`,so.name as `Service Offering`,n.name as `Application`,collect (jvm.name) as `JVM name`,p1.name as `IT service owner`,rm.name as `Responsible Manager`,sm.name as `Support Manager` ,se.name as `Service Executive`,scon.name as `Support Contact`");
			List<Neo4jSNodeDetailModel> result_list=null;
			List<String> lst=new ArrayList<String>();
			lst.add("row");
			lst.add("graph");
			obj.setResultDataContents(lst);
			obj.setIncludeStats(true);
			map.put("statements", obj);
			
			logger.info(map.toString());
			String url="http://itds-script-02:7474/db/data/transaction";
			try {
				result_list= Neo4jRestUtil.getJVMDetails(url,String.class, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info(e);
				e.printStackTrace();
			}
			 return result_list;
	      
			
			
		}
		
		
		@GET
		@Path("/fetchTracks")
		@Produces(MediaType.APPLICATION_JSON)
		
		public List<String> getTracks()
		{
			Map<String, applicationStatement> map = new HashMap<String, applicationStatement>();
			applicationStatement obj=new applicationStatement();
			
			obj.setStatement("MATCH (tm:TRACK_MASTER) where tm.active=1 return tm.name, tm.active order by tm.sequence");
			List<String> result_list=null;
			List<String> lst=new ArrayList<String>();
			lst.add("row");
			lst.add("graph");
			obj.setResultDataContents(lst);
			obj.setIncludeStats(true);
			map.put("statements", obj);
			
			logger.info(map.toString());
			String url="http://itds-script-02:7474/db/data/transaction";
			try {
				result_list= Neo4jRestUtil.getNeoTracks(url,String.class, map);
				logger.info(result_list);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.info(e);
				e.printStackTrace();
			}
			 return result_list;
	      
			
			
		}
		
		
		
		@GET
		@Path("/scm")
		@Produces(MediaType.APPLICATION_JSON)
		
		public Map<String,String> getSCM()
		{
			 
			Map<String,String> map=new HashMap();
			  map.put("it_service_owner", "Marryat Des");
			  map.put("support_manager", "Broughton, Mark");
			  map.put("service_exceutive","Sesh Tirumala");
			  map.put("service_hier", "Supply Chain Management");
			  map.put("support_director", "Liem, Stephen A");
			  return map;
			
		}
		
		
		@GET
		@Path("/nodeinfo/{component_code}")
		@Produces(MediaType.APPLICATION_JSON)
		
		public Neo4jSNodeDetailModelv2 getNodeInfo(@PathParam("component_code") String component_code)
		{
			Neo4jDAO neoobj=new Neo4jDAO();
			Neo4jSNodeDetailModelv2 result=neoobj.getNodeDetails(component_code);
			return result;
			
		}
		
		@GET
		@Path("/getflow/{track_name}/{counter}")
		@Produces(MediaType.APPLICATION_JSON)
		
		public String getFlow(@PathParam("track_name") String track_name,@PathParam("counter") String counter)
		{
			Neo4jDAO neoobj=new Neo4jDAO();
			 return neoobj.getFlow(track_name,counter);
			
			
		}
}
