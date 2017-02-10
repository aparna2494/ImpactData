package com.cisco.bccsp.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "transaction")
@XmlType(propOrder = { "incidentNumber", "transactionNumber", "shipSet",
  "lineNumber", "transactionType", "customerId", "customerName", "amount",
  "activeFlag", "transactionStatus", "createUser", "createDate", "updateUser", 
  "updateDate"})
public class Transaction {
	
	private String incidentNumber = "";
	private Long transactionNumber = 0l;
	private String shipSet = "";
	private String lineNumber = "";
	private String transactionType = "";	
	private String customerId = "";
	private String customerName = "";
	private Float amount = 0.0f;
	private String activeFlag = "";
	private String transactionStatus = "";
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	@XmlElement		
	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	
	@XmlElement		
	public Long getTransactionNumber() {
		return transactionNumber;
	}
	
	public void setTransactionNumber(Long transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	@XmlElement		
	public String getShipSet() {
		return shipSet;
	}

	public void setShipSet(String shipSet) {
		this.shipSet = shipSet;
	}

	@XmlElement		
	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	@XmlElement	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@XmlElement	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@XmlElement	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@XmlElement	
	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@XmlElement	
	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	@XmlElement	
	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
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
	
}
