package com.vc.cloudbalance.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.vc.cloudbalance.R;
import com.vc.cloudbalance.model.ActionMDL;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberDataCountMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.ActionDAL;
import com.vc.cloudbalance.sqlite.BalanceDataDAL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.webservice.BalanceDataWS;
import com.vc.cloudbalance.webservice.MemberWS;
import com.vc.util.ObjectHelper;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Common {

	public static int GetAgeByBirthday(String birthdayStr) {
		Date birthday = ObjectHelper.Convert2Date(birthdayStr, "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}
		return age;

	}

	public static String GetDeviceId(Context mContext) {
		TelephonyManager tm = (TelephonyManager) mContext
				.getApplicationContext().getSystemService(
						Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId() == null ? "" : tm.getDeviceId();
		if (imei.equals("")) {
			imei = getMAC();
		}
		if (imei.equals("")) {
			imei = Secure.getString(mContext.getApplicationContext()
					.getContentResolver(), Secure.ANDROID_ID);
		}
		return imei;
	}

	public static String getMAC() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

	public static String[] GetLanguageList() {
		return new String[] { "中文", "English" };
	}
	public static String[] GetHeightList()
	{
		List<String> heights= new LinkedList<String>();
		String[] arrayString = new String[heights.size()];
		//arrayString=items.toArray(arrayString);
		for(int i=10;i<=220;i++)
		{
			heights.add(i+"");
		}
		arrayString=heights.toArray(arrayString);
		return arrayString;
	}
	public static void AddMemberThread(final Context mContext) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AddMember(mContext);
				super.run();
			}

		}.start();
	}

	public static boolean AddMember(Context mContext) {
		if (App.getApp(mContext).getUser() != null) {
			MemberDAL memberDal = new MemberDAL(mContext);
			ActionDAL actionDAL = new ActionDAL(mContext);
			List<ActionMDL> actions = actionDAL
					.SelectByAction(ActionMDL.AddMember);
			if (actions != null && actions.size() > 0) {
				for (ActionMDL actionMDL : actions) {
					actionDAL.Locked(actionMDL.getId(), 1);
					MemberMDL memberMDL = memberDal.SelectByClientId(actionMDL
							.getData());
					if (memberMDL != null
							&& TextUtils.isEmpty(memberMDL.getMemberid())) {

						JSONObject jsonObject = new MemberWS(mContext)
								.insertMember(App.getApp(mContext).getUser()
										.getUserid(), memberMDL);
						if (JUtil.GetJsonStatu(jsonObject)) {
							String memberid = JUtil.GetNormalString(jsonObject,
									"memberid");
							memberMDL.setMemberid(memberid);
							jsonObject = new MemberWS(mContext)
									.updateMemberIcon(memberMDL);
							new MemberDAL(mContext).UpdateUploadByMemberId(
									memberMDL.getClientid(), memberid);
							actionDAL.Del(actionMDL.getId());
						} else {
							actionDAL.Locked(actionMDL.getId(), 0);
						}

					} else {
						actionDAL.Del(actionMDL.getId());
					}
				}
			}
		}
		return false;

	}

	public static void DelMemberThread(final MemberMDL member,
			final Context mContext) {

		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DelMember(member, mContext);
				super.run();
			}

		}.start();
	}

	public static boolean DelMember(MemberMDL member, Context mContext) {
		if (!TextUtils.isEmpty(member.getMemberid())) {
			ActionDAL actionDAL = new ActionDAL(mContext);
			ActionMDL actionMDL = actionDAL.Select(ActionMDL.DelMember,
					member.getMemberid());
			actionDAL.Locked(actionMDL.getId(), 1);
			JSONObject jsonObject = new MemberWS(mContext).deleteMember(
					member.getUserid(), member);
			if (JUtil.GetJsonStatu(jsonObject)) {

				actionDAL.Del(actionMDL.getId());
			} else {
				actionDAL.Locked(actionMDL.getId(), 0);
			}
		} else {

		}

		return false;

	}

	public static void UpdateMemberThread(final MemberMDL member,
			final Context mContext) {

		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				UpdateMember(member, mContext);
				super.run();
			}

		}.start();
	}

	public static boolean UpdateMember(MemberMDL member, Context mContext) {

		if (!TextUtils.isEmpty(member.getMemberid())) {
			ActionDAL actionDAL = new ActionDAL(mContext);
			ActionMDL actionMDL = actionDAL.Select(ActionMDL.UpdateMember,
					member.getClientid());
			actionDAL.Locked(actionMDL.getId(), 1);
			JSONObject jsonObject = new MemberWS(mContext).updateMember(
					member.getUserid(), member);
			if (JUtil.GetJsonStatu(jsonObject)) {
				if (member.getClientImg() != null
						&& member.getClientImg().length > 0)
					jsonObject = new MemberWS(mContext)
							.updateMemberIcon(member);

				actionDAL.Del(actionMDL.getId());
			} else {
				actionDAL.Locked(actionMDL.getId(), 0);
			}
		}

		return false;

	}

	public static void AddBalanceDataThread(final Context mContext) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AddBalanceData(mContext);
				super.run();
			}

		}.start();
	}

	public static boolean AddBalanceData(Context mContext) {
		if (App.getApp(mContext).getUser() != null) {
			BalanceDataDAL dal = new BalanceDataDAL(mContext);

			List<BalanceDataMDL> mdls = dal.SelectUnUploadData();
			for (BalanceDataMDL mdl : mdls) {
				if (mdl != null && !TextUtils.isEmpty(mdl.getMemberid())) {

					JSONObject jsonObject = new BalanceDataWS(mContext)
							.insertBalanceData(mdl);
					if (JUtil.GetJsonStatu(jsonObject)) {

						dal.UpdateUnloadData(mdl);
					} 
				} 
			}

		}
		return false;

	}

	public static void SynBalanceDataThread(final Context mContext) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SynBalanceData(mContext);
				super.run();
			}

		}.start();
	}

	public static boolean SynBalanceData(Context mContext) {
		if (App.getApp(mContext).getUser() != null) {
			String userid = App.getApp(mContext).getUser().getUserid();

			JSONObject jsonObject = new BalanceDataWS(mContext)
					.getMamberdataCount(userid);
			List<MemberDataCountMDL> datas = JUtil.fromJson(jsonObject,
					new TypeToken<List<MemberDataCountMDL>>() {
					}.getType());
			if (datas != null && datas.size() > 0) {
				BalanceDataDAL dal = new BalanceDataDAL(mContext);
				for (int i = 0; i < datas.size(); i++) {
					MemberMDL member = new MemberDAL(mContext).SelectById(datas
							.get(i).getMemberid());
					if (member != null) {
						int data_count = dal.SelectCountById(member
								.getMemberid());
						if (data_count != ObjectHelper.Convert2Int(datas.get(i)
								.getRecordcount())) {
							jsonObject = new BalanceDataWS(mContext)
									.getMamberdata(userid, member.getMemberid());
							List<BalanceDataMDL> balanceDataMDLs = JUtil
									.fromJson(
											jsonObject,
											new TypeToken<List<BalanceDataMDL>>() {
											}.getType());
							if (balanceDataMDLs != null) {
								dal.Insert(member,
										balanceDataMDLs);
							}
						}
					}
				}

			}
		}
		return false;

	}

	public static void DelBalanceDataThread(final Context mContext) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AddBalanceData(mContext);
				super.run();
			}

		}.start();
	}

	public static boolean DelBalanceData(Context mContext) {
		if (App.getApp(mContext).getUser() != null) {
			BalanceDataDAL dal = new BalanceDataDAL(mContext);
			ActionDAL actionDAL = new ActionDAL(mContext);
			List<ActionMDL> actions = actionDAL
					.SelectByAction(ActionMDL.DelBalanceData);
			if (actions != null && actions.size() > 0) {
				for (ActionMDL actionMDL : actions) {
					actionDAL.Locked(actionMDL.getId(), 1);
					BalanceDataMDL mdl = dal.SelectById(actionMDL.getData());
					if (mdl != null && !TextUtils.isEmpty(mdl.getMemberid())) {

						JSONObject jsonObject = new BalanceDataWS(mContext)
								.insertBalanceData(mdl);
						if (JUtil.GetJsonStatu(jsonObject)) {

							actionDAL.Del(actionMDL.getId());
						} else {
							actionDAL.Locked(actionMDL.getId(), 0);
						}

					} else {
						actionDAL.Del(actionMDL.getId());
					}
				}
			}
		}
		return false;

	}
	public static boolean checkNetWork(Context mContext)
	{
		 ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);  
	        if (cm != null) {  
	            NetworkInfo[] infos = cm.getAllNetworkInfo();  
	            if (infos != null) {  
	                for (NetworkInfo ni : infos) {  
	                    if (ni.isConnected()) {  
	                        return true;  
	                    }  
	                }  
	            }  
	        }  
	        return false;  
		
	}
	public static boolean checkMobileNO(String mobiles){     
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
        Matcher m = p.matcher(mobiles);     
        //logger.info(m.matches()+"---");     
        return m.matches();     
    } 
   
    public static boolean checkEmail(String email){     
     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);     
        Matcher m = p.matcher(email);     
        //logger.info(m.matches()+"---");     
        return m.matches();     
    } 

	public static String GetBluetoothValIndex(String str, int i) {
		try {
			return str.substring(i * 2, (i + 1) * 2);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	public static void playMp3(Context mContext)
	{
		new mp3Task(mContext).execute("");
	}
	static class mp3Task extends AsyncTask<String, Object, Object> {
		Context mContext;

		public mp3Task(Context context) {
			mContext = context;
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub

//			if (!CurrApplication.getInstance(mContext).isPlayMp3())
//				return null;
			String filePath =  App.packageName ;
			String fileName =  App.packageName + "/shake_match.mp3";

			File mfile = new File(fileName);
			if (!mfile.exists()) {

				InputStream isInputStream = mContext.getResources()
						.openRawResource(R.raw.shake_match);

				try {
					File file = new File(filePath);
					if (!file.exists())
						file.mkdirs();
					FileOutputStream fos = new FileOutputStream(fileName);
					byte[] buffer = new byte[8192];
					int count = 0;
					while ((count = isInputStream.read(buffer)) > 0) {
						fos.write(buffer, 0, count);
					}
					fos.close();
					isInputStream.close();
					return true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			if (mfile.exists()) {
				MediaPlayer mp = new MediaPlayer();
				try {
					mp.setDataSource(fileName);
					mp.prepare();
					mp.start();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

	}
}
