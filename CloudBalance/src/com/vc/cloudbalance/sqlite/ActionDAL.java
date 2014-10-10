package com.vc.cloudbalance.sqlite;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.DatabaseHelper;
import com.vc.cloudbalance.model.ActionMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.model.UserMDL;
import com.vc.util.LogUtils;

public class ActionDAL {
	Context context;
	DatabaseHelper mDbHelper = null;
	SQLiteDatabase mDb = null;
	String selectPara = " id,action,data,locked";

	public ActionDAL(Context c) {
		context = c;
		mDbHelper = DatabaseHelper.getInstance(c);
		mDb = mDbHelper.getReadableDatabase();
	}
	public List<ActionMDL> SelectByAction(String action ) {
		try {
			
			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Action where action=? and locked=0";
				Cursor cursor = mDb
						.rawQuery(sql,new String[] {action });
				List<ActionMDL> datas = new LinkedList<ActionMDL>();
				while (cursor.moveToNext()) {
					ActionMDL i = null;
					i = convert(cursor);
					datas.add(i);
				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public List<ActionMDL> Select( ) {
		try {
			
			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Action where locked=0 ";
				Cursor cursor = mDb
						.rawQuery(sql,new String[] { });
				List<ActionMDL> datas = new LinkedList<ActionMDL>();
				while (cursor.moveToNext()) {
					ActionMDL i = null;
					i = convert(cursor);
					datas.add(i);
				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public ActionMDL Select(String action,String data ) {
		try {
			
			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Action where action=? and data=? and  locked=0 ";
				Cursor cursor = mDb
						.rawQuery(sql,new String[] {action,data });
				ActionMDL i = null;
				if (cursor.moveToNext()) {
					
					i = convert(cursor);
					
				}
				cursor.close();
				return i;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	private ActionMDL convert(Cursor cursor) {
		ActionMDL i = new ActionMDL();
		try {
			i.setId(cursor.getString(0));
			i.setAction(cursor.getString(1));
			i.setData(cursor.getString(2));
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return i;
	}
	public boolean Locked(String id ,int lock)
	{
		try {
			String sql = "update Action set locked=? where id=?";
			
			try {
			
				// 批量处理操作
				
					mDb.execSQL(
							sql,
							new Object[] { lock,id });
				
				
				return true;
			} catch (Exception e) {

			} 

			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}
	public boolean Del(String id) {
		try {
			
			try {
				mDb.execSQL("delete from Action where id="+id);
				
				return true;
			} catch (Exception e) {

			} 

			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}
	
	public boolean Insert(ActionMDL data) {
		try {
			String sql = "insert into Action (" + selectPara + ") values (?,?,?,0)";
			
			try {
			
				// 批量处理操作
				
					mDb.execSQL(
							sql,
							new Object[] { data.getId(), data.getAction(),
									data.getData() });
				
				
				return true;
			} catch (Exception e) {

			} 

			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}
}
