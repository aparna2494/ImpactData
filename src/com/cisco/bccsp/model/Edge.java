package com.cisco.bccsp.model;

public class Edge {
private String id;
private String source;
private String target;
private Boolean edge_fixed;
public Boolean getEdge_fixed() {
	return edge_fixed;
}
public void setEdge_fixed(Boolean edge_fixed) {
	this.edge_fixed = edge_fixed;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public String getTarget() {
	return target;
}
public void setTarget(String target) {
	this.target = target;
}

}
