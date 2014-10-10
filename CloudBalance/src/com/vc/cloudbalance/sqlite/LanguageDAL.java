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
	// ���ݿ�洢·��
	String filePath = App.packageName+"/language.db";
	String pathStr =  App.packageName;

	public SQLiteDatabase openDatabase(Context context) {
		System.out.println("filePath:" + filePath);
		File jhPath = new File(filePath);
		// �鿴���ݿ��ļ��Ƿ����
		if (jhPath.exists()) {
			// ������ֱ�ӷ��ش򿪵����ݿ�
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		} else {
			// �������ȴ����ļ���
			File path = new File(pathStr);
			if (!path.exists()) {
				if (path.mkdir()) {
					System.out.println("�����ɹ�");
				} else {
					System.out.println("����ʧ��");
				}
			}
			try {
				// �õ���Դ
				AssetManager am = context.getAssets();
				// �õ����ݿ��������
				InputStream is = am.open("language.db");
				// �������д��SDcard����
				FileOutputStream fos = new FileOutputStream(jhPath);
				// ����byte���� ����1KBдһ��
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// ���رվͿ�����
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			// ���û��������ݿ� �����Ѿ�����д��SD�����ˣ�Ȼ����ִ��һ��������� �Ϳ��Է������ݿ���
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
