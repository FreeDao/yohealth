package com.vc.cloudbalance.widget;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.vc.cloudbalance.*; 
import com.vc.cloudbalance.common.Constants;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.MemberDAL;
import com.vc.cloudbalance.widget.LockableHorizontalScrollView.OnCustomTouchListenter;
import com.vc.util.FileHelper;
import com.vc.util.ImageUtil;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
@EViewGroup(R.layout.view_item_member)
public class View_Item_Member extends LinearLayout{
	private Context mContext;
	
	private Scroller mScroller; 
	@ViewById
	LockableHorizontalScrollView lockableHorizontalScrollView1;
	@ViewById
	ImageView imgHeadImage,imgLine;
	@ViewById
	TextView tvName;
	@ViewById
	LinearLayout llFont,llBack;
	@ViewById
	Button btnData,btnEdit;
	@ViewById
	CircleImageView circleImageView;
	MemberMDL member;
	boolean isLoad;
	int offset_x=0;
	byte[] imgStream;
	Bitmap bitmap;
	public View_Item_Member(Context context) {
		super(context);
		mContext=context;
		
		 
		// TODO Auto-generated constructor stub
	}
	public View_Item_Member(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		
		// TODO Auto-generated constructor stub
	}
	@Click(R.id.btnData)
	void showDataActivity()
	{
		Intent intent  = MemberDataActivity_.intent(mContext).get();
		intent.putExtra(Constants.EXTRA_KEY_ID_STRING, member.getMemberid());
		mContext.startActivity(intent);
	}
	@Click(R.id.btnEdit)
	void showMemberInfoActivity()
	{
		
		Intent intent = MemberInfoActivity_.intent(mContext).get();
		intent.putExtra(Constants.EXTRA_KEY_ID_STRING, member.getMemberid());
		mContext.startActivity(intent);
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
				if (!isLoad) {
					offset_x=llBack.getWidth();
					lockableHorizontalScrollView1.setScroll_x(offset_x);
					isLoad = true;
				}

			}
		});
		
//		llFont.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.e("clickFontView", "clickFontView");
//			}
//		});
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
				showDataActivity();
				Log.e("clickFontView", "clickFontView");
			}

			@Override
			public void prePage() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void touch() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public MemberMDL getMember() {
		return member;
	}
	public void setMember(MemberMDL member) {
		if(member!=null)
		{
			tvName.setText(member.getMembername());
			if (member.getClientImg() != null && member.getClientImg().length > 0) {
				imgStream = member.getClientImg();
				bitmap = ImageUtil.decodeSampledBitmapFromByte(imgStream,96,96);
				if (bitmap != null)
					setImageBitmap(bitmap);
			}
			if(member.getIconfile()!=null && !member.getIconfile().equals(""))
				loadImage(member.getIconfile());
		}
		this.member = member;
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
	public void setImageUrl(String url) {
		imgHeadImage.setImageURI(Uri.parse(url));
		imgHeadImage.setVisibility(View.GONE);
		circleImageView.setVisibility(View.VISIBLE);
		circleImageView.setImageURI(Uri.parse(url));
		imgStream = FileHelper.encodeByteFile(url);
	}

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
		imgHeadImage=null;
		
		circleImageView.setImageBitmap(null);
		circleImageView=null;
		imgStream=null;
		if(bitmap!=null)
			bitmap.recycle();
		bitmap=null;
	}
}
