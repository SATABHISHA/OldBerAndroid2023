package com.nextgenbermobileapp.info;

public class WeekDateInfo {

	public String Day=null;
	public String WeekDate = null;
	public String StartDate = null;
	public String EndDate = null;
	public String BERStatus = null;
	
	public WeekDateInfo(String Day,String WeekDate, String StartDate, String EndDate,String BERStatus) {
		  super();
		  this.Day=Day;
		  this.WeekDate = WeekDate;
		  this.StartDate= StartDate;
		  this.EndDate = EndDate;
		  this.BERStatus = BERStatus;
		  
	}
	
	public String getDay() {
		  return Day;
		 }
		 public void setDay(String Day) {
		  this.Day=Day;
		 }
		 
		 public String getWeekDate() {
			  return WeekDate;
			 }
			 public void setName(String WeekDate) {
			  this.WeekDate = WeekDate;
			 }
			 public String getStartDate() {
			  return StartDate;
			 }
			 public void setStartDate(String StartDate) {
			  this.StartDate = StartDate;
			 }
			 public String getEndDate() {
			  return EndDate;
			 }
			 public void setEndDate(String EndDate) {
			  this.EndDate = EndDate;
			 }
			 public String getBERStatus() {
			  return BERStatus;
			 }
			 public void setBERStatus(String BERStatus) {
			  this.BERStatus = BERStatus;
			 }
	
}
