package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "incidentdetails")
public class Timeline {

	private String activity="";
	private String activityID="";
	private String activityStatus="";
	private String activitytime="";
	private String customClass="";
	
	@XmlElement
	public String getCustomClass() {
		return customClass;
	}
	public void setCustomClass(String customClass) {
		this.customClass = customClass;
	}
	@XmlElement
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	@XmlElement
	public String getActivityID() {
		return activityID;
	}
	public void setActivityID(String activityID) {
		this.activityID = activityID;
	}
	
	@XmlElement
	public String getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	@XmlElement
	public String getActivitytime() {
		return activitytime;
	}
	public void setActivitytime(String activitytime) {
		this.activitytime = activitytime;
	}
}
