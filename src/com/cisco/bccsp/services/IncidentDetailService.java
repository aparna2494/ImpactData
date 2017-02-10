package com.cisco.bccsp.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import com.cisco.bccsp.db.dao.RemedyDAO;
import com.cisco.bccsp.model.IncidentDetails;


@Path("incidentdetail")
public class IncidentDetailService {
	 private static Logger logger = Logger.getLogger(
			 OpenIncidentDetailsService.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public IncidentDetailService() {
		
	}

	
	@GET
	@Path("/{incident_number}")
	@Produces(MediaType.APPLICATION_JSON)
	public IncidentDetails getCpeDetailsList(@PathParam("incident_number") String incident_number)
	{
			logger.info("getting  open incident list");
		 
       
		return RemedyDAO.getIncidentDetails(incident_number);
		
	}
	
}

