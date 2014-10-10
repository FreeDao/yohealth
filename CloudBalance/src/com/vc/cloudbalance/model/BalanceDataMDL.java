package com.vc.cloudbalance.model;

import java.util.Date;

import com.vc.cloudbalance.widget.View_AdultBalanceNoFatData;
import com.vc.cloudbalance.widget.View_BalanceDataChart;
import com.vc.util.ObjectHelper;

public class BalanceDataMDL {
	private int id;
	private String memberid;
	private String userid;
	private String weidate;
	private String weight;
	private String bmi;
	private String fatpercent;
	private String muscle;
	private String bone;
	private String water;
	private String basalmetabolism;
	private String innerfat;
	private int upload;
	private String clientmemberid;
	private String dataid;
	private byte[] clientImg;
	private String haveimg;
	private String height;
	private String picurl;
	
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getHaveimg() {
		return haveimg;
	}
	public void setHaveimg(String haveimg) {
		this.haveimg = haveimg;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public byte[] getClientImg() {
		return clientImg;
	}
	public void setClientImg(byte[] clientImg) {
		this.clientImg = clientImg;
	}
	public String getVal(int Mode)
	{
		
		if(Mode==View_BalanceDataChart.Bone)
			return getBone();
		else if(Mode==View_BalanceDataChart.Fat)
			return getFatpercent();
		else if(Mode==View_BalanceDataChart.Muscle)
			return getMuscle();
		else if(Mode==View_BalanceDataChart.Water)
			return getWater();
		else if(Mode==View_BalanceDataChart.Weight)
			return getWeight();
		else if(Mode==View_BalanceDataChart.BMI)
			return getBmi();
		else 
			return "0";
	}
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	public String getClientmemberid() {
		return clientmemberid;
	}
	public void setClientmemberid(String clientmemberid) {
		this.clientmemberid = clientmemberid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getWeidate() {
		return ObjectHelper.Convert2Date(this.weidate,  "yyyy-MM-dd HH:mm:ss");
	}
	public String getWeidateString() {
		return weidate;
	}
	public void setWeidate(Date weidate) {
		this.weidate = ObjectHelper.Convert2String(weidate, "yyyy-MM-dd HH:mm:ss") ;
	}
	public void setWeidateString(String weidate) {
		this.weidate = weidate;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getBmi() {
		return bmi;
	}
	public void setBmi(String bmi) {
		this.bmi = bmi;
	}
	public String getFatpercent() {
		return fatpercent;
	}
	public void setFatpercent(String fatpercent) {
		this.fatpercent = fatpercent;
	}
	public String getMuscle() {
		return muscle;
	}
	public void setMuscle(String muscle) {
		this.muscle = muscle;
	}
	public String getBone() {
		return bone;
	}
	public void setBone(String bone) {
		this.bone = bone;
	}
	public String getWater() {
		return water;
	}
	public void setWater(String water) {
		this.water = water;
	}
	public String getBasalmetabolism() {
		return basalmetabolism;
	}
	public void setBasalmetabolism(String basalmetabolism) {
		this.basalmetabolism = basalmetabolism;
	}
	public String getInnerfat() {
		return innerfat;
	}
	public void setInnerfat(String innerfat) {
		this.innerfat = innerfat;
	}
	public int getUpload() {
		return upload;
	}
	public void setUpload(int upload) {
		this.upload = upload;
	}
	
}
