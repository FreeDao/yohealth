package com.vc.image;

import com.vc.lib.R;
import com.vc.util.DensityHelper;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/* 
 * */
public class UroadImageView extends RelativeLayout implements IImageView {

	private Context mContext;
	private ProgressBar mProgressBar;
	private ImageView failImageView;
	private ImageViewTouchBase imageView;
	private ImageViewFactory factory;
	private String url = "";
	private PopupWindow window;
	private BitmapDisplayConfig config;
	private String imgText = "";
	private float maxscale = 0f;

	public UroadImageView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public UroadImageView(Context context, float maxscale) {
		super(context);
		this.mContext = context;
		init();
	}

	public UroadImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public UroadImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public ImageViewFactory getFactory() {
		return factory;
	}

	private void init() {
		removeAllViews();
		factory = ImageViewFactory.create(mContext);
		factory.configLoadfailImage(R.drawable.base_nodata);
		config = factory.getDisplayConfig();
		initImageView();
		initLoadImage();
		initProgressBar();
	}

	private void initLoadImage() {
		failImageView = new ImageView(mContext);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		failImageView.setLayoutParams(params);
		addView(failImageView);
	}

	private void initImageView() {
		if (maxscale != 0f) {
			imageView = new ImageViewTouchBase(mContext, maxscale);
		} else {
			imageView = new ImageViewTouchBase(mContext);
		}

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		imageView.setLayoutParams(params);
		addView(imageView);
	}

	public void setBorder(int bw, int color) {
		this.setPadding(bw, bw, bw, bw);
		this.setBackgroundColor(color);
	}

	private void initProgressBar() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleInverse);
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		mProgressBar.setLayoutParams(params);
		mProgressBar.setVisibility(View.GONE);
		addView(mProgressBar);
	}

	public void setLoading() {
		mProgressBar.setVisibility(View.VISIBLE);
		failImageView.setVisibility(View.GONE);
	}

	public void setScaleEnabled(boolean e) {
		imageView.setScaleEnabled(e);
	}

	public void setEndLoading() {
		mProgressBar.setVisibility(View.GONE);
		failImageView.setVisibility(View.GONE);
	}

	public ImageView getImageView() {
		return imageView;
	}

	public ImageViewTouchBase getImageViewTouchBase() {
		return imageView;
	}

	public void setBaseScaleType(ImageView.ScaleType type) {
		imageView.setBaseScaleType(type);
	}

	public void setConfig(BitmapDisplayConfig displayConfig) {
		config = displayConfig;
	}

	public BitmapDisplayConfig getConfig() {
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#dispose(boolean)
	 */
	@Override
	public void dispose(boolean isClearCache) {
		factory.recycle(imageView);
		if (isClearCache) {
			factory.clearCache(url);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#setImageUrl(java.lang.String)
	 */
	@Override
	public void setImageUrl(String url) {
		this.url = url;
		factory.display(this, url, null);
	}

	public void setImageUrlNoLoading(String url) {
		this.url = url;
		config = factory.getDisplayConfig();
		config.setShowLoading(false);
		factory.display(this, url, config);
	}

	public void setImageUrlCus(String url, BitmapDisplayConfig config) {
		this.url = url;
		factory.display(this, url, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#setImageUrlNotCache(java.lang.String)
	 */
	@Override
	public void setImageUrlNotCache(String url) {
		this.url = url;
		config = factory.getDisplayConfig();
		config.setIsUseCache(false);
		factory.display(this, url, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#setImageUrlNotCache(java.lang.String)
	 */
	@Override
	public void setImageUrlMemoryCache(String url) {
		this.url = url;
		config = factory.getDisplayConfig();
		config.setIsUseMemoryCache(true);
		factory.display(this, url, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#setImageUrl(java.lang.String, int)
	 */
	@Override
	public void setImageUrl(String url, int faileResid) {
		this.url = url;
		factory.configLoadfailImage(faileResid);
		factory.display(this, url, null);
	}

	/**
	 * 设置图片的url、加载失败时的图片、是否显示加载
	 * **/
	public void setImageUrl(String url, int faileResid, boolean isSetLoading) {
		this.url = url;
		factory.configLoadfailImage(faileResid);
		config = factory.getDisplayConfig();
		if (isSetLoading) {
			config.setShowLoading(true);
		} else {
			config.setShowLoading(false);
		}
		factory.display(this, url, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#loadRes(int)
	 */
	@Override
	public void loadRes(int res) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#loadUri(android.net.Uri)
	 */
	@Override
	public void loadUri(Uri uri) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#loadUri(java.lang.String)
	 */
	@Override
	public void loadUri(String fileName) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#setScaleEnable(boolean)
	 */
	@Override
	public void setScaleEnable(boolean isScale) {
		// TODO Auto-generated method stub
		imageView.setScaleEnabled(isScale);
	}

	/*
	 * (non-Javadoc)点击全屏
	 * 
	 * 
	 * @see com.uroad.image.IImageView#toggleFillScreen()
	 */
	@Override
	public void toggleFillScreen() {
		// url为空或者不存在或者图片加载为空（包含了black.jpg）,不弹出窗口
		if (url != null && !"".equals(url) && url.indexOf("black.jpg") == -1) {
			//View view = null;
			// if (window == null) {

			//view = LayoutInflater.from(mContext).inflate(R.layout.base_pop_cctv_showimage, null);
			ImageView iView = new ImageView(mContext);
			//TextView tvRoadName = (TextView) view.findViewById(R.id.tvRoadName);
			iView.setImageBitmap(factory.getByCache(url));
			int width = DensityHelper.getScreenWidth(mContext);
			int height = DensityHelper.getScreenHeight(mContext);
			LayoutParams params = new LayoutParams(width, width);
			params.topMargin = height / 5;
			//iView.setLayoutParams(params);

			//tvRoadName.setText(imgText);

			window = new PopupWindow(iView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			window.setAnimationStyle(R.style.base_popup_animation);
			window.setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			window.setOutsideTouchable(true);
			window.setFocusable(true);
			window.setBackgroundDrawable(new BitmapDrawable());
			window.update();
			iView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					window.dismiss();
				}
			});

			// }
			window.showAtLocation(imageView, Gravity.CENTER, 0, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#cancelLoadingTask()
	 */
	@Override
	public void cancelLoadingTask() {
		// TODO Auto-generated method stub
		factory.checkImageTask(url, imageView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uroad.image.IImageView#clientRender(int)
	 */
	@Override
	public void clientRender(int res) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param text
	 *            显示在图片下面的配置文字（要先设置文字再全屏才会显示文字）
	 * **/
	public void setText(String text) {
		this.imgText = text;
	}

}
