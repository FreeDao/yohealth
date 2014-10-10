package com.vc.cloudbalance.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewExtend extends ScrollView{
	GestureDetector gestureDetector;
	boolean isTouchChildren=false;
	public ScrollViewExtend(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ScrollViewExtend(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	private float downY = 0;
	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
	public void IsTouchingChildren()
	{
		isTouchChildren=true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isTouchChildren)
		{
			getParent().requestDisallowInterceptTouchEvent(false);
			return super.onTouchEvent(event);
		}
		else {
			getParent().requestDisallowInterceptTouchEvent(true);
		    
			 super.onTouchEvent(event);
			 return gestureDetector.onTouchEvent(event);
			
		}
//		getParent().requestDisallowInterceptTouchEvent(false);
//		return super.onTouchEvent(event);
	    //return gestureDetector.onTouchEvent(event);
	}
 
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		if(ev.getAction()==MotionEvent.ACTION_DOWN)
			isTouchChildren=false;
		if(isTouchChildren)
		{
			getParent().requestDisallowInterceptTouchEvent(false);
		   // gestureDetector.onTouchEvent(ev);
		    return super.dispatchTouchEvent(ev);
		    //return true;
		}
		else {
			getParent().requestDisallowInterceptTouchEvent(true);
		    gestureDetector.onTouchEvent(ev);
			 super.dispatchTouchEvent(ev);
		    return true;
			
		}
		
	} 

}
