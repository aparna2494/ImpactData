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



import com.cisco.bccsp.db.dao.CpeDetailsDAO;
import com.cisco.bccsp.model.CpeDetails;


@Path("cpedetails")
public class CpeDetailsService {
	 private static Logger logger = Logger.getLogger(
			 CpeDetailsService.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public CpeDetailsService() {
		
	}

	
	@GET
	@Path("/{entity}/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CpeDetails> getCpeDetailsList(@PathParam("entity") String entity,@PathParam("category") String category)
	{
			logger.info("getting  cpe details list");
		 
       
		return CpeDetailsDAO.getCpeDetails(entity, category);
		
	}
	
}

