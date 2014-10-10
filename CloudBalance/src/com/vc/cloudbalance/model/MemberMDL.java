package com.vc.cloudbalance.model;

public class MemberMDL {
	private String memberid;
	private String membername;
	private String iconfile;
	private String birthday;
	private String height;
	private String waist;
	private String sex;
	private String targetweight;
	private String modeltype;
	private int upload;
	private String userid;
	private String clientid;
	private byte[] clientImg;
	private boolean guest=false;
	
	public boolean isGuest() {
		return guest;
	}
	public void setGuest(boolean guest) {
		this.guest = guest;
	}
	public byte[] getClientImg() {
		return clientImg;
	}
	public void setClientImg(byte[] clientImg) {
		this.clientImg = clientImg;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getMembername() {
		return membername;
	}
	public void setMembername(String membername) {
		this.membername = membername;
	}
	public String getIconfile() {
		return iconfile;
	}
	public void setIconfile(String iconfile) {
		this.iconfile = iconfile;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWaist() {
		return waist;
	}
	public void setWaist(String waist) {
		this.waist = waist;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getTargetweight() {
		return targetweight;
	}
	public void setTargetweight(String targetweight) {
		this.targetweight = targetweight;
	}
	public String getModeltype() {
		return modeltype;
	}
	public void setModeltype(String modeltype) {
		this.modeltype = modeltype;
	}
	public int getUpload() {
		return upload;
	}
	public void setUpload(int upload) {
		this.upload = upload;
	}

	
}
