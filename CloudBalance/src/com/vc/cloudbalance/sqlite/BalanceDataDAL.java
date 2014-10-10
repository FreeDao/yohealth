package com.vc.cloudbalance.sqlite;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.DatabaseHelper;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.util.LogUtils;

public class BalanceDataDAL {
	Context context;
	DatabaseHelper mDbHelper = null;
	SQLiteDatabase mDb = null;
	String selectPara = "id,memberid,userid,weidate,weight,bmi,fatpercent,muscle,bone,water,basalmetabolism,innerfat,upload,clientmemberid,dataid,clientImg,height,haveimg,imgurl";

	public BalanceDataDAL(Context c) {
		context = c;
		mDbHelper = DatabaseHelper.getInstance(c);
		mDb = mDbHelper.getReadableDatabase();
	}
	public BalanceDataMDL SelectLastData(String memberid,String clientid)
	{
		try {

			synchronized (App.threadDBLock) {
				String sql ;
				sql="select * from BalanceData where 1=1 %s  order by weidate desc limit 1 ";
				String sqlwhere="";
				if(!TextUtils.isEmpty(memberid))
					sqlwhere+=" and memberid = '"+memberid+"'";
				else{
					if(!TextUtils.isEmpty(clientid))
						sqlwhere+=" and clientmemberid = '"+clientid+"'";
				}
				
				sql=String.format(sql, sqlwhere);
				Cursor cursor = mDb.rawQuery(sql, new String[] {  });
				BalanceDataMDL i = null;
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
	public BalanceDataMDL SelectById(String id) {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from BalanceData  where id=? ";
				Cursor cursor = mDb.rawQuery(sql, new String[] { id });
				BalanceDataMDL datas = null;
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
	public List<BalanceDataMDL> SelectUnUploadData() {
		try {

			synchronized (App.threadDBLock) {
				String sql ;
				sql="select * from BalanceData where id in (select max(id) as maxid from BalanceData  group by substr(weidate,0,11)) and upload =0 order by weidate desc ";
				Cursor cursor = mDb.rawQuery(sql, new String[] {  });
				List<BalanceDataMDL> datas = new LinkedList<BalanceDataMDL>();
				while (cursor.moveToNext()) {
					BalanceDataMDL i = null;
					i = convert(cursor);
					datas.add(i);
				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return new LinkedList<BalanceDataMDL>();
		}
	}
	public List<BalanceDataMDL> SelectThisDateData(String memberid,String clientid,String startTime,String endTime) {
		try {

			synchronized (App.threadDBLock) {
				String sql ;
				sql="select * from BalanceData where weidate >=? and weidate<=? %s order by weidate desc ";
				String sqlwhere="";
				if(!TextUtils.isEmpty(memberid))
					sqlwhere+=" and memberid = '"+memberid+"'";
				else{
					if(!TextUtils.isEmpty(clientid))
						sqlwhere+=" and clientmemberid = '"+clientid+"'";
				}
				sql=String.format(sql, sqlwhere);
				Cursor cursor = mDb.rawQuery(sql, new String[] {startTime ,endTime });
				List<BalanceDataMDL> datas = new LinkedList<BalanceDataMDL>();
				while (cursor.moveToNext()) {
					BalanceDataMDL i = null;
					i = convert(cursor);
					datas.add(i);
				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return new LinkedList<BalanceDataMDL>();
		}
	}
	public List<BalanceDataMDL> SelectDayDataByTime(String memberid,String clientid,String startTime,String endTime) {
		try {

			synchronized (App.threadDBLock) {
				String sql ;
				sql="select * from BalanceData where id in (select max(id) as maxid from BalanceData where 1=1 %s group by substr(weidate,0,11))  order by weidate desc ";
				String sqlwhere="";
				if(!TextUtils.isEmpty(memberid))
					sqlwhere+=" and memberid = '"+memberid+"'";
				else{
					if(!TextUtils.isEmpty(clientid))
						sqlwhere+=" and clientmemberid = '"+clientid+"'";
				}
				
				if(!TextUtils.isEmpty(startTime)&&!TextUtils.isEmpty(endTime))
					sqlwhere+=" and weidate >= '"+startTime+"' and weidate< '"+endTime+"' ";
				sql=String.format(sql, sqlwhere);
				Cursor cursor = mDb.rawQuery(sql, new String[] {  });
				List<BalanceDataMDL> datas = new LinkedList<BalanceDataMDL>();
				while (cursor.moveToNext()) {
					BalanceDataMDL i = null;
					i = convert(cursor);
					datas.add(i);
				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return new LinkedList<BalanceDataMDL>();
		}
	}
	public int SelectCountById(String memberId) {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  count(*) from BalanceData where memberid =? group by substr(weidate,0,11) ";
				Cursor cursor = mDb.rawQuery(sql, new String[] { memberId });
				
				if (cursor.moveToNext()) {
					return cursor.getInt(0);

				}
				cursor.close();
				return 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	public List<BalanceDataMDL> SelectByMemberId(String memberId) {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from BalanceData  where memberid=? order by weidate desc";
				Cursor cursor = mDb.rawQuery(sql, new String[] { memberId });
				List<BalanceDataMDL> datas = new LinkedList<BalanceDataMDL>();
				while (cursor.moveToNext()) {
					BalanceDataMDL i = null;
					i = convert(cursor);
					datas.add(i);
				}
				cursor.close();
				return datas;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return new LinkedList<BalanceDataMDL>();
		}
	}

	public List<BalanceDataMDL> SelectByClientMemberId(String clientmemberId) {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  " + selectPara
						+ " from BalanceData  where clientmemberId=? order by weidate desc";
				Cursor cursor = mDb.rawQuery(sql,
						new String[] { clientmemberId });
				List<BalanceDataMDL> datas = new LinkedList<BalanceDataMDL>();
				while (cursor.moveToNext()) {
					BalanceDataMDL i = null;
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

	public List<BalanceDataMDL> SelectUnUpload() {
		try {

			synchronized (App.threadDBLock) {
				String sql = "select  "
						+ selectPara
						+ " from BalanceData  where upload=0 and memberid !='' and memberid is not null ";
				Cursor cursor = mDb.rawQuery(sql, new String[] {});
				List<BalanceDataMDL> datas = new LinkedList<BalanceDataMDL>();
				while (cursor.moveToNext()) {
					BalanceDataMDL i = null;
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

	// id,memberid,userid,weidate,weight,bmi,fatpercent,muscle,bone,water,basalmetabolism,innerfat,upload,clientmemberid
	private BalanceDataMDL convert(Cursor cursor) {
		BalanceDataMDL i = new BalanceDataMDL();
		try {
			i.setId(cursor.getInt(0));
			i.setMemberid(cursor.getString(1));
			i.setUserid(cursor.getString(2));
			i.setWeidateString(cursor.getString(3));
			i.setWeight(cursor.getString(4));
			i.setBmi(cursor.getString(5));
			i.setFatpercent(cursor.getString(6));
			i.setMuscle(cursor.getString(7));
			i.setBone(cursor.getString(8));
			i.setWater(cursor.getString(9));
			i.setBasalmetabolism(cursor.getString(10));
			i.setInnerfat(cursor.getString(11));
			i.setUpload(cursor.getInt(12));
			i.setClientmemberid(cursor.getString(13));
			i.setDataid(cursor.getString(14));
			i.setClientImg(cursor.getBlob(15));
			i.setHeight(cursor.getString(16));
			i.setHaveimg(cursor.getString(17));
			i.setPicurl(cursor.getString(18));
		} catch (Exception e) {
			// TODO: handle exception
		}
		// id,memberid,userid,weidate,weight,bmi,fatpercent,muscle,bone,water,basalmetabolism,innerfat,upload,clientmemberid
		return i;
	}
	public Boolean UpdateUnloadData(BalanceDataMDL data )
	{
		try {
			synchronized (App.threadDBLock) {
				String sql = "update BalanceData set upload=1 where id=?";

				try {
					mDb.execSQL(
							sql,
							new Object[] { data.getId()});
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
	public Boolean setUnloadData(BalanceDataMDL data )
	{
		try {
			synchronized (App.threadDBLock) {
				String sql = "update BalanceData set upload=0 where id=?";

				try {
					mDb.execSQL(
							sql,
							new Object[] { data.getId()});
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
	public boolean Insert(BalanceDataMDL data) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "insert into BalanceData (" + selectPara
						+ ") values (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
				try {
					mDb.execSQL(
							sql,
							new Object[] {  data.getMemberid(),
									data.getUserid(), data.getWeidateString(),
									data.getWeight(), data.getBmi(),
									data.getFatpercent(), data.getMuscle(),
									data.getBone(), data.getWater(),
									data.getBasalmetabolism(),
									data.getInnerfat(), data.getUpload(),
									data.getClientmemberid(),data.getDataid(),data.getClientImg(),data.getHeight(),data.getHaveimg(),data.getPicurl() });
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
	
	public boolean DelById(int id) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "delete from BalanceData where id=?";

				try {
					mDb.execSQL(sql, new Object[] { id });
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
				String sql = "delete from BalanceData where memberid=?";

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
	public boolean UpdateImage(BalanceDataMDL mdl) {
		try {
			synchronized (App.threadDBLock) {
				mDb.beginTransaction(); // 手动设置开始事务

				try {
					
						String sql = "update BalanceData set  clientImg=? where clientid=?";
						mDb.execSQL(
								sql,
								new Object[] { mdl.getClientImg(),
										mdl.getId() });

					
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
	public boolean Insert(MemberMDL member, List<BalanceDataMDL> balancedatas) {
		try {
			synchronized (App.threadDBLock) {
				String sql = "insert into BalanceData (" + selectPara
						+ ") values (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				try {
					mDb.beginTransaction();
					mDb.execSQL("delete from BalanceData where memberid= ?",
							new Object[] { member.getMemberid() });
					for (BalanceDataMDL data : balancedatas) {
						mDb.execSQL(
								sql,
								new Object[] { 
										 member.getMemberid(), member.getUserid(),
										data.getWeidateString(),
										data.getWeight(), data.getBmi(),
										data.getFatpercent(), data.getMuscle(),
										data.getBone(), data.getWater(),
										data.getBasalmetabolism(),
										data.getInnerfat(), data.getUpload(),
										data.getClientmemberid() ,data.getDataid(),data.getClientImg(),data.getHeight(),data.getHaveimg(),data.getPicurl()});

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

}
