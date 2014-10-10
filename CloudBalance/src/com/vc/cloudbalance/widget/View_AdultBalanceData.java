package com.vc.cloudbalance.widget;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import com.example.hellojni.HelloJni;
import com.vc.cloudbalance.R;
import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.JUtil;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.LocalBitmapHelper;
import com.vc.cloudbalance.common.SOHelper;
import com.vc.cloudbalance.model.ActionMDL;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.ActionDAL;
import com.vc.cloudbalance.sqlite.BalanceDataDAL;
import com.vc.cloudbalance.webservice.BalanceDataWS;
import com.vc.util.ObjectHelper;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;

@EViewGroup(R.layout.view_adultbalancedata)
public class View_AdultBalanceData extends LinearLayout {
	Context mContext;
	@ViewById
	RelativeLayout rlPanel;
	@ViewById
	TextView tvMsg, tvMiddleVal, tvMiddleVal_2, tvLeftText, tvRightText,
			tvMiddlePercent;
	@ViewById
	TextView tvTopLeft_1, tvTopLeft_2, tvTopLeft_3;
	@ViewById
	TextView tvTopRight_1, tvTopRight_2, tvTopRight_3;
	@ViewById
	TextView tvBottomLeft_1, tvBottomLeft_2, tvBottomLeft_3;
	@ViewById
	TextView tvBottomRight_1, tvBottomRight_2, tvBottomRight_3;
	@ViewById
	Button btnRank, btnSuggest;
	@ViewById
	RelativeLayout rlRoot;
	@ViewById
	ImageView imgLeftUp, imgRightUp;

	@ViewById
	LinearLayout llLeftPanel, llRightPanel, llBigCircle, llUpPanelTopLeft,
			llUpPanelTopRight, llUpPanelBottomLeft, llUpPanelBottomRight;
	PopupWindow rankPopupWindow, suggestPopupWindow;
	View rankPopupContentView, suggestPopupContentView,
			HideRankPopupContentView, HideSuggestPopupContentView;
	boolean isLoad = false;
	boolean isRunning = false;
	boolean isRed = false;
	boolean isLockData = false;
	boolean isRankOpenPopWindow = false;
	boolean isSuggestOpenPopWindow = false;
	String percent1 = "--", percent2 = "--", tizhongshuyu = "--",
			jianyitizhong1 = "--", jianyitizhong2 = "--",
			zhifanghanliang = "--", jianyizhifang1 = "--",
			jianyizhifang2 = "--", shuifenhanliang = "--",
			jianyishuifen1 = "--", jianyishuifen2 = "--";

	public View_AdultBalanceData(Context context) {
		super(context);
		mContext = context;
		// init();
		// TODO Auto-generated constructor stub
	}

	public View_AdultBalanceData(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// init();
		// TODO Auto-generated constructor stub
	}

	MemberMDL member;

