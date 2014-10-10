package com.vc.cloudbalance.webservice;

import org.json.JSONObject;

import com.vc.cloudbalance.model.MemberMDL;
import com.vc.net.RequestParams;
import com.vc.net.SyncHttpClient;
import com.vc.util.FileHelper;

import android.content.Context;

public class MemberWS extends BaseWS{
	public MemberWS(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public JSONObject insertMember(String userid,MemberMDL member)
	{
		try {
			String url = GetMethodURL("updateMember");
			RequestParams params = getParams();
			params.put("userid", userid);
			params.put("memberid","");
            params.put("membername",member.getMembername());
            params.put("birthday",member.getBirthday());
            params.put("height",member.getHeight());
            params.put("waist",member.getWaist());
            params.put("sex",member.getSex());
            params.put("targetweight",member.getTargetweight());
            params.put("modeltype",member.getModeltype());
            params.put("itag","1");
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject updateMember(String userid,MemberMDL member)
	{
		try {
			String url = GetMethodURL("updateMember");
			RequestParams params = getParams();
			params.put("userid", userid);
			params.put("memberid",member.getMemberid());
            params.put("membername",member.getMembername());
            params.put("birthday",member.getBirthday());
            params.put("height",member.getHeight());
            params.put("waist",member.getWaist());
            params.put("sex",member.getSex());
            params.put("targetweight",member.getTargetweight());
            params.put("modeltype",member.getModeltype());
            params.put("itag","2");
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject deleteMember(String userid,MemberMDL member)
	{
		try {
			String url = GetMethodURL("updateMember");
			RequestParams params = getParams();
			params.put("userid", userid);
			params.put("memberid",member.getMemberid());
            params.put("membername",member.getMembername());
            params.put("birthday",member.getBirthday());
            params.put("height",member.getHeight());
            params.put("waist",member.getWaist());
            params.put("sex",member.getSex());
            params.put("targetweight",member.getTargetweight());
            params.put("modeltype",member.getModeltype());
            params.put("itag","3");
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject getMember(String userid)
	{
		try {
			String url = GetMethodURL("getMember");
			RequestParams params = getParams();
			params.put("userid", userid);
			
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public JSONObject updateMemberIcon(MemberMDL mdl)
	{
		try {
			String url = GetMethodURL("updateMemberIcon");
			RequestParams params = getParams();
			params.put("userid", mdl.getUserid());
			params.put("memberid", mdl.getMemberid());
			params.put("icondata",FileHelper.BytetoStreamString(mdl.getClientImg()));
			SyncHttpClient synchttpclient = getAsyncHttpClient();
			JSONObject result = synchttpclient.postToJson(url, params);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
