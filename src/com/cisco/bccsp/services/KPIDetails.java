package com.cisco.bccsp.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.dao.AGListDAO;
import com.cisco.bccsp.db.dao.KPIDetailsDAO;
import com.cisco.bccsp.db.dao.RosterDAO;
@Path("KPI")
public class KPIDetails {
	
	 private static Logger logger = Logger.getLogger(
			 KPIDetails.class.getName());

	    
	    @Context HttpServletRequest servletRequest;    
	    
	    public KPIDetails() {		
	    }
	    
	    @POST
		@Path("/tracks")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public String getUniqueTracks(Document collectionName)
		{
				logger.info("getting component name");
			 
	       
			return KPIDetailsDAO.getUniqueTracks(collectionName.getString("collection"));
			
		}
	    
	    @GET
		@Path("/DBnames")
		@Produces(MediaType.APPLICATION_JSON)
		public String getDB()
		{
				logger.info("getting db");
			 
	       
			return KPIDetailsDAO.getDB();
			
		}
	    
	        @POST
	 		@Path("/components")
	 		@Consumes(MediaType.APPLICATION_JSON)
	 		@Produces(MediaType.APPLICATION_JSON)
	 		public String getUniqueComponents(Document collectionName)
	 		{
	        	logger.info(collectionName);
	 			return KPIDetailsDAO.getUniqueComponents(collectionName.getString("collection"),collectionName.getString("track_name"));
	 		//	return "Hello";
	 	}
	        
	    @POST
	    @Path("/getUniqueKPIs")
	    @Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
	    public String getKPIs(Document kpiInfo){
	    	logger.info(kpiInfo);
	    	return KPIDetailsDAO.getUniqueKPI(kpiInfo.getString("collection"),kpiInfo.getString("track_name"),kpiInfo.getString("component_name"));
	    }

	    
	    @POST
		@Path("/getKPIdetails")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
	   	public String getKPIdetails(Document trackName)
		{
	    	logger.info(trackName);
	    	return KPIDetailsDAO.getKPIdetails(trackName.getString("track_name"),trackName.getString("collection"),trackName.getString("node_id"));
	        
	    }
	    

	    
	    @POST
		@Path("/updateKPIdetails")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
	   	public boolean updateKPIDetails(Document KPIdetails)
		{
	    	 return KPIDetailsDAO.updateKPIdetails(KPIdetails.getString("collection_name"),KPIdetails.getString("track_name"),KPIdetails.getString("component_name"),
	    			  KPIdetails.getString("kpi_name"),
	    			  KPIdetails.getInteger("critical"),KPIdetails.getInteger("high"),KPIdetails.getInteger("medium"),KPIdetails.getString("kpi_status"));
	    	  
	        
	    }
	    
	    @GET
		@Path("/KPImapping")
		@Produces(MediaType.APPLICATION_JSON)
	   	public List<Document> KPImapping()
		{
	    	 return KPIDetailsDAO.KPImapping();
	    	  
	        
	    }


	    
	    @POST
		@Path("/createKPI")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
	   	public boolean createKPI(Document KPIdetails)
		{
	    	logger.info(KPIdetails);
	    	 return KPIDetailsDAO.createKPI(KPIdetails.getString("collection_name"),KPIdetails.getString("track_name"),KPIdetails.getString("component_name"),
	    			 KPIdetails.getString("kpi_name"),
	    			 KPIdetails.getInteger("critical"),KPIdetails.getInteger("high"),KPIdetails.getInteger("medium"),KPIdetails.getString("kpi_status"),
	    			 KPIdetails.getString("DB_NAME"), KPIdetails.getString("query"), KPIdetails.getInteger("frequency"),KPIdetails.getBoolean("jobupdate"));

	    }
	    
	    @POST
		@Path("/updateQuery")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
	   	public boolean updateQuery(Document queryDetails){
	    	logger.info(queryDetails);

	    	return KPIDetailsDAO.updateJob(queryDetails.getString("kpi_name"), queryDetails.getString("component_name"), queryDetails.getString("track_name"),
	    			queryDetails.getString("query"));
	    			}
	    
	    
	    
	   
	    
	
}
