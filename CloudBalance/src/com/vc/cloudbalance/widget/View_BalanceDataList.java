package com.vc.cloudbalance.widget;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.vc.cloudbalance.R;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.model.ModeTypeEnum;
import com.vc.cloudbalance.sqlite.BalanceDataDAL;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
@EViewGroup(R.layout.view_balancedatalist)
public class View_BalanceDataList extends LinearLayout{
	Context mContext;
	
	@ViewById
	LinearLayout llDataList;
	@ViewById
	ScrollViewExtend scrollView;
	private MemberMDL member;
	List<BalanceDataMDL> balanceDatas;
	View_ShowBalanceData parentView;
	
	public View_BalanceDataList(Context context,MemberMDL m,View_ShowBalanceData parentView) {
		super(context);
		mContext=context;
		member=m;
		this.parentView=parentView;
		// TODO Auto-generated constructor stub
	}
	public View_BalanceDataList(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		// TODO Auto-generated constructor stub
	}
	
	@AfterViews
	void init()
	{
		
		
		
	}
	public void removeData()
	{
		llDataList.removeAllViews();
	}
	public void loadData()
	{
		if(TextUtils.isEmpty(member.getMemberid()))
		{
			balanceDatas=new BalanceDataDAL(mContext).SelectByClientMemberId(member.getClientid());
		}
		else {
			balanceDatas=new BalanceDataDAL(mContext).SelectByMemberId(member.getMemberid());
		}
		llDataList.removeAllViews();
		int ii=0;
		for(int i=0;i<balanceDatas.size();i++)
		{
			if (member == null || member.getModeltype().equals(ModeTypeEnum.Adult)) {
				View_Item_BalanceData v = View_Item_BalanceData_.build(mContext,scrollView,parentView);
				v.setData(balanceDatas.get(i),member);
				llDataList.addView(v);
			} else if (member.getModeltype().equals(ModeTypeEnum.Children)||member.getModeltype().equals(ModeTypeEnum.Bady)) {
				View_Item_BalanceData_Baby v = View_Item_BalanceData_Baby_.build(mContext,scrollView,parentView);
				v.setData(balanceDatas.get(i),member);
				llDataList.addView(v);
			} 
			
		}
	}
	public void setGestureDetector(GestureDetector gestureDetector) {
		scrollView.setGestureDetector(gestureDetector);
	}
	
}
