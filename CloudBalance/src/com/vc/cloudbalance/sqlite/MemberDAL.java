package com.vc.cloudbalance.sqlite;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.DatabaseHelper;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.util.LogUtils;
import com.vc.util.ObjectHelper;

public class MemberDAL {
	Context context;
	DatabaseHelper mDbHelper = null;
	SQLiteDatabase mDb = null;
	String selectPara = "memberid , membername ,iconfile ,birthday ,height ,waist ,sex ,targetweight ,modeltype ,upload,userid,clientid ,clientImg";

	public MemberDAL(Context c) {
		context = c;
		mDbHelper = DatabaseHelper.getInstance(c);
		mDb = mDbHelper.getReadableDatabase();
	}

	public MemberMDL SelectById(String memberid) {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Member  where memberid=? ";
				Cursor cursor = mDb.rawQuery(sql, new String[] { memberid });
				MemberMDL datas = null;
				if (cursor.moveToNext()) {
					datas = convert(cursor);

				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public MemberMDL SelectByClientId(String clientid) {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Member  where clientid=? ";
				Cursor cursor = mDb.rawQuery(sql, new String[] { clientid });
				MemberMDL datas = null;
				if (cursor.moveToNext()) {
					datas = convert(cursor);

				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public List<MemberMDL> Select(String userid) {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Member  where userid=? ";
				Cursor cursor = mDb.rawQuery(sql, new String[] { userid });
				List<MemberMDL> datas = new LinkedList<MemberMDL>();
				while (cursor.moveToNext()) {
					MemberMDL i = null;
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

	public List<MemberMDL> SelectGuest() {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Member  where userid is null or userid='' ";
				Cursor cursor = mDb.rawQuery(sql, new String[] {});
				List<MemberMDL> datas = new LinkedList<MemberMDL>();
				while (cursor.moveToNext()) {
					MemberMDL i = null;
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

	public List<MemberMDL> SelectUnUpload() {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from Member  where upload=0 ";
				Cursor cursor = mDb.rawQuery(sql, new String[] {});
				List<MemberMDL> datas = new LinkedList<MemberMDL>();
				while (cursor.moveToNext()) {
					MemberMDL i = null;
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

	// memberid , membername ,iconfile ,birthday ,height ,waist ,sex
	// ,targetweight ,modeltype ,upload,userid
	private MemberMDL convert(Cursor cursor) {
		MemberMDL i = new MemberMDL();
		try {
			i.setMemberid(cursor.getString(0));
			i.setMembername(cursor.getString(1));
			i.setIconfile(cursor.getString(2));
			i.setBirthday(cursor.getString(3));
			i.setHeight(cursor.getString(4));
			i.setWaist(cursor.getString(5));
			i.setSex(cursor.getString(6));
			i.setTargetweight(cursor.getString(7));
			i.setModeltype(cursor.getString(8));
			i.setUpload(cursor.getInt(9));
			i.setUserid(cursor.getString(10));
			i.setClientid(cursor.getString(11));
			i.setClientImg(cursor.getBlob(12));
		} catch (Exception e) {
			// TODO: handle exception
		}
		// memberid , membername ,iconfile ,birthday ,height ,waist ,sex
		// ,targetweight ,modeltype ,upload,userid
		return i;
	}

	public boolean Insert(MemberMDL data) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "insert into Member (" + selectPara
						+ ") values (? , ? ,? ,? ,? ,? ,? ,? ,? ,?,?,?,?)";

				try {
					mDb.execSQL(
							sql,
							new Object[] { data.getMemberid(),
									data.getMembername(), data.getIconfile(),
									data.getBirthday(), data.getHeight(),
									data.getWaist(), data.getSex(),
									data.getTargetweight(),
									data.getModeltype(), data.getUpload(),
									data.getUserid(), data.getClientid(),
									data.getClientImg() });
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean DelByMemberId(String memberid) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "delete from Member where memberid=?";

				try {
					mDb.execSQL(sql, new Object[] { memberid });
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean DelByClientId(String clientId) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "delete from Member where clientid=?";

				try {
					mDb.execSQL(sql, new Object[] { clientId });
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean Insert(List<MemberMDL> members) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "insert into Member (" + selectPara
						+ ") values (? , ? ,? ,? ,? ,? ,? ,? ,? ,?,?,?,?)";

				try {
					mDb.beginTransaction();
					mDb.execSQL("delete from Member ");
					for (MemberMDL data : members) {
						mDb.execSQL(
								sql,
								new Object[] { data.getMemberid(),
										data.getMembername(),
										data.getIconfile(), data.getBirthday(),
										data.getHeight(), data.getWaist(),
										data.getSex(), data.getTargetweight(),
										data.getModeltype(), data.getUpload(),
										data.getUserid(), data.getClientid(),
										data.getClientImg() });

					}
					mDb.setTransactionSuccessful();
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				} finally {
					mDb.endTransaction();
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean UpdateUploadByMemberId(String memberid) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "update Member upload=1  where memberid=?";

				try {
					mDb.execSQL(sql, new Object[] { memberid });

					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean UpdateImage(MemberMDL mdl) {
		try {
			synchronized (App.threadDBLock) {
				mDb.beginTransaction(); // 手动设置开始事务

				try {
					if (!TextUtils.isEmpty(mdl.getClientid())) {
						String sql = "update Member set  clientImg=? where clientid=?";
						mDb.execSQL(
								sql,
								new Object[] { mdl.getClientImg(),
										mdl.getClientid() });

					} else if (!TextUtils.isEmpty(mdl.getMemberid())) {
						String sql = "update Member set  clientImg=? where memberid=?";
						mDb.execSQL(
								sql,
								new Object[] { mdl.getClientImg(),
										mdl.getMemberid() });
					}

					mDb.setTransactionSuccessful();
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				} finally {
					mDb.endTransaction();
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean UpdateUploadByMemberId(String clientid, String memberid) {
		try {
			synchronized (App.threadDBLock) {
				mDb.beginTransaction(); // 手动设置开始事务
				String sql = "update Member set  upload=1,memberid=?  where clientid=?";

				try {
					mDb.execSQL(sql, new Object[] { memberid, clientid });
					sql = "update BalanceData memberid=?  where clientmemberid=?";
					mDb.execSQL(sql, new Object[] { memberid, clientid });
					mDb.setTransactionSuccessful();
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				} finally {
					mDb.endTransaction();
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean UpdateUploadByClientId(String memberid, String clientid) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "update Member set upload=1 ,memberid =? where clientid=?";

				try {
					mDb.execSQL(sql, new Object[] { memberid, clientid });
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
				}
			}
			return false;

		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.e(e.toString());
			return false;
		}
	}

	public boolean Update(MemberMDL data) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "update Member set membername=?,iconfile=? , birthday=?, height=?,waist=?,sex=?,targetweight=?,modeltype=?,upload=?,userid=?,clientImg=? where memberid=?";

				try {
					mDb.execSQL(
							sql,
							new Object[] { data.getMembername(),
									data.getIconfile(), data.getBirthday(),
									data.getHeight(), data.getWaist(),
									data.getSex(), data.getTargetweight(),
									data.getModeltype(), data.getUpload(),
									data.getUserid(), data.getClientImg(),
									data.getMemberid() });
					return true;
				} catch (Exception e) {
					LogUtils.e(e.toString());
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
