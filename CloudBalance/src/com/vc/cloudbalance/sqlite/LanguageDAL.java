package com.vc.cloudbalance.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.DatabaseHelper;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.LanguageMDL;

public class LanguageDAL {
	Context context;
	// DatabaseHelper mDbHelper = null;
	SQLiteDatabase mDb = null;
	// 数据库存储路径
	String filePath = App.packageName+"/language.db";
	String pathStr =  App.packageName;

	public SQLiteDatabase openDatabase(Context context) {
		System.out.println("filePath:" + filePath);
		File jhPath = new File(filePath);
		// 查看数据库文件是否存在
		if (jhPath.exists()) {
			// 存在则直接返回打开的数据库
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		} else {
			// 不存在先创建文件夹
			File path = new File(pathStr);
			if (!path.exists()) {
				if (path.mkdir()) {
					System.out.println("创建成功");
				} else {
					System.out.println("创建失败");
				}
			}
			try {
				// 得到资源
				AssetManager am = context.getAssets();
				// 得到数据库的输入流
				InputStream is = am.open("language.db");
				// 用输出流写到SDcard上面
				FileOutputStream fos = new FileOutputStream(jhPath);
				// 创建byte数组 用于1KB写一次
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// 最后关闭就可以了
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			// 如果没有这个数据库 我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
			return openDatabase(context);
		}
	}

	public LanguageDAL(Context c) {
		context = c;

		mDb = openDatabase(context);

	}

	public HashMap<String, LanguageMDL> select() {
		String result = "";
		try {
			synchronized (App.threadDBLock) {

				String sql;
				sql = "select key,lan1,lan2 from language  ";
				Cursor cursor = mDb.rawQuery(sql, new String[] {});
				HashMap<String, LanguageMDL> languages = new HashMap<String, LanguageMDL>();
				while (cursor.moveToNext()) {
					LanguageMDL languageMDL = new LanguageMDL(
							cursor.getString(0), cursor.getString(1),
							cursor.getString(2));
					languages.put(cursor.getString(0), languageMDL);
				}
				cursor.close();
				mDb.close();
				return languages;
			}
		} catch (Exception e) {
			// TODO: handle exception
			if(mDb!=null)
				mDb.close();
			return null;
		}
	}

}
