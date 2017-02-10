package com.cisco.bccsp.services;
	import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/*import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;*/
import javax.servlet.http.HttpServletRequest;

import com.cisco.bccsp.db.dao.AlertDetailsDAO;
import com.cisco.bccsp.db.dao.GenericDataFetchDao;
import com.cisco.bccsp.db.dao.UserDetailsDAO;
import com.cisco.bccsp.resources.Property;
import com.cisco.bccsp.util.RestUtil;
import com.cisco.bccsp.util.ldap.LDAPUtil;
import com.google.gson.Gson;

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

	@Path("getalertdetailsdataservice")
public class getAlertDetails {
	
	
	
	public getAlertDetails() {
			
		}

		String result ;String bccAlertTrendsData;
		 Gson gson = new Gson(); 
		 private static Logger logger = Logger.getLogger(
				 getCSCCDataService.class.getName());
					
		 @GET
		 @Path("/getalertdetails/{alertID}")
		 @Produces(MediaType.APPLICATION_JSON)
		public String getAlertData(@PathParam("alertID") String alertID)
		{		Map<String,String> AMap = new HashMap<String, String>();

			
				
				/*Map<String, String> map = new HashMap<String, String>();
				map.put("targetColl","bcc_alerts_sec");
				
				map.put("optionalPara","alert_id:"+alertID);*/
				
				try {
					/*String orderDataStr = Property.getDomain_url()+"dataService";
					result = (String) RestUtil.post(orderDataStr,String.class, map);*/
					result=GenericDataFetchDao.getDocument("bcc_alerts_sec", "alert_id",Long.parseLong(alertID));
				
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
		 @Path("/getalerttrenddetails/{alertID}")
		 @Produces(MediaType.APPLICATION_JSON)
		public String getAlertTrendsData(@PathParam("alertID") String alertID)
		{		Map<String,String> AMap = new HashMap<String, String>();

				/*Map<String, String> bccDataMap = new HashMap<String, String>();
				bccDataMap.put("targetColl", "bcc_alert_trend");
				
				bccDataMap.put("optionalPara" ,"alert_id:"+alertID);
				*/
				try {
					/*String getDataUrl = Property.getDomain_url() + "dataService";
					bccAlertTrendsData = (String) RestUtil.post(getDataUrl, String.class,
					bccDataMap);*/
					bccAlertTrendsData=GenericDataFetchDao.getDocument("bcc_alert_trend", "alert_id",Long.parseLong(alertID));

					try
					{
						JSONParser js = new JSONParser();
						
						if(bccAlertTrendsData!=null && bccAlertTrendsData.length()>0){
							
						JSONArray a = (JSONArray) js.parse(bccAlertTrendsData);
						
							
							
						}
						
					}catch (Exception e1){
						AMap.put("result","No data available");
						bccAlertTrendsData = gson.toJson(AMap); 
					}


					
				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.info(bccAlertTrendsData);
				return bccAlertTrendsData;
			}
			@GET
			 @Path("/getalerthistorydetails/{flowComponentId}")
			 @Produces(MediaType.APPLICATION_JSON)
			public String getAlertHistoryData(@PathParam("flowComponentId") String flowComponentId)
			{		Map<String,String> AMap = new HashMap<String, String>();
					logger.info("getting  alerts data"+flowComponentId);
					/*Map<String, String> bccDataMap = new HashMap<String, String>();
					bccDataMap.put("targetColl", "bcc_alerts_sec");
					//System.out.println("alertId is--"+alertId);
					bccDataMap.put("optionalPara" ,"id:"+flowComponentId+",alert_status:closed");
					//bccDataMap.put("optionalPara" ,"ALERT_ID:110");
*/					try {
						/*String getDataUrl = Property.getDomain_url() + "dataService";
						bccAlertTrendsData = (String) RestUtil.post(getDataUrl, String.class,
						bccDataMap);*/
						bccAlertTrendsData=GenericDataFetchDao.getDocument("bcc_alerts_sec", "id",flowComponentId,"alert_status","closed");

						try
						{
							JSONParser js = new JSONParser();
							
							if(bccAlertTrendsData!=null && bccAlertTrendsData.length()>0){
								
							JSONArray a = (JSONArray) js.parse(bccAlertTrendsData);
							
								
								
							}
							
						}catch (Exception e1){
							AMap.put("result","No data available");
							bccAlertTrendsData = gson.toJson(AMap); 
						}


					} catch (Exception e) {
						e.printStackTrace();
					}

					return bccAlertTrendsData;
				}
			 @Context HttpServletRequest servletRequest;    
			    
			@GET
			 @Path("/savealertcomment/{comment}/{alertID}/{coll}")
			 @Produces(MediaType.APPLICATION_JSON)
			public String saveAlertComments(@PathParam("comment") String comment,@PathParam("alertID") String alertID,@PathParam("coll") String coll){
				String whoIsIt = "";
				String result ="";
				if (servletRequest.getHeader("AUTH_USER") != null){
			        whoIsIt = servletRequest.getHeader("AUTH_USER");
				} else	{
					whoIsIt = servletRequest.getHeader("TEST_USER");
				}
				
				Map<String, String> data = new HashMap<String, String>();
				data.put("comments",comment);
				data.put("alertID", alertID);
				//String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
				Long unixTime = System.currentTimeMillis() / 1000L;
				String time = unixTime.toString();
				data.put("time",time);
				data.put("reach",whoIsIt);
				logger.error(comment+alertID+time+whoIsIt);
				//Map<String, String> formData = new HashMap<String, String>();
				                                //formData.put("bcc_alerts_sec", new Gson().toJson(data));
				                                try {
				                                	result= AlertDetailsDAO.savecomment(data,coll);
				                                	
													//result= (String) RestUtil.post(Property.getDomain_url()+"bcc/update_alert_comments", String.class, formData);
													}catch (Exception e){

													}
				return result;
			}
			@GET
			 @Path("/saveassignee/{alertID}/{assigneename}/{collectionname}")
			 @Produces(MediaType.TEXT_XML)
			public String saveAlertAssignee(@PathParam("alertID") String alertID,@PathParam("assigneename") String assigneename,@PathParam("collectionname") String collectionname){
				String whoIsIt = "";
				String result ="";
				String assignedby="";
				if (servletRequest.getHeader("AUTH_USER") != null){
			        assignedby = servletRequest.getHeader("AUTH_USER");
				} else	{
					assignedby = servletRequest.getHeader("TEST_USER");
				}
				
				logger.info(assigneename);
				if(assigneename.equals("loggeduser"))
				{
					
				
				if (servletRequest.getHeader("AUTH_USER") != null){
			        whoIsIt = servletRequest.getHeader("AUTH_USER");
				} else	{
					whoIsIt = servletRequest.getHeader("TEST_USER");
				}
				
				}
				else if(assigneename.equals("unassigned"))
				{
					whoIsIt="";
					assignedby="";
				}
				else
					whoIsIt=assigneename;
				Map<String, String> user = new HashMap();
				user.put("alertID", alertID);
				user.put("userName", whoIsIt);
				user.put("assignedby",assignedby);
				try {
					AlertDetailsDAO.assignAlert(user,collectionname);
						//result= (String) RestUtil.post(Property.getDomain_url()+"bcc/update_alert_prole",String.class, formData);
					
				} catch ( Exception e) {
					e.printStackTrace();
				}
				return whoIsIt+" "+assignedby;
			}
			
			@GET
			 @Path("/verifyassignee/{newassignee}")
			 @Produces(MediaType.TEXT_XML)
			public String verifyAssignee(@PathParam("newassignee") String verify_id){
				
				logger.info("inside the verification");	
				String verify_user=LDAPUtil.verifyLDAPPerson(verify_id);
				logger.info(verify_user);
					return verify_user;
			}
			
			
			@GET
			 @Path("/closealert/{alertID}")
			 @Produces(MediaType.TEXT_XML)
			public String closeAlerts(@PathParam("alertID") String alertID){
				String whoIsIt = "";
				String result ="";
				
				if (servletRequest.getHeader("AUTH_USER") != null){
			        whoIsIt = servletRequest.getHeader("AUTH_USER");
				} else	{
					whoIsIt = servletRequest.getHeader("TEST_USER");
				}
				
				Map<String, String> user = new HashMap();
				user.put("alertId", alertID);
				user.put("userName", whoIsIt);
				//Map<String, String> formData = new HashMap();
				//formData.put("update_alert_prole", new Gson().toJson(user));
				
				try {
					 AlertDetailsDAO.closeAlert(alertID);
						//result= (String) RestUtil.post(Property.getDomain_url()+"bcc/update_alert_prole",String.class, formData);
					} catch ( Exception e) {
					e.printStackTrace();
				}
				return whoIsIt;
			}
	
/*public static void main(String[] args)
{
			Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "neo4j" ) );
			Session session = driver.session();

			session.run( "CREATE (a:Person {name: {name}, title: {title}})",
			        parameters( "name", "Arthur", "title", "King" ) );

			StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} " +
			                                      "RETURN a.name AS name, a.title AS title",
			        parameters( "name", "Arthur" ) );
			while ( result.hasNext() )
			{
			    Record record = result.next();
			    System.out.println( record.get( "title" ).asString() + " " + record.get( "name" ).asString() );
			}

			session.close();
			driver.close();
}*/
	}

