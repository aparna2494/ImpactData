package com.cisco.bccsp.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "chat")
@XmlType(propOrder = { "chatId","chatSubjectId","appId", "text", "initUser", "userList", 
  "startTime", "endTime", "event", "from", "chatActive", "user", "userType"})

public class Chat {
	
	private String chatId = "";
	private String chatSubjectId = "";
	private String appId = "";
	private String text = "";
	private String initUser = "";
	private List<String> userList = null;
	private Date startTime = new Date();
	private Date endTime = null;
	private String event = null;
	private String from = null;
	private String chatActive = null;
	private String user = null;
	private String userType = null;
	
	@XmlElement	
	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	
	@XmlElement	
	public String getChatSubjectId() {
		return chatSubjectId;
	}

	public void setChatSubjectId(String chatSubjectId) {
		this.chatSubjectId = chatSubjectId;
	}
	
	@XmlElement	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@XmlElement	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@XmlElement	
	public String getInitUser() {
		return initUser;
	}

	public void setInitUser(String initUser) {
		this.initUser = initUser;
	}
	
	@XmlElement	
	public List<String> getUserList() {
		return userList;
	}

	@XmlElement	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@XmlElement	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@XmlElement	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	@XmlElement	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	@XmlElement	
	public String getChatActive() {
		return chatActive;
	}

	public void setChatActive(String chatActive) {
		this.chatActive = chatActive;
	}
	
	@XmlElement	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	@XmlElement	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
