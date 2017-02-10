package com.cisco.bccsp.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "incident")
@XmlType(propOrder = { "incidentNumber", "releasePriorityByUser", 
  "issueExistBeforeRelease", "workaroundAvailable", "contacts", 
  "incidentStatusInRemedy", "releasePriorityInRemedy", "taggedAsReleaseIssue", 
  "transactions", "createUser", "createDate", "updateUser", "updateDate","transactionsImpacted","isSoftwareIssue","impTransactionType","amountEnteredByUser",
  "project"})
public class Incident {
	

	private String incidentNumber = "";
	private String releasePriorityByUser = "PS3"; 
	private String issueExistBeforeRelease = "N";
	private String workaroundAvailable = "N";
	private String contacts = "";
	private String incidentStatusInRemedy = "";
	private String releasePriorityInRemedy = "";
	private String taggedAsReleaseIssue = "N";
	private List<Transaction> transactions;
	
	private Float totalAmount = new Float(0.0f);

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	private String transactionsImpacted = "N";
	private String isSoftwareIssue = "N";
	private String impTransactionType = "none";
	private String amountEnteredByUser = "0";
	private String project="";
	
	@XmlElement	
	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	
	@XmlElement	
	public String getReleasePriorityByUser() {
		return releasePriorityByUser;
	}

	public void setReleasePriorityByUser(String releasePriorityByUser) {
		this.releasePriorityByUser = releasePriorityByUser;
	}

	@XmlElement	
	public String getIssueExistBeforeRelease() {
		return issueExistBeforeRelease;
	}

	public void setIssueExistBeforeRelease(String issueExistBeforeRelease) {
		this.issueExistBeforeRelease = issueExistBeforeRelease;
	}

	@XmlElement	
	public String getWorkaroundAvailable() {
		return workaroundAvailable;
	}

	public void setWorkaroundAvailable(String workaroundAvailable) {
		this.workaroundAvailable = workaroundAvailable;
	}

	@XmlElement	
	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	@XmlElement	
	public String getIncidentStatusInRemedy() {
		return incidentStatusInRemedy;
	}

	public void setIncidentStatusInRemedy(String incidentStatusInRemedy) {
		this.incidentStatusInRemedy = incidentStatusInRemedy;
	}

	@XmlElement	
	public String getReleasePriorityInRemedy() {
		return releasePriorityInRemedy;
	}

	public void setReleasePriorityInRemedy(String releasePriorityInRemedy) {
		this.releasePriorityInRemedy = releasePriorityInRemedy;
	}

	@XmlElement	
	public String getTaggedAsReleaseIssue() {
		return taggedAsReleaseIssue;
	}

	public void setTaggedAsReleaseIssue(String taggedAsReleaseIssue) {
		this.taggedAsReleaseIssue = taggedAsReleaseIssue;
	}
	
	@XmlElement	
	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@XmlElement	
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@XmlElement	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@XmlElement	
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@XmlElement	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@XmlElement	
	public String getTransactionsImpacted() {
		return transactionsImpacted;
	}

	public void setTransactionsImpacted(String transactionsImpacted) {
		this.transactionsImpacted = transactionsImpacted;
	}
	
	@XmlElement	
	public String getIsSoftwareIssue() {
		return isSoftwareIssue;
	}

	public void setIsSoftwareIssue(String isSoftwareIssue) {
		this.isSoftwareIssue = isSoftwareIssue;
	}

	@XmlElement	
	public String getImpTransactionType() {
		return impTransactionType;
	}

	public void setImpTransactionType(String impTransactionType) {
		this.impTransactionType = impTransactionType;
	}

	@XmlElement	
	public String getAmountEnteredByUser() {
		return amountEnteredByUser;
	}

	public void setAmountEnteredByUser(String amountEnteredByUser) {
		this.amountEnteredByUser = amountEnteredByUser;
	}
	
	@XmlElement	
	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@XmlElement	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}


}
