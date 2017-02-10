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
import com.cisco.bccsp.model.KPIModel;
import com.cisco.bccsp.model.Node;
import com.cisco.bccsp.model.NodeModel;


@Path("nodedetails")
public class saveNodeDetails {

	
		 private static Logger logger = Logger.getLogger(
				 saveNodeDetails.class.getName());
		 @Context HttpServletRequest servletRequest;  
		public saveNodeDetails() {
			
		}

		@POST
		@Path("/save")
		@Consumes(MediaType.APPLICATION_JSON)
		public Map<String, String> saveCallDetails( Node node)
		{
			return NodeDetailsDAO.savenodedetails(node);
			
		}
		@POST
		@Path("/update")
		@Consumes(MediaType.APPLICATION_JSON)
		public Map<String, String> updateNodeDetails( Node node)
		{
			return NodeDetailsDAO.updatenodedetails(node);
			
		}
		@POST
		@Path("/savekpi")
		@Consumes(MediaType.APPLICATION_JSON)
		public Map<String, String> savekpiDetails( NodeModel node)
		{
			return NodeDetailsDAO.savekpidetails(node);
			
		}
		@GET
		@Path("/fetchnodeID")
		@Produces(MediaType.TEXT_PLAIN)
		public String FetchMaxID()
		{
					
			return NodeDetailsDAO.fetchmaxID();
			
		}
		
	}

