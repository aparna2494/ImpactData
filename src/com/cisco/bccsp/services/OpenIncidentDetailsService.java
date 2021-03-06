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





import com.cisco.bccsp.db.dao.OpenIncidentDetailsDAO;
import com.cisco.bccsp.model.IncidentDetails;


@Path("openincidentdetails")
public class OpenIncidentDetailsService {
	 private static Logger logger = Logger.getLogger(
			 OpenIncidentDetailsService.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public OpenIncidentDetailsService() {
		
	}

	
	@GET
	@Path("/{cpe_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<IncidentDetails> getCpeDetailsList(@PathParam("cpe_name") String cpe_name)
	{
			logger.info("getting  open incident list");
		 
       
		return OpenIncidentDetailsDAO.getOpenIncidentsDetails(cpe_name);
		
	}
	
}

