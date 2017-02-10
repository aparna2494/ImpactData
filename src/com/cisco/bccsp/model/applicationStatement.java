package com.cisco.bccsp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "applicationStatement")
public class applicationStatement {
	private String statement="";
	private List<String> resultDataContents=null;
	private Boolean  includeStats=true;
	@XmlElement	
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	@XmlElement	
	public List<String> getResultDataContents() {
		return resultDataContents;
	}
	public void setResultDataContents(List<String> resultDataContents) {
		this.resultDataContents = resultDataContents;
	}
	@XmlElement	
	public Boolean getIncludeStats() {
		return includeStats;
	}
	public void setIncludeStats(Boolean includeStats) {
		this.includeStats = includeStats;
	}
}
