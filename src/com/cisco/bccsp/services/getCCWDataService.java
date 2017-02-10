package com.cisco.bccsp.services;
import java.util.HashMap;
import java.util.Map;

import com.cisco.bccsp.resources.Property;


import com.cisco.bccsp.util.RestUtil;




import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Path("getCCWdataservice")
public class getCCWDataService {
public getCCWDataService() {
		//System.out.println("inside java");
	}

	Map<String,String> CCWMap = new HashMap<String, String>();
	 private static Logger logger = Logger.getLogger(
			 getCSCCDataService.class.getName());
	 @GET
	 @Path("/getCCWdata")
	 @Produces(MediaType.APPLICATION_JSON)
	public Map<String,String> getCSCCData()
	{
		
			logger.info("getting  CCW data");
			Map<String, String> map = new HashMap<String, String>();
			map.put("targetColl","bcc_ccw_quotes_tvs_data");
			
			try {
				String orderDataStr = Property.getDomain_url()+"dataService";
				String result = (String) RestUtil.post(orderDataStr,String.class, map);
				
				try
				{
					JSONParser js = new JSONParser();
					
					if(result!=null && result.length()>0){
						
					JSONArray a = (JSONArray) js.parse(result);
					
						for (Object o : a)
						{
							
							JSONObject orderData = (JSONObject) o;
							JSONObject oo = (JSONObject)orderData.get("Quotes Initiated");
							CCWMap.put("qinitiated",oo.get("COUNT").toString());
							CCWMap.put("qinitiatedthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Quotes Submitted");
							CCWMap.put("qsubmitted",oo.get("COUNT").toString());
							CCWMap.put("qsubmittedthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Lost Quotes");
							CCWMap.put("qlost",oo.get("COUNT").toString());
							CCWMap.put("qlostthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Quotes AIP");
							CCWMap.put("qaip",oo.get("COUNT").toString());
							CCWMap.put("qaipthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Approved Quotes");
							CCWMap.put("qapproved",oo.get("COUNT").toString());
							CCWMap.put("qapprovedthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Rejected Quotes");
							CCWMap.put("qrejected",oo.get("COUNT").toString());
							CCWMap.put("qrejectedthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Expired Quotes");
							CCWMap.put("qexpired",oo.get("COUNT").toString());
							CCWMap.put("qexpiredthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Quotes where Order Initiated");
							CCWMap.put("qtooinitiated",oo.get("COUNT").toString());
							CCWMap.put("qtooinitiatedthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Order Initiated");
							CCWMap.put("oinitiated",oo.get("COUNT").toString());
							CCWMap.put("oinitiatedthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Orders In Error");
							CCWMap.put("oinerror",oo.get("COUNT").toString());
							CCWMap.put("oinerrorthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Orders Submitted");
							CCWMap.put("osubmitted",oo.get("COUNT").toString());
							CCWMap.put("osubmittedthreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Order Struck for SO # Generation");
							CCWMap.put("ostuckso",oo.get("COUNT").toString());
							CCWMap.put("ostucksothreshold",oo.get("COLOR_NOTIFICATION").toString());
							oo=(JSONObject)orderData.get("Orders Entered");
							CCWMap.put("oentered",oo.get("COUNT").toString());
							CCWMap.put("oenteredthreshold",oo.get("COLOR_NOTIFICATION").toString());
						}
						
					}
					
				}catch (Exception e1){
					e1.printStackTrace();
				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}
       
		return CCWMap;
		
	}

}
