package com.cisco.bccsp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cisco.bccsp.db.dao.ChatDao;
import com.cisco.bccsp.db.dao.UserDetailsDAO;
import com.cisco.bccsp.model.Chat;
import com.cisco.bccsp.model.Incident;
import com.cisco.bccsp.model.IncidentDetails;
import com.cisco.bccsp.model.UserDetails;
import com.cisco.bccsp.util.RestUtil;


@Path("chat")
public class ChatService {
	
    private static Logger logger = Logger.getLogger(
      ChatService.class.getName());
    
    @Context HttpServletRequest servletRequest;    
    
    public ChatService() {
    	
    }
    
    @GET
	@Path("/{chatId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getChat(@PathParam("chatId") String chatId)
	{
			logger.info("getting chat details");
		 
       
		return ChatDao.getChat(chatId);
		
	}
    
    @GET
	@Path("/active/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getActiveChatsByUser(@PathParam("userId") String userId)
	{
			logger.info("getting chat details");
		 
       
		return ChatDao.getActiveChatsByUser(userId);
		
	}
    
    @GET
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	public String getActiveChats()
	{
			logger.info("getting active chat details");
		 
       
		return ChatDao.getActiveChats();
		
	}
		
	/**
	 * startchat
	 * 
	 * REST call to start a chat and log it in the database. The chat will
	 * be provided by the posted JSON object.
	 * 
	 */
	@POST
	@Path("/start")
	@Consumes(MediaType.APPLICATION_JSON)
	public String startChat(Chat chat) {
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		String whoIsIt = "";
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}

		String result = ChatDao.startChat(chat);
		
		stopWatch.stop();
		
		logger.info("logging the chat took: " + stopWatch.getTime());
		logger.info("response: " + result);
		
        return result;

	}

	
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateChat(Chat chat) {
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		String whoIsIt = "";
		
		if (servletRequest.getHeader("AUTH_USER") != null){
	        whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else	{
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		
		
		Status status = Status.BAD_REQUEST;

		Object entity = ChatDao.updateChat(chat);
        
		if (entity instanceof Chat) {
			status = Status.CREATED;
		}
		
		stopWatch.stop();
		
		logger.info("logging the chat took: " + stopWatch.getTime());
		
        return Response.status(status).type(MediaType.APPLICATION_JSON)
          .entity(entity).build();

	}	
	
	@POST
	@Path("/currentAvailability")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getRosterAvailablity(IncidentDetails incident)
	{
			logger.info("getting roster availability");
		 
       
		return ChatDao.getRosterAvailability(incident);
		
	}
	
	@POST
	@Path("/applicationAvailability")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApplicationAvailablity(IncidentDetails incident)
	{
			logger.info("getting application availability");
		 
       
		return ChatDao.getApplicationAvailability(incident);
		
	}
	
	@POST
	@Path("/agAvailablity")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgAvailability(IncidentDetails incident)
	{
			logger.info("getting AG roster");
		 
       
		return ChatDao.getAGUsersAvailability(incident);
		
	}
}
