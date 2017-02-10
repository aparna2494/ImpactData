package com.cisco.bccsp.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EMailUtil {
	
    private static Logger logger = Logger.getLogger(
    		EMailUtil.class.getName());
	
	public static void send(String to, String messageSubject,
	  String messageBody) {
		
	      String from = "caseimpact-tool@cisco.com";
	      String host = "outbound.cisco.com";
	      //String to = "ckoyi@cisco.com";
	      String cc = "gsqs_q2fy16_ce_normalization@cisco.com,cepmo_normalization_q2fy16@external.cisco.com";
	      if(to.equals("ckoyi@cisco.com")||to.equals("jhasting@cisco.com")||to.equals("anraju@cisco.com"))
	    		  {
	    	  			cc = to;
	    		  }
	    		  

	      //Get the session object  
	      Properties properties = System.getProperties();  
	      properties.setProperty("mail.smtp.host", host);  
	      Session session = Session.getDefaultInstance(properties);  
	  
	     //compose the message  
	      try{  
	         MimeMessage message = new MimeMessage(session);  
	         message.setFrom(new InternetAddress(from));  
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
	         message.addRecipients(Message.RecipientType.CC,InternetAddress.parse(cc));  
	         message.setSubject(messageSubject);  
	         message.setContent(messageBody, "text/html; charset=utf-8");
	         
	  
	         // Send message  
	         Transport.send(message);  
	         logger.info("message sent successfully....");  
	  
	      }catch (MessagingException mex) {mex.printStackTrace();} 		
		
		
	}

}
