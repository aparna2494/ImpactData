package com.cisco.bccsp.services;
import java.util.HashMap;
import java.util.Map;

import com.cisco.bccsp.db.dao.GenericDataFetchDao;
import com.cisco.bccsp.db.dao.getAlertsDao;
import com.cisco.bccsp.resources.Property;
import com.cisco.bccsp.util.RestUtil;





import com.google.gson.Gson;

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


@Path("getalertsdataservice")
public class getAlertDataService {
public getAlertDataService() {
		
	}

	Map<String,String> AMap = new HashMap<String, String>();
	Gson gson = new Gson();
	String result ;
	String results ;
	 private static Logger logger = Logger.getLogger(
			 getCSCCDataService.class.getName());
	 @Context HttpServletRequest servletRequest;
	 @GET
	 @Path("/getalertsdata")
	 @Produces(MediaType.APPLICATION_JSON)
	public String getAlertData()
	{
		
			logger.info("getting  alerts data");
			Map<String, String> map = new HashMap<String, String>();
			map.put("targetColl","bcc_monitoring");
			
			map.put("optionalPara","UNIT_NAME:Commerce");
			
			try {
				String orderDataStr = Property.getDomain_url()+"dataService";
				result = (String) RestUtil.post(orderDataStr,String.class, map);
				
				try
				{
					JSONParser js = new JSONParser();
					
					if(result!=null && result.length()>0){
						
					JSONArray a = (JSONArray) js.parse(result);
					
						
					}
					
				}catch (Exception e1){
					AMap.put("result","No data available");
					result = gson.toJson(AMap); 
				}


				
			} catch (Exception e) {
				e.printStackTrace();
			}
       
		return result;
		
	}
	@GET
	 @Path("/getroomdetails/{track_id}")
	 @Produces(MediaType.APPLICATION_JSON)
	public String getSparkRoomDetails(@PathParam("track_id") String track_id)
	{
		

			try {
				/*String orderDataStr = Property.getDomain_url()+"dataService";
				results = (String) RestUtil.post(orderDataStr,String.class, maps);*/
				results=GenericDataFetchDao.getDocument("track_mappings", "track_id",Long.parseLong(track_id));
				try
				{
					JSONParser js = new JSONParser();
					
					if(results!=null && results.length()>0){
						
					JSONArray a = (JSONArray) js.parse(results);
					
						
					}
					
				}catch (Exception e1){
					AMap.put("result","No data available");
					results = gson.toJson(AMap); 
				}


				
			} catch (Exception e) {
				e.printStackTrace();
			}
       //logger.info(results);
		return results;
		
	}
			 @GET
			 @Path("/getalertsmonitoringdata/{collection}")
			 @Produces(MediaType.APPLICATION_JSON)
			public String getAlertMonitoringData(@PathParam("collection") String collection)
			{
				
				 	logger.info("collection name"+collection);
					Map<String, String> maps = new HashMap<String, String>();
					maps.put("targetColl",collection);
					
					maps.put("optionalPara","alert_status:open");
					
					try {
						/*String orderDataStr = Property.getDomain_url()+"dataService";
						results = (String) RestUtil.post(orderDataStr,String.class, maps);*/
						String whoIsIt = "";
						
						if (servletRequest.getHeader("AUTH_USER") != null){
					        whoIsIt = servletRequest.getHeader("AUTH_USER");
						} else	{
							whoIsIt = servletRequest.getHeader("TEST_USER");
						}
						logger.info(whoIsIt);
						results=getAlertsDao.getDocument(whoIsIt,collection);
						//results=GenericDataFetchDao.getDocument(collection, "alert_status","open");
						logger.info(results.length());
						try
						{
							JSONParser js = new JSONParser();
							
							if(results!=null && results.length()>0){
								
							JSONArray a = (JSONArray) js.parse(results);
							
								
							}
							
						}catch (Exception e1){
							AMap.put("result","No data available");
							results = gson.toJson(AMap); 
						}


						
					} catch (Exception e) {
						e.printStackTrace();
					}
		       //logger.info(results);
				return results;
				
			}
			}
