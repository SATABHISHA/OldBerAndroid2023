package com.nextgenbermobileapp.info;

import java.util.ArrayList;

public class LocationDetailsInfo {

	
	public String ID=null;
	public String Name=null;
	public String CountryID = null;
	public String StateID = null;
	
	private ArrayList<LocationInfo> store_location_info=new ArrayList<LocationInfo>();
	
	/*public String Loc_ID = null;
	public String ExpenseTypeID = null;
	public String StartDate = null;
	public String EndDate = null;  
	public String Rate = null; */
	 
	 public LocationDetailsInfo(String ID,String Name,String CountryID, String StateID, String Loc_ID,String ExpenseTypeID,String StartDate,String EndDate,String Rate) {
		  super();
		  this.ID=ID;
		  this.Name=Name;
		  this.CountryID=CountryID;
		  this.StateID=StateID;
		  /*this.Loc_ID=Loc_ID;
		  this.ExpenseTypeID=ExpenseTypeID;
		  this.StartDate=StartDate;
		  this.EndDate=EndDate;
		  this.Rate=Rate;*/
		  
	}
	 
	 
	 
	 
	 public LocationDetailsInfo(String ID,String Name,String CountryID, String StateID,ArrayList<LocationInfo> result_info) {
		  super();
		  this.ID=ID;
		  this.Name=Name;
		  this.CountryID=CountryID;
		  this.StateID=StateID;
		  this.store_location_info=result_info;
		  /*this.Loc_ID=Loc_ID;
		  this.ExpenseTypeID=ExpenseTypeID;
		  this.StartDate=StartDate;
		  this.EndDate=EndDate;
		  this.Rate=Rate;*/
		  
	}
	 public LocationDetailsInfo(String ID,String Name,String CountryID, String StateID) {
		  super();
		  this.ID=ID;
		  this.Name=Name;
		  this.CountryID=CountryID;
		  this.StateID=StateID;
		  /*this.store_location_info=result_info;*/
		  /*this.Loc_ID=Loc_ID;
		  this.ExpenseTypeID=ExpenseTypeID;
		  this.StartDate=StartDate;
		  this.EndDate=EndDate;
		  this.Rate=Rate;*/
		  
	}
	 
	 public String getID() {
		  return ID;
		 }
		 public void setID(String ID) {
		  this.ID=ID;
		 }
		 
	 
	 public String getName() {
		 return Name;
	 }
	 
	 public void setName(String Name) {
		 this.Name = Name;
	 }
		 
	public String getCountryID() {
			  return CountryID;
	}
	public void setCountryID(String CountryID) {
			  this.CountryID = CountryID;
	}
	
	public String getStateID() {
		  return StateID;
	}
	
	public void setStateID(String StateID) {
			  this.StateID = StateID;
	}
	 
	public ArrayList<LocationInfo> getLocationInfo()
	{
		return this.store_location_info;
	}
	
	public void addLocationInfo(LocationInfo eval)
	{
		if(store_location_info != null)
		{
			store_location_info.add(eval);
		}
	}
	
	@Override
	public String toString() {
		return Name;
	}
	
	
	/*public String getLoc_ID() {
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
	}*/
}
