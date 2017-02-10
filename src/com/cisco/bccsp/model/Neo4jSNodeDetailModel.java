package com.cisco.bccsp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="neo4jnodedetail")
public class Neo4jSNodeDetailModel {
private String service_domain;
private String service_category;
private String service;
private String service_offering;
private String application;
private List<String> jvm_name;
private List<String> concurrent_program;
private String it_service_owner;
private String responsible_manager;
private String support_manager;
private String service_exceutive;
private String support_contact;

@XmlElement
public String getService_domain() {
	return service_domain;
}
public void setService_domain(String service_domain) {
	this.service_domain = service_domain;
}

@XmlElement
public String getService_category() {
	return service_category;
}
public void setService_category(String service_category) {
	this.service_category = service_category;
}

@XmlElement
public String getService() {
	return service;
}
public void setService(String service) {
	this.service = service;
}

@XmlElement
public String getService_offering() {
	return service_offering;
}
public void setService_offering(String service_offering) {
	this.service_offering = service_offering;
}

@XmlElement
public String getApplication() {
	return application;
}
public void setApplication(String application) {
	this.application = application;
}

@XmlElement
public List<String> getJvm_name() {
	return jvm_name;
}
public void setJvm_name(List<String> jvm_name) {
	this.jvm_name = jvm_name;
}

@XmlElement
public List<String> getConcurrent_program() {
	return concurrent_program;
}
public void setConcurrent_program(List<String> concurrent_program) {
	this.concurrent_program = concurrent_program;
}
@XmlElement
public String getIt_service_owner() {
	return it_service_owner;
}
public void setIt_service_owner(String it_service_owner) {
	this.it_service_owner = it_service_owner;
}

@XmlElement
public String getResponsible_manager() {
	return responsible_manager;
}
public void setResponsible_manager(String responsible_manager) {
	this.responsible_manager = responsible_manager;
}

@XmlElement
public String getSupport_manager() {
	return support_manager;
}
public void setSupport_manager(String support_manager) {
	this.support_manager = support_manager;
}

@XmlElement
public String getService_exceutive() {
	return service_exceutive;
}
public void setService_exceutive(String service_exceutive) {
	this.service_exceutive = service_exceutive;
}

@XmlElement
public String getSupport_contact() {
	return support_contact;
}
public void setSupport_contact(String support_contact) {
	this.support_contact = support_contact;
}

}
