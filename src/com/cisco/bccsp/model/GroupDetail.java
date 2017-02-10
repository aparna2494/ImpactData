package com.cisco.bccsp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "groupDetail")
public class GroupDetail {
	
	private List<groupMemberLDAP> groupPermission=null;
	private List<groupMemberLDAP> groupMember=null;
	private List<ApplicationList> applicationList=null;
	private List<String> remedy_AG=null;
	private String sync_type=null;
	private String mailerAD_name =null;
	private String groupName =null;
	private Double groupID;
	public Double getGroupID() {
		return groupID;
	}
	public void setGroupID(Double groupID) {
		this.groupID = groupID;
	}
	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
	private String groupAdmin=null;
	private int memberCount;
	public int getMemberCount() {
		return memberCount;
	}
	@XmlElement
	public String getGroupAdmin() {
		return groupAdmin;
	}
	public void setGroupAdmin(String groupAdmin) {
		this.groupAdmin = groupAdmin;
	}
	@XmlElement
	public List<groupMemberLDAP> getGroupPermission() {
		return groupPermission;
	}
	public void setGroupPermission(List<groupMemberLDAP> groupPermission) {
		this.groupPermission = groupPermission;
	}
	@XmlElement
	public List<groupMemberLDAP> getGroupMember() {
		return groupMember;
	}
	public void setGroupMember(List<groupMemberLDAP> groupMember) {
		this.groupMember = groupMember;
	}
	@XmlElement
	public List<ApplicationList> getApplicationList() {
		return applicationList;
	}
	public void setApplicationList(List<ApplicationList> applicationList) {
		this.applicationList = applicationList;
	}
	@XmlElement
	
	public List<String> getRemedy_AG() {
		return remedy_AG;
	}
	public void setRemedy_AG(List<String> remedy_AG) {
		this.remedy_AG = remedy_AG;
	}
	@XmlElement
	public String getSync_type() {
		return sync_type;
	}
	public void setSync_type(String sync_type) {
		this.sync_type = sync_type;
	}
	@XmlElement
	public String getMailerAD_name() {
		return mailerAD_name;
	}
	public void setMailerAD_name(String mailerAD_name) {
		this.mailerAD_name = mailerAD_name;
	}
	@XmlElement
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	

}
