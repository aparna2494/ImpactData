package com.cisco.bccsp.util;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class GetRestUtil {

	
	public static String get(String webserviceURL)
	{
		/*Client client;
		GenericType<List<String>> list = new GenericType<List<String>>() {};
		String REST_SERVICE_URL =webserviceURL;
		
		client = ClientBuilder.newBuilder().build();
		Response response=client
        .target(REST_SERVICE_URL)
        .request(MediaType.APPLICATION_JSON)
        .get();
		String result = response.readEntity(String.class);
		System.out.println(result);
		return result;*/
		
		
 		RestTemplate restTemplate = new RestTemplate();
 		
 		
		String result = restTemplate.getForObject(webserviceURL,String.class);
		
		
		return result;
        
		
	}
}
