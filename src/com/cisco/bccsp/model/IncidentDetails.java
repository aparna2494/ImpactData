package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "incidentdetails")
@XmlType(propOrder = { "incidentNumber", "cet_value", 
		  "incidentNotes", "reported_date", "resolved_date", 
		  "status", "ag", "assignee", 
		  "description", "age", "application", "appId", "notes"})
public class IncidentDetails {

	
	private String incidentNumber = "";
	private String cet_value="";
	private String incidentNotes="";
	private String reported_date="";
	private String resolved_date="";
	
	private String status="";
	private String AG="";
	private String assignee="";
	private String description="";
	private String age="";
	private String application="";
	private int appId = 0;
	private String notes;
	
	@XmlElement
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@XmlElement
	public String getAG() {
		return AG;
	}
	public void setAG(String aG) {
		AG = aG;
	}
	@XmlElement
	public String getResolved_date() {
		return resolved_date;
	}
	public void setResolved_date(String resolved_date) {
		this.resolved_date = resolved_date;
	}
	@XmlElement	
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
	@XmlElement	
	public String getIncidentNumber() {
		return incidentNumber;
	}
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	
	@XmlElement	
	public String getCet_value() {
		return cet_value;
	}
	public void setCet_value(String cet_value) {
		this.cet_value = cet_value;
	}
	
	@XmlElement	
	public String getIncidentNotes() {
		return incidentNotes;
	}
	public void setIncidentNotes(String incidentNotes) {
		this.incidentNotes = incidentNotes;
	}
	
	@XmlElement	
	public String getReported_date() {
		return reported_date;
	}
	public void setReported_date(String reported_date) {
		this.reported_date = reported_date;
	}
	
	@XmlElement	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	
	@XmlElement	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElement	
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	
	@XmlElement	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	
	@XmlElement	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}
