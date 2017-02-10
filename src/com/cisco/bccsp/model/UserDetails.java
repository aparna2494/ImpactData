package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userdetails")
public class UserDetails {

	
	
	private String userFullName="";
	private String userJobTitle="";
	private long userOpenCasesCount=0;
	private long userOpenEscalationsCount=0;
	private long userTotalCasesCount=0;
	private long  userTotalEscalationsCount=0;
	private String userLocation="";
	
	@XmlElement
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	
	@XmlElement
	public String getUserJobTitle() {
		return userJobTitle;
	}
	public void setUserJobTitle(String userJobTitle) {
		this.userJobTitle = userJobTitle;
	}
	
	@XmlElement
	public long getUserOpenCasesCount() {
		return userOpenCasesCount;
	}
	public void setUserOpenCasesCount(long userOpenCasesCount) {
		this.userOpenCasesCount = userOpenCasesCount;
	}
	
	@XmlElement
	public long getUserOpenEscalationsCount() {
		return userOpenEscalationsCount;
	}
	public void setUserOpenEscalationsCount(long userOpenEscalationsCount) {
		this.userOpenEscalationsCount = userOpenEscalationsCount;
	}
	
	@XmlElement
	public long getUserTotalCasesCount() {
		return userTotalCasesCount;
	}
	public void setUserTotalCasesCount(long userTotalCasesCount) {
		this.userTotalCasesCount = userTotalCasesCount;
	}
	
	@XmlElement
	public long getUserTotalEscalationsCount() {
		return userTotalEscalationsCount;
	}
	public void setUserTotalEscalationsCount(long userTotalEscalationsCount) {
		this.userTotalEscalationsCount = userTotalEscalationsCount;
	}
	
	@XmlElement
	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	

}