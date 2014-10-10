package com.vc.cloudbalance.adapter;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class BasePageAdapter extends PagerAdapter {
	private List<View> mList;
	private Context mContext;
	public BasePageAdapter(Context c,List<View> vs)
	{
		mContext=c;
		mList=vs;
	}
	@Override
	public int getCount() {
		if(mList.size()<=3)
			return mList.size();
		return Integer.MAX_VALUE;
//		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	public void finishUpdate(ViewGroup container) {
		// TODO Auto-generated method stub
		// super.finishUpdate(container);
	}
	public List<View> getDatas()
	{
		return mList;
		 
	}
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		//((ViewPager) arg0).removeView((View)mList.get(arg1%mList.size()));
//		((ViewPager) arg0).removeView((View)mList.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		// TODO Auto-generated method stub
		try {
			if(((View)mList.get(arg1%mList.size())).getParent()==null)
				((ViewPager) arg0).addView((View)mList.get(arg1%mList.size()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
			
		return mList.get(arg1%mList.size());
//		((ViewPager) arg0).addView((View)mList.get(arg1));
//		return mList.get(arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}
}
