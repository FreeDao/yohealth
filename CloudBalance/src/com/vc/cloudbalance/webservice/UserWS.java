package com.vc.cloudbalance.webservice;

import java.util.List;

import org.json.JSONObject;

import com.vc.net.RequestParams;
import com.vc.net.SyncHttpClient;
import android.content.Context;

public class UserWS extends BaseWS{
	public UserWS(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public JSONObject registerUser(String userid,String username,String password)
	{
		try {
			String url = GetMethodURL("registerUser");
			RequestParams params = getParams();
			params.put("userid", userid);
			params.put("username",username);
            params.put("password",password);
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject UserLogin(String logintype,String parm1,String parm2)
	{
		try {
			String url = GetMethodURL("UserLogin");
			RequestParams params = getParams();
			params.put("logintype", logintype);
			params.put("parm1",parm1);
            params.put("parm2",parm2);
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}	
	public JSONObject UserBang(String userid,String bangtype,String parm1,String parm2)
	{
		try {
			String url = GetMethodURL("UserBang");
			RequestParams params = getParams();
			params.put("userid", userid);
			params.put("bangtype", bangtype);
			params.put("parm1",parm1);
            params.put("parm2",parm2);
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}	
	public JSONObject UserJieBang(String userid,String bangtype)
	{
		try {
			String url = GetMethodURL("UserJieBang");
			RequestParams params = getParams();
			params.put("userid", userid);
			params.put("bangtype", bangtype);
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}	
}
