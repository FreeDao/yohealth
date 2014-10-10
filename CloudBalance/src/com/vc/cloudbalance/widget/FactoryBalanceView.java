package com.vc.cloudbalance.widget;

import com.vc.cloudbalance.R;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.model.ModeTypeEnum;
import com.vc.util.ObjectHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.vc.cloudbalance.widget.*;

public class FactoryBalanceView extends LinearLayout {
	Context mContext;
	View_AdultBalanceData AdultBalanceData;
	View_AdultBalanceNoFatData AdultBalanceNoFatData;
	View_BigFlowerBalanceData BigFlowerBalanceData;
	MemberMDL member;

	public FactoryBalanceView(Context context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public FactoryBalanceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.imgGoBalance) {

			}
		}
	};

	public void setImageUrl(String url) {
		if (BigFlowerBalanceData != null)
		{
			BigFlowerBalanceData.setImageUrl(url);
		}
	}

	public void init(MemberMDL member) {
		this.member = member;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		if (member == null || member.isGuest() ||member.getModeltype().equals(ModeTypeEnum.Adult)) {
			AdultBalanceData = View_AdultBalanceData_.build(mContext);
			AdultBalanceData.setLayoutParams(params);
			AdultBalanceData.setMember(member);
			addView(AdultBalanceData);
			AdultBalanceNoFatData = View_AdultBalanceNoFatData_.build(mContext);
			AdultBalanceNoFatData.setVisibility(View.GONE);
			AdultBalanceNoFatData.setMember(member);
			AdultBalanceNoFatData.setLayoutParams(params);
			addView(AdultBalanceNoFatData);
		} else if (member.getModeltype().equals(ModeTypeEnum.Children)) {
			BigFlowerBalanceData = View_BigFlowerBalanceData_.build(mContext,
					member);
			BigFlowerBalanceData.setLayoutParams(params);
			addView(BigFlowerBalanceData);
		} else if (member.getModeltype().equals(ModeTypeEnum.Bady)) {
			BigFlowerBalanceData = View_BigFlowerBalanceData_.build(mContext,
					member);
			BigFlowerBalanceData.setLayoutParams(params);
			addView(BigFlowerBalanceData);
		}

	}

	public void setData(String weight, String val, String data) {
		
		if (BigFlowerBalanceData != null)
			BigFlowerBalanceData.setData(weight, val, data);
		int deviceMode = ObjectHelper.Convert2Int(Common.GetBluetoothValIndex(
				data, 11));
		if (AdultBalanceData != null && AdultBalanceNoFatData != null) {
			if (deviceMode > 10 && deviceMode < 20) {
				// ·ÇÖ¬·¾³Ó
				AdultBalanceData.setVisibility(View.GONE);
				AdultBalanceNoFatData.setVisibility(View.VISIBLE);
				if (AdultBalanceNoFatData != null)
					AdultBalanceNoFatData.setData(weight, val, data);
			} else {
				if (AdultBalanceData != null)
					AdultBalanceData.setData(weight, val, data);
				AdultBalanceData.setVisibility(View.VISIBLE);
				AdultBalanceNoFatData.setVisibility(View.GONE);
			}
		}

	}
}
