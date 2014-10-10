package com.vc.cloudbalance.model;

public class UserMDL {
	private String userid;
	private String username;
	
	private String qqopenid;
	private String qqaccesstoken;
	private String qqname;
	private String wbopenid;
	private String wbaccesstoken;
	private String wbname;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getQqopenid() {
		return qqopenid;
	}
	public void setQqopenid(String qqopenid) {
		this.qqopenid = qqopenid;
	}
	
	public String getQqname() {
		return qqname;
	}
	public void setQqname(String qqname) {
		this.qqname = qqname;
	}
	public String getWbopenid() {
		return wbopenid;
	}
	public void setWbopenid(String wbopenid) {
		this.wbopenid = wbopenid;
	}
	
	public String getQqaccesstoken() {
		return qqaccesstoken;
	}
	public void setQqaccesstoken(String qqaccesstoken) {
		this.qqaccesstoken = qqaccesstoken;
	}
	public String getWbaccesstoken() {
		return wbaccesstoken;
	}
	public void setWbaccesstoken(String wbaccesstoken) {
		this.wbaccesstoken = wbaccesstoken;
	}
	public String getWbname() {
		return wbname;
	}
	public void setWbname(String wbname) {
		this.wbname = wbname;
	}
	
}
