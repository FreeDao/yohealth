package com.vc.cloudbalance.model;

public class LanguageMDL {
	private String key;
	private String Val1;
	private String Val2;
	public LanguageMDL(String k,String v1,String v2){
		key=k;
		Val1=v1;
		Val2=v2;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVal1() {
		return Val1;
	}
	public void setVal1(String val1) {
		Val1 = val1;
	}
	public String getVal2() {
		return Val2;
	}
	public void setVal2(String val2) {
		Val2 = val2;
	}
	public String GetVal(String l)
	{
		if(l.equals("1"))
			return getVal1();
		else if(l.equals("2")){
			return getVal2();
		}
		else {
			return getVal1();
			
		}
	}
}
