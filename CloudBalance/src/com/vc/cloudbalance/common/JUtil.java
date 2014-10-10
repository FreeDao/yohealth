package com.vc.cloudbalance.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

public class JUtil {
	public static boolean GetJsonStatu(JSONObject jsonObject) {
		try {
			if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
	}

	public static boolean GetJsonStatu(JSONArray jsonArray) {
		try {
			if (jsonArray.getJSONObject(1).getJSONObject("status").getString("mark").equals("ok")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取接口返回的错误信息
	 * 
	 * @Title: GetErrorString
	 * @Description: TODO
	 * @param jsonObject
	 * @param key
	 * @return
	 * @return: String
	 */
	public static String GetErrorString(JSONObject jsonObject) {
		try {
			return jsonObject.getString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 
	 * @Title: GetnormalString
	 * @Description: TODO
	 * @param jsonObject
	 * @param key
	 * @return
	 * @return: String
	 */
	public static String GetNormalString(JSONObject jsonObject, String key) {
		try {
			return jsonObject.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String GetJsonString(String val) {
		String reString = "";
		if (val.toLowerCase().equals("null"))
			return reString;
		return val;
	}
	
	public static String GetString(JSONObject jsonObject, String key) {
		try {
			return GetJsonString(jsonObject.getJSONObject("data").getString(key));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "";
		}
	}

	public static String GetString(Map<String, String> map, String key) {
		try {
			return GetJsonString(map.get(key));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "";
		}
	}

	public static Date GetDateDefault(JSONObject jsonObject, String key, String type, Locale locale) {
		try {
			String dateString = GetString(jsonObject, key);
			if (dateString == null || dateString.trim().equals("") || dateString.trim().equals("null"))
				return null;
			DateFormat df = new SimpleDateFormat(type, locale);
			Date date = new Date();
			try {
				date = df.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block\
				Log.e("StringToDate", dateString + "    " + e);
				e.printStackTrace();
			}
			return date;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static JSONObject GetJsonObject(JSONObject jsonObject, String key) {
		try {
			return new JSONObject(GetString(jsonObject, key));
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	// 序列化类为字符串
	public static String object2String(Object obj) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			String serStr = byteArrayOutputStream.toString("ISO-8859-1");
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

			objectOutputStream.close();
			byteArrayOutputStream.close();
			return serStr;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}

	}

	// 反序列化字符串为对象
	@SuppressWarnings("unchecked")
	public static Object getObjectFromString(String serStr) {
		try {
			String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
			return object;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public static boolean GetJsonStatu(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static <T> T fromJson(String json, Type typeOfT) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			return fromJson(jsonObject, typeOfT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static <T> T fromJson1(JSONObject json, Type typeOfT) {
		Gson gson = new Gson();
		String jsonString = "";
		try {
			jsonString = GetString(json, "boardarray");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.fromJson(jsonString, typeOfT);
	}

	public static <T> T fromJson(JSONObject json, Type typeOfT) {

		Gson gson = new Gson();
		String jsonString = "";
		try {
			jsonString = json.getString("data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.fromJson(jsonString, typeOfT);
	}

	public static <T> T fromJson(JSONObject json, Class<T> classOfT) {

		Gson gson = new Gson();
		String jsonString = "";
		try {
			jsonString = json.getString("data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.fromJson(jsonString, classOfT);
	}

	public static String toJson(Object obj) {
		Gson gson = new Gson();
		String jsonString = "";
		try {
			jsonString = gson.toJson(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}

	public static <T> T fromJson(String json, Class<T> classOfT) {

		Gson gson = new Gson();
		return gson.fromJson(json, classOfT);
	}
	public static <T> T simpleFromJson(String json,Type typeOfT) {

		Gson gson = new Gson();
		
		return gson.fromJson(json, typeOfT);
	}
}
