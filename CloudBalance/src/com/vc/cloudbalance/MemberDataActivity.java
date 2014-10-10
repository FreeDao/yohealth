package com.vc.cloudbalance;

import java.util.LinkedList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.example.hellojni.HelloJni;
import com.vc.cloudbalance.adapter.BasePageAdapter;
import com.vc.cloudbalance.common.App;
import com.vc.cloudbalance.common.BaseActivity;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.common.LanguageConstants;
import com.vc.cloudbalance.common.LanguageHelper;
import com.vc.cloudbalance.common.UIHelper;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.widget.*;
import com.vc.cloudbalance.widget.View_ShowBalanceData.OnParentViewChangeListener;
import com.vc.util.ObjectHelper;

@EActivity(R.layout.activity_memberdata)
public class MemberDataActivity extends BaseActivity {
	List<View> views;
	@ViewById
	ViewFlipper viewFlipper;
	@ViewById
	ImageView imgPage1, imgPage2;
	@ViewById
	LinearLayout llPageIndexPanel;
	@Extra(Constants.EXTRA_KEY_ID_STRING)
	String MemberId;
	MemberMDL member;
	private BluetoothAdapter mBluetoothAdapter;
	private static final long SCAN_PERIOD = 10000;
	private boolean mScanning;
	FactoryBalanceView factoryBalanceView;
	HelloJni sohelper;
	Animation leftInAnimation;
	Animation leftOutAnimation;
	Animation rightInAnimation;
	Animation rightOutAnimation;
	private GestureDetector mGestureDetector;
	int ViewIndex = 0;
	boolean isDebug = false;
	View_ShowBalanceData showbalancedataview;
	boolean isGuest = false;

	static {

		System.loadLibrary("hello-jni");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		scanLeDevice(false);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case View_BigFlowerBalanceData.PHOTO_PICKED_WITH_DATA: // 调用Gallery返回的
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
				factoryBalanceView.setImageUrl(img_path);
			}
			break;
		case View_BigFlowerBalanceData.CAMERA_WITH_DATA: // 照相机程序返回的
			if (resultCode == 0)
				return;
			factoryBalanceView.setImageUrl(View_BigFlowerBalanceData.mCurrentPhotoFile.getPath());
			break;

		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		final int version = android.os.Build.VERSION.SDK_INT;
		if (version >= 18) {
			final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			mBluetoothAdapter = bluetoothManager.getAdapter();

			if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 1);
			}

			scanLeDevice(true);
		}
		super.onResume();
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			// mHandler.postDelayed(new Runnable() {
			// @Override
			// public void run() {
			// mScanning = false;
			// mBluetoothAdapter.stopLeScan(mLeScanCallback);
			// }
			// }, SCAN_PERIOD);

			mScanning = true;
			if (mBluetoothAdapter != null)
				mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			if (mBluetoothAdapter != null)
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}

	}

	protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {

			String aa = "";
			try {
				// 0FFF02A109FF0045FFFF80FFFF117DAA0201060909596F4865616C7468000000000000000000000000000000000000000000000000000000000000000000
				String scanRecordStr = bytesToHex(scanRecord);
				final String val = scanRecordStr.substring(
						scanRecordStr.indexOf("02"),
						scanRecordStr.indexOf("02") + 28);
				//Log.e("val", val);
				// 0FFF02A109FF0045FFFF80FFFF117DAA0201060909596F4865616C7468000000000000000000000000000000000000000000000000000000000000000000
				int sex = ObjectHelper.Convert2Int(member.getSex());// 0 是女 1是男
				int age = ObjectHelper.Convert2Int(member.getBirthday());
				int height = ObjectHelper.Convert2Int(member.getHeight());
				final float weight = (float) (Integer.parseInt(
						Common.GetBluetoothValIndex(val, 4)
								+ Common.GetBluetoothValIndex(val, 5), 16)) / 10;
				int dz = Integer.parseInt(Common.GetBluetoothValIndex(val, 6)
						+ Common.GetBluetoothValIndex(val, 7), 16);
				final String realdata = sohelper.getHealth(sex, age, height,
						weight, dz);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						factoryBalanceView.setData(weight + "", realdata, val);
					}
				});

			} catch (Exception e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	
	@AfterViews
	void init() {
		
		if (MemberId.equals("guest"))
			isGuest = true;
		else
			isGuest = false;
		if (!isGuest) {
			mGestureDetector = new GestureDetector(onGestureListener);
			leftInAnimation = AnimationUtils.loadAnimation(mContext,
					R.anim.in_left);
			leftOutAnimation = AnimationUtils.loadAnimation(mContext,
					R.anim.out_left);
			rightInAnimation = AnimationUtils.loadAnimation(mContext,
					R.anim.in_right);
			rightOutAnimation = AnimationUtils.loadAnimation(mContext,
					R.anim.out_right);
			sohelper = new HelloJni();
			views = new LinkedList<View>();
			member = new MemberDAL(mContext).SelectById(MemberId);
			if (member != null) {
				setTitleText(member.getMembername());
				factoryBalanceView = new FactoryBalanceView(mContext);
				factoryBalanceView.init(member);
				viewFlipper.addView(factoryBalanceView);

			}
			showbalancedataview = View_ShowBalanceData_.build(mContext, member);
			showbalancedataview.setParentViewFlipper(viewFlipper);
			showbalancedataview
					.setOnParentViewChangeListener(new OnParentViewChangeListener() {

						@Override
						public void onPreviou() {
							// TODO Auto-generated method stub
							showPreviousPage();
						}

						@Override
						public void onNext() {
							// TODO Auto-generated method stub

						}
					});
			viewFlipper.addView(showbalancedataview);
			viewFlipper.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					mGestureDetector.onTouchEvent(event);
					return true;
				}
			});
		} else {
			member = App.GuestMember;
			setTitleText("客人");
			llPageIndexPanel.setVisibility(View.GONE);
			mGestureDetector = new GestureDetector(onGestureListener);
			sohelper = new HelloJni();
			views = new LinkedList<View>();

			factoryBalanceView = new FactoryBalanceView(mContext);
			factoryBalanceView.init(member);
			viewFlipper.addView(factoryBalanceView);

		}

	}

	public void showNextPage() {
		if (ViewIndex == 1)
			return;
		ViewIndex = 1;
		viewFlipper.setInAnimation(leftInAnimation);
		viewFlipper.setOutAnimation(leftOutAnimation);
		viewFlipper.showNext();// 向右滑动
		imgPage1.setImageResource(R.drawable.rb_page_f1);
		imgPage2.setImageResource(R.drawable.rb_page_f2);
		showbalancedataview.loadListData();
	}

	public void showPreviousPage() {
		if (ViewIndex == 0)
			return;
		ViewIndex = 0;
		viewFlipper.setInAnimation(rightInAnimation);
		viewFlipper.setOutAnimation(rightOutAnimation);
		viewFlipper.showPrevious();// 向左滑动
		imgPage1.setImageResource(R.drawable.rb_page_f2);
		imgPage2.setImageResource(R.drawable.rb_page_f1);
		showbalancedataview.removeListData();

	}

	OnGestureListener onGestureListener = new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if (e1.getX() - e2.getX() > 120) {
				showNextPage();
				return true;
			} else if (e1.getX() - e2.getX() < -120) {
				showPreviousPage();
				return true;
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	};
}
