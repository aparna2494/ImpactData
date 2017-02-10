package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "login")
@XmlType(propOrder={"userid","password", "rememberme", "trackId", 
	"timezoneId"})

public class Login {
	
	private String userid;
	private String password;
	private boolean rememberme;
	private long trackId = -999;
	private long timezoneId = -999;
	
	public Login() {
		
	}
	
	@XmlElement
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public boolean isRememberme() {
		return rememberme;
	}

	public void setRememberme(boolean rememberme) {
		this.rememberme = rememberme;
	}


	public long getTrackId() {
		return trackId;
	}

	public void setTrackId(long trackId) {
		this.trackId = trackId;
	}

	public long getTimezoneId() {
		return timezoneId;
	}

	public void setTimezoneId(long timezoneId) {
		this.timezoneId = timezoneId;
	}

	
	
}