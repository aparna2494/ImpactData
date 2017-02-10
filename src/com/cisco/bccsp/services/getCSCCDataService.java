package com.cisco.bccsp.services;
import java.util.HashMap;
import java.util.Map;

import com.cisco.bccsp.resources.Property;


import com.cisco.bccsp.util.RestUtil;




import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Path("getCSCCdataservice")
public class getCSCCDataService {
public getCSCCDataService() {
		
	}

	Map<String,String> CSCCMap = new HashMap<String, String>();
	 private static Logger logger = Logger.getLogger(
			 getCSCCDataService.class.getName());
	 @GET
	 @Path("/getCSCCdata")
	 @Produces(MediaType.APPLICATION_JSON)
	public Map<String,String> getCSCCData()
	{
		
			logger.info("getting  CSCC data");
			Map<String, String> map = new HashMap<String, String>();
			map.put("targetColl","bcc_cscc_quotes_tvs_data");
			
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
						JSONObject oo = (JSONObject)orderData.get("Invalid");
						CSCCMap.put("qcurrentinvalidfilter",oo.get("COUNT").toString());
						CSCCMap.put("qcurrentinvalidfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Invalid Orderable or Valid");
						CSCCMap.put("qcurrentvalidfilter",oo.get("COUNT").toString());
						CSCCMap.put("qcurrentvalidfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("In-Valid Submitted");
						CSCCMap.put("qinvalidsubmittedfilter",oo.get("COUNT").toString());
						CSCCMap.put("qinvalidsubmittedfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Order In Progress");
						CSCCMap.put("qtooipfilter",oo.get("COUNT").toString());
						CSCCMap.put("qtooipfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Order Submitted");
						CSCCMap.put("qtoosubmitted",oo.get("COUNT").toString());
						CSCCMap.put("qtoosubmittedthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Order Booked");
						CSCCMap.put("qtoobookedfilter",oo.get("COUNT").toString());
						CSCCMap.put("qtoobookedfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Conversion In Progress");
						CSCCMap.put("qconversionipfilter",oo.get("COUNT").toString());
						CSCCMap.put("qconversionipfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Conversion Failed");
						CSCCMap.put("qconversionfailedfilter",oo.get("COUNT").toString());
						CSCCMap.put("qconversionfailedfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Conversion Revalidated");
						CSCCMap.put("qconversionrevalfilter",oo.get("COUNT").toString());
						CSCCMap.put("qconversionrevalfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Conversion Resubmitted");
						CSCCMap.put("qconversionresubfilter",oo.get("COUNT").toString());
						CSCCMap.put("qconversionresubfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Entitled");
						CSCCMap.put("qentitledfilter",oo.get("COUNT").toString());
						CSCCMap.put("qentitledfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						oo=(JSONObject)orderData.get("Order Complete");
						CSCCMap.put("qordercompletedfilter",oo.get("COUNT").toString());
						CSCCMap.put("qordercompletedfilterthreshold",oo.get("COLOR_NOTIFICATION").toString());
						
						}
						
					}
					
				}catch (Exception e1){
					e1.printStackTrace();
				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}
       
		return CSCCMap;
		
	}

}
