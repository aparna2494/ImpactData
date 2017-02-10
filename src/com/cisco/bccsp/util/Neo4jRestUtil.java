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

import com.cisco.bccsp.model.Neo4jSNodeDetailModel;
import com.cisco.bccsp.model.applicationStatement;

public class Neo4jRestUtil {

	public static List<Neo4jSNodeDetailModel> getJobDetails(String webserviceURL, Class returnClass,
			Map<String, applicationStatement> map2) throws Exception  {
		List<Neo4jSNodeDetailModel> result_list=new ArrayList<Neo4jSNodeDetailModel>();
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
		JSONObject obj=(JSONObject) parser.parse(result);
		JSONArray result_arr=(JSONArray) obj.get("results");
		Iterator result_itr=result_arr.iterator();
		while(result_itr.hasNext())
		{
			JSONObject data =(JSONObject)result_itr.next();
			JSONArray data_arr=(JSONArray)data.get("data");
			Iterator data_itr=data_arr.iterator();
			while(data_itr.hasNext())
			{
				JSONObject row =(JSONObject)data_itr.next();
				JSONArray row_arr=(JSONArray)row.get("row");
				Iterator row_itr=row_arr.iterator();
				Neo4jSNodeDetailModel neomodel=new Neo4jSNodeDetailModel();
				if(row_itr.hasNext())neomodel.setService_domain(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService_category(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService_offering(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setApplication(row_itr.next().toString());
				if(row_itr.hasNext())
					{
					List<String>prg_list=(List<String>) row_itr.next();
					
					neomodel.setConcurrent_program(prg_list);
					}
				if(row_itr.hasNext())neomodel.setIt_service_owner(row_itr.next().toString());
				
				if(row_itr.hasNext())neomodel.setResponsible_manager((String)row_itr.next());
				if(row_itr.hasNext())neomodel.setSupport_manager(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService_exceutive(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setSupport_contact((String)row_itr.next());
				result_list.add(neomodel);
			}
		}
		return result_list;
	}

	
	
	public static List<Neo4jSNodeDetailModel> getJVMDetails(String webserviceURL, Class returnClass,
			Map<String, applicationStatement> map2) throws Exception  {
		List<Neo4jSNodeDetailModel> result_list=new ArrayList<Neo4jSNodeDetailModel>();
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
		JSONObject obj=(JSONObject) parser.parse(result);
		JSONArray result_arr=(JSONArray) obj.get("results");
		Iterator result_itr=result_arr.iterator();
		while(result_itr.hasNext())
		{
			JSONObject data =(JSONObject)result_itr.next();
			JSONArray data_arr=(JSONArray)data.get("data");
			Iterator data_itr=data_arr.iterator();
			while(data_itr.hasNext())
			{
				JSONObject row =(JSONObject)data_itr.next();
				JSONArray row_arr=(JSONArray)row.get("row");
				Iterator row_itr=row_arr.iterator();
				Neo4jSNodeDetailModel neomodel=new Neo4jSNodeDetailModel();
				if(row_itr.hasNext())neomodel.setService_domain(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService_category(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService_offering(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setApplication(row_itr.next().toString());
				if(row_itr.hasNext())
					{
					List<String>prg_list=(List<String>) row_itr.next();
					
					neomodel.setJvm_name(prg_list);
					}
				if(row_itr.hasNext())neomodel.setIt_service_owner(row_itr.next().toString());
				
				if(row_itr.hasNext())neomodel.setResponsible_manager((String)row_itr.next());
				if(row_itr.hasNext())neomodel.setSupport_manager(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setService_exceutive(row_itr.next().toString());
				if(row_itr.hasNext())neomodel.setSupport_contact((String)row_itr.next());
				result_list.add(neomodel);
			}
		}
		return result_list;
	}
	
	
	public static List<String> getNeoTracks(String webserviceURL, Class returnClass,
			Map<String, applicationStatement> map2) throws Exception  {
		List<String> result_list=new ArrayList<String>();
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
		JSONObject obj=(JSONObject) parser.parse(result);
		JSONArray result_arr=(JSONArray) obj.get("results");
		Iterator result_itr=result_arr.iterator();
		while(result_itr.hasNext())
		{
			JSONObject data =(JSONObject)result_itr.next();
			JSONArray data_arr=(JSONArray)data.get("data");
			Iterator data_itr=data_arr.iterator();
			while(data_itr.hasNext())
			{
				JSONObject row =(JSONObject)data_itr.next();
				JSONArray row_arr=(JSONArray)row.get("row");
				
				
				result_list.add(row_arr.get(0).toString());
			}
		}
		return result_list;
	}

}
