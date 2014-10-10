package com.vc.cloudbalance;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;

import cn.sharesdk.sina.weibo.SinaWeibo;

import com.vc.cloudbalance.common.ApplicationMamager;
import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.common.JUtil;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.UserMDL;
import com.vc.cloudbalance.sqlite.UserDAL;
import com.vc.cloudbalance.webservice.UserWS;
import com.vc.util.FileHelper;
import com.vc.util.ObjectHelper;
@EActivity(R.layout.activity_reg)
public class RegActivity extends BaseActivity{
	
	@ViewById
	EditText etUserName,etPassword,etComPassword;
	
	@Click(R.id.btnReg)
	void Reg()
	{
		//DialogHelper.ShowLoadingDialog(mContext, "正在注册");
		
		String username=etUserName.getText().toString();
		String password=etPassword.getText().toString();
		String compassword=etComPassword.getText().toString();
		if(TextUtils.isEmpty(username)||!Common.checkMobileNO(username)||!Common.checkEmail(username) )
		{
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.alert_AccountFormatErr, mContext));
			return ;
		}
		if(TextUtils.isEmpty(password)||!password.equals(compassword) )
		{
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.alert_passwordNotSame, mContext));
			return ;
		}
		if(Common.checkNetWork(mContext)){
			new regTask().execute(0);
		}else {
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.hud_netDisConnect, mContext));
		}
		//if(!ObjectHelper.isMobileNO(username)&&!ObjectHelper.isEmail(username))
		//{
		//	DialogHelper.showTost(mContext, "账户格式错误");
		//	return ;
		//}
		
	}
	private void initLanguage()
	{
		UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemSettingRegisterView_BarTitleRegister, this);
		UIHelper.SetText(R.id.btnReg, LanguageConstants.lb_ItemSettingRegisterView_BarTitleRegister, this);
		UIHelper.SetText(R.id.lbTip2, LanguageConstants.lb_ItemSettingRegisterView_tip, this);
		UIHelper.SetText(R.id.lbTip1, LanguageConstants.lb_ItemSettingRegisterView_AccountNameHead, this);
		UIHelper.SetText(R.id.lbTip3, LanguageConstants.lb_ItemSettingRegisterView_SetAccountHead, this);
		//UIHelper.SetText(R.id.btnLogOut, LanguageConstants.lb_ItemUserManageView_LogOut, this);
		etUserName.setHint(LanguageHelper.GetVal(LanguageConstants.lb_ItemSettingRegisterView_TextFieldTipName, mContext));
		etPassword.setHint(LanguageHelper.GetVal(LanguageConstants.lb_ItemSettingRegisterView_TextFieldTipPassword, mContext));
		etComPassword.setHint(LanguageHelper.GetVal(LanguageConstants.lb_ItemSettingRegisterView_TextFieldTipPasswordConfirm, mContext));
		//UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemUserListView_BarTitleFamily, this);
	}
	@AfterViews
	void init()
	{
		initLanguage();
	}
	class regTask extends AsyncTask<Integer, String, JSONObject> {
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			DialogHelper.CloseLoadingDialog();
			if (result == null)
				DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.alert_passwordNotSame, mContext));
			else {
				if (JUtil.GetJsonStatu(result)){
					DialogHelper.showTost(mContext, "注册成功");
					UserMDL user = JUtil.fromJson(result, UserMDL.class);
					GetApp().setUser(user);
					new UserDAL(mContext).Insert(user);
					ApplicationMamager.getInstance().goMainActivity(mContext);
				}
				else
					DialogHelper.showTost(mContext, JUtil.GetErrorString(result));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			DialogHelper.ShowLoadingDialog(mContext, LanguageHelper.GetVal(LanguageConstants.svp_registering, mContext));
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			String username=etUserName.getText().toString();
			String password=etPassword.getText().toString();
			String compassword=etComPassword.getText().toString();
			
			JSONObject jsonObject = null;
			UserMDL user = GetApp().getUser();
			String userid="0";
			if(user!=null)
				userid=user.getUserid();
			jsonObject = new UserWS(mContext).registerUser(userid,username,password);
			
			return jsonObject;

		}

	}
}
