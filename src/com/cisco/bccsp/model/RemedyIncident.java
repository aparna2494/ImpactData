package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "remedyincident")
@XmlType(propOrder = { "incidentNumber", "ermoRelId", "status" })
public class RemedyIncident {
	
	private String incidentNumber = "";
	private String ermoRelId = "";
	private String status = "";

	@XmlElement		
	public String getIncidentNumber() {
		return incidentNumber;
	}
	
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	
	@XmlElement	
	public String getErmoRelId() {
		return ermoRelId;
	}
	
	public void setErmoRelId(String ermoRelId) {
		this.ermoRelId = ermoRelId;
	}
	
	@XmlElement	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
