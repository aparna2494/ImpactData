package com.cisco.bccsp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThresholdModel {
	@JsonProperty("Critical") private Long critical;
	@JsonProperty("Medium") private Long medium;
	@JsonProperty("High") private Long high;
public Long getCritical() {
	return critical;
}
public void setCritical(Long critical) {
	this.critical = critical;
}
public Long getMedium() {
	return medium;
}
public void setMedium(Long medium) {
	this.medium = medium;
}
public Long getHigh() {
	return high;
}
public void setHigh(Long high) {
	this.high = high;
}

}
