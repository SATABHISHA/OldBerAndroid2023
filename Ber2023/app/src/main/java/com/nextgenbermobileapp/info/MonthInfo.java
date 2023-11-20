package com.nextgenbermobileapp.info;

import java.util.ArrayList;

public class MonthInfo {

	public String Month=null;
	public String Day=null;
	public String WeekDate = null;
	public String StartDate = null;
	public String EndDate = null;
	public String BERStatus = null;
	
	
	private ArrayList<WeekDateInfo> lstWeekDateInfo = new ArrayList<WeekDateInfo>();
	  
	 public MonthInfo(String Day,String WeekDate, String StartDate, String EndDate,String BERStatus) {
	  super();
	  this.Day=Day;
	  this.WeekDate = WeekDate;
	  this.StartDate= StartDate;
	  this.EndDate = EndDate;
	  this.BERStatus = BERStatus;
	  
	 }
	 
	 public MonthInfo(String Month,String Day,String WeekDate, String StartDate, String EndDate,String BERStatus) {
		  super();
		  this.Day=Day;
		  this.Month=Month;
		  this.WeekDate = WeekDate;
		  this.StartDate= StartDate;
		  this.EndDate = EndDate;
		  this.BERStatus = BERStatus;
		  
		 }
	 
	 public MonthInfo(String Month,ArrayList<WeekDateInfo> data) {
		  super();
		  this.Month=Month;
		  this.lstWeekDateInfo=data;
		 }
	 
	 
	 
	 public String getDay() {
		  return Day;
		 }
		 public void setDay(String Day) {
		  this.Day=Day;
		 }
		 
	 
	 public String getMonth() {
		  return Month;
		 }
		 public void setMonth(String Month) {
		  this.Month = Month;
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
		 
		 public ArrayList<WeekDateInfo> getEvalCritList()
			{
				return this.lstWeekDateInfo;
			}
			
			public void addEvalCrit(WeekDateInfo weekDateInfo)
			{
				if(lstWeekDateInfo != null)
				{
					lstWeekDateInfo.add(weekDateInfo);
				}
			}	 
		 
}
