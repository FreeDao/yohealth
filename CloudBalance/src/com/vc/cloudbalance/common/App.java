package com.vc.cloudbalance.common;

import org.json.JSONObject;

import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.model.UserMDL;
import com.vc.cloudbalance.sqlite.UserDAL;
import com.vc.cloudbalance.webservice.UserWS;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

public class App extends Application{
	
	private UserMDL User;
	public static MemberMDL GuestMember;
	public static Object threadDBLock="";
	public static String packageName="data/data/com.vc.cloudbalance";
	public UserMDL getUser() {
		if(User==null)
		{
			User=new UserDAL(getApplicationContext()).Select(); 
		}
		return User;
	}


	public void setUser(UserMDL user) {
		User = user;
	}


	public static App getApp(Context mContext) {
		return (App) mContext.getApplicationContext();
	}
	
	
	
}
