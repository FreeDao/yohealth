package com.vc.cloudbalance.widget;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.vc.cloudbalance.R;
import com.vc.cloudbalance.adapter.BasePageAdapter;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.MemberDAL;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Scroller;
import android.widget.ViewFlipper;
@EViewGroup(R.layout.view_showbalancedata)
public class View_ShowBalanceData extends LinearLayout{
	Context mContext;
//	@ViewById
//	MyViewPager viewpager;
	@ViewById
	RadioButton rb1,rb2;
	@ViewById
	RadioGroup rbg1;
	@ViewById
	ViewFlipper viewFlipper;
	ViewFlipper viewFlipperParent;
	List<View> views;
	BasePageAdapter adapter;
	private MemberMDL member;
	Animation leftInAnimation;
    Animation leftOutAnimation;
    Animation rightInAnimation;
    Animation rightOutAnimation;
    private GestureDetector mGestureDetector;
    private OnParentViewChangeListener onParentViewChangeListener;
    View_BalanceDataList listView;
    int ViewIndex=0;
	public View_ShowBalanceData(Context context,MemberMDL mdl) {
		super(context);
		mContext=context;
		member=mdl;
		// TODO Auto-generated constructor stub
	}
	public View_ShowBalanceData(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		// TODO Auto-generated constructor stub
	}
	public void setParentViewFlipper(ViewFlipper v)
	{
		viewFlipperParent=v;
	}
	public void setOnParentViewChangeListener(OnParentViewChangeListener o)
	{
		onParentViewChangeListener=o;
	}
	public void setMember(MemberMDL mdl){
		member=mdl;
	}
	public void loadListData()
	{
		if(listView!=null)
			listView.loadData();
	}
	public void removeListData(){
		if(listView!=null)
			listView.removeData();
	}
	@AfterViews
	void init()
	{
		mGestureDetector=  new GestureDetector(onGestureListener);
		leftInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.in_left);
        leftOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.out_left);
         rightInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.in_right);
         rightOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.out_right);
         
		views = new LinkedList<View>();
		
		View_BalanceDataChart chartView=View_BalanceDataChart_.build(mContext,member);
		views.add(0, chartView);
		listView=View_BalanceDataList_.build(mContext,member,this);
		listView.setGestureDetector(mGestureDetector);
		
		views.add(listView);
//		adapter = new BasePageAdapter(mContext, views);
//		viewpager.setAdapter(adapter);
		viewFlipper.addView(chartView);
		viewFlipper.addView(listView);
		viewFlipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mGestureDetector.onTouchEvent(event);
				return true;
			}
		});
		rbg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.rb1)
				{
					showPreviousPage();
				}
				else if(checkedId==R.id.rb2)
				{
					showNextPage();
				}
			}
		});
	}
	
	
	
	
	void showNextPage()
	{
		
		if(ViewIndex==1)
			return ;
		
		ViewIndex=1;
		viewFlipper.setInAnimation(leftInAnimation);
		viewFlipper.setOutAnimation(leftOutAnimation);
		viewFlipper.showNext();// 向右滑动
		
	}
	
	void showPreviousPage()
	{
		
		if(ViewIndex==0){
			
			return ;
		}
		
		ViewIndex=0;
		viewFlipper.setInAnimation(rightInAnimation);
		viewFlipper.setOutAnimation(rightOutAnimation);
		viewFlipper.showPrevious();// 向左滑动
		
	}
	void prePager()
	{
		if(ViewIndex==0){
			if(onParentViewChangeListener!=null)
				onParentViewChangeListener.onPreviou();
			return ;
		}
		rb1.setChecked(true);
	}
	OnGestureListener onGestureListener = new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if (e1.getX() - e2.getX() > 120) {
				rb2.setChecked(true);
				
				return true;
			} else if (e1.getX() - e2.getX() < -120) {
				if(ViewIndex==0){
					if(onParentViewChangeListener!=null)
						onParentViewChangeListener.onPreviou();
					return false;
				}
				rb1.setChecked(true);
				
				return true;
			}
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	};
	public interface OnParentViewChangeListener{
		void onNext();
		void onPreviou();
	}
}
