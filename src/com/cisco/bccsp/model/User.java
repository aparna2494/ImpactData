package com.cisco.bccsp.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "user")
@XmlType(propOrder={"userId","fullName","activeFlag","createDate","createUser","updateDate","updateUser",
		"userCookie", "accessLvlId", "lastLogin","description","userId","title","location"})

public class User {
	
	private String userId = null;
	private String managerId = null;
	private String fullName = null;
	private String activeFlag = null;
	private Date createDate = null;
	private String createUser = null;
	private Date updateDate = null;
	private String updateUser = null;
	private long accessLvlId = -999;
	private Date lastLogin = null;
	private String description = null;

	private String title = null;

	private String location = null;
	private boolean status;
	public User() {		
	}
	@XmlElement
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@XmlElement
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@XmlElement
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@XmlElement
	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	
	@XmlElement
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@XmlElement
	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	@XmlElement
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@XmlElement
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@XmlElement
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@XmlElement
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	@XmlElement
	public long getAccessLvlId() {
		return accessLvlId;
	}

	public void setAccessLvlId(long accessLvlId) {
		this.accessLvlId = accessLvlId;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", fullName=" + fullName
				+ ", activeFlag=" + activeFlag + ", createDate=" + createDate
				+ ", createUser=" + createUser + ", updateDate=" + updateDate
				+ ", updateUser=" + updateUser + ", accessLvlId=" + accessLvlId 
				+ ", lastLogin=" + lastLogin + "]";
	}	
	
}
