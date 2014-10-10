package com.vc.cloudbalance;

import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

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
@EActivity(R.layout.activity_account)
public class AccoutActivity extends BaseActivity  implements PlatformActionListener{
	@ViewById
	ImageView imgSys,imgQQ,imgWB;
	@ViewById
	TextView tvSysUserName;
	@ViewById
	Button btnSysReg,btnQQBind,btnQQUnBind,btnWBBind,btnWBUnBind;
	UserMDL user ;
	
	@AfterViews
	void init()
	{
		
		ShareSDK.initSDK(this);
		LoadData();
		initLanguage();
	}
	
	
	private void initLanguage()
	{
		UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemUserManageView_BarTitleMangeView, this);
		UIHelper.SetText(R.id.btnQQBind, LanguageConstants.lb_ItemUserManageView_CellBind, this);
		UIHelper.SetText(R.id.btnWBBind, LanguageConstants.lb_ItemUserManageView_CellBind, this);
		UIHelper.SetText(R.id.btnQQUnBind, LanguageConstants.lb_ItemUserManageView_CellUnBind, this);
		UIHelper.SetText(R.id.btnWBUnBind, LanguageConstants.lb_ItemUserManageView_CellUnBind, this);
		UIHelper.SetText(R.id.btnLogOut, LanguageConstants.lb_ItemUserManageView_LogOut, this);
		
		
		
