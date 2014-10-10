package com.vc.cloudbalance.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.vc.cloudbalance.model.LanguageMDL;
import com.vc.cloudbalance.sqlite.AppConfigDAL;
import com.vc.cloudbalance.sqlite.LanguageDAL;

public class LanguageHelper {
	private static List<LanguageMDL> _Languages;
	private static HashMap<String,LanguageMDL> Maps;
	private static String LanguageVal="";
	public static List<LanguageMDL> GetLanguages()
	{
		if(_Languages==null)
		{
			init();
		}
		return _Languages;
	}
	private static void init()
	{
		Maps=new HashMap<String, LanguageMDL>();
		
		
	}
	public static String GetVal(String key,Context mContext)
	{
		if(Maps==null)
			Maps=new LanguageDAL(mContext).select();
		if(Maps==null)
			return "";
		LanguageMDL language= Maps.get(key);
		if(language==null)
			return "";
		if(LanguageVal.equals(""))
			LanguageVal=new AppConfigDAL(mContext).select(Constants.SQL_KEY_LANGUAGE_STRING);
		return language.GetVal(LanguageVal);
	}
}
