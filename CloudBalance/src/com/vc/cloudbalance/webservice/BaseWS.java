package com.vc.cloudbalance.webservice;

import android.content.Context;

import com.vc.cloudbalance.common.Common;
import com.vc.net.PersistentCookieStore;
import com.vc.net.RequestParams;
import com.vc.net.SyncHttpClient;
public class BaseWS {
	protected Context mContext;

	public static String serverIp = "115.28.15.46";
	public static String serverPort="8080";
	public static String PIC_URL = "http://" + serverIp + "/gde/picture/";

	public static String Method_URL = "http://" + serverIp + ":"+serverPort+"/YMAPIServer/index.php?/";
	
	protected String GetMethodURL(String str) {
		
		serverIp="218.244.159.194";
		serverPort="9000";
		return "http://" + serverIp + ":"+serverPort+"/yr/index.php/write/" + str;
	}
	
	public BaseWS(Context context) {
		mContext = context;
	}
	public RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.put("clientuid", Common.GetDeviceId(mContext));
		return params;
	}
	
	public SyncHttpClient getAsyncHttpClient() {
		SyncHttpClient httpClient = new SyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
		httpClient.setCookieStore(myCookieStore);
		return httpClient;
	}
}
