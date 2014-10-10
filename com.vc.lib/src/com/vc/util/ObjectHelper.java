package com.vc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class ObjectHelper {
	public static String Convert2String(Date date, String type) {
		if (date == null)
			return "";
		DateFormat format = new SimpleDateFormat(type);
		String strString = format.format(date);
		return strString;
	}
	public static String trim(String str)
	{
		if(str==null)
			return "";
		else
			return str.trim();
	}
	/**
	 * @Name: Convert2MathCount
	 * @Author: 陈伟炎
	 * @Date: 2012-8-11
	 * @param count
	 * @param obj
	 * @return
	 * @Description: 返回小数点后几位数
	 */
	public static String Convert2MathCount(int count, Object obj) {
		if (obj == null)
			return "";
		try {
			return String.format("%." + count + "f", obj);
		} catch (Exception e) {
			// TODO: handle exception
			return obj.toString();
		}
	}

	public static String FormatDateString(int year, int month, int dayOfMonth) {
		String strmonth = "";
		String dayofmonthstr = "";
		if (month + 1 < 10) {
			strmonth = "0" + (month + 1);
		} else {
			strmonth = (month + 1) + "";
		}
		if (dayOfMonth < 10) {
			dayofmonthstr = "0" + dayOfMonth;
		} else {
			dayofmonthstr = dayOfMonth + "";
		}
		return year + "-" + strmonth + "-" + dayofmonthstr + "";
	}

	public static String Convert2String(Object value) {
		try {
			return String.valueOf(value);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}

	public static JSONObject Convert2JsonObject(Object value) {
		try {
			return new JSONObject(value.toString());
		} catch (Exception e) {
			// TODO: handle exception
			return new JSONObject();
		}
	}

	public static long Convert2Long(Object object) {
		try {
			long l = Long.parseLong(object.toString());
			return Long.parseLong(object.toString());
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static double Convert2Double(Object object) {
		try {

			return Double.parseDouble(object.toString());
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static Date Convert2Date(String dateString) {
		return Convert2Date(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date Convert2Date(String dateString, String type) {
		if (dateString == null || dateString.trim().equals("")
				|| dateString.trim().equals("null"))
			return null;
		DateFormat df = new SimpleDateFormat(type);
		Date date = new Date();
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block\
			Log.e("StringToDate", dateString + "    " + e);
			e.printStackTrace();
		}
		return date;
	}

	public static int Convert2Int(Object obj) {
		try {
			return Integer.valueOf(obj.toString().trim());
		} catch (Exception e) {
			// TODO: handle exceptionet
			return 0;
		}
	}

	public static float Convert2Float(String val) {
		try {
			return Float.valueOf(val);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static String GetBundleString(Activity activity, String key) {
		try {
			Bundle bundle = activity.getIntent().getExtras();
			if (bundle == null)
				return "";
			String value = bundle.getString(key);
			if (value == null || value.equals(""))
				return "";
			return value;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}

	}

	public static int GetBundleInt(Activity activity, String key) {
		try {
			Bundle bundle = activity.getIntent().getExtras();
			if (bundle == null)
				return 0;
			return bundle.getInt(key, 0);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	public static String GetBundleString(Intent intent, String key) {
		try {
			Bundle bundle = intent.getExtras();
			if (bundle == null)
				return "";
			String value = bundle.getString(key);
			if (value == null || value.equals(""))
				return "";
			return value;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}

	}

	public static int GetBundleInt(Intent intent, String key) {
		try {
			Bundle bundle = intent.getExtras();
			if (bundle == null)
				return 0;
			return bundle.getInt(key, 0);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	/**
	 * 获取日期离今天还有多少天
	 * 
	 * @param smdate
	 * @return
	 */
	public static int GetReciprocalDays(String smdate) {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return daysBetween(sdf.format(today), smdate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 验证是否IP地址
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIP(String ip) {
		Pattern pattern = Pattern.compile("(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])");
		
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	/**
	 * 验证是否数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			Integer.valueOf(str.toString());
			return true;
		} catch (Exception e) {
			// TODO: handle exceptionet
			return false;
		}

	}

	/**
	 * 验证是否空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(EditText et) {
		try {

			return et.getText().toString().trim().equals("");
		} catch (Exception e) {
			// TODO: handle exceptionet
			return true;
		}

	}
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern

		.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}

	public static boolean isEmail(String email) {

		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		Pattern p = Pattern.compile(str);

		Matcher m = p.matcher(email);

		return m.matches();

	}
}
