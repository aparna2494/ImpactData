package com.cisco.bccsp.services;

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

import com.cisco.bccsp.error.ServiceError;
import com.cisco.bccsp.db.dao.UserDAO;
import com.cisco.bccsp.model.User;
import com.cisco.bccsp.util.ldap.LDAPUtil;

@Path("userinfo")
public class UserInfo {
	 private static Logger logger = Logger.getLogger(
			 UserInfo.class.getName());
	 @Context HttpServletRequest servletRequest;  
	public UserInfo() {
		
	}
	@GET
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@PathParam("userid") String userId) {

	Object responseObj = null;
	Status status = Status.UNAUTHORIZED;
	User user = null;

	HttpServletRequest request = null;
	// Create session to manage user login
	//HttpSession session = request.getSession(true);
	//String sessionid = session.getId();

	// Get userid from JSON passed
	//String userId = login.getUserid();

	//logger.info("sessionid: " + sessionid + "', userId: " + userId);

	/*if ((login != null) && (login.getUserid() != null)
	&& (login.getPassword() != null)
	&& LDAPUtil.login(userId, login.getPassword())) {



	}*/

	try
	{
	if (userId != null) {

	user = LDAPUtil.getLDAPPerson(userId);
	user.setStatus(true);


	}
	} 
	catch(Exception e)
	{
	user =new User();
	user.setStatus(false);
	}

	return user;
	}
	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserbyUserID() {
		
		Object responseObj = null;
		Status status = Status.UNAUTHORIZED;
		User user = null;
		String whoIsIt = "";

		if (servletRequest.getHeader("AUTH_USER") != null) {
			whoIsIt = servletRequest.getHeader("AUTH_USER");
		} else {
			whoIsIt = servletRequest.getHeader("TEST_USER");
		}
		if (whoIsIt != null) {
			user = UserDAO.getUserbyUserID(whoIsIt);
		
		if (user != null) {

			responseObj = user;
			
			if (responseObj != null) {
				status = Status.OK;
				UserDAO.registerLogin(whoIsIt);
			}

		}
	}
		if (responseObj == null) {

			// catch the case where resource is not present/active in the DB
			String serviceError = "NORESOURCE";
			String serviceFriendlyMessage = "You are currently not registered to use this application";
			String serviceTechnicalMessage = "Resource is not present or not active";

			responseObj = new ServiceError(serviceError,
					serviceFriendlyMessage, serviceTechnicalMessage, null);
		}

		return Response.status(status).type(MediaType.APPLICATION_JSON)
				.entity(responseObj).build();
	}
	
}

