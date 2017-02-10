package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "applicationlist")
public class ApplicationList {
private String id="";
private String label="";
@XmlElement
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
@XmlElement
public String getLabel() {
	return label;
}
public void setLabel(String label) {
	this.label = label;
}
}
