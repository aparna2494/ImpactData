package com.cisco.bccsp.model;

import java.util.Date;

public class KPIModel {
private Long kpi_id;
private String kpi_name;
private String kpi_status;
private String skms_id;
public String getSkms_id() {
	return skms_id;
}
public void setSkms_id(String skms_id) {
	this.skms_id = skms_id;
}
private ThresholdModel threshold;
private String database;
private String query;

private long frequency;
private long nextRunTime;


public long getFrequency() {
	return frequency;
}
public void setFrequency(long frequency) {
	this.frequency = frequency;
}
public long getNextRunTime() {
	return nextRunTime;
}
public void setNextRunTime(long nextRunTime) {
	this.nextRunTime = nextRunTime;
}
public String getDatabase() {
	return database;
}
public void setDatabase(String database) {
	this.database = database;
}
public String getQuery() {
	return query;
}
public void setQuery(String query) {
	this.query = query;
}
public Long getKpi_id() {
	return kpi_id;
}
public void setKpi_id(Long kpi_id) {
	this.kpi_id = kpi_id;
}
public String getKpi_name() {
	return kpi_name;
}
public void setKpi_name(String kpi_name) {
	this.kpi_name = kpi_name;
}
public String getKpi_status() {
	return kpi_status;
}
public void setKpi_status(String kpi_status) {
	this.kpi_status = kpi_status;
}
public ThresholdModel getThreshold() {
	return threshold;
}
public void setThreshold(ThresholdModel threshold) {
	this.threshold = threshold;
}

}
