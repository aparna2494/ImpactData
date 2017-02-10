package com.cisco.bccsp.util;



import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.apache.log4j.Logger;






public class RestUtil {
	
	
	public static String post(String webserviceURL, Class returnClass,
			Map<String, String> parameters) throws Exception {
	
    	
 		JSONObject obj = new JSONObject();
 		
 			
 		RestTemplate restTemplate = new RestTemplate();
 		
 		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
 		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			map.add(entry.getKey(), entry.getValue());
		}
		
		String result = restTemplate.postForObject(webserviceURL, map, String.class);
		
		
		return result;
} 
}

