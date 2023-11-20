package com.nextgenbermobileapp.info;

public class LocationInfo {

	public String Loc_ID = null;
	public String ExpenseTypeID = null;
	public String StartDate = null;
	public String EndDate = null;  
	public String Rate = null; 
	
	
	public LocationInfo(String Loc_ID,String ExpenseTypeID,String StartDate,String EndDate,String Rate) 
	{
		  super();
		  this.Loc_ID=Loc_ID;
		  this.ExpenseTypeID=ExpenseTypeID;
		  this.StartDate=StartDate;
		  this.EndDate=EndDate;
		  this.Rate=Rate;
		  
	}
	public LocationInfo(String Loc_ID,String ExpenseTypeID,String Rate) 
	{
		  super();
		  this.Loc_ID=Loc_ID;
		  this.ExpenseTypeID=ExpenseTypeID;
		  this.StartDate=StartDate;
		  this.EndDate=EndDate;
		  this.Rate=Rate;
		  
	}
	public String getLoc_ID() {
		  return Loc_ID;
	}
	public void setLoc_ID(String Loc_ID) {
			  this.Loc_ID = Loc_ID;
	}
	
	public String getExpenseTypeID() {
		  return ExpenseTypeID;
	}
	public void setExpenseTypeID(String ExpenseTypeID) {
			  this.ExpenseTypeID = ExpenseTypeID;
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
	public String getRate() {
		  return Rate;
	}
	public void setRate(String Rate) {
			  this.Rate = Rate;
	}
}
