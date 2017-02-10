package com.cisco.bccsp.error;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "serviceerror")
@XmlType(propOrder={"code", "friendlyMessage", "technicalMessage", "sourceObject"})

public class ServiceError {
	
	public static String NOAUTH = "NOAUTH";
	public static String UNKNOWN = "UNKNOWN";
	public static String DUP = "DUPLICATE";
	public static String BADTRX = "BAD_TRANSACTION";
	public static String SEVERE = "SEVERE";
	public static String WARN = "WARN";
	public static String INFO = "INFO";
	
	private String code;
	private String friendlyMessage;
	private String technicalMessage;
	private Object sourceObject;  // must be a serializable rest object

	public ServiceError() {
		this.code = "NOMSG";
		this.friendlyMessage = "no friendly message set";
		this.technicalMessage = "no technical message set";	
		this.sourceObject = null;
	}
	
	public ServiceError(String code, String friendlyMessage, String technicalMessage, Object sourceObject) {
		this.code = code;
		this.friendlyMessage = friendlyMessage;
		this.technicalMessage = technicalMessage;
		this.sourceObject = sourceObject;
	}

	@XmlElement
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@XmlElement
	public String getFriendlyMessage() {
		return friendlyMessage;
	}

	public void setFriendlyMessage(String friendlyMessage) {
		this.friendlyMessage = friendlyMessage;
	}

	@XmlElement
	public String getTechnicalMessage() {
		return technicalMessage;
	}

	public void setTechnicalMessage(String technicalMessage) {
		this.technicalMessage = technicalMessage;
	}	
	
	@XmlElement
	public Object getSourceObject() {
		return sourceObject;
	}

	public void setSourceObject(Object sourceObject) {
		this.sourceObject = sourceObject;
	}

	
}
