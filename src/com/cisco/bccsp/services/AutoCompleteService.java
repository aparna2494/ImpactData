package com.cisco.bccsp.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.cisco.bccsp.db.dao.AutoCompleteDAO;

import com.cisco.bccsp.model.AutoComplete;


@Path("autocomplete_list")
public class AutoCompleteService {
	 private static Logger logger = Logger.getLogger(
			 AutoCompleteService.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public AutoCompleteService() {
		
	}

	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<AutoComplete> getAutoCompleteList()
	{
			logger.info("getting autocomplete list");
		 
       
		return AutoCompleteDAO.getIncidents();
		
	}
	
}
