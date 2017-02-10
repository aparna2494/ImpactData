package com.cisco.bccsp.model;

public class CallDetails {

	private String callUserID="";
	private String affectedUserID="";
	private String incidentNumber="";
	private String incidentExist="";
	
	private String reason="";
	private String resolution="";
	private String startTime="";
	private String endTime="";
	private String createdUser="";
	private String createdDate="";
	public String getCallUserID() {
		return callUserID;
	}
	public void setCallUserID(String callUserID) {
		this.callUserID = callUserID;
	}
	public String getAffectedUserID() {
		return affectedUserID;
	}
	public void setAffectedUserID(String affectedUserID) {
		this.affectedUserID = affectedUserID;
	}
	public String getIncidentNumber() {
		return incidentNumber;
	}
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	public String getIncidentExist() {
		return incidentExist;
	}
	public void setIncidentExist(String incidentExist) {
		this.incidentExist = incidentExist;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
}
