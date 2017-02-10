package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cpedetails")
public class CpeDetails {

	
	private String incidentNumber = "";
	private String customername="";
	private String customercec="";
	private String country="";
	private String job_title="";
	private String description="";
	@XmlElement	
	public String getCustomercec() {
		return customercec;
	}
	public void setCustomercec(String customercec) {
		this.customercec = customercec;
	}
	
	@XmlElement	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@XmlElement	
	public String getJob_title() {
		return job_title;
	}
	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}
	
	@XmlElement	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@XmlElement	
	public String getIncidentNumber() {
		return incidentNumber;
	}
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	
	@XmlElement	
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
}