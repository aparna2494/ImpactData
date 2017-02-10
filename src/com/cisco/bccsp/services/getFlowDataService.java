package com.cisco.bccsp.services;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cisco.bccsp.db.dao.GenericDataFetchDao;
import com.cisco.bccsp.db.dao.NodeDetailsDAO;
import com.cisco.bccsp.db.dao.getFlowDao;
import com.cisco.bccsp.resources.Property;
import com.cisco.bccsp.util.RestUtil;

@Path("getflowdata")
public class getFlowDataService {
	private static Logger logger = Logger.getLogger(getFlowDataService.class.getName());
	@Context HttpServletRequest servletRequest;  
	 @GET
	 @Path("/getflows/{collection_name}")
	 @Produces(MediaType.APPLICATION_JSON)
	public String getCSCCData(@PathParam("collection_name") String collection_name)
	{
		 String result ="";
			
			/*Map<String, String> map = new HashMap<String, String>();
			map.put("targetColl","bp_flow_data");
			*/
			try {
				/*String orderDataStr = Property.getDomain_url()+"dataService";
				result= (String) RestUtil.post(orderDataStr,String.class, map);*/
				logger.info("getting data from "+collection_name);
				String whoIsIt = "";
				
				if (servletRequest.getHeader("AUTH_USER") != null){
			        whoIsIt = servletRequest.getHeader("AUTH_USER");
				} else	{
					whoIsIt = servletRequest.getHeader("TEST_USER");
				}
				logger.info(whoIsIt);
				
				result=getFlowDao.getDocument(whoIsIt,collection_name);
				//result=GenericDataFetchDao.getDocument(collection_name);
				//result=GenericDataFetchDao.getFlowData(whoIsIt,collection_name).toString();

				
			} catch (Exception e) {
				e.printStackTrace();
			}
       
		return result;
		
	}
	 @GET
	 @Path("/getneoflows")
	 @Produces(MediaType.APPLICATION_JSON)
	public String getNeoFlowData()
	{
		 String result ="";
			logger.info("getting neo flow");
			/*Map<String, String> map = new HashMap<String, String>();
			map.put("targetColl","bp_flow_data");
			*/
			try {
				/*String orderDataStr = Property.getDomain_url()+"dataService";
				result= (String) RestUtil.post(orderDataStr,String.class, map);*/
				result=GenericDataFetchDao.getDocument("neo4j_bp_flow_data");
				
logger.info(result);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
       
		return result;
		
	}
	 @GET
	 @Path("/getnid/{kpi_id}/{component_id}")
	 @Produces(MediaType.APPLICATION_JSON)
	public String getnidData(@PathParam("kpi_id") String kpi_id,@PathParam("component_id") String flowComponentid)
	{
		 String result ="";
			
			/*Map<String, String> map = new HashMap<String, String>();
			map.put("targetColl","bp_flow_data");
			*/
			try {
				/*String orderDataStr = Property.getDomain_url()+"dataService";
				result= (String) RestUtil.post(orderDataStr,String.class, map);*/

				result=NodeDetailsDAO.getSKMSid(kpi_id,flowComponentid);
				

				
			} catch (Exception e) {
				e.printStackTrace();
			}
       
		return result;
		
	}
	 
	 

}
