package com.cisco.bccsp.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

@Provider
public class RequestFilter implements ContainerRequestFilter {
	
	private static final ServerResponse ACCESS_DENIED = 
	  new ServerResponse("Access denied for this resource", 401, 
        new Headers<Object>());
	
    private static Logger logger = Logger.getLogger(RequestFilter.class.getName());

    @Context HttpServletRequest servletRequest;
    
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if (servletRequest.getHeader("AUTH_USER") == null && servletRequest.getHeader("TEST_USER") == null) {
			requestContext.abortWith(ACCESS_DENIED);
            return;
		}
		
	    String user = servletRequest.getHeader("AUTH_USER");
	    if (user == null) {
	    	user = servletRequest.getHeader("TEST_USER");
	    }
		
		
		logger.info("Auth User :" + user);
		
		// log all PUT and POST requests that are JSON
	    if (("POST".equals(requestContext.getMethod()) || 
	       "PUT".equals(requestContext.getMethod())) &&
	      MediaType.APPLICATION_JSON_TYPE.getSubtype().equals(
	        requestContext.getMediaType().getSubtype()) &&
	      requestContext.getEntityStream() != null ) {
			
	    	StopWatch stopWatch = new StopWatch();
	    	stopWatch.start();

	    	String method = requestContext.getMethod();
			UriInfo uriInfo = requestContext.getUriInfo();
			String path = uriInfo.getPath();
		
			ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
			Method invokedMethod = methodInvoker.getMethod();
			
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(requestContext.getEntityStream(), baos);
            byte[] bytes = baos.toByteArray();
            requestContext.setEntityStream(new ByteArrayInputStream(bytes));
            
            stopWatch.stop();
            
            logger.info("User: " + user + ", Request Method: " + method +
                    ", Java Method: " + invokedMethod.getName() + 
                    ", Path: " + path + ", Time: " + stopWatch.getTime() +
                    ", Payload:\n" +  new String(bytes, "UTF-8"));
            
	    }     
	    
		
	}
	
}
