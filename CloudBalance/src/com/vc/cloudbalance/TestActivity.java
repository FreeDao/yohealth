package com.vc.cloudbalance;

import java.util.LinkedList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.hellojni.HelloJni;
import com.vc.cloudbalance.adapter.BasePageAdapter;
import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.widget.*;

@EActivity(R.layout.test)
public class TestActivity extends BaseActivity implements OnGestureListener,
		OnTouchListener {
	List<View> views;
	MemberMDL member;
	BasePageAdapter adapter;
	@ViewById
	ViewFlipper viewpager;
	private GestureDetector mGestureDetector;

	@AfterViews
	void init() {
		member = new MemberDAL(mContext).SelectById("192");
		 leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.in_left);
         leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.out_left);
          rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.in_right);
          rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.out_right);

		mGestureDetector = new GestureDetector(this);
		viewpager.setOnTouchListener(this);
		// viewpager.startFlipping();

		TextView tView = new TextView(mContext);
		tView.setText("pager1");
		tView.setTextColor(Color.BLUE);
		viewpager.addView(tView);
		 View_ShowBalanceData showbalancedataview=View_ShowBalanceData_.build(mContext,member);
				
		
		viewpager.addView(showbalancedataview);

		// views = new LinkedList<View>();
		// member = new MemberDAL(mContext).SelectById("192");
		// if (member != null) {
		// setTitleText(member.getMembername());
		// TextView tView= new TextView(mContext);
		// tView.setText("ssss");
		// tView.setTextColor(Color.BLUE);
		// views.add(tView);
		//
		// }
		// View_ShowBalanceData
		// showbalancedataview=View_ShowBalanceData_.build(mContext,member);
		//
		// views.add(showbalancedataview);
		//
		// adapter = new BasePageAdapter(mContext, views);
		// viewpager.setAdapter(adapter);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	Animation leftInAnimation;
    Animation leftOutAnimation;
    Animation rightInAnimation;
    Animation rightOutAnimation;

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 120) {
			viewpager.setInAnimation(leftInAnimation);
			viewpager.setOutAnimation(leftOutAnimation);
			viewpager.showNext();// 向右滑动
			return true;
		} else if (e1.getX() - e2.getY() < -120) {
			viewpager.setInAnimation(rightInAnimation);
			viewpager.setOutAnimation(rightOutAnimation);
			viewpager.showPrevious();// 向左滑动
			return true;
		}
		return true;
		
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
