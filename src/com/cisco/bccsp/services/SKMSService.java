package com.cisco.bccsp.services;

import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.cisco.bccsp.db.util.BCCException;

@Path("skms")
public class SKMSService {
	
    private static Logger logger = Logger.getLogger(
      SKMSService.class.getName());

    
    @Context HttpServletRequest servletRequest;    
    
    public SKMSService() {
	
    }
 
	
	/**
	 * Get a list of questions for knowledge articles
	 * @param searchFilter
	 * @return String
	 * @throws BCCException 
	 */
	@POST
	@Path("/questions")
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuestions(String searchFilter) throws BCCException {
		logger.info("hello");
		JSONObject request = new JSONObject();
		String filter = null;
		
		try {
			request = (JSONObject) new JSONParser().parse(searchFilter);
			filter = request.get("searchFilter").toString();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

        logger.info("getting SKMS questions");                            	            	
		String whoIsIt = "";
		String response = null;
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			response = restTemplate.getForObject(
		        "http://vm-gsqs-um-007/skms.beta/skmssearch/views/faq_services.json?display_id=page_1&filters[type]=kedb&filters[keys]=" + filter + "&filters[service_cat]=", String.class);
			response = response.replaceAll( "</?a[^>]*>", "" );
			}
		catch (Exception e) {
			response = e.getStackTrace().toString();
		}
		return response;
	}
	
	@POST
	@Path("/answer")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAnswer(String nid) throws BCCException {
		logger.info("in here");
		JSONObject request = new JSONObject();
		String filter = null;
		
		try {
			request = (JSONObject) new JSONParser().parse(nid);
			filter = request.get("nid").toString();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

        logger.info("getting SKMS answers");                            	            	
		String whoIsIt = "";
		String response = null;
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			response = restTemplate.getForObject(
		        "http://vm-gsqs-um-007/skms.beta/skmssearch/views/faq_services.json?display_id=detailed_page&filters[nid]=" + filter, String.class);
			response = response.replaceAll( "</?a[^>]*>", "" );
			response = response.replaceAll( "</?strong[^>]*>", "" );
			}
		catch (Exception e) {
			response = e.getStackTrace().toString();
		}
		return response;
	}
}
