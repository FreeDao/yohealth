package com.vc.cloudbalance.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class UIHelper {
	public static void SetText(int id,String key,Activity activity)
	{
		View view =activity.findViewById(id);
		String val=LanguageHelper.GetVal(key, activity);
		if(val.equals(""))
			return ;
		if(view instanceof TextView)
		{
			((TextView)(view)).setText(val);
		}
		else if(view instanceof Button)
		{
			((Button)(view)).setText(val);
		}
		else if (view instanceof RadioButton) {
			((RadioButton)(view)).setText(val);
		}
		
	}
	public static void SetText(int id,String key,View parentView)
	{
		View view =parentView.findViewById(id);
		String val=LanguageHelper.GetVal(key, parentView.getContext());
		if(val.equals(""))
			return ;
		if(view instanceof TextView)
		{
			((TextView)(view)).setText(val);
		}
		else if(view instanceof Button)
		{
			((Button)(view)).setText(val);
		}
		else if (view instanceof RadioButton) {
			((RadioButton)(view)).setText(val);
		}
	}
}
