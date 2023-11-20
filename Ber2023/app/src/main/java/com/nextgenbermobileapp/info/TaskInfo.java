package com.nextgenbermobileapp.info;

public class TaskInfo {

	public String UserID=null;
	public String ID=null;
	public String Code = null;
	public String ContractNo = null;
	public String Description = null;
	
	 
	 public TaskInfo(String UserID,String ID,String Code, String ContractNo, String Description) {
		  super();
		  
		  this.UserID=UserID;
		  this.ID=ID;
		  this.Code=Code;
		  this.ContractNo=ContractNo;
		  this.Description=Description;
		  
		 }
	 
	 public String getUserID() {
		  return UserID;
		 }
	 public void setUserID(String UserID) {
		  this.UserID=UserID;
		 }
	 
	 public String getID() {
		  return ID;
	 }
	 public void setID(String ID) {
		  this.ID=ID;
	 }
	
	 public String getCode() {
		  return Code;
	 }
	 public void setCode(String Code) {
		  this.Code=Code;
	 }
	 public String getContractNo() {
		  return ContractNo;
	 }
	 public void setContractNo(String ContractNo) {
		  this.ContractNo=ContractNo;
	 }
	 public String getDescription() {
		  return Description;
	 }
	 public void setDescription(String Description) {
		  this.Description=Description;
	 }
	 
	 @Override
		public String toString() {
			return Description;
		}
}
