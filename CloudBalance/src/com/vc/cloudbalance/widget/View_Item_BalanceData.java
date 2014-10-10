package com.vc.cloudbalance.widget;

import java.util.List;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.vc.cloudbalance.*;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.model.ActionMDL;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.ActionDAL;
import com.vc.cloudbalance.sqlite.BalanceDataDAL;
import com.vc.cloudbalance.widget.LockableHorizontalScrollView.OnCustomTouchListenter;
import com.vc.util.ObjectHelper;
@EViewGroup(R.layout.view_item_balancedata)
public class View_Item_BalanceData  extends LinearLayout{
	private Context mContext;
	
	private Scroller mScroller; 
	@ViewById
	LockableHorizontalScrollView lockableHorizontalScrollView1;
	@ViewById
	ImageView imgHeadImage,imgLine;
	@ViewById
	TextView tvDate,tvDay,tvTime,tvWeight,tvBMI;
	@ViewById
	LinearLayout llFont,llBack;
	@ViewById
	Button btnDelete;
	MemberMDL member;
	boolean isLoad;
	int offset_x=0;
	View_ShowBalanceData parentView;
	ScrollViewExtend scroll;
	BalanceDataMDL data;
	public View_Item_BalanceData(Context context,ScrollViewExtend scroll,View_ShowBalanceData parentView) {
		super(context);
		mContext=context;
		this.scroll=scroll;
		 this.parentView=parentView;
		// TODO Auto-generated constructor stub
	}
	public View_Item_BalanceData(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		
		// TODO Auto-generated constructor stub
	}
	public void setData(BalanceDataMDL data,MemberMDL m)
	{
		member=m;
		this.data=data;
		tvDate.setText(ObjectHelper.Convert2String(data.getWeidate(), "yyyy/MM"));
		tvDay.setText(ObjectHelper.Convert2String(data.getWeidate(), "dd"));
		tvTime.setText(ObjectHelper.Convert2String(data.getWeidate(), "HH:mm"));
		tvWeight.setText(data.getWeight());
		tvBMI.setText(data.getBmi());
	}
	@Click(R.id.btnDelete)
	void delete()
	{
		BalanceDataDAL dal = new BalanceDataDAL(mContext);
		String startTime =ObjectHelper.Convert2String(data.getWeidate(), "yyyy-MM-dd 00:00:00");
		String endTime =ObjectHelper.Convert2String(data.getWeidate(), "yyyy-MM-dd 23:59:59");
		List<BalanceDataMDL> old_datas=dal.SelectThisDateData(data.getMemberid(),data.getClientmemberid(), startTime, endTime);
		if(old_datas.size()<=0)
			return;
		if(old_datas.size()==1)
		{
			DialogHelper.showTost(mContext, "当天只有一条记录，不能删除");
			return ;
		}
		boolean isNeedUpload=false;
		if(old_datas.get(0).getId()==data.getId())
		{
			isNeedUpload=true;
		}
		if(dal.DelById(data.getId()))
		{
			if(isNeedUpload)
			{
				dal.setUnloadData(old_datas.get(1));
				
				Common.SynBalanceDataThread(mContext);
			}
			
		}
	}
	@AfterViews
	void init() {
		// TODO Auto-generated method stub
		int screenWidth  = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth(); 
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)llFont.getLayoutParams();
		params.width=screenWidth;
		llFont.setLayoutParams(params);
		ViewTreeObserver vto1 = llBack.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isLoad||offset_x==0) {
					offset_x=llBack.getWidth();
					
					lockableHorizontalScrollView1.setScroll_x(offset_x);
					isLoad = true;
				}

			}
		});
		
		lockableHorizontalScrollView1.setOnCustomTouchListenter(new OnCustomTouchListenter() {
			
			@Override
			public void open() {
				// TODO Auto-generated method stub
				imgLine.setImageResource(R.drawable.bg_split_f2);
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				imgLine.setImageResource(R.drawable.bg_split_f1);
				
			}
			
			@Override
			public void click() {
				// TODO Auto-generated method stub
				Log.e("clickFontView", "clickFontView");
			}

			@Override
			public void prePage() {
				// TODO Auto-generated method stub
				//viewpager.setCurrentItem(0, true);
				Log.e("prePage", "prePage");
				parentView.prePager();
			}

			@Override
			public void touch() {
				// TODO Auto-generated method stub
				scroll.IsTouchingChildren();
			}
		}); 	
	}
	
	
}
