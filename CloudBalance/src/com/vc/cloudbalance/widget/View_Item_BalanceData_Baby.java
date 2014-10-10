package com.vc.cloudbalance.widget;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.vc.cloudbalance.*;
import com.vc.cloudbalance.common.Common;
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.common.DialogHelper;
import com.vc.cloudbalance.model.ActionMDL;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.ActionDAL;
import com.vc.cloudbalance.sqlite.BalanceDataDAL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.widget.LockableHorizontalScrollView.OnCustomTouchListenter;
import com.vc.util.FileHelper;
import com.vc.util.ImageUtil;
import com.vc.util.ObjectHelper;

import de.hdodenhof.circleimageview.CircleImageView;
@EViewGroup(R.layout.view_item_balancedata_baby)
public class View_Item_BalanceData_Baby  extends LinearLayout{
	private Context mContext;
	
	private Scroller mScroller; 
	@ViewById
	LockableHorizontalScrollView lockableHorizontalScrollView1;
	@ViewById
	CircleImageView circleImageView;
	@ViewById
	ImageView imgHeadImage,imgLine;
	@ViewById
	TextView tvDate,tvDay,tvTime,tvWeight,tvHeight;
	@ViewById
	LinearLayout llFont,llBack;
	@ViewById
	Button btnDelete;
	MemberMDL member;
	boolean isLoad;
	int offset_x=0;
	View_ShowBalanceData parentView;
	ScrollViewExtend scroll;
	BalanceDataMDL data;
	byte[] imgStream;
	Bitmap bitmap;
	public View_Item_BalanceData_Baby(Context context,ScrollViewExtend scroll,View_ShowBalanceData parentView) {
		super(context);
		mContext=context;
		this.scroll=scroll;
		 this.parentView=parentView;
		// TODO Auto-generated constructor stub
	}
	public View_Item_BalanceData_Baby(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		
		// TODO Auto-generated constructor stub
	}
	public void setData(BalanceDataMDL data,MemberMDL m)
	{
		member=m;
		this.data=data;
		tvDate.setText(ObjectHelper.Convert2String(data.getWeidate(), "yyyy/MM/dd"));
		
		tvWeight.setText(data.getWeight());
		tvHeight.setText(data.getHeight());
		if(data.getHaveimg()!=null&&data.getHaveimg().equals("1"))
		{
			if(data.getClientImg()!=null)
			{
				imgStream=data.getClientImg();
				mHandler.sendEmptyMessage(0);
			}
			else {
				loadImage(data.getPicurl());
			}
		}
	}
	@Click(R.id.btnDelete)
	void delete()
	{
		BalanceDataDAL dal = new BalanceDataDAL(mContext);
		String startTime =ObjectHelper.Convert2String(data.getWeidate(), "yyyy-MM-dd 00:00:00");
		String endTime =ObjectHelper.Convert2String(data.getWeidate(), "yyyy-MM-dd 23:59:59");
		List<BalanceDataMDL> old_datas=dal.SelectThisDateData(data.getMemberid(),data.getClientmemberid(), startTime, endTime);
		if(old_datas.size()<=0)
			return;
		if(old_datas.size()==1)
		{
			DialogHelper.showTost(mContext, "当天只有一条记录，不能删除");
			return ;
		}
		boolean isNeedUpload=false;
		if(old_datas.get(0).getId()==data.getId())
		{
			isNeedUpload=true;
		}
		if(dal.DelById(data.getId()))
		{
			if(isNeedUpload)
			{
				dal.setUnloadData(old_datas.get(1));
				
				Common.SynBalanceDataThread(mContext);
			}
			
		}
	}
	@AfterViews
	void init() {
		// TODO Auto-generated method stub
		int screenWidth  = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth(); 
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)llFont.getLayoutParams();
		params.width=screenWidth;
		llFont.setLayoutParams(params);
		ViewTreeObserver vto1 = llBack.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isLoad||offset_x==0) {
					offset_x=llBack.getWidth();
					
					lockableHorizontalScrollView1.setScroll_x(offset_x);
					isLoad = true;
				}

			}
		});
		
		lockableHorizontalScrollView1.setOnCustomTouchListenter(new OnCustomTouchListenter() {
			
			@Override
			public void open() {
				// TODO Auto-generated method stub
				imgLine.setImageResource(R.drawable.bg_split_f2);
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				imgLine.setImageResource(R.drawable.bg_split_f1);
				
			}
			
			@Override
			public void click() {
				// TODO Auto-generated method stub
				Log.e("clickFontView", "clickFontView");
			}

			@Override
			public void prePage() {
				// TODO Auto-generated method stub
				//viewpager.setCurrentItem(0, true);
				Log.e("prePage", "prePage");
				parentView.prePager();
			}

			@Override
			public void touch() {
				// TODO Auto-generated method stub
				scroll.IsTouchingChildren();
			}
		}); 	
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
						byte[] dataByte = new byte[1024];
						int count = -1;
						while ((count = inputStream.read(dataByte, 0, 1024)) != -1)
							outStream.write(dataByte, 0, count);

						dataByte = null;
						imgStream = outStream.toByteArray();
						data.setClientImg(imgStream);
						new BalanceDataDAL(mContext).UpdateImage(data);
						mHandler.sendEmptyMessage(1);
					} catch (Exception e) {
						// TODO: handle exception
					}

					super.run();
				}

			}.start();
		} else {
			imgHeadImage.setImageURI(Uri.parse(urlString));
			imgHeadImage.setVisibility(View.GONE);
			circleImageView.setVisibility(View.VISIBLE);
			circleImageView.setImageURI(Uri.parse(urlString));
			imgStream = FileHelper.encodeByteFile(urlString);
		}
	}
	Handler mHandler = new  Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			bitmap = ImageUtil.decodeSampledBitmapFromByte(imgStream,96,96);
			
			if (bitmap != null)
				setImageBitmap(bitmap);
			super.handleMessage(msg);
		}
		
	};
	
	public void setImageBitmap(Bitmap bitmap) {
		// imgMember.setImageURI(Uri.parse(url));
		imgHeadImage.setVisibility(View.GONE);
		circleImageView.setVisibility(View.VISIBLE);
		circleImageView.setImageBitmap(bitmap);
		// imgStream = FileHelper.encodeByteFile(url);
	}
	public void recycle()
	{
		imgHeadImage.setImageBitmap(null);
		circleImageView.setImageBitmap(null);
		imgStream=null;
		if(bitmap!=null)
			bitmap.recycle();
	}
	
}
