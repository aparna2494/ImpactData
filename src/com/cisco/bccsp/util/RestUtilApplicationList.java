package com.cisco.bccsp.util;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.apache.log4j.Logger;

import com.cisco.bccsp.model.ApplicationList;
import com.cisco.bccsp.model.applicationStatement;






public class RestUtilApplicationList {
	
	
	public static List<ApplicationList> post(String webserviceURL, Class returnClass,
			Map<String, applicationStatement> map2) throws Exception {
	
    	
 		JSONObject obj = new JSONObject();
 		
 		List<ApplicationList> applist_array=new ArrayList<ApplicationList>();	
 		RestTemplate restTemplate = new RestTemplate();
 		JSONParser parser = new JSONParser();
 		MultiValueMap<String, applicationStatement> map = new LinkedMultiValueMap<String, applicationStatement>();
 		for (Map.Entry<String, applicationStatement> entry : map2.entrySet()) {
			map.add(entry.getKey(), entry.getValue());
		}
 		HttpHeaders headers = new HttpHeaders();
 		headers.setContentType(MediaType.APPLICATION_JSON);      
 		headers.set("Authorization", "Basic bmVvNGo6YWRtaW4=");
 		HttpEntity<MultiValueMap<String, applicationStatement>> request = new HttpEntity<MultiValueMap<String, applicationStatement>>(map, headers);

		String result = restTemplate.postForObject(webserviceURL, request, String.class);
		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		List<String> Application_list=new ArrayList<String>();
		obj=(JSONObject) parser.parse(result);
		//System.out.println(result);
		//System.out.println(obj.get("commit"));
		JSONArray results_array=new JSONArray();
		results_array=(JSONArray) obj.get("results");
		Iterator result_array_itr=results_array.iterator();
		while(result_array_itr.hasNext())
		{
			JSONObject res =(JSONObject)result_array_itr.next();
			JSONArray data_array=new JSONArray();
			data_array=(JSONArray) res.get("data");
			Iterator data_array_itr=data_array.iterator();
			while(data_array_itr.hasNext())
			{
				
				JSONObject data =(JSONObject)data_array_itr.next();
				JSONObject graph=new JSONObject();
				graph=(JSONObject) data.get("graph");
				
					
					
					JSONArray nodes_array=new JSONArray();
					nodes_array=(JSONArray) graph.get("nodes");
					Iterator nodes_array_itr=nodes_array.iterator();
					while(nodes_array_itr.hasNext())
					{
						
						JSONObject node =(JSONObject)nodes_array_itr.next();
						ApplicationList applst=new ApplicationList();
						applst.setId((String) node.get("id"));
						JSONObject property=(JSONObject)node.get("properties");
						applst.setLabel((String) property.get("name"));
						applist_array.add(applst);
					}
				
			}
			
		}
		
		
		
		return applist_array;
} 
}

