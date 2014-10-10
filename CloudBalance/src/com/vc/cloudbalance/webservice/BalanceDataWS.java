package com.vc.cloudbalance.webservice;

import org.json.JSONObject;

import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.net.RequestParams;
import com.vc.net.SyncHttpClient;
import com.vc.util.FileHelper;

import android.content.Context;

public class BalanceDataWS extends BaseWS{
	public BalanceDataWS(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public JSONObject insertBalanceData(BalanceDataMDL mdl)
	{
		try {
			String url = GetMethodURL("updateMamberdata3");
			RequestParams params = getParams();
			params.put("userid", mdl.getUserid());
			params.put("memberid",mdl.getMemberid());
            params.put("weidate",mdl.getWeidateString());
            params.put("weight",mdl.getWeight());
            params.put("bmi",mdl.getBmi());
            params.put("fatpercent",mdl.getFatpercent());
            params.put("muscle",mdl.getMuscle());
            params.put("bone",mdl.getBone());
            params.put("water",mdl.getWater());
            params.put("innerfat",mdl.getInnerfat());
            params.put("basalmetabolism", mdl.getBasalmetabolism());
            params.put("icondata",FileHelper.BytetoStreamString(mdl.getClientImg()));
            params.put("haveimg",mdl.getHaveimg());
            params.put("height",mdl.getHeight());
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject getRanking2(String userid,String memberid,String bmi,String sex,String height,String weight)
	{
		try {
			String url = GetMethodURL("getRanking2");
			RequestParams params = getParams();
			params.put("userid", userid);
			params.put("memberid",memberid);
            params.put("bmi",bmi);
            params.put("sex",sex);
            params.put("height",height);
            params.put("weight",weight);
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject getMamberdataCount(String userid)
	{
		try {
			String url = GetMethodURL("getMamberdataCount");
			RequestParams params = getParams();
			params.put("userid",userid);
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject getMamberdata(String userid,String memberid)
	{
		try {
			String url = GetMethodURL("getMamberdata");
			RequestParams params = getParams();
			params.put("userid",userid);
			params.put("memberid",memberid);
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
