package com.vc.cloudbalance.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.DatabaseHelper;

public class AppConfigDAL {
	Context context;
	DatabaseHelper mDbHelper = null;
	SQLiteDatabase mDb = null;

	public AppConfigDAL(Context c) {
		context = c;
		mDbHelper = DatabaseHelper.getInstance(c);
		mDb = mDbHelper.getReadableDatabase();
	}
	public String select(String key)
	{
		String result = "";
		try {
			synchronized (App.threadDBLock) {
				
				String sql = "select [Value] from AppConfig where KeyName =?";
				Cursor cursor = mDb.rawQuery(sql,new String[]{key});
				if (cursor.moveToFirst()) {
					result=cursor.getString(0);
				}
				
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	public boolean insert(String key,String val)
	{
		boolean result = false;
		try {
			synchronized (App.threadDBLock) {
				
				String sql = "delete from AppConfig where KeyName =?";
				mDb.execSQL(sql,new Object[]{key});
				sql = "insert into  AppConfig (KeyName,[Value]) values (?,?)";
				mDb.execSQL(sql,new Object[]{key,val});
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
}
