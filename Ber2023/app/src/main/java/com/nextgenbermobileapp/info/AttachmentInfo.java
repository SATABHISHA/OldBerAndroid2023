package com.nextgenbermobileapp.info;

public class AttachmentInfo {

	
	public String ID=null;
	public String ExpenseDate = null;
	public String ImageURL = null;
	public String ImageCode = null;
	
	 
	 public AttachmentInfo(String ID,String ExpenseDate,String ImageURL, String ImageCode) {
		  super();
		  this.ID=ID;
		  this.ExpenseDate= ExpenseDate;
		  this.ImageURL=ImageURL;
		  this.ImageCode=ImageCode;
		  
		 }
	 
	 public String getID() {
		  return ID;
	 }
	 public void setID(String ID) {
		  this.ID=ID;
	 }
	
	 public String getExpenseDate() {
		  return ExpenseDate;
	 }
	 public void setExpenseDate(String ExpenseDate) {
		  this.ExpenseDate=ExpenseDate;
	 }
	 
	 public String getImageURL() {
		  return ImageURL;
	 }
	 public void setImageURL(String ImageURL) {
		  this.ImageURL=ImageURL;
	 }
	 public String getImageCode() {
		  return ImageCode;
	 }
	 public void setImageCode(String ImageCode) {
		  this.ImageCode=ImageCode;
	 }
	 
}
