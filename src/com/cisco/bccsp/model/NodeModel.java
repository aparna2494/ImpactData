package com.cisco.bccsp.model;

import java.util.List;

public class NodeModel {
private String id;
private String component_name;
private Long weight;
private String track_name;
private Long track_id;
private List<KPIModel> kpi_data;
private String component_code;
private String parent=null;
private String masterid;
//this field is only for UI purpose. not stored in mongo
private String neo_node_on_ui;



public String getNeo_node_on_ui() {
	return neo_node_on_ui;
}
public void setNeo_node_on_ui(String neo_node_on_ui) {
	this.neo_node_on_ui = neo_node_on_ui;
}
public String getMasterid() {
	return masterid;
}
public void setMasterid(String masterid) {
	this.masterid = masterid;
}
public String getParent() {
	return parent;
}
public void setParent(String parent) {
	this.parent = parent;
}
public String getComponent_code() {
	return component_code;
}
public void setComponent_code(String component_code) {
	this.component_code = component_code;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getComponent_name() {
	return component_name;
}
public void setComponent_name(String component_name) {
	this.component_name = component_name;
}
public Long getWeight() {
	return weight;
}
public void setWeight(Long weight) {
	this.weight = weight;
}
public String getTrack_name() {
	return track_name;
}
public void setTrack_name(String track_name) {
	this.track_name = track_name;
}
public Long getTrack_id() {
	return track_id;
}
public void setTrack_id(Long track_id) {
	this.track_id = track_id;
}
public List<KPIModel> getKpi_data() {
	return kpi_data;
}
public void setKpi_data(List<KPIModel> kpi_data) {
	this.kpi_data = kpi_data;
}

}
