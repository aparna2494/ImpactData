package com.cisco.bccsp.model;

import java.util.List;
import java.util.Map;

public class Neo4jSNodeDetailModelv2 {
	
	private String psName;
	private String serviceDomain;
	private String serviceCategory;
	private String service;
	private String serviceOffering;
	private String application;
	private List<Map<String,String>> job;
	private List<Map<String,String>> jvm;
	private String itServiceOwner;
	private String responsibleManager;
	private String supportManager;
	private String serviceExecutive;
	private List<Map<String,String>> supportContact;
	private List<Map<String,String>> dependantState;
	private String supportDirector;
	public String getPsName() {
		return psName;
	}
	public void setPsName(String psName) {
		this.psName = psName;
	}
	public String getServiceDomain() {
		return serviceDomain;
	}
	public void setServiceDomain(String serviceDomain) {
		this.serviceDomain = serviceDomain;
	}
	public String getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getServiceOffering() {
		return serviceOffering;
	}
	public void setServiceOffering(String serviceOffering) {
		this.serviceOffering = serviceOffering;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public List<Map<String, String>> getJob() {
		return job;
	}
	public void setJob(List<Map<String, String>> job) {
		this.job = job;
	}
	public List<Map<String, String>> getJvm() {
		return jvm;
	}
	public void setJvm(List<Map<String, String>> jvm) {
		this.jvm = jvm;
	}
	public String getItServiceOwner() {
		return itServiceOwner;
	}
	public void setItServiceOwner(String itServiceOwner) {
		this.itServiceOwner = itServiceOwner;
	}
	public String getResponsibleManager() {
		return responsibleManager;
	}
	public void setResponsibleManager(String responsibleManager) {
		this.responsibleManager = responsibleManager;
	}
	public String getSupportManager() {
		return supportManager;
	}
	public void setSupportManager(String supportManager) {
		this.supportManager = supportManager;
	}
	public String getServiceExecutive() {
		return serviceExecutive;
	}
	public void setServiceExecutive(String serviceExecutive) {
		this.serviceExecutive = serviceExecutive;
	}
	public List<Map<String, String>> getSupportContact() {
		return supportContact;
	}
	public void setSupportContact(List<Map<String, String>> supportContact) {
		this.supportContact = supportContact;
	}
	public List<Map<String, String>> getDependantState() {
		return dependantState;
	}
	public void setDependantState(List<Map<String, String>> dependantState) {
		this.dependantState = dependantState;
	}
	public String getSupportDirector() {
		return supportDirector;
	}
	public void setSupportDirector(String supportDirector) {
		this.supportDirector = supportDirector;
	}
	@Override
	public String toString() {
		return "Neo4jSNodeDetailModelv2 [psName=" + psName + ", serviceDomain="
				+ serviceDomain + ", serviceCategory=" + serviceCategory
				+ ", service=" + service + ", serviceOffering="
				+ serviceOffering + ", application=" + application + ", job="
				+ job + ", jvm=" + jvm + ", itServiceOwner=" + itServiceOwner
				+ ", responsibleManager=" + responsibleManager
				+ ", supportManager=" + supportManager + ", serviceExecutive="
				+ serviceExecutive + ", supportContact=" + supportContact
				+ ", dependantState=" + dependantState + ", supportDirector="
				+ supportDirector + "]";
	}
	
	
	
}
