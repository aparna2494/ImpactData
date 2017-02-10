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



import com.cisco.bccsp.db.dao.TimelineActivityDAO;
import com.cisco.bccsp.model.Timeline;


@Path("timelinedetails")
public class timelineService {
	 private static Logger logger = Logger.getLogger(
			 CpeDetailsService.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public timelineService() {
		
	}

	
	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Timeline> getTimeLineList(@PathParam("userId") String userId)
	{
			logger.info("getting  cpe details list");
		 
       
		return TimelineActivityDAO.getTimelineDetails(userId);
		
	}
	
}

