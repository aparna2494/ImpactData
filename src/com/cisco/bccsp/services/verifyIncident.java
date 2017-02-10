package com.cisco.bccsp.services;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.cisco.bccsp.util.ldap.LDAPUtil;




import com.cisco.bccsp.db.dao.CpeDetailsDAO;
import com.cisco.bccsp.db.dao.RemedyDAO;
import com.cisco.bccsp.model.CpeDetails;
import com.cisco.bccsp.model.Login;
import com.cisco.bccsp.model.User;

@Path("verifyincident")
public class verifyIncident {
	 private static Logger logger = Logger.getLogger(
			 verifyIncident.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public verifyIncident() {
		
	}

	
	@GET
	@Path("/{incident_number}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@PathParam("incident_number") String incident_number) {
		logger.info("verifying incident");
		return RemedyDAO.getStatus(incident_number);
	
	}

	
}

