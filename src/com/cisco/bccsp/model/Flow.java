package com.cisco.bccsp.model;

import java.util.List;

public class Flow {
	private String _id;
public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
private List<Node> node;
private List<EdgeModel> edge;
public List<Node> getNode() {
	return node;
}
public void setNode(List<Node> node) {
	this.node = node;
}
public List<EdgeModel> getEdge() {
	return edge;
}
public void setEdge(List<EdgeModel> edge) {
	this.edge = edge;
}


}
