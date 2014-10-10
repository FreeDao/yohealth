package com.vc.cloudbalance.widget;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.example.hellojni.HelloJni;
import com.vc.cloudbalance.R;
import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.LocalBitmapHelper;
import com.vc.cloudbalance.common.SOHelper;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.ActionMDL;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.ActionDAL;
import com.vc.cloudbalance.sqlite.BalanceDataDAL;
import com.vc.dialog.ListViewWindow;
import com.vc.util.FileHelper;
import com.vc.util.ImageUtil;
import com.vc.util.LogUtils;
import com.vc.util.ObjectHelper;

import de.hdodenhof.circleimageview.CircleImageView;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EViewGroup(R.layout.view_bigflowerbalancedata)
public class View_BigFlowerBalanceData extends LinearLayout {
	Context mContext;
	@ViewById
	ImageView imgFlower, imgTrunk, imgGoBalance, imgUrl;
	@ViewById
	CircleImageView circleImageView;
	@ViewById
	LinearLayout llRightLeaf, llLeftLeaf;
	@ViewById
	TextView tvHeight, tvWeight, tvMsg;
	int right_leaf_width = 0, right_leaf_height = 0, left_leaf_width = 0,
			left_leaf_height = 0;
	boolean isLoad = false;
	int trunkHeight = 0;
	int minTop = 0, maxRightTop = 0, maxLeftTop = 0, targetRightHeight = 0,
			targetLeftHeight = 0, nowRightHeight = 0, nowLeftHeight = 0;
	boolean isRunning = false,isRunningSlow=false;
	MemberMDL member;
	int maxHeight = 150, maxWeight = 50;
	boolean isRed = false;
	boolean isLockData = false;
	/* 用来标识请求照相功能的activity */

