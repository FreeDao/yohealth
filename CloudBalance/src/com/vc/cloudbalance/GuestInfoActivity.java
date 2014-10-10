package com.vc.cloudbalance;

import java.util.Calendar;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.model.ModeTypeEnum;
import com.vc.util.ObjectHelper;
@EActivity(R.layout.activity_guest)
public class GuestInfoActivity extends BaseActivity {
	@ViewById
	EditText etAge,etHeight;
	@ViewById
	RadioButton rbGender1, rbGender2;
	
	@AfterViews
	void init() {
		etAge.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					showDate();
				return true;
			}
		});
		 initLanguage();
	}
	private void initLanguage()
	{
		UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemGuestInfoView_BarTitleGuest, this);
		UIHelper.SetText(R.id.btnAction, LanguageConstants.lb_ItemGuestInfoView_Tip4, this);
		UIHelper.SetText(R.id.tvTip3, LanguageConstants.lb_ItemGuestInfoView_Tip3, this);
		UIHelper.SetText(R.id.tvTip2, LanguageConstants.lb_ItemGuestInfoView_Tip2, this);
		UIHelper.SetText(R.id.tvTipAge, LanguageConstants.lb_ItemGuestInfoView_TipAge, this);
		UIHelper.SetText(R.id.tvTip1, LanguageConstants.lb_ItemGuestInfoView_Tip1, this);
		UIHelper.SetText(R.id.rbGender2, LanguageConstants.lb_ItemGuestInfoView_genderMale, this);
		UIHelper.SetText(R.id.rbGender1, LanguageConstants.lb_ItemGuestInfoView_genderFeMale, this);
		UIHelper.SetText(R.id.tvUnitAge, LanguageConstants.lb_ItemGuestInfoView_unitAge, this);
		UIHelper.SetText(R.id.tvUnitCM, LanguageConstants.lb_ItemGuestInfoView_unitCM, this);
		
		//UIHelper.SetText(R.id.tvTitle, LanguageConstants.lb_ItemUserListView_BarTitleFamily, this);
	}
	private void showDate() {
		Dialog dialog = null;
		Calendar c = Calendar.getInstance();
		String text = (String) etAge.getTag();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (!TextUtils.isEmpty(text)) {
			Date date = ObjectHelper.Convert2Date(text, "yyyy-MM-dd");

			cal.setTime(date);
			if (date != null) {
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				day = cal.get(Calendar.DAY_OF_MONTH);
				;

			}
		}
		dialog = new DatePickerDialog(mContext,
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker dp, int year, int month,
							int dayOfMonth) {
						String nowmonth = (month + 1) < 10 ? ("0" + (month + 1))
								: (month + 1) + "";
						String nowdayOfMonth = dayOfMonth < 10 ? ("0" + dayOfMonth)
								: dayOfMonth + "";
						String selectDateTime = year + "-" + nowmonth + "-"
								+ nowdayOfMonth;
						etAge.setTag(selectDateTime);
						int age = Common.GetAgeByBirthday(selectDateTime);
						etAge.setText(age + "");
					}
				}, year, // 传入年份
				month, // 传入月份
				day // 传入天数
		);
		dialog.show();

	}
	@Click(R.id.btnAction)
	void action()
	{
		String Birthday = (String) etAge.getTag();
		String Height = etHeight.getText().toString();
		String Gender = "";
		if (rbGender1.isChecked())
			Gender = "0";
		else
			Gender = "1";
		MemberMDL member = new MemberMDL();
		member.setGuest(true);
		member.setBirthday(Birthday);
		member.setHeight(Height);
		member.setSex(Gender);
		member.setModeltype(ModeTypeEnum.Adult);
		App.GuestMember=member;
		Intent intent  = MemberDataActivity_.intent(mContext).get();
		intent.putExtra(Constants.EXTRA_KEY_ID_STRING,"guest");
		mContext.startActivity(intent);
		//mdl.set
	}
	
}
