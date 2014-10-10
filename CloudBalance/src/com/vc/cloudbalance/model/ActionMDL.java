package com.vc.cloudbalance.model;

public class ActionMDL {
	public static final String DelMember="DelMember";
	public static final String UpdateMember="UpdateMember";
	public static final String AddMember="AddMember";
	public static final String AddBalanceData="AddBalanceData";
	public static final String DelBalanceData="DelBalanceData";
	private String id;
	private String action;
	private String data;
	private int locked;
	
	public int getLocked() {
		return locked;
	}
	public void setLocked(int locked) {
		this.locked = locked;
	}
	public ActionMDL(String arg1,String arg2,String arg3)
	{
		setId(arg1);
		setAction(arg2);
		setData(arg3);
	}
	public ActionMDL(){}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
