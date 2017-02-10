package com.cisco.bccsp.services;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.cisco.bccsp.db.dao.FlowDetailsDAO;
import com.cisco.bccsp.db.dao.NodeDetailsDAO;
import com.cisco.bccsp.model.Flow;
import com.cisco.bccsp.model.Node;
import com.cisco.bccsp.model.NodeModel;


@Path("saveflow")
public class saveFlow {

	
		 private static Logger logger = Logger.getLogger(
				 saveFlow.class.getName());
		 @Context HttpServletRequest servletRequest;  
		public saveFlow() {
			
		}

		@PUT
		@Path("/save")
		@Consumes(MediaType.APPLICATION_JSON)
		public Map<String, String> saveDetails( Flow flow)
		{
			return FlowDetailsDAO.saveflowdetails(flow,"bp_flow_neo_data_stage");
			
		}
		
		@PUT
		@Path("/publish")
		@Consumes(MediaType.APPLICATION_JSON)
		public Map<String, String> publishDetails( Flow flow)
		{
			return FlowDetailsDAO.saveflowdetails(flow,"bp_flow_neo_data");
			
		}
		
	}