package com.vc.cloudbalance.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "yohealth";
	public static final int DATABASE_VERSION = 1;

	private static final String AppConfig_CREATE = "CREATE TABLE [AppConfig] ([KeyName] NVARCHAR2, [Value] NVARCHAR2);";
	private static final String User_CREATE = "CREATE TABLE [User] ([userid] NVARCHAR2, [username] NVARCHAR2,[qqopenid] NVARCHAR2,[qqtoken] NVARCHAR2,[qqname] NVARCHAR2,[wbopenid] NVARCHAR2,[wbtoken] NVARCHAR2,[wbname] NVARCHAR2);";
	private static final String Member_CREATE = "CREATE TABLE [Member] ([clientid] NVARCHAR2,[memberid] NVARCHAR2, [membername] NVARCHAR2,[iconfile] NVARCHAR2,[birthday] NVARCHAR2,[height] NVARCHAR2,[waist] NVARCHAR2,[sex] NVARCHAR2,[targetweight] NVARCHAR2,[modeltype] NVARCHAR2,[userid] NVARCHAR2,[upload] INTEGER,[clientImg] BLOB);";
	private static final String Action_CREATE = "CREATE TABLE [Action] ([id] NVARCHAR2,[action] NVARCHAR2, [data] NVARCHAR2,[locked] INTEGER);";
	private static final String BalanceData_CREATE = "CREATE TABLE [BalanceData] ([id] integer PRIMARY KEY autoincrement,[memberid] NVARCHAR2, [userid] NVARCHAR2,[weidate] NVARCHAR2,[weight] NVARCHAR2,[bmi] NVARCHAR2,[fatpercent] NVARCHAR2,[muscle] NVARCHAR2,[bone] NVARCHAR2,[water] NVARCHAR2,[basalmetabolism] NVARCHAR2,[innerfat] NVARCHAR2,[upload] INTEGER,[clientmemberid] NVARCHAR2,[dataid] NVARCHAR2,[clientImg] BLOB,[height] NVARCHAR2,[haveimg] NVARCHAR2,[imgurl] NVARCHAR2);";
	private static DatabaseHelper instance;

	public synchronized static DatabaseHelper getInstance(Context c) {
		if (null == instance) {
			instance = new DatabaseHelper(c);
		}
		return instance;
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context c) {
		// TODO Auto-generated constructor stub
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(AppConfig_CREATE);
		db.execSQL(User_CREATE);
		db.execSQL(Member_CREATE);
		db.execSQL(Action_CREATE);
		db.execSQL(BalanceData_CREATE);
		init(db);
	}
	private void init(SQLiteDatabase db)
	{
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion <= 1) {

		}

	}

}