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
import com.cisco.bccsp.util.GetRestUtil;


@Path("cep")
public class CepService {
	 private static Logger logger = Logger.getLogger(
			 CepService.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public CepService() {
		
	}

	
	@GET
	@Path("/{incident_number}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCpeDetailsList(@PathParam("incident_number") String incident_number)
	{
			logger.info("getting  cep_flag");
		 
			
		return GetRestUtil.get("http://vm-gse-analytics:8080/cepAPI/rest/"+incident_number);
		
	}
	
}

