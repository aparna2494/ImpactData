package com.cisco.bccsp.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;


import com.cisco.bccsp.util.ldap.LDAPUtil;

import com.cisco.bccsp.model.groupMemberLDAP;

@Path("ldap")
public class getGroupMembers {
	
    private static Logger logger = Logger.getLogger(
    		getGroupMembers.class.getName());

    	    
    	    @Context HttpServletRequest servletRequest;    
    	    
    	    public getGroupMembers() {
    	    	
    	    }
    	    
    		@POST
    		@Path("/details")
    		@Consumes(MediaType.APPLICATION_JSON)
    		@Produces(MediaType.APPLICATION_JSON)
    		public List<groupMemberLDAP> getGroupDetails(groupMemberLDAP grpper)
    		{
    				logger.info("getting LDAP details");
    			 
    	       
    			return LDAPUtil.getGroupDetails(grpper);
    			
    		}


}

