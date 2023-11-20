package com.nextgenbermobileapp.info;

public class ExpensesInfo {

	public String LocationID = null;
	public String ID = null;
	public String Name = null;
	public String CostTypeID = null;
	public String CostType = null;
	public String RateType = null;
	public String RateEntryText = null;
	public String UseRate = null;
	public String defaultText = null;

	public ExpensesInfo(String LocationID, String ID, String Name,
			String CostTypeID, String CostType, String RateType,
			String RateEntryText, String UseRate, String defaultText) {
		super();
		this.LocationID = LocationID;
		this.ID = ID;
		this.Name = Name;
		this.CostTypeID = CostTypeID;
		this.CostType = CostType;
		this.RateType = RateType;
		this.RateEntryText = RateEntryText;
		this.UseRate = UseRate;
		this.defaultText = defaultText;
		// setDefaultText(defaultText);

	}

	public String getdefaultText() {
		return LocationID;
	}

	public void setdefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public String getLocationID() {
		return LocationID;
	}

	public void setLocationID(String LocationID) {
		this.LocationID = LocationID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getCostTypeID() {
		return CostTypeID;
	}

	public void setCostTypeID(String CostTypeID) {
		this.CostTypeID = CostTypeID;
	}

	public String getCostType() {
		return CostType;
	}

	public void setCostType(String CostType) {
		this.CostType = CostType;
	}

	public String getRateType() {
		return RateType;
	}

	public void setRateType(String RateType) {
		this.RateType = RateType;
	}

	public String getRateEntryText() {
		return RateEntryText;
	}

	public void setRateEntryText(String RateEntryText) {
		this.RateEntryText = RateEntryText;
	}

	public String getUseRate() {
		return UseRate;
	}

	public void setUseRate(String UseRate) {
		this.UseRate = UseRate;
	}
}
