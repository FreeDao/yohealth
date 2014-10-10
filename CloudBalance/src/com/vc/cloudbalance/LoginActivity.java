package com.vc.cloudbalance;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.AsyncTask;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.vc.cloudbalance.common.ApplicationMamager;
import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.common.JUtil;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.model.UserMDL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.sqlite.UserDAL;
import com.vc.cloudbalance.webservice.MemberWS;
import com.vc.cloudbalance.webservice.UserWS;
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements PlatformActionListener{
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR= 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	@ViewById
	EditText etUserName,etPassword;
	
	@AfterViews
	void init()
	{
		initLanguage();
		ShareSDK.initSDK(this);
	}
	private void initLanguage()
	{
		UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemLoginView_BarTitleLogin, this);
		UIHelper.SetText(R.id.tvReg, LanguageConstants.lb_ItemSettingLoginView_Register, this);
		UIHelper.SetText(R.id.lbTip1, LanguageConstants.lb_ItemSettingLoginView_Tip1, this);
		etUserName.setHint(LanguageHelper.GetVal(LanguageConstants.lb_ItemSettingLoginView_TFNameTip, mContext));
		etPassword.setHint(LanguageHelper.GetVal(LanguageConstants.lb_ItemSettingLoginView_TFPasswordTip, mContext));
		UIHelper.SetText(R.id.btnLogin, LanguageConstants.lb_ItemSettingLoginView_BtnLogin, this);
		UIHelper.SetText(R.id.lbTip2, LanguageConstants.lb_ItemSettingLoginView_Tip2, this);
		
	}
	@Click(R.id.btnLogin)
	void Login()
	{
		String username=etUserName.getText().toString();
		String password=etPassword.getText().toString();
		new loginTask().execute("1",username,password);
	}
	@Click(R.id.tvReg)
	void Reg()
	{
		Intent intent = RegActivity_.intent(this).get();
		startActivity(intent);
	}
	
	@Click(R.id.imgQQ)
	void QQLogin()
	{
		if(Common.checkNetWork(mContext)){
			authorize(new QQ(this));
		}else {
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.hud_netDisConnect, mContext));
		}
		
	}
	@Click(R.id.imgWeiBo)
	void WeiBoLogin()
	{
		if(Common.checkNetWork(mContext)){
			authorize(new SinaWeibo(this));
		}else {
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.hud_netDisConnect, mContext));
		}
		
	}
	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}
		if(plat.isValid()) {
			plat.removeAccount();
		}
		plat.setPlatformActionListener(this);
		if(!plat.getName().equals(SinaWeibo.NAME))
			plat.SSOSetting(true);
		
		plat.showUser(null);
	}
	private void login(String plat, String userId,String token, HashMap<String, Object> userInfo) {
		String logintype="1";
		if(plat.equals(SinaWeibo.NAME))
			logintype="3";
		if(plat.equals(QQ.NAME))
			logintype="2";
		String[] strs={logintype,userId,token};
		Message message = new Message();
		message.what=1;
		message.obj=strs;
		handler.sendMessage(message);
		
		
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1)
			{
				String[] strs =(String[])msg.obj;
				new loginTask().execute(strs[0],strs[1],strs[2]);
			}
			super.handleMessage(msg);
			
		}
		
	};
	class loginTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			DialogHelper.CloseLoadingDialog();
			if (TextUtils.isEmpty(result))
			{
				DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.hud_loginSuccess, mContext));
				ApplicationMamager.getInstance().goMainActivity(mContext);
			}
			else {
				
					DialogHelper.showTost(mContext, result);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			DialogHelper.ShowLoadingDialog(mContext,LanguageHelper.GetVal(LanguageConstants.svp_logining, mContext));
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
 			String logintype=params[0];
			String parm1=params[1];
			String parm2=params[2];
			if(Common.checkNetWork(mContext)){
				//authorize(new SinaWeibo(this));
			}else {
				return LanguageHelper.GetVal(LanguageConstants.hud_netDisConnect, mContext);
			}
			JSONObject jsonObject = null;
			jsonObject = new UserWS(mContext).UserLogin(logintype, parm1, parm2);
			if (jsonObject == null)
				return LanguageHelper.GetVal(LanguageConstants.hud_loginFail, mContext);
			else {
				if (JUtil.GetJsonStatu(jsonObject)){
					
					UserMDL user = JUtil.fromJson(jsonObject, UserMDL.class);
					if(user!=null){
						GetApp().setUser(user);
						new UserDAL(mContext).Insert(user);
						jsonObject = new MemberWS(mContext).getMember(user.getUserid());
						if (JUtil.GetJsonStatu(jsonObject))
						{
							
							List<MemberMDL> members = JUtil.fromJson(jsonObject, new TypeToken<List<MemberMDL>>() { }.getType());
							List<MemberMDL> clientMembers = new MemberDAL(mContext).Select(user.getUserid());
							for (MemberMDL member : members) {
								member.setUserid(user.getUserid());
								for (MemberMDL memberMDL : clientMembers) {
									if(member.getMemberid().equals(memberMDL.getMemberid()))
									{
											member.setClientid(memberMDL.getClientid());
											break;
									}
								}
								
							}
							for (MemberMDL member : members) 
							{
								if(TextUtils.isEmpty(member.getClientid()))
									member.setClientid(UUID.randomUUID().toString());
							}
							new MemberDAL(mContext).Insert(members);
						}
						Common.SynBalanceDataThread(mContext);
					}
					else {
						return LanguageHelper.GetVal(LanguageConstants.hud_loginFail, mContext);
					}
					return "";
				}
				else
					return LanguageHelper.GetVal(LanguageConstants.hud_loginFail, mContext);
			}
			

		}

	}
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			
			login(platform.getName(), platform.getDb().getUserId(), platform.getDb().getToken(),res);
		
		}
		System.out.println(res);
	}
	
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			
		}
		t.printStackTrace();
	}
	
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			
		}
	}
	
}
