package com.vc.cloudbalance.sqlite;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.DatabaseHelper;
import com.vc.cloudbalance.model.UserMDL;
import com.vc.util.LogUtils;
public class UserDAL {
	Context context;
	DatabaseHelper mDbHelper = null;
	SQLiteDatabase mDb = null;
	String selectPara = " userid,username,qqopenid,qqtoken,qqname,wbopenid,wbtoken,wbname ";

	public UserDAL(Context c) {
		context = c;
		mDbHelper = DatabaseHelper.getInstance(c);
		mDb = mDbHelper.getReadableDatabase();
	}

	public UserMDL Select() {
		try {
			synchronized (App.threadDBLock) {
				String sql = "select " + selectPara
						+ " from User limit 1 ";
				Cursor cursor = mDb.rawQuery(sql, new String[] {  });
				UserMDL i = null;
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

	private UserMDL convert(Cursor cursor) {
		UserMDL i = new UserMDL();
		try {
			i.setUserid(cursor.getString(0));
			i.setUsername(cursor.getString(1));
			i.setQqopenid(cursor.getString(2));
			i.setQqaccesstoken(cursor.getString(3));
			i.setQqname(cursor.getString(4));
			i.setWbopenid(cursor.getString(5));
			i.setWbaccesstoken(cursor.getString(6));
			i.setWbname(cursor.getString(7));
		} catch (Exception e) {
			// TODO: handle exception
		}

		return i;
	}
	public boolean Del() {
		try {
			synchronized (App.threadDBLock) {
			try {
				mDb.execSQL("delete from User");
				
				return true;
			} catch (Exception e) {

			} 
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}
	public boolean Insert(UserMDL data) {
		try {
			synchronized (App.threadDBLock) {
			String sql = "insert into User (" + selectPara + ") values (?,?,?,?,?,?,?,?)";
			mDb.beginTransaction(); // 手动设置开始事务
			try {
				mDb.execSQL("delete from User");
				// 批量处理操作
				
					mDb.execSQL(
							sql,
							new Object[] { data.getUserid(), data.getUsername(),
									data.getQqopenid(),data.getQqaccesstoken(),data.getQqname(),data.getWbopenid(),data.getWbaccesstoken(),data.getWbname() });
				
				mDb.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
				return true;
			} catch (Exception e) {

			} finally {
				mDb.endTransaction(); // 处理完成
			}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}
}
