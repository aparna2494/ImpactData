package com.cisco.bccsp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "roster")
@XmlType(propOrder = { "agId", "appId", "date", "month", "week", "userId", "startShiftTime", "endShiftTime", "shiftId"})
public class Roster {
	
	private int agId = 0;
	private int appId = 0;
	private String date = "";
	private int month = 0;
	private int week = 0;
	private String userId = "";
	private double startShiftTime = 0.0;
	private double endShiftTime = 0.0;
	private int shiftId = 0;
	
	@XmlElement
	public int getAGId() {
		return agId;
	}
	public void setAGId(int agId) {
		this.agId = agId;
	}
	@XmlElement
	public int getappId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	@XmlElement
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@XmlElement
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	@XmlElement
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	@XmlElement
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@XmlElement
	public double getStartShiftTime() {
		return startShiftTime;
	}
	public void setStartShiftTime(double startShiftTime) {
		this.startShiftTime = startShiftTime;
	}
	@XmlElement
	public double getEndShiftTime() {
		return endShiftTime;
	}
	public void setEndShiftTime(double endShiftTime) {
		this.endShiftTime = endShiftTime;
	}
	@XmlElement
	public int getShiftId() {
		return shiftId;
	}
	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}
}
