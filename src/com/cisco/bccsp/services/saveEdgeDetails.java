package com.cisco.bccsp.services;

import java.util.ArrayList;
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

import com.cisco.bccsp.db.dao.NodeDetailsDAO;
import com.cisco.bccsp.model.Edge;

@Path("edgedetails")
public class saveEdgeDetails {

	
		 private static Logger logger = Logger.getLogger(
				 saveEdgeDetails.class.getName());
		 @Context HttpServletRequest servletRequest;  
		public saveEdgeDetails() {
			
		}

		@POST
		@Path("/save")
		@Consumes(MediaType.APPLICATION_JSON)
		public Map<String, String> saveCallDetails( Edge edge)
		{
			return NodeDetailsDAO.saveedgedetails(edge);
			
		}
		
		
	}

