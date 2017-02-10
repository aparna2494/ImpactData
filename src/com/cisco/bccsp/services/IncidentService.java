package com.cisco.bccsp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cisco.bccsp.action.bo.IncidentActionBO;
import com.cisco.bccsp.db.dao.AutoCompleteDAO;
import com.cisco.bccsp.db.dao.IncidentDAO;
import com.cisco.bccsp.db.dao.RemedyDAO;
import com.cisco.bccsp.model.AutoComplete;
import com.cisco.bccsp.model.Incident;
import com.cisco.bccsp.model.IncidentDetails;
import com.cisco.bccsp.model.RemedyIncident;
import com.cisco.bccsp.util.RestUtil;


@Path("incident")
public class IncidentService {
	
    private static Logger logger = Logger.getLogger(
      IncidentService.class.getName());

    private IncidentActionBO incidentActionBO = null;
    
    @Context HttpServletRequest servletRequest;    
    
    public IncidentService() {
    	incidentActionBO = new IncidentActionBO();  	
    }
    
	@GET
	@Path("/details/{incidentNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getIncidentDetails(@PathParam("incidentNumber") String incidentNumber)
	{
			logger.info("getting incident details");
		 
       
		return IncidentDAO.getIncidentDetails(incidentNumber);
		
	}
	
	@GET
	@Path("/workinfo/{incidentNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getIncidentWLDetails(@PathParam("incidentNumber") String incidentNumber)
	{
			logger.info("getting incident details");
		 
       
		return IncidentDAO.getIncidentWLDetails(incidentNumber);
		
	}
	
	@GET
	@Path("/rkm/{keyword}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRKMDetails(@PathParam("keyword") String keyword)
	{
			logger.info("getting RKM details");
		 
       
		return IncidentDAO.getRKMDetails(keyword);
		
	}
    
    @GET
	@Path("/autocomplete_list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<AutoComplete> getAutoCompleteList()
	{
			logger.info("getting  autocomplete list");
		 
       
		return AutoCompleteDAO.getIncidents();
		
	}
	
 
    /**
     *  getRemedyIncident
     *  
     *  REST call to get a specific remedy incident
     * 
     * @param incidentNumber
     * @return
     */
	@GET
	@Path("/remedy/{incidentNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public RemedyIncident getRemedyIncident( 
	  @PathParam("incidentNumber") String incidentNumber) {
		
		logger.info("getting remedy incident: " + incidentNumber); 
       
		return RemedyDAO.getIncident(incidentNumber);
		
	}
	
    /**
     *  getIncident
     *  
     *  REST call to get a specific incident
     * 
     * @param incidentNumber
     * @return
     */
	@GET
	@Path("/{incidentNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Incident getIncident( 
	  @PathParam("incidentNumber") String incidentNumber) {

        logger.info("getting incident: " + incidentNumber);                            	            	

		return IncidentDAO.getIncident(incidentNumber);
	}
	
	
	
	/**
	 * getIncidents
	 * 
	 * REST call to get a list of incidents, if a filter is present it will
	 * be used to reduced the results.
	 * 
	 * @param searchFilter
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Incident> getIncidents(
	  @DefaultValue("ALL") @QueryParam("searchFilter") String searchFilter) {

        logger.info("getting incidents");                            	            	
		
		return IncidentDAO.getIncidents(searchFilter);
		
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateIncident(IncidentDetails incident) {
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		String whoIsIt = "";
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}

		String response = IncidentDAO.updateIncident(incident);
		
		stopWatch.stop();
		
		logger.info("incident update took: " + stopWatch.getTime());
		
        return response;

	}

	
	/**
	 * addIncident
	 * 
	 * REST call to add an incident to the data base.  The incident will
	 * be provided by the posted JSON object.
	 * 
	 * @param incident
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIncident(Incident incident) {
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		String whoIsIt = "";
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		
		
		Status status = Status.BAD_REQUEST;

		Object entity = incidentActionBO.insertIncident(whoIsIt, incident);
        
		if (entity instanceof Incident) {
			status = Status.CREATED;
		}
		
		stopWatch.stop();
		
		logger.info("incident insert took: " + stopWatch.getTime());
		
        return Response.status(status).type(MediaType.APPLICATION_JSON)
          .entity(entity).build();

	}

	
	
	/**
	 * updateIncident
	 * 
	 * REST call to update an incident to the database.  The incident will
	 * be provided by the posted JSON object.
	 * 
	 * @param incident
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateIncident(Incident incident) {
		
        StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		String whoIsIt = "";
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		
		
		Status status = Status.BAD_REQUEST;

		Object entity = incidentActionBO.updateIncident(whoIsIt, incident);
        
		if (entity instanceof Incident) {
			status = Status.OK;
		}
		
		stopWatch.stop();
		
		logger.info("incident update took: " + stopWatch.getTime());
		
        return Response.status(status).type(MediaType.APPLICATION_JSON)
          .entity(entity).build();

	}	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public String createIncident(String json)  {

		logger.info("creating incident");                            	            	
		String whoIsIt = "";
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		
		return RemedyDAO.createIncident(whoIsIt, json);
		
	}
	
	
}
