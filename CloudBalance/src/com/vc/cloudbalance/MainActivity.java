package com.vc.cloudbalance;

import java.util.LinkedList;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sharesdk.framework.utils.UIHandler;

import com.vc.cloudbalance.common.AppService;
import com.vc.cloudbalance.common.ApplicationMamager;
import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.common.CrashHandler;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.widget.*;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
	private final static String TAG = MainActivity.class.getSimpleName();

	List<MemberMDL> members;
	List<View_Item_Member> memberViews;
	@ViewById
	LinearLayout llMemberPanel;
	@ViewById
	TextView tvTitle;
	
	@Click(R.id.btnSetting)
	void showSettingActivity() {
		Intent intent = SettingActivity_.intent(this).get();
		startActivity(intent);
	}

	@Click(R.id.btnAddMember)
	void showAddMemberActivity() {
		Intent intent = MemberInfoActivity_.intent(this).get();
		startActivity(intent);
	}
	@Click(R.id.btnTest)
	void Experience()
	{
		Intent intent  = GuestInfoActivity_.intent(mContext).get();
		
		mContext.startActivity(intent);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		LoadMembers();
		initLanguage();
		System.gc();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showGoLoginPageAlert();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void showGoLoginPageAlert() {

		DialogHelper.showComfrimDialog(this, "提示", "确定退出登陆？", "确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						ApplicationMamager.getInstance().quit();
						dialog.dismiss();

					}
				}, "取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

	}

	@AfterViews
	void init() {
		final int version = android.os.Build.VERSION.SDK_INT;
		if (version < 18) {
			new AlertDialog.Builder(mContext)

			.setTitle("警告")

			.setMessage("此软件只支持Android 4.3以上系统，抱歉！")

			.setPositiveButton("确定", null)

			.show().setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					ApplicationMamager.getInstance().quit();
				}
			});
			
		}
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		AppService.actionStart(mContext);
	}
	private void initLanguage()
	{
		UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemUserListView_BarTitleFamily, this);
		UIHelper.SetText(R.id.btnTest, LanguageConstants.lb_ItemUserListView_GuestMode, this);
		//UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemUserListView_BarTitleFamily, this);
	}
	private void LoadMembers() {
		if (GetApp().getUser() != null)
			members = new MemberDAL(mContext).Select(GetApp().getUser()
					.getUserid());
		else
			members = new MemberDAL(mContext).SelectGuest();
		if(memberViews!=null)
		{
			for (View_Item_Member item : memberViews) {
				item.recycle();
			}
		}
		llMemberPanel.removeAllViews();
		memberViews = new LinkedList<View_Item_Member>();
		if (members != null && members.size() > 0) {
			for (int i = 0; i < members.size(); i++) {
				View_Item_Member v = View_Item_Member_.build(mContext);
				v.setMember(members.get(i));
				memberViews.add(v);
				llMemberPanel.addView(v);
			}
		}

	}
}
