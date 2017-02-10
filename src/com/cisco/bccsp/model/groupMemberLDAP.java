package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "groupPermission")
public class groupMemberLDAP {

	private Boolean chatWithIncident;
	private Boolean chatWithoutIncident;
	private Boolean callSEC;
	private Boolean createCase;
	private String userId;
	private String memberOf;
	private String userID="";
	private String userName="";
	private String title="";
	private Boolean conflict=false;
	
	@XmlElement
	public Boolean getConflict() {
		return conflict;
	}
	public void setConflict(Boolean conflict) {
		this.conflict = conflict;
	}
	
	@XmlElement
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	@XmlElement
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@XmlElement
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@XmlElement
	public String getMemberOf() {
		return memberOf;
	}
	public void setMemberOf(String memberOf) {
		this.memberOf = memberOf;
	}
	@XmlElement
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@XmlElement
	public Boolean getChatWithIncident() {
		return chatWithIncident;
	}
	public void setChatWithIncident(Boolean chatWithIncident) {
		this.chatWithIncident = chatWithIncident;
	}
	@XmlElement
	public Boolean getChatWithoutIncident() {
		return chatWithoutIncident;
	}
	public void setChatWithoutIncident(Boolean chatWithoutIncident) {
		this.chatWithoutIncident = chatWithoutIncident;
	}
	@XmlElement
	public Boolean getCallSEC() {
		return callSEC;
	}
	public void setCallSEC(Boolean callSEC) {
		this.callSEC = callSEC;
	}
	@XmlElement
	public Boolean getCreateCase() {
		return createCase;
	}
	public void setCreateCase(Boolean createCase) {
		this.createCase = createCase;
	}
}
