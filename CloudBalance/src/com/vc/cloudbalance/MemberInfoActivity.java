package com.vc.cloudbalance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vc.cloudbalance.common.ApplicationMamager;
import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.common.JUtil;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.ActionMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.model.ModeTypeEnum;
import com.vc.cloudbalance.model.UserMDL;
import com.vc.cloudbalance.sqlite.ActionDAL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.sqlite.UserDAL;
import com.vc.cloudbalance.webservice.UserWS;
import com.vc.dialog.ListViewWindow;
import com.vc.util.FileHelper;
import com.vc.util.ImageUtil;
import com.vc.util.ObjectHelper;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_memberinfo)
public class MemberInfoActivity extends BaseActivity {
	@ViewById
	RelativeLayout rlRoot;
	@ViewById
	ImageView imgMember;
	@ViewById
	EditText etName, etTargetWeight, etHeight, etAge;
	@ViewById
	RadioButton rbGender1, rbGender2, rbMode1, rbMode2, rbMode3;
	@ViewById
	Button btnAction;
	@ViewById
	TextView tvSave, tvTitle;
	@ViewById
	ImageView imgHelp;
	@ViewById
	CircleImageView circleImageView;
	/* 用来标识请求照相功能的activity */

	private static final int CAMERA_WITH_DATA = 1001;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 1002;
	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera");
	public File mCurrentPhotoFile;// 照相机拍照得到的图片
	ListViewWindow takePic_listViewWindow;

	@Extra(Constants.EXTRA_KEY_ID_STRING)
	String MemberId;

