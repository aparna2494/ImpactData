package com.cisco.bccsp.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.dao.AGListDAO;
import com.cisco.bccsp.db.dao.RosterDAO;
import com.cisco.bccsp.model.Roster;


@Path("roster")
public class RosterService {
	
    private static Logger logger = Logger.getLogger(
      RosterService.class.getName());
    
    @Context HttpServletRequest servletRequest;    
    
    public RosterService() {
    	
    }
    
	
    @GET
	@Path("/agList")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAGs()
	{
		 
       
		return RosterDAO.getUniqueAGs();
		
	}
    
    @GET
	@Path("/applicationList")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApplication()
	{
		 
       
		return RosterDAO.getUniqueApplication();
		
	}
	
  
	
	@POST
	@Path("/receiveRoster")
	@Consumes(MediaType.APPLICATION_JSON)
	public void receiveRoster(String roster)
	{
		if(!RosterDAO.insertRoster(roster))
			logger.warn("Roster insert not successful");
	}
	
	
	@POST
	@Path("/getRoster")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Document> getRoster(Document trackName)
	{
		if(trackName.containsKey("application")) {
			return RosterDAO.getUsersForField("application", trackName.getString("application"), trackName.getString("fromDate"), trackName.getString("toDate"));
		}
		else if (trackName.containsKey("AG")) {
			return RosterDAO.getUsersForField("AG", trackName.getString("AG"), trackName.getString("fromDate"), trackName.getString("toDate"));
		}
		
		return new ArrayList<Document>();
    }
	
    @POST
	@Path("/dateList")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getDates(Document trackName)
	{
		 
    	if(trackName.containsKey("application")) {
			return RosterDAO.getDates("application", trackName.getString("application"));
		}
		else if (trackName.containsKey("AG")) {
			return RosterDAO.getDates("AG", trackName.getString("AG"));
		}
    	return new String();
	}
    
	@POST
	@Path("/getRosterDate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Document> getRosterDate(Document trackName)
	{
		if(trackName.containsKey("application")) {
			return RosterDAO.getUsers("application", trackName.getString("application"), trackName.getString("Date"));
		}
		else if (trackName.containsKey("AG")) {
			return RosterDAO.getUsers("AG", trackName.getString("AG"), trackName.getString("Date"));
		}
		
		return new ArrayList<Document>();
    }
	

}