	public void setMember(MemberMDL mdl) {
		member = mdl;
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (!isLockData)
				tvMsg.setText(LanguageHelper
						.GetVal(LanguageConstants.lb_playViewLinkStateTip_WeightNotConfirm,
								mContext));
			if (isRed) {
				tvMsg.setTextColor(getResources().getColor(R.color.white));

			} else {
				tvMsg.setTextColor(getResources().getColor(
						R.color.base_font_color));
			}
			isRed = !isRed;
			super.handleMessage(msg);
		}

	};

	@AfterViews
	void init() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(mContext);
		HideRankPopupContentView = inflater.inflate(R.layout.pop_jianyi, null);
		RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		HideRankPopupContentView.setLayoutParams(lParams);
		HideRankPopupContentView.setVisibility(View.INVISIBLE);
		rlRoot.addView(HideRankPopupContentView);
		HideSuggestPopupContentView = inflater.inflate(R.layout.pop_jianyi,
				null);
		lParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		HideSuggestPopupContentView.setLayoutParams(lParams);
		HideSuggestPopupContentView.setVisibility(View.INVISIBLE);
		rlRoot.addView(HideSuggestPopupContentView);
		Bitmap circleBitmap = LocalBitmapHelper.ReadBgImg(mContext,
				R.drawable.bg_big_circle);
		int height = circleBitmap.getHeight();
		llBigCircle.getLayoutParams().width = circleBitmap.getWidth();
		llBigCircle.getLayoutParams().height = height;
		height = height * 10 / 9;
		android.widget.LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) rlPanel
				.getLayoutParams();
		layoutParams.height = height;
		rlPanel.setLayoutParams(layoutParams);

		ViewTreeObserver vto1 = getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isLoad) {
					int llLeftPanelheight = llLeftPanel.getHeight();
					int llLeftPanelwidth = llLeftPanel.getWidth();
					int llRightPanelheight = llLeftPanel.getHeight();
					int llRightPanelwidth = llLeftPanel.getWidth();

					int max = 0;
					max = Math.max(llLeftPanelheight, llLeftPanelwidth);
					max = Math.max(max, llRightPanelheight);
					max = Math.max(max, llRightPanelwidth);

					llLeftPanel.getLayoutParams().width = max;
					llLeftPanel.getLayoutParams().height = max;
					llRightPanel.getLayoutParams().width = max;
					llRightPanel.getLayoutParams().height = max;
					isLoad = true;
				}

			}
		});

	}

	@Click(R.id.btnRank)
	void showRank() {
		if (rankPopupWindow == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			rankPopupContentView = inflater.inflate(R.layout.pop_yiping, null);
			TextView tvPre1 = (TextView) rankPopupContentView
					.findViewById(R.id.tvPre1);
			TextView tvPre2 = (TextView) rankPopupContentView
					.findViewById(R.id.tvPre2);
			tvPre1.setText(percent1.replace("%", ""));
			tvPre2.setText(percent2.replace("%", ""));
			rankPopupWindow = new PopupWindow(rankPopupContentView,
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
			rankPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			rankPopupWindow.setOutsideTouchable(true);
			rankPopupWindow.setFocusable(true);
			rankPopupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					isRankOpenPopWindow = false;
					btnRank.setBackgroundResource(R.drawable.btn_rank_selector);
				}
			});
		}
		if (isRankOpenPopWindow) {
			rankPopupWindow.dismiss();
			isRankOpenPopWindow = false;
			btnRank.setBackgroundResource(R.drawable.btn_rank_selector);
		} else {
			int hei = HideRankPopupContentView.getHeight();
			// 保存anchor在屏幕中的位置
			int[] location = new int[2];
			// 保存anchor上部中点
			int[] anchorCenter = new int[2];
			// 读取位置anchor座标
			btnRank.getLocationOnScreen(location);
			// 计算anchor中点
			anchorCenter[0] = location[0] + btnRank.getWidth() / 2;
			anchorCenter[1] = location[1];
			rankPopupWindow.showAtLocation(btnRank, Gravity.TOP | Gravity.LEFT,
					anchorCenter[0], anchorCenter[1] - hei - 20);

			// helpPopupWindow.showAsDropDown(imgHelp);
			isRankOpenPopWindow = true;
			btnRank.setBackgroundResource(R.drawable.bg_rank_f3);
		}
	}

	@Click(R.id.btnSuggest)
	void showSuggest() {
		if (suggestPopupWindow == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			suggestPopupContentView = inflater.inflate(R.layout.pop_jianyi,
					null);
			TextView tvPianShou = (TextView) suggestPopupContentView
					.findViewById(R.id.tvPianShou);
			TextView tvJianYi1 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYi1);
			TextView tvJianYi2 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYi2);
			TextView tvZhiFan = (TextView) suggestPopupContentView
					.findViewById(R.id.tvZhiFan);
			TextView tvJianYiZhiFan1 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiZhiFan1);
			TextView tvJianYiZhiFan2 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiZhiFan2);
			TextView tvShuiFen = (TextView) suggestPopupContentView
					.findViewById(R.id.tvShuiFen);
			TextView tvJianYiShuiFen1 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiShuiFen1);
			TextView tvJianYiShuiFen2 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiShuiFen2);
			// tizhongshuyu="--",jianyitizhong1="--",jianyitizhong2="--",zhifanghanliang="--",jianyizhifang1="--",jianyizhifang2="--",shuifenhanliang="--",jianyishuifen1="--",jianyishuifen2="--"
			tvPianShou.setText(tizhongshuyu);
			tvJianYi1.setText(jianyitizhong1);
			tvJianYi2.setText(jianyitizhong2);
			tvZhiFan.setText(zhifanghanliang);
			tvJianYiZhiFan1.setText(jianyizhifang1);
			tvJianYiZhiFan2.setText(jianyizhifang2);
			tvShuiFen.setText(shuifenhanliang);
			tvJianYiShuiFen1.setText(jianyishuifen1);
			tvJianYiShuiFen2.setText(jianyishuifen2);

			suggestPopupWindow = new PopupWindow(suggestPopupContentView,
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
			suggestPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			suggestPopupWindow.setOutsideTouchable(true);
			suggestPopupWindow.setFocusable(true);
			suggestPopupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					isRankOpenPopWindow = false;
					btnSuggest
							.setBackgroundResource(R.drawable.btn_suggest_selector);
				}
			});
		}
		if (isSuggestOpenPopWindow) {
			suggestPopupWindow.dismiss();
			isSuggestOpenPopWindow = false;
			btnSuggest.setBackgroundResource(R.drawable.btn_suggest_selector);
		} else {
			int hei = HideSuggestPopupContentView.getHeight();
			// 保存anchor在屏幕中的位置
			int[] location = new int[2];
			// 保存anchor上部中点
			int[] anchorCenter = new int[2];
			// 读取位置anchor座标
			btnSuggest.getLocationOnScreen(location);
			// 计算anchor中点
			anchorCenter[0] = location[0] + btnSuggest.getWidth() / 2;
			anchorCenter[1] = location[1];
			suggestPopupWindow
					.showAtLocation(btnSuggest, Gravity.TOP | Gravity.LEFT,
							anchorCenter[0], anchorCenter[1] - hei - 20);

			// helpPopupWindow.showAsDropDown(imgHelp);
			isSuggestOpenPopWindow = true;
			btnSuggest.setBackgroundResource(R.drawable.bg_suggest_f3);
		}
	}

	public void setData(String weight, String val, String data) {
		// String sss= new HelloJni().getHealth(0,2,3,4,5);
		String[] valArray = val.split(",");
		String bmi = valArray[0];
		if (weight.equals("0") || weight.equals("0.0"))
			return;
		int devicestate = Integer.parseInt(
				Common.GetBluetoothValIndex(data, 8), 16);
		float dz = Integer.parseInt(Common.GetBluetoothValIndex(data, 6)
				+ Common.GetBluetoothValIndex(data, 7), 16);
		if ((devicestate & (int) Math.pow(2, 1)) > 0) {
			if (isLockData)
				return;
			isLockData = true;
			BalanceDataMDL lastData = new BalanceDataDAL(mContext)
					.SelectLastData(member.getMemberid(), member.getClientid());
			float thisVal = ObjectHelper.Convert2Float(weight);
			DecimalFormat fnum = new DecimalFormat("##0.0");
			new getRanking2Task().execute(member.getUserid(),
					member.getMemberid(), bmi, member.getSex(),
					member.getHeight(), weight);
			if (!member.isGuest()) {
				if (lastData != null) {
					float lastVal = ObjectHelper.Convert2Float(lastData
							.getWeight());

					float cha = thisVal - lastVal;
					if (cha > 0) {
						imgLeftUp.setImageResource(R.drawable.ic_red_up);
						tvLeftText.setText(fnum.format(cha));
					} else {
						imgLeftUp.setImageResource(R.drawable.ic_red_dowm);
						tvLeftText.setText(fnum.format((0 - cha)));
					}
				}

				float targetVal = ObjectHelper.Convert2Float(member
						.getTargetweight());

				float cha = thisVal - targetVal;
				if (cha > 0) {
					imgRightUp.setImageResource(R.drawable.ic_red_up);
					tvRightText.setText(fnum.format(cha));
				} else {
					imgRightUp.setImageResource(R.drawable.ic_red_dowm);
					tvRightText.setText(fnum.format((0 - cha)));
				}
			}
			if ((devicestate & (int) Math.pow(2, 0)) > 0) {
				tvMsg.setText(LanguageHelper.GetVal(
						LanguageConstants.lb_playViewLinkStateTip_overWeight,
						mContext));
				doSuggest(bmi, member.getHeight(), valArray[1], valArray[4],
						member.getSex(), member.getBirthday());
				return;
			}

			if (dz < 500 || dz > 700) {
				tvMsg.setText(LanguageHelper.GetVal(
						LanguageConstants.lb_playViewLinkStateTip_wearoutshoe,
						mContext));
				doSuggest(bmi, member.getHeight(), "", "", member.getSex(),
						member.getBirthday());
				return;
			}

			doSuggest(bmi, member.getHeight(), valArray[1], valArray[4],
					member.getSex(), member.getBirthday());

			if (!member.isGuest()) {
				BalanceDataMDL balanceDataMDL = new BalanceDataMDL();
				// balanceDataMDL.setId(UUID.randomUUID().toString());
				balanceDataMDL.setWeidate(new Date());
				balanceDataMDL.setWeight(weight);
				balanceDataMDL.setBmi(valArray[0]);
				// balanceDataMDL.setFatpercent("");
				// balanceDataMDL.setMuscle("");
				// balanceDataMDL.setBone("");
				// balanceDataMDL.setWater("");
				// balanceDataMDL.setBasalmetabolism("");

				balanceDataMDL.setFatpercent(valArray[1]);
				balanceDataMDL.setMuscle(valArray[2]);
				balanceDataMDL.setBone(valArray[3]);
				balanceDataMDL.setWater(valArray[4]);
				balanceDataMDL.setMemberid(member.getMemberid());
				balanceDataMDL.setClientmemberid(member.getClientid());
				if (App.getApp(mContext).getUser() != null)
					balanceDataMDL.setUserid(App.getApp(mContext).getUser()
							.getUserid());
				boolean state = new BalanceDataDAL(mContext)
						.Insert(balanceDataMDL);
				if (state) {
					ActionMDL actionMDL = new ActionMDL(UUID.randomUUID()
							.toString(), ActionMDL.AddBalanceData,
							balanceDataMDL.getId() + "");
					new ActionDAL(mContext).Insert(actionMDL);
					Common.AddBalanceDataThread(mContext);
				}
			}
			isRunning = false;

			tvMsg.setTextColor(getResources().getColor(R.color.base_font_color));
			isRed = false;
			tvMsg.setText("");
			Common.playMp3(mContext);

		} else {
			isLockData = false;
		}

		// String tvMiddleVal=valArray[0];
		// String tvMiddleVal="";
		tvMiddlePercent.setText(bmi);

		if (weight.contains(".")) {
			String[] strs = weight.split("\\.");
			tvMiddleVal.setText(strs[0]);
			tvMiddleVal_2.setText(strs[1].subSequence(0, 1));
		} else {
			tvMiddleVal.setText(weight);
			tvMiddleVal_2.setText("0");
		}
		if (dz < 500 || dz > 700) {
			tvTopLeft_1.setText("0");
			tvTopLeft_2.setText("0");
			tvTopRight_1.setText("0");
			tvTopRight_2.setText("0");
			tvBottomLeft_1.setText("0");
			tvBottomLeft_2.setText("0");
			tvBottomRight_1.setText("0");
			tvBottomRight_2.setText("0");
		} else {
			if (valArray[1].contains(".")) {
				String[] strs = valArray[2].split("\\.");
				tvTopLeft_1.setText(strs[0]);
				tvTopLeft_2.setText(strs[1].subSequence(0, 1));
			} else {
				tvTopLeft_1.setText(valArray[1]);
				tvTopLeft_2.setText("0");
			}

			if (valArray[2].contains(".")) {
				String[] strs = valArray[1].split("\\.");
				tvTopRight_1.setText(strs[0]);
				tvTopRight_2.setText(strs[1].subSequence(0, 1));
			} else {
				tvTopRight_1.setText(valArray[1]);
				tvTopRight_2.setText("0");
			}

			if (valArray[3].contains(".")) {
				String[] strs = valArray[3].split("\\.");
				tvBottomLeft_1.setText(strs[0]);
				tvBottomLeft_2.setText(strs[1].subSequence(0, 1));
			} else {
				tvBottomLeft_1.setText(valArray[1]);
				tvBottomLeft_2.setText("0");
			}

			if (valArray[4].contains(".")) {
				String[] strs = valArray[4].split("\\.");
				tvBottomRight_1.setText(strs[0]);
				tvBottomRight_2.setText(strs[1].subSequence(0, 1));
			} else {
				tvBottomRight_1.setText(valArray[1]);
				tvBottomRight_2.setText("0");
			}
		}

		if (!isRunning && !isLockData) {
			isRunning = true;
			new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (isRunning) {
						mHandler.sendEmptyMessage(0);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					super.run();
				}

			}.start();
		}
	}

	public void doSuggest(String bmi, String heightval, String fatstring,
			String waterstring, String sex, String agestring) {
		float bmiVal = ObjectHelper.Convert2Float(bmi);
		float height = ObjectHelper.Convert2Float(heightval);
		DecimalFormat fnum = new DecimalFormat("##0.0");
		jianyitizhong1 = fnum.format((18.5 * (height / 100) * (height / 100)));
		jianyitizhong2 = fnum.format((24 * (height / 100) * (height / 100)));
		if (bmiVal < 18.5) {
			// UIHelper.SetText(R.id.tvBottomRight,
			// LanguageConstants.alert_AccountFormatErr, this);
			tizhongshuyu = "偏瘦";
		} else if (bmiVal >= 18.5 && bmiVal < 25) {
			tizhongshuyu = "正常";

			// UIHelper.SetText(R.id.tvBottomRight,
			// LanguageConstants.alert_AccountFormatErr, this);
		} else if (bmiVal >= 25 && bmiVal < 29.9) {
			tizhongshuyu = "偏胖";

			// UIHelper.SetText(R.id.tvBottomRight,
			// LanguageConstants.alert_AccountFormatErr, this);
		} else if (bmiVal >= 29.9 && bmiVal < 39.9) {
			tizhongshuyu = "肥胖";
			// UIHelper.SetText(R.id.tvBottomRight,
			// LanguageConstants.alert_AccountFormatErr, this);
		} else if (bmiVal >= 39.9) {
			tizhongshuyu = "严重肥胖";

			// UIHelper.SetText(R.id.tvBottomRight,
			// LanguageConstants.alert_AccountFormatErr, this);
		}
		int age = Common.GetAgeByBirthday(agestring);
		if (!fatstring.equals("")) {
			float fat = ObjectHelper.Convert2Float(fatstring);
			int fanwei1 = 0, fanwei2 = 0, fanwei3 = 0, fanwei4 = 0, fanwei5 = 0, fanwei6 = 0, fanwei7 = 0, fanwei8 = 0;
			if (sex.equals("0")) {

				if (age >= 18 && age <= 39) {
					fanwei1 = 5;
					fanwei2 = 20;
					fanwei3 = 21;
					fanwei4 = 34;
					fanwei5 = 35;
					fanwei6 = 39;
					fanwei7 = 40;
					fanwei8 = 45;
					jianyizhifang1 = 21 + "";
					jianyizhifang2 = 34 + "";
				} else if (age >= 40 && age <= 50) {
					fanwei1 = 5;
					fanwei2 = 21;
					fanwei3 = 22;
					fanwei4 = 35;
					fanwei5 = 36;
					fanwei6 = 40;
					fanwei7 = 41;
					fanwei8 = 45;
					jianyizhifang1 = 22 + "";
					jianyizhifang2 = 35 + "";
				} else if (age >= 60) {
					fanwei1 = 5;
					fanwei2 = 22;
					fanwei3 = 23;
					fanwei4 = 36;
					fanwei5 = 37;
					fanwei6 = 41;
					fanwei7 = 42;
					fanwei8 = 45;
					jianyizhifang1 = 23 + "";
					jianyizhifang2 = 36 + "";
				} else {

				}

			} else {
				if (age >= 18 && age <= 39) {
					fanwei1 = 5;
					fanwei2 = 10;
					fanwei3 = 11;
					fanwei4 = 21;
					fanwei5 = 22;
					fanwei6 = 26;
					fanwei7 = 27;
					fanwei8 = 45;
					jianyizhifang1 = 11 + "";
					jianyizhifang2 = 21 + "";
				} else if (age >= 40 && age <= 50) {
					fanwei1 = 5;
					fanwei2 = 11;
					fanwei3 = 12;
					fanwei4 = 22;
					fanwei5 = 23;
					fanwei6 = 27;
					fanwei7 = 28;
					fanwei8 = 45;
					jianyizhifang1 = 12 + "";
					jianyizhifang2 = 22 + "";
				} else if (age >= 60) {
					fanwei1 = 5;
					fanwei2 = 13;
					fanwei3 = 14;
					fanwei4 = 24;
					fanwei5 = 25;
					fanwei6 = 29;
					fanwei7 = 30;
					fanwei8 = 45;
					jianyizhifang1 = 14 + "";
					jianyizhifang2 = 24 + "";
				} else {

				}
			}
			if (fat < fanwei1) {
				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
				zhifanghanliang = "偏低";
			} else if (fat >= fanwei1 && fat <= fanwei2) {
				zhifanghanliang = "偏低";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			} else if (fat >= fanwei3 && fat <= fanwei4) {
				zhifanghanliang = "标准";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			} else if (fat >= fanwei5 && fat <= fanwei6) {
				zhifanghanliang = "偏高";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			} else if (fat >= fanwei7 && fat <= fanwei8) {
				zhifanghanliang = "过高";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			}
		}

		if (!waterstring.equals("")) {

			float water = ObjectHelper.Convert2Float(waterstring);
			float fanwei1 = 0, fanwei2 = 0, fanwei3 = 0, fanwei4 = 0, fanwei5 = 0, fanwei6 = 0, fanwei7 = 0, fanwei8 = 0, fanwei9 = 0, fanwei10 = 0;
			if (sex.equals("0")) {

				if (age <= 30) {
					fanwei1 = (float) (37.8);
					fanwei2 = (float) (46.0);
					fanwei3 = (float) (46.1);

					fanwei4 = (float) (49.4);
					fanwei5 = (float) (49.5);
					fanwei6 = (float) (52.9);
					fanwei7 = (float) (53.0);
					fanwei8 = (float) (56.3);
					fanwei9 = (float) (56.4);
					fanwei10 = (float) (66.0);
					jianyizhifang1 = 49.5 + "";
					jianyizhifang2 = 52 + "";
				} else {
					fanwei1 = (float) (37.8);
					fanwei2 = (float) (46.6);
					fanwei3 = (float) (44.7);
					fanwei4 = (float) (48);
					fanwei5 = (float) (48.1);
					fanwei6 = (float) (51.5);
					fanwei7 = (float) (51.6);
					fanwei8 = (float) (54.9);
					fanwei9 = (float) (55.0);
					fanwei10 = (float) (66.0);
					jianyizhifang1 = 48.1 + "";
					jianyizhifang2 = 51.5 + "";
				}

			} else {
				if (age <= 30) {
					fanwei1 = (float) (37.8);
					fanwei2 = (float) (51.1);
					fanwei3 = (float) (51.2);

					fanwei4 = (float) (53.5);
					fanwei5 = (float) (53.6);
					fanwei6 = (float) (57.0);
					fanwei7 = (float) (57.1);
					fanwei8 = (float) (60.4);
					fanwei9 = (float) (60.5);
					fanwei10 = (float) (66.0);
					jianyishuifen1 = 53.6 + "";
					jianyishuifen2 = 57 + "";
				} else {
					fanwei1 = (float) (37.8);
					fanwei2 = (float) (48.7);
					fanwei3 = (float) (48.8);
					fanwei4 = (float) (52.2);
					fanwei5 = (float) (52.3);
					fanwei6 = (float) (55.6);
					fanwei7 = (float) (55.7);
					fanwei8 = (float) (59);
					fanwei9 = (float) (59.1);
					fanwei10 = (float) (66.0);
					jianyishuifen1 = 52.3 + "";
					jianyishuifen2 = 55.6 + "";
				}
			}
			if (water < fanwei1) {
				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
				shuifenhanliang = "高";
			} else if (water >= fanwei1 && water <= fanwei2) {
				shuifenhanliang = "高";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			} else if (water >= fanwei3 && water <= fanwei4) {
				shuifenhanliang = "偏高";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			} else if (water >= fanwei5 && water <= fanwei6) {
				shuifenhanliang = "标准";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			} else if (water >= fanwei7 && water <= fanwei8) {
				shuifenhanliang = "偏瘦";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			} else if (water >= fanwei9 && water <= fanwei10) {
				shuifenhanliang = "瘦";

				// UIHelper.SetText(R.id.tvBottomRight,
				// LanguageConstants.alert_AccountFormatErr, this);
			}
		}
		if (suggestPopupContentView != null) {
			TextView tvPianShou = (TextView) suggestPopupContentView
					.findViewById(R.id.tvPianShou);
			TextView tvJianYi1 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYi1);
			TextView tvJianYi2 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYi2);
			TextView tvZhiFan = (TextView) suggestPopupContentView
					.findViewById(R.id.tvZhiFan);
			TextView tvJianYiZhiFan1 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiZhiFan1);
			TextView tvJianYiZhiFan2 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiZhiFan2);
			TextView tvShuiFen = (TextView) suggestPopupContentView
					.findViewById(R.id.tvShuiFen);
			TextView tvJianYiShuiFen1 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiShuiFen1);
			TextView tvJianYiShuiFen2 = (TextView) suggestPopupContentView
					.findViewById(R.id.tvJianYiShuiFen2);
			// tizhongshuyu="--",jianyitizhong1="--",jianyitizhong2="--",zhifanghanliang="--",jianyizhifang1="--",jianyizhifang2="--",shuifenhanliang="--",jianyishuifen1="--",jianyishuifen2="--"
			tvPianShou.setText(tizhongshuyu);
			tvJianYi1.setText(jianyitizhong1);
			tvJianYi2.setText(jianyitizhong2);
			tvZhiFan.setText(zhifanghanliang);
			tvJianYiZhiFan1.setText(jianyizhifang1);
			tvJianYiZhiFan2.setText(jianyizhifang2);
			tvShuiFen.setText(shuifenhanliang);
			tvJianYiShuiFen1.setText(jianyishuifen1);
			tvJianYiShuiFen2.setText(jianyishuifen2);
		}

	}

	class getRanking2Task extends AsyncTask<String, Object, Object> {

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			if (rankPopupContentView != null) {
				TextView tvPre1 = (TextView) rankPopupContentView
						.findViewById(R.id.tvPre1);
				TextView tvPre2 = (TextView) rankPopupContentView
						.findViewById(R.id.tvPre2);
				tvPre1.setText(percent1.replace("%", ""));
				tvPre2.setText(percent2.replace("%", ""));
			}
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String userid = params[0];
			String memberid = params[1];
			String bmi = params[2];
			String sex = params[3];
			String height = params[4];
			String weight = params[5];
			JSONObject jsonObject = new BalanceDataWS(mContext).getRanking2(
					userid, memberid, bmi, sex, height, weight);
			if (JUtil.GetJsonStatu(jsonObject)) {
				percent1 = JUtil.GetString(jsonObject, "percent");
				percent2 = JUtil.GetString(jsonObject, "percent2");
				// actionDAL.Del(actionMDL.getId());
			} else {
				// actionDAL.Locked(actionMDL.getId(), 0);
				percent1 = "--";
				percent2 = "--";
			}
			return null;
		}

	}
}
