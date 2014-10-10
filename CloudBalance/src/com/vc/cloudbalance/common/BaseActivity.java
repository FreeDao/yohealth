package com.vc.cloudbalance.common;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import com.vc.cloudbalance.R;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
@NoTitle
@EActivity
public class BaseActivity extends Activity{
	public Context mContext;
	@ViewById
	public	Button btnBack;
	@ViewById
	public TextView tvTitle;
	@Click(R.id.btnBack)
	public void goBack()
	{
		this.finish();
	}
	
	@AfterViews
	public void InitBase()
	{
		ApplicationMamager.getInstance().addActivity(this);
		mContext=this;
	}
	public void setTitleText(String str)
	{
		tvTitle.setText(str);
	}
	public App GetApp()
	{
		return App.getApp(this);
	}
	
	
	
}
