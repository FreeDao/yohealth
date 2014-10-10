package com.vc.cloudbalance.common;

import java.util.LinkedList;
import java.util.List;

import com.vc.cloudbalance.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ApplicationMamager {
	private List<Activity> mList = new LinkedList<Activity>();
	private static ApplicationMamager instance;

	private ApplicationMamager() {
	}

	public synchronized static ApplicationMamager getInstance() {
		if (null == instance) {
			instance = new ApplicationMamager();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
		// Log.e("add Activity", activity.getLocalClassName());
	}

	

	public void exit(Context c) {

		try {
			if (mList != null) {
				for (Activity activity : mList) {

					if (activity != null)
						activity.finish();
				}
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}

		} catch (Exception e) {
			//Log.e("退出", e.getMessage());
		}
	}
	public void goMainActivity(Context mContext)
	{
		Intent intent = MainActivity_.intent(mContext).get();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mContext.startActivity(intent);
	}
	/**
	 * 返回主界面，清除主界面外的界面
	 * 
	 */
	public void clearActivity() {

		for (int  i=0;i<mList.size() ; i++) {
			if (mList.get(i) != null
					&& (!mList.get(i).getLocalClassName().toLowerCase()
							.equals("mainactivity"))) {
				mList.get(i).finish();
				mList.remove(i);
				// Log.e("destory Activity", activity.getLocalClassName());
			}
		}
		
		System.gc();
	}

	public void quit() {
		for (int  i=0;i<mList.size() ; i++) {
			if (mList.get(i) != null)
			{
				mList.get(i).finish();
				mList.remove(i);
			}
		}
	}
}