	MemberMDL member;
	byte[] imgStream;
	Bitmap bitmap;
	PopupWindow helpPopupWindow;
	boolean isOpenPopWindow = false;
	View PopupContentView, HidePopupContentView;
	boolean isLoad = false;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			circleImageView.setImageBitmap(null);
			bitmap.recycle();
		}
		circleImageView=null;
		bitmap=null;
		super.onDestroy();
	}

	@Override
	public void goBack() {
		if (TextUtils.isEmpty(MemberId)) {

			DialogHelper.showComfrimDialog(this, LanguageHelper.GetVal(
					LanguageConstants.lb_dialog_tip, mContext), LanguageHelper
					.GetVal(LanguageConstants.query_quitAddMember, mContext),
					LanguageHelper.GetVal(
							LanguageConstants.alertButton_confirm, mContext),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							MemberInfoActivity.super.goBack();

						}
					}, LanguageHelper.GetVal(
							LanguageConstants.alertButton_cancel, mContext),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
		} else {
			super.goBack();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (TextUtils.isEmpty(MemberId)) {

				DialogHelper
						.showComfrimDialog(this, LanguageHelper.GetVal(
								LanguageConstants.lb_dialog_tip, mContext),
								LanguageHelper.GetVal(
										LanguageConstants.query_quitAddMember,
										mContext), LanguageHelper.GetVal(
										LanguageConstants.alertButton_confirm,
										mContext),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										MemberInfoActivity.super.goBack();

									}
								}, LanguageHelper.GetVal(
										LanguageConstants.alertButton_cancel,
										mContext),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	private void initLanguage() {

		UIHelper.SetText(R.id.tvReg,
				LanguageConstants.lb_ItemSettingLoginView_Register, this);
		UIHelper.SetText(R.id.lbTip1,
				LanguageConstants.lb_ItemEditUserView_ItemName, this);
		UIHelper.SetText(R.id.lbTip2,
				LanguageConstants.lb_ItemEditUserView_ItemGender, this);
		UIHelper.SetText(R.id.lbTip3,
				LanguageConstants.lb_ItemEditUserView_ItemAge, this);
		UIHelper.SetText(R.id.lbTip4,
				LanguageConstants.lb_ItemEditUserView_ItemYearAgeUnit, this);
		UIHelper.SetText(R.id.lbTip5,
				LanguageConstants.lb_ItemEditUserView_ItemMode, this);
		UIHelper.SetText(R.id.lbTip6,
				LanguageConstants.lb_ItemEditUserView_ItemHeight, this);
		UIHelper.SetText(R.id.lbTip7,
				LanguageConstants.lb_ItemGuestInfoView_unitCM, this);
		UIHelper.SetText(R.id.lbTip8,
				LanguageConstants.lb_ItemEditUserView_ItemTargetWeight, this);
		UIHelper.SetText(R.id.lbTip9,
				LanguageConstants.lb_ItemEditUserView_ItemName, this);

		UIHelper.SetText(R.id.rbGender1,
				LanguageConstants.lb_ItemEditUserView_ItemGenderFeMale, this);
		UIHelper.SetText(R.id.rbGender2,
				LanguageConstants.lb_ItemEditUserView_ItemGenderMale, this);
		UIHelper.SetText(R.id.rbMode1,
				LanguageConstants.lb_ItemEditUserView_ItemCalModeAudlt, this);
		UIHelper.SetText(R.id.rbMode2,
				LanguageConstants.lb_ItemEditUserView_ItemCalModeChild, this);
		UIHelper.SetText(R.id.rbMode3,
				LanguageConstants.lb_ItemEditUserView_ItemCalModeBaby, this);
	}

	@AfterViews
	void init() {
		initLanguage();
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		HidePopupContentView = inflater.inflate(R.layout.pop_help, null);
		RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		HidePopupContentView.setLayoutParams(lParams);
		HidePopupContentView.setVisibility(View.INVISIBLE);
		rlRoot.addView(HidePopupContentView);

		etAge.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					showDate();
				return true;
			}
		});
		etHeight.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					int height = ObjectHelper.Convert2Int(etHeight.getText()
							.toString());
					if (height < 10 && height > 220) {
						DialogHelper.showTost(mContext, LanguageHelper.GetVal(
								LanguageConstants.alert_inputheighterror,
								mContext));
						v.setFocusable(true);
					}
				}
			}
		});
		ViewTreeObserver vto1 = imgMember.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isLoad) {
					LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgMember
							.getLayoutParams();
					lp.width = imgMember.getWidth();
					lp.height = imgMember.getHeight();
					imgMember.setLayoutParams(lp);
					LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) circleImageView
							.getLayoutParams();
					lp2.width = imgMember.getWidth();
					lp2.height = imgMember.getHeight();
					circleImageView.setLayoutParams(lp2);
					isLoad = true;
					if (!TextUtils.isEmpty(MemberId)) {
						UIHelper.SetText(
								R.id.tvTitle,
								LanguageConstants.lb_ItemEditUserView_BarTitleEditUser,
								MemberInfoActivity.this);
						member = new MemberDAL(mContext).SelectById(MemberId);
						btnAction.setText(LanguageHelper
								.GetVal(LanguageConstants.lb_ItemEditUserView_BtnDelete,
										mContext));
						tvSave.setVisibility(View.VISIBLE);
						tvSave.setText(LanguageHelper.GetVal(
								LanguageConstants.lb_ItemEditUserView_BtnSave,
								mContext));
						setData();
					} else {
						UIHelper.SetText(
								R.id.tvTitle,
								LanguageConstants.lb_ItemEditUserView_BarTitleAddUser,
								MemberInfoActivity.this);
						tvSave.setVisibility(View.INVISIBLE);
						btnAction.setText(LanguageHelper.GetVal(
								LanguageConstants.lb_ItemEditUserView_BtnAdd,
								mContext));
					}

				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: // 调用Gallery返回的
			if (data != null) {
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor actualimagecursor = managedQuery(uri, proj, null, null,
						null);
				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToFirst();

				String img_path = actualimagecursor
						.getString(actual_image_column_index);
				setImageUrl(img_path);
			}
			break;
		case CAMERA_WITH_DATA: // 照相机程序返回的
			if (resultCode == 0)
				return;
			setImageUrl(mCurrentPhotoFile.getPath());
			break;

		}
	}

	public void setData() {
		etName.setText(member.getMembername());
		etTargetWeight.setText(member.getTargetweight());
		etAge.setTag(member.getBirthday());
		if (!TextUtils.isEmpty(member.getBirthday())) {
			int age = Common.GetAgeByBirthday(member.getBirthday());
			etAge.setText(age + "");
		}
		etHeight.setText(member.getHeight());
		if (member.getModeltype().equals("0"))
			rbMode1.setChecked(true);
		else if (member.getModeltype().equals("1"))
			rbMode2.setChecked(true);
		else {
			rbMode3.setChecked(true);
		}
		if (member.getSex().equals("1"))
			rbGender2.setChecked(true);
		else
			rbGender1.setChecked(true);

		if (member.getClientImg() != null && member.getClientImg().length > 0) {
			imgStream = member.getClientImg();
			bitmap = ImageUtil.decodeSampledBitmapFromByte(imgStream,imgMember.getWidth(),imgMember.getHeight());
			if (bitmap != null)
				setImageBitmap(bitmap);
		}
		if (member.getIconfile() != null && !member.getIconfile().equals(""))
			loadImage(member.getIconfile());
	}

	private void loadImage(final String urlString) {
		if (urlString.startsWith("http")) {
			new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						URL url = new URL(urlString);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setDoInput(true);
						conn.connect();
						InputStream inputStream = conn.getInputStream();
						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						byte[] data = new byte[1024];
						int count = -1;
						while ((count = inputStream.read(data, 0, 1024)) != -1)
							outStream.write(data, 0, count);

						data = null;
						imgStream = outStream.toByteArray();
						member.setClientImg(imgStream);
						new MemberDAL(mContext).UpdateImage(member);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								bitmap = ImageUtil.decodeSampledBitmapFromByte(imgStream,imgMember.getWidth(),imgMember.getHeight());
								if (bitmap != null)
									setImageBitmap(bitmap);

							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}

					super.run();
				}

			}.start();
		} else {
			imgMember.setImageURI(Uri.parse(urlString));
			imgMember.setVisibility(View.GONE);
			circleImageView.setVisibility(View.VISIBLE);
			circleImageView.setImageURI(Uri.parse(urlString));
			imgStream = FileHelper.encodeByteFile(urlString);
		}
	}

	@Click(R.id.imgHelp)
	void clickPop() {
		if (helpPopupWindow == null) {
			LayoutInflater inflater = LayoutInflater.from(this);
			PopupContentView = inflater.inflate(R.layout.pop_help, null);

			helpPopupWindow = new PopupWindow(PopupContentView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
			helpPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			helpPopupWindow.setOutsideTouchable(true);
			helpPopupWindow.setFocusable(true);
			helpPopupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					isOpenPopWindow = false;
					imgHelp.setImageResource(R.drawable.bg_help);
				}
			});
		}
		if (isOpenPopWindow) {
			helpPopupWindow.dismiss();
			isOpenPopWindow = false;
			imgHelp.setImageResource(R.drawable.bg_help);
		} else {
			int hei = HidePopupContentView.getHeight();
			// 保存anchor在屏幕中的位置
			int[] location = new int[2];
			// 保存anchor上部中点
			int[] anchorCenter = new int[2];
			// 读取位置anchor座标
			imgHelp.getLocationOnScreen(location);
			// 计算anchor中点
			anchorCenter[0] = location[0] + imgHelp.getWidth() / 2;
			anchorCenter[1] = location[1] - imgHelp.getHeight()
					- imgHelp.getHeight();
			helpPopupWindow.showAtLocation(imgHelp, Gravity.TOP | Gravity.LEFT,
					anchorCenter[0], anchorCenter[1] - hei);

			// helpPopupWindow.showAsDropDown(imgHelp);
			isOpenPopWindow = true;
			imgHelp.setImageResource(R.drawable.bg_help_f2);
		}
	}

	@Click(R.id.btnAction)
	void Action() {
		if (member != null) {
			if (TextUtils.isEmpty(member.getMemberid())) {
				new MemberDAL(mContext).DelByClientId(member.getClientid());
			} else {
				ActionMDL actionMDL = new ActionMDL(UUID.randomUUID()
						.toString(), ActionMDL.DelMember, member.getMemberid());
				new ActionDAL(mContext).Insert(actionMDL);
				new MemberDAL(mContext).DelByMemberId(member.getMemberid());
				Common.DelMemberThread(member, mContext);
			}
			finish();
		} else {
			save();
		}
	}
	boolean Ver()
	{
		if(TextUtils.isEmpty(etName.getText().toString()))
		{
			DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.alert_FillName, mContext));
			return false;
		}
		if(TextUtils.isEmpty(etAge.getText().toString()))
		{
			DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.alert_AgeVal, mContext));
			return false;
		}
		if(TextUtils.isEmpty(etHeight.getText().toString()))
		{
			DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.alert_inputheighterror, mContext));
			return false;
		}
		else {
			int val=ObjectHelper.Convert2Int(etHeight.getText().toString());
			if(val<10||val>220)
			{
				DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.alert_inputheighterror, mContext));
				return false;
			}
		}
		return true;
	}
	@Click(R.id.tvSave)
	void save() {
		if(!Ver())
		{
			return ;
		}
		if (TextUtils.isEmpty(MemberId)) {
			DialogHelper.showComfrimDialog(
					this,
					LanguageHelper.GetVal(LanguageConstants.lb_dialog_tip,
							mContext),
					LanguageHelper.GetVal(
							LanguageConstants.query_CantChangeGenderTip, mContext),
					LanguageHelper.GetVal(
							LanguageConstants.alertButton_confirm, mContext),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							doSave();

						}
					}, LanguageHelper.GetVal(
							LanguageConstants.alertButton_cancel, mContext),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});
		}
		else {
			doSave();
		}
	}
	private void doSave()
	{
		String Name = etName.getText().toString();
		String TargetWeight = etTargetWeight.getText()
				.toString();
		String Birthday = (String) etAge.getTag();
		String Height = etHeight.getText().toString();
		String Gender = "";
		if (rbGender1.isChecked())
			Gender = "0";
		else
			Gender = "1";
		String ModelType = "";
		if (rbMode1.isChecked())
			ModelType = ModeTypeEnum.Adult;
		else if (rbMode2.isChecked())
			ModelType = ModeTypeEnum.Children;
		else
			ModelType = ModeTypeEnum.Bady;

		if (member == null) {
			member = new MemberMDL();
			member.setUpload(0);

			member.setClientid(UUID.randomUUID().toString());
		}
		member.setClientImg(imgStream);
		if (GetApp().getUser() == null)
			member.setUserid("");
		else
			member.setUserid(GetApp().getUser().getUserid());
		member.setMembername(Name);
		member.setTargetweight(TargetWeight);
		member.setBirthday(Birthday);
		member.setHeight(Height);
		member.setSex(Gender);
		member.setModeltype(ModelType);
		boolean state = false;
		if (TextUtils.isEmpty(member.getMemberid())) {
			state = new MemberDAL(mContext).Insert(member);

			if (state) {
				ActionMDL actionMDL = new ActionMDL(UUID
						.randomUUID().toString(),
						ActionMDL.AddMember, member
								.getClientid());
				new ActionDAL(mContext).Insert(actionMDL);
				Common.AddMemberThread(mContext);
				DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.hud_addSuccess, mContext));
			}
		} else {
			state = new MemberDAL(mContext).Update(member);
			if (state) {
				ActionMDL actionMDL = new ActionMDL(UUID
						.randomUUID().toString(),
						ActionMDL.UpdateMember, member
								.getClientid());
				new ActionDAL(mContext).Insert(actionMDL);
				Common.UpdateMemberThread(member, mContext);
				DialogHelper.showTost(mContext, LanguageHelper.GetVal(LanguageConstants.hud_modifySuccess, mContext));
			}
		}
		if (state) {
			ApplicationMamager.getInstance()
					.goMainActivity(mContext);
			MemberInfoActivity.this.finish();
		} else {
			DialogHelper.showTost(mContext,LanguageHelper.GetVal(LanguageConstants.hud_adviceFail, mContext));
		}
	}
	@Click(R.id.imgMember)
	void selectMemberImage() {
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
	}

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
		//imgMember.setImageURI(Uri.parse(url));
		imgMember.setVisibility(View.GONE);
		circleImageView.setVisibility(View.VISIBLE);
		imgStream = FileHelper.encodeByteFile(url);
		bitmap=ImageUtil.decodeSampledBitmapFromByte(imgStream, imgMember.getWidth(), imgMember.getHeight());
		circleImageView.setImageBitmap(bitmap);
		
		//circleImageView.setImageURI(Uri.parse(url));
		
	}

	public void setImageBitmap(Bitmap bitmap) {
		// imgMember.setImageURI(Uri.parse(url));
		imgMember.setVisibility(View.GONE);
		circleImageView.setVisibility(View.VISIBLE);
		circleImageView.setImageBitmap(bitmap);
		// imgStream = FileHelper.encodeByteFile(url);
	}

	private void showDate() {
		Dialog dialog = null;
		Calendar c = Calendar.getInstance();
		String text = (String) etAge.getTag();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (!TextUtils.isEmpty(text)) {
			Date date = ObjectHelper.Convert2Date(text, "yyyy-MM-dd");

			cal.setTime(date);
			if (date != null) {
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);
				day = cal.get(Calendar.DAY_OF_MONTH);
				;

			}
		}
		dialog = new DatePickerDialog(mContext,
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker dp, int year, int month,
							int dayOfMonth) {
						String nowmonth = (month + 1) < 10 ? ("0" + (month + 1))
								: (month + 1) + "";
						String nowdayOfMonth = dayOfMonth < 10 ? ("0" + dayOfMonth)
								: dayOfMonth + "";
						String selectDateTime = year + "-" + nowmonth + "-"
								+ nowdayOfMonth;
						etAge.setTag(selectDateTime);
						int age = Common.GetAgeByBirthday(selectDateTime);
						etAge.setText(age + "");
					}
				}, year, // 传入年份
				month, // 传入月份
				day // 传入天数
		);
		dialog.show();

	}
}