	public static final int CAMERA_WITH_DATA = 1001;
	/* 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = 1002;
	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera");
	public static File mCurrentPhotoFile;// 照相机拍照得到的图片
	ListViewWindow takePic_listViewWindow;
	byte[] imgStream;
	Bitmap bitmap;
	RelativeLayout.LayoutParams leftLeafLayoutParams;
	public View_BigFlowerBalanceData(Context context, MemberMDL mdl) {
		super(context);
		mContext = context;
		member = mdl;

		// init();
		// TODO Auto-generated constructor stub
	}

	public View_BigFlowerBalanceData(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// init();
		// TODO Auto-generated constructor stub
	}

	@Click(R.id.imgFlower)
	void flowerClick() {
		String[] eventTypes = new String[] {
				LanguageHelper.GetVal(
						LanguageConstants.CustomActionSheet_methosTakePic,
						mContext),
				LanguageHelper.GetVal(
						LanguageConstants.CustomActionSheet_methosPhotoLibrary,
						mContext) };
		takePic_listViewWindow = new ListViewWindow(mContext,
				onPicSelectListener, eventTypes, LanguageHelper.GetVal(
						LanguageConstants.CustomActionSheet_choose, mContext));
		takePic_listViewWindow.show();
		// llRightLeaf.layout(0, 60, right_leaf_width, 60+right_leaf_height);
		
	}

	@Click(R.id.imgGoBalance)
	void goBalanceClick() {

	}

	private void initLanguage() {
		UIHelper.SetText(R.id.lbTip2,
				LanguageConstants.lb_ItemGuestInfoView_unitCM, this);
		UIHelper.SetText(R.id.lbTip3,
				LanguageConstants.lb_ItemNewStyleAudltPlayView_KG2, this);
		UIHelper.SetText(R.id.tvTip1,
				LanguageConstants.lb_ItemNewStyleBabyPlayView_pleaseStandOn,
				this);
	}

	@Click(R.id.llRightLeaf)
	void showHeightDialog() {
		final EditText et = new EditText(mContext);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		new AlertDialog.Builder(mContext)
				.setTitle(
						LanguageHelper.GetVal(LanguageConstants.lb_inputheight,
								mContext))
				.setView(et)
				.setPositiveButton(
						LanguageHelper
								.GetVal(LanguageConstants.alertButton_confirm,
										mContext),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								int height = ObjectHelper.Convert2Int(et
										.getText().toString());
								if (height < 10 || height > 220) {
									DialogHelper.showTost(
											mContext,
											LanguageHelper
													.GetVal(LanguageConstants.alert_inputheighterror,
															mContext));
									showHeightDialog();
								} else {
									tvHeight.setText(et.getText().toString());
									dialog.dismiss();
								}
							}
						})
				.setNegativeButton(
						LanguageHelper.GetVal(
								LanguageConstants.alertButton_cancel, mContext),
						null).show();

	}

	@AfterViews
	void init() {
		// TODO Auto-generated method stub
		initLanguage();
		tvHeight.setText(member.getHeight() + "");
		ViewTreeObserver vto1 = getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isLoad) {
					leftLeafLayoutParams = (RelativeLayout.LayoutParams)llLeftLeaf.getLayoutParams();
					//leftLeafLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					right_leaf_width = llRightLeaf.getWidth();
					right_leaf_height = llRightLeaf.getHeight();
					left_leaf_width = llLeftLeaf.getWidth();
					left_leaf_height = llLeftLeaf.getHeight();
					trunkHeight = imgTrunk.getHeight();
					maxRightTop = trunkHeight - right_leaf_height;
					maxLeftTop = trunkHeight - left_leaf_height;
					targetRightHeight = maxRightTop;
					targetLeftHeight = maxLeftTop;
					nowRightHeight = targetRightHeight;
					nowLeftHeight = targetLeftHeight;
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) circleImageView
							.getLayoutParams();
					params.width = imgUrl.getWidth();
					params.height = imgUrl.getHeight();
					circleImageView.setLayoutParams(params);
					
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							targetRightHeight = maxRightTop
									- (int) ((maxRightTop * 1f / maxHeight) * ObjectHelper
											.Convert2Int(member.getHeight()));
//							targetLeftHeight = maxLeftTop
//									- (int) ((maxLeftTop * 1f / maxWeight) * maxWeight);
						}
					}, 500);

					isLoad = true;
					isRunningSlow = true;
					
				}

			}
		});
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {

					while (isRunningSlow) {
						try {

							Thread.sleep(5);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						int offset_r = targetRightHeight - nowRightHeight;
//
						int step = 5;
						if (offset_r != 0) {
							if (offset_r > 0)
								nowRightHeight += step;
							else if (offset_r < 0)
								nowRightHeight -= step;

							if (nowRightHeight < 0)
								nowRightHeight = 0;
							if (nowRightHeight > maxRightTop)
								nowRightHeight = maxRightTop;
							if (nowRightHeight > targetRightHeight
									&& nowRightHeight - targetRightHeight < 5)
								nowRightHeight = targetRightHeight;
							else if (targetRightHeight > nowRightHeight
									&& targetRightHeight - nowRightHeight < 5)
								nowRightHeight = targetRightHeight;
							mRightHandler.sendEmptyMessage(nowRightHeight);
						}

						int offset_l = targetLeftHeight - nowLeftHeight;
						step = 3;
						if (offset_l != 0) {
							if (offset_l > 0)
								nowLeftHeight += step;
							else if (offset_l < 0)
								nowLeftHeight -= step;

							if (nowLeftHeight < 0)
								nowLeftHeight = 0;
							if (nowLeftHeight > maxLeftTop)
								nowLeftHeight = maxLeftTop;
							if (nowLeftHeight > targetLeftHeight
									&& nowLeftHeight - targetLeftHeight < 3)
								nowLeftHeight = targetLeftHeight;
							else if (targetLeftHeight > nowLeftHeight
									&& targetLeftHeight - nowLeftHeight < 3)
								nowLeftHeight = targetLeftHeight;
							
							mLeftHandler.sendEmptyMessage(nowLeftHeight);
						}

						// if(nowRightHeight-targetRightHeight<5||targetRightHeight-nowRightHeight<5)

						// Log.e("TAG",
						// "targetRightHeight="+targetRightHeight+",nowRightHeight="+nowRightHeight);

					}

					try {

						Thread.sleep(30);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}.start();
	}

	public void setData(String weight, String val, String data) {
		// String sss= new HelloJni().getHealth(0,2,3,4,5);
//		if (isLockData)
//			return;
//		isLockData=true;
//		float weightVal = ObjectHelper.Convert2Float(weight);
////		tvWeight.setText(weight);
//		targetLeftHeight = maxLeftTop
//				- (int) ((maxLeftTop * 1f / maxWeight) * weightVal);
//		LogUtils.e("targetLeftHeight:"+targetLeftHeight+"");
		
		if (weight.equals("0") || weight.equals("0.0"))
			return;
		String[] valArray = val.split(",");
		String bmi = valArray[0];
		int devicestate = Integer.parseInt(
				Common.GetBluetoothValIndex(data, 8), 16);
		if ((devicestate & (int) Math.pow(2, 1)) > 0) {
			if (isLockData)
				return;
			BalanceDataMDL balanceDataMDL = new BalanceDataMDL();
			//balanceDataMDL.setId(UUID.randomUUID().toString());
			balanceDataMDL.setWeidate(new Date());
			balanceDataMDL.setWeight(weight);
			balanceDataMDL.setBmi(valArray[0]);
			balanceDataMDL.setFatpercent("");
			balanceDataMDL.setMuscle("");
			balanceDataMDL.setBone("");
			balanceDataMDL.setWater("");
			balanceDataMDL.setBasalmetabolism("");
			
//			balanceDataMDL.setFatpercent(valArray[1]);
//			balanceDataMDL.setMuscle(valArray[2]);
//			balanceDataMDL.setBone(valArray[3]);
//			balanceDataMDL.setWater(valArray[4]);
			if(imgStream==null)
			{
				balanceDataMDL.setHaveimg("0");
			}
			else {
				balanceDataMDL.setHaveimg("1");
				balanceDataMDL.setClientImg(imgStream);
			}
			balanceDataMDL.setHeight(tvHeight.getText().toString());
			balanceDataMDL.setMemberid(member.getMemberid());
			balanceDataMDL.setClientmemberid(member.getClientid());
			if(App.getApp(mContext).getUser()!=null)
				balanceDataMDL.setUserid(App.getApp(mContext).getUser().getUserid());
			boolean state = new BalanceDataDAL(mContext).Insert(balanceDataMDL);
			if (state) {
				ActionMDL actionMDL = new ActionMDL(UUID.randomUUID()
						.toString(), ActionMDL.AddBalanceData,
						balanceDataMDL.getId()+"");
				new ActionDAL(mContext).Insert(actionMDL);
				Common.AddBalanceDataThread(mContext);
			}
			isRunning = false;
			isLockData = true;
			tvMsg.setTextColor(getResources().getColor(R.color.base_font_color));
			isRed = false;
			tvMsg.setText("");
			Common.playMp3(mContext);
		} else {
			isLockData = false;
		}
		
		float weightVal = ObjectHelper.Convert2Float(weight);
		tvWeight.setText(weight);
		targetLeftHeight = maxLeftTop
				- (int) ((maxLeftTop * 1f / maxWeight) * weightVal);
		LogUtils.e("targetLeftHeight:"+targetLeftHeight+"");
		LogUtils.e(targetLeftHeight+"");
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

	Handler mLeftHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int h = msg.what;
			//LogUtils.e("h:"+h+"");
			leftLeafLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			leftLeafLayoutParams.topMargin=h;
			llLeftLeaf.setLayoutParams(leftLeafLayoutParams);
			//llLeftLeaf.layout(0, h, left_leaf_width, h + left_leaf_height);
			//tvWeight.setText(h+"");
			//super.handleMessage(msg);
		}

	};
	Handler mRightHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int h = msg.what;
			llRightLeaf.layout(0, h, right_leaf_width, h + right_leaf_height);
			super.handleMessage(msg);
		}

	};

	public void setData(String val) {

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

	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			((Activity) mContext).startActivityForResult(intent,
					PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mContext, "找不到相册", Toast.LENGTH_LONG).show();
		}
	}

	public Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("return-data", false);
		return intent;
	}

	protected void doTakePhoto() {
		try {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
			if (!mCurrentPhotoFile.exists()) {
				try {
					mCurrentPhotoFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Intent intent = getTakePickIntent(mCurrentPhotoFile);
			((Activity) mContext).startActivityForResult(intent,
					CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mContext, "无法启用拍照功能", Toast.LENGTH_LONG).show();
		}
	}

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMddHHmmss");
		return dateFormat.format(date) + ".png";
	}

	public Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentPhotoFile));
		intent.putExtra("return-data", true);
		return intent;
	}

	private com.vc.dialog.ListViewWindow.OnSelectListener onPicSelectListener = new com.vc.dialog.ListViewWindow.OnSelectListener() {

		@Override
		public void onSelect(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (which == 0) {
				doTakePhoto();
			} else if (which == 1) {
				doPickPhotoFromGallery();
			}

		}
	};

	public void setImageUrl(String url) {

		circleImageView.setVisibility(View.VISIBLE);
		//circleImageView.setImageURI(Uri.parse(url));
		imgStream = FileHelper.encodeByteFile(url);
		bitmap=ImageUtil.decodeSampledBitmapFromByte(imgStream, imgUrl.getWidth(), imgUrl.getHeight());
		circleImageView.setImageBitmap(bitmap);
	}

	// public void setImageBitmap(Bitmap bitmap) {
	// imgMember.setImageURI(Uri.parse(url));
	// imgMember.setVisibility(View.GONE);
	// circleImageView.setVisibility(View.VISIBLE);
	// circleImageView.setImageBitmap(bitmap);
	// imgStream = FileHelper.encodeByteFile(url);
	// }

}
