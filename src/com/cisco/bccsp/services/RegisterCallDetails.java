package com.cisco.bccsp.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.cisco.bccsp.db.dao.CallDetailsDAO;
import com.cisco.bccsp.model.CallDetails;


@Path("calldetails")
public class RegisterCallDetails {

	
		 private static Logger logger = Logger.getLogger(
				 CpeDetailsService.class.getName());
		 @Context HttpServletRequest servletRequest;  
		public RegisterCallDetails() {
			
		}

		@POST
		@Path("/save")
		@Consumes(MediaType.APPLICATION_JSON)
		public Map<String, String> saveCallDetails( CallDetails calldetail)
		{logger.info(calldetail);
		String whoIsIt = "";
		logger.info(calldetail.getEndTime());
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		Long unixTime = System.currentTimeMillis() / 1000L;
		String time = Objects.toString(unixTime, null);
		calldetail.setCreatedDate(time);
		calldetail.setCreatedUser(whoIsIt);
		
				logger.info("saving the call details list");
			 
	      
		return CallDetailsDAO.savecalldetails(calldetail);
			
		}
		
		
	}

