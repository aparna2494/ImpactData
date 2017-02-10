package com.cisco.bccsp.services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.cisco.bccsp.db.dao.ApplicationListDAO;
import com.cisco.bccsp.db.util.BCCException;
import com.cisco.bccsp.model.ApplicationList;
import com.cisco.bccsp.model.applicationStatement;
import com.cisco.bccsp.util.RestUtilApplicationList;

@Path("applicationlist")
public class ApplicationListService {
	
    private static Logger logger = Logger.getLogger(
    		ApplicationListService.class.getName());

    
    @Context HttpServletRequest servletRequest;    
    
    public ApplicationListService() {
	
    }
 
	
	/**
	 * Get a list of questions for knowledge articles
	 * @param searchFilter
	 * @return String
	 * @throws BCCException 
	 */
	@GET
	@Path("/details")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationList> getApplication() throws BCCException {
		logger.info("hello");
		
		
		                           	            	
		String whoIsIt = "";
		List<ApplicationList> result=new ArrayList<ApplicationList>();
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		
		try {
			Map<String, applicationStatement> map = new HashMap<String, applicationStatement>();
			applicationStatement obj=new applicationStatement();
			obj.setStatement("MATCH (n:APPLICATION) RETURN n ");
			List<String> lst=new ArrayList<String>();
			lst.add("row");
			lst.add("graph");
			obj.setResultDataContents(lst);
			obj.setIncludeStats(true);
			map.put("statements", obj);
			RestTemplate restTemplate = new RestTemplate();
			logger.info(map.toString());
			String url="http://itds-script-02:7474/db/data/transaction";
			//result =  RestUtilApplicationList.post(url,String.class, map);
			result =  ApplicationListDAO.getapplicationlist();
			//System.out.println("done");
			}
		catch (Exception e) {
			
		}
		return result;
	}
}