		//UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemUserListView_BarTitleFamily, this);
	}
	
	void LoadData()
	{
		user = GetApp().getUser();
		if(!TextUtils.isEmpty(user.getUsername()))
		{
			tvSysUserName.setText(user.getUsername());
			btnSysReg.setVisibility(View.GONE);
			//btnSysLogout.setVisibility(View.VISIBLE);
		}
		else {
			//btnSysLogout.setVisibility(View.GONE);
			btnSysReg.setVisibility(View.VISIBLE);
		}
		if(!TextUtils.isEmpty(user.getQqopenid()))
		{
			//tvQQUserName.setText(user.getQqname());
			btnQQBind.setVisibility(View.GONE);
			btnQQUnBind.setVisibility(View.VISIBLE);
		}
		else {
			btnQQUnBind.setVisibility(View.GONE);
			btnQQBind.setVisibility(View.VISIBLE);
		}
		if(!TextUtils.isEmpty(user.getWbopenid()))
		{
			//tvWBUserName.setText(user.getWbname());
			btnWBBind.setVisibility(View.GONE);
			btnWBUnBind.setVisibility(View.VISIBLE);
		}
		else {
			btnWBUnBind.setVisibility(View.GONE);
			btnWBBind.setVisibility(View.VISIBLE);
		}
	}
	@Click(R.id.btnSysReg)
	void Reg()
	{
		Intent intent = RegActivity_.intent(this).get();
		startActivity(intent);
	}
	@Click(R.id.btnLogOut)
	void Logout()
	{
		DialogHelper.showComfrimDialog(this, LanguageHelper.GetVal(LanguageConstants.lb_dialog_tip, mContext), LanguageHelper.GetVal(LanguageConstants.query_logout, mContext),LanguageHelper.GetVal(LanguageConstants.alertButton_confirm, mContext),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						GetApp().setUser(null);
						new UserDAL(mContext).Del();
					 AccoutActivity.this.finish();

					}
				}, LanguageHelper.GetVal(LanguageConstants.alertButton_cancel, mContext), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		
	}
	
	@Click(R.id.btnQQBind)
	void QQLogin()
	{
		if(Common.checkNetWork(mContext)){
			authorize(new QQ(this));
		}else {
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.hud_netDisConnect, mContext));
		}
	}
	
	@Click(R.id.btnWBBind)
	void WeiBoLogin()
	{
		if(Common.checkNetWork(mContext)){
			authorize(new SinaWeibo(this));
		}else {
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.hud_netDisConnect, mContext));
		}
		
	}
	@Click(R.id.btnQQUnBind)
	void QQUnBind()
	{
		DialogHelper.showComfrimDialog(this, LanguageHelper.GetVal(LanguageConstants.lb_dialog_tip, mContext), LanguageHelper.GetVal(LanguageConstants.query_unbindQQ, mContext),LanguageHelper.GetVal(LanguageConstants.alertButton_confirm, mContext),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						new unBindTask().execute("2");
						dialog.dismiss();

					}
				}, LanguageHelper.GetVal(LanguageConstants.alertButton_cancel, mContext), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
		
		
	}
	
	@Click(R.id.btnWBUnBind)
	void WeiBoUnBind()
	{
		DialogHelper.showComfrimDialog(this, LanguageHelper.GetVal(LanguageConstants.lb_dialog_tip, mContext), LanguageHelper.GetVal(LanguageConstants.query_unbindWeiBo, mContext),LanguageHelper.GetVal(LanguageConstants.alertButton_confirm, mContext),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						new unBindTask().execute("3");
						dialog.dismiss();

					}
				}, LanguageHelper.GetVal(LanguageConstants.alertButton_cancel, mContext), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
		
	}
	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}
		if(plat.isValid()) {
			plat.removeAccount();
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(true);
		plat.showUser(null);
	}
	private void bind(String plat, String userId,String token, HashMap<String, Object> userInfo) {
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
				new bindTask().execute(strs[0],strs[1],strs[2]);
			}
			super.handleMessage(msg);
			
		}
		
	};
	class unBindTask extends AsyncTask<String, String, JSONObject> {
		
		
		String logintype;
		
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			DialogHelper.CloseLoadingDialog();
			if (result == null)
				DialogHelper.showTost(mContext,  LanguageHelper.GetVal(LanguageConstants.hud_unbindFail, mContext));
			else {
				if (JUtil.GetJsonStatu(result)){
					DialogHelper.showTost(mContext,  LanguageHelper.GetVal(LanguageConstants.hud_unbindSuccess, mContext));
					if(logintype.equals("2"))
					{
						user.setQqopenid(null);
						user.setQqaccesstoken(null);
						
					}
					else
					{
						user.setWbopenid(null);
						user.setWbaccesstoken(null);
					}
					
					GetApp().setUser(user);
					new UserDAL(mContext).Insert(user);
					LoadData();
				}
				else
					DialogHelper.showTost(mContext, JUtil.GetErrorString(result));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			DialogHelper.ShowLoadingDialog(mContext, LanguageHelper.GetVal(LanguageConstants.svp_unbinding, mContext));
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			 logintype=params[0];
			 
			
			JSONObject jsonObject = null;
			jsonObject = new UserWS(mContext).UserJieBang(user.getUserid(),logintype);
			
			return jsonObject;

		}

	}
	class bindTask extends AsyncTask<String, String, JSONObject> {
		
		
		String logintype;
		String parm1;
		String parm2;
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			DialogHelper.CloseLoadingDialog();
			if (result == null)
				DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.hud_unbindFail, mContext));
			else {
				if (JUtil.GetJsonStatu(result)){
					DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.hud_bindSuccess, mContext));
					if(logintype.equals("2"))
					{
						user.setQqopenid(parm1);
						user.setQqaccesstoken(parm2);
						
					}
					else
					{
						user.setWbopenid(parm1);
						user.setWbaccesstoken(parm2);
					}
					
					GetApp().setUser(user);
					new UserDAL(mContext).Insert(user);
					LoadData();
				}
				else
					DialogHelper.showTost(mContext, JUtil.GetErrorString(result));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			DialogHelper.ShowLoadingDialog(mContext,LanguageHelper.GetVal(LanguageConstants.svp_binding, mContext));
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			 logintype=params[0];
			 parm1=params[1];
			 parm2=params[2];
			
			JSONObject jsonObject = null;
			jsonObject = new UserWS(mContext).UserBang(user.getUserid(),logintype, parm1, parm2);
			
			return jsonObject;

		}

	}
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			
			bind(platform.getName(), platform.getDb().getUserId(), platform.getDb().getToken(),res);
		
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
