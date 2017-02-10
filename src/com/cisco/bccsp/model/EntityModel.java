package com.cisco.bccsp.model;

import java.util.List;

public class EntityModel {
private String entityName;
private String entityType;
private List<DependencyModel> dependecy_data;
public String getEntityName() {
	return entityName;
}
public void setEntityName(String entityName) {
	this.entityName = entityName;
}
public String getEntityType() {
	return entityType;
}
public void setEntityType(String entityType) {
	this.entityType = entityType;
}
public List<DependencyModel> getDependecy_data() {
	return dependecy_data;
}
public void setDependecy_data(List<DependencyModel> dependecy_data) {
	this.dependecy_data = dependecy_data;
}
}
