package com.vc.cloudbalance;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.UserMDL;
import com.vc.cloudbalance.sqlite.AppConfigDAL;
import com.vc.cloudbalance.sqlite.UserDAL;
import com.vc.dialog.ListViewWindow;
import com.vc.util.ObjectHelper;
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity{
	ListViewWindow language_listViewWindow;
	@ViewById
	TextView btnUserManager,btnLogout,btnLogin,tvLanguage;
	@ViewById
	LinearLayout rlLogin,rlLogOut;
	
	
	void init()
	{
		UserMDL user = GetApp().getUser();
		if(user==null)
		{
			rlLogin.setVisibility(View.GONE);
			rlLogOut.setVisibility(View.VISIBLE);
		}
		else {
			rlLogin.setVisibility(View.VISIBLE);
			rlLogOut.setVisibility(View.GONE);
		}
		String val=new AppConfigDAL(mContext).select(Constants.SQL_KEY_LANGUAGE_STRING);
		if(val.equals("2"))
			tvLanguage.setText(Common.GetLanguageList()[1]);
		else
			tvLanguage.setText(Common.GetLanguageList()[0]);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		init();
		initLanguage();
		super.onResume();
	}
	private com.vc.dialog.ListViewWindow.OnSelectListener onLanguageSelectListener = new com.vc.dialog.ListViewWindow.OnSelectListener() {

		@Override
		public void onSelect(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
			tvLanguage.setText(Common.GetLanguageList()[which]);
			String val="";
			if(which==0)
				val="1";
			else
				val="2";
			new AppConfigDAL(mContext).insert(Constants.SQL_KEY_LANGUAGE_STRING,val);
			initLanguage();
		}
	};
	@Click(R.id.llLanguage)
	void showLanguageWindow()
	{
		String[] arrayList = Common.GetLanguageList();
		language_listViewWindow = new ListViewWindow(mContext,
				onLanguageSelectListener, arrayList, LanguageHelper.GetVal(LanguageConstants.lb_ItemSettingView_Languish, mContext),LanguageHelper.GetVal(LanguageConstants.alertButton_cancel, mContext));
		language_listViewWindow.show();
	}
	
	@Click(R.id.btnLogin)
	void showLoginActivity(){
		Intent intent = LoginActivity_.intent(this).get();
		startActivity(intent);
	}
	@Click(R.id.btnLogout)
	void Logout(){
		DialogHelper.showComfrimDialog(this, LanguageHelper.GetVal(LanguageConstants.lb_dialog_tip, mContext), LanguageHelper.GetVal(LanguageConstants.query_logout, mContext),LanguageHelper.GetVal(LanguageConstants.alertButton_confirm, mContext),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						GetApp().setUser(null);
						new UserDAL(mContext).Del();
						init();

					}
				}, LanguageHelper.GetVal(LanguageConstants.alertButton_cancel, mContext), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		
	}
	private void initLanguage()
	{
		UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemSettingView_BarTitleSetting, this);
		UIHelper.SetText(R.id.lbAccount, LanguageConstants.lb_ItemSettingView_CellHeadTitleAccount, this);
		UIHelper.SetText(R.id.btnUserManager, LanguageConstants.lb_ItemSettingView_ItemAccountManage, this);
		UIHelper.SetText(R.id.btnLogout, LanguageConstants.lb_ItemSettingView_UserCellLogout, this);
		UIHelper.SetText(R.id.btnLogin, LanguageConstants.lb_ItemSettingView_ItemLogin, this);
		UIHelper.SetText(R.id.lbLanguage, LanguageConstants.lb_ItemSettingView_CellHeadLanguish, this);
		UIHelper.SetText(R.id.tvChooseLanguage, LanguageConstants.lb_ItemSettingView_Languish, this);
		UIHelper.SetText(R.id.lbWeightUnit, LanguageConstants.lb_ItemSettingView_ItemDanWei, this);
		UIHelper.SetText(R.id.radio0, LanguageConstants.lb_ItemSettingView_UnitCellKG, this);
		UIHelper.SetText(R.id.radio1, LanguageConstants.lb_ItemSettingView_UnitCellLB, this);
		UIHelper.SetText(R.id.lbSystem, LanguageConstants.lb_ItemSettingView_CellHeadSystem, this);
		UIHelper.SetText(R.id.tvHelper, LanguageConstants.lb_ItemSettingView_CellHeadHowToUse, this);
		UIHelper.SetText(R.id.lbAbout, LanguageConstants.lb_ItemSettingView_CellHeadAboutUs, this);
		
		//UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemUserListView_BarTitleFamily, this);
	}
	@Click(R.id.btnUserManager)
	void showAccoutActivity(){
		Intent intent = AccoutActivity_.intent(this).get();
		startActivity(intent);
	}
}
