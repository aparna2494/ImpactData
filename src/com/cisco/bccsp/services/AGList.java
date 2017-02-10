package com.cisco.bccsp.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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

import com.cisco.bccsp.error.ServiceError;
import com.cisco.bccsp.util.ldap.LDAPUtil;
import com.cisco.bccsp.db.dao.AGListDAO;
import com.cisco.bccsp.db.util.BCCException;

@Path("AGList")
public class AGList {
	 private static Logger logger = Logger.getLogger(
			 AGList.class.getName());
	 @Context HttpServletRequest servletRequest;  
	
	public AGList(){
	}
    @GET
	@Path("/aggroups")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAssignedGroup()
	{
			logger.info("getting AGgroups");
		 
       
		return AGListDAO.getAssignedGroup();
		
	}
 }
	

