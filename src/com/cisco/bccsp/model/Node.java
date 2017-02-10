package com.cisco.bccsp.model;

public class Node {
@Override
	public String toString() {
		return "Node [data=" + data + ", position=" + position
				+ ", node_fixed=" + node_fixed + ", locked=" + locked
				+ ", selected=" + selected + ", classes=" + classes
				+ ", grabbable=" + grabbable + ", selectable=" + selectable
				+ ", removed=" + removed + ", group=" + group + "]";
	}
private NodeModel data;
private PositionModel position;
private Boolean node_fixed;
private Boolean locked;
private Boolean selected;
private String classes;
private Boolean grabbable;
private Boolean selectable;
private Boolean removed;
private String group;

public String getGroup() {
	return group;
}
public void setGroup(String group) {
	this.group = group;
}
public Boolean getLocked() {
	return locked;
}
public void setLocked(Boolean locked) {
	this.locked = locked;
}
public Boolean getSelected() {
	return selected;
}
public void setSelected(Boolean selected) {
	this.selected = selected;
}
public String getClasses() {
	return classes;
}
public void setClasses(String classes) {
	this.classes = classes;
}
public Boolean getGrabbable() {
	return grabbable;
}
public void setGrabbable(Boolean grabbable) {
	this.grabbable = grabbable;
}
public Boolean getSelectable() {
	return selectable;
}
public void setSelectable(Boolean selectable) {
	this.selectable = selectable;
}
public Boolean getRemoved() {
	return removed;
}
public void setRemoved(Boolean removed) {
	this.removed = removed;
}
public Boolean getNode_fixed() {
	return node_fixed;
}
public void setNode_fixed(Boolean node_fixed) {
	this.node_fixed = node_fixed;
}
public NodeModel getData() {
	return data;
}
public void setData(NodeModel data) {
	this.data = data;
}
public PositionModel getPosition() {
	return position;
}
public void setPosition(PositionModel position) {
	this.position = position;
}

}
