package com.vc.cloudbalance.widget;

import java.lang.reflect.Field;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.OverScroller;
import android.widget.Scroller;

public class LockableHorizontalScrollView extends HorizontalScrollView {
	private Field mScrollerField;
	ScrollerEx scrollerEx = null;
	static final int ANIMATED_SCROLL_GAP = 250;
	private long mLastScroll;
	OnCustomTouchListenter onCustomTouchListenter;
	int screenWidth = 0;
	boolean isCancel = false;
	public static boolean test = false;

	public LockableHorizontalScrollView(Context context, AttributeSet attrset) {
		super(context, attrset);
		screenWidth = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();
		// this.setSmoothScrollingEnabled(false);
		initScroller();
	}

	public OnCustomTouchListenter getOnCustomTouchListenter() {
		return onCustomTouchListenter;
	}

	public void setOnCustomTouchListenter(
			OnCustomTouchListenter onCustomTouchListenter) {
		this.onCustomTouchListenter = onCustomTouchListenter;
	}

	// true if we can scroll (not locked)
	// false if we cannot scroll (locked)
	private boolean mScrollable = true;
	private int scroll_x = 0;

	public int getScroll_x() {
		return scroll_x;
	}

	public void setScroll_x(int scroll_x) {
		this.scroll_x = scroll_x;
	}

	public void setIsScrollable(boolean scrollable) {
		mScrollable = scrollable;
	}

	public boolean getIsScrollable() {
		return mScrollable;
	}

	private void initScroller() {
		try {
			mScrollerField = HorizontalScrollView.class
					.getDeclaredField("mScroller");
			mScrollerField.setAccessible(true);
			String type = mScrollerField.getType().getSimpleName();

			if ("OverScroller".equals(type)) {
				scrollerEx = new ScrollerEx() {
					private OverScroller mScroller = null;

					public void startScroll(int startX, int startY, int dx,
							int dy, int duration) {
						mScroller.startScroll(startX, startY, dx, dy, duration);
					}

					public boolean isFinished() {
						return mScroller.isFinished();
					}

					public Object getScroller() {
						return mScroller;
					}

					@SuppressLint("NewApi")
					public void create(Context context,
							Interpolator interpolator) {
						mScroller = new OverScroller(context, interpolator);
					}

					public void abortAnimation() {
						if (mScroller != null) {
							mScroller.abortAnimation();
						}
					}
				};
			} else {
				scrollerEx = new ScrollerEx() {
					private Scroller mScroller = null;

					public void startScroll(int startX, int startY, int dx,
							int dy, int duration) {
						mScroller.startScroll(startX, startY, dx, dy, duration);
					}

					public boolean isFinished() {
						return mScroller.isFinished();
					}

					public Object getScroller() {
						return mScroller;
					}

					public void create(Context context,
							Interpolator interpolator) {
						mScroller = new Scroller(context, interpolator);
					}

					public void abortAnimation() {
						if (mScroller != null) {
							mScroller.abortAnimation();
						}
					}
				};
			}

		} catch (Exception ex) {
		}
	}

	public final void smoothScrollBy(int dx, int dy, int addDuration) {

		float tension = 0f;

		scrollerEx.abortAnimation();

		Interpolator ip = new OvershootInterpolator(tension);
		scrollerEx.create(getContext(), ip);

		try {
			mScrollerField.set(this, scrollerEx.getScroller());
		} catch (Exception e) {
		}

		long duration = AnimationUtils.currentAnimationTimeMillis()
				- mLastScroll;
		if (duration > ANIMATED_SCROLL_GAP) {
			scrollerEx.startScroll(getScrollX(), getScrollY(), dx, dy,
					addDuration);

			awakenScrollBars();

			// awakenScrollBars(mScroller.getDuration());

			invalidate();
		} else {
			if (!scrollerEx.isFinished()) {
				scrollerEx.abortAnimation();
			}
			scrollBy(dx, dy);
		}
		mLastScroll = AnimationUtils.currentAnimationTimeMillis();

	}

	public final void smoothScrollTo(int x, int y, int duration) {
		smoothScrollBy(x - getScrollX(), y - getScrollY(), duration);
	}

	public boolean IsOpen() {
		if (nowX == 0)
			return false;
		else {
			return true;
		}
	}

	private int downX = 0;
	int nowX = 0;
	private long down_time;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (onCustomTouchListenter != null)
			onCustomTouchListenter.touch();
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.e("LockableHorizontalScrollView->onTouchEvent", "");
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			down_time = new Date().getTime();
			isCancel = false;
			// /return false;
			test = false;
			break;
		case MotionEvent.ACTION_MOVE: {
			// Log.e("ACTION_MOVE","ACTION_MOVE");
			if (downX < ev.getX() && !IsOpen()) {
				isCancel = true;
				test = true;
				// getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
			// getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		case MotionEvent.ACTION_UP: {
			long up_time = new Date().getTime();
			if (Math.abs((ev.getX() - downX)) <= 40
					&& (!IsOpen() || (IsOpen() && ev.getX() < (screenWidth - getScroll_x())))) {
				if (up_time - down_time < 200) {
					if (onCustomTouchListenter != null)
						onCustomTouchListenter.click();
				}
			}
			// Log.e("ACTION_UP",ev.getX()+"   "+new
			// Date().getTime()+"   "+SystemClock.uptimeMillis()+
			// "   "+up_time+"    "+down_time+"   "+(up_time-down_time));
		}
		case MotionEvent.ACTION_CANCEL: {

			if (Math.abs((ev.getX() - downX)) > 50) {
				if (ev.getX() - downX > 0) {
					if (nowX == 0) {
						if (onCustomTouchListenter != null)
							onCustomTouchListenter.prePage();
					}
					nowX = 0;
					smoothScrollTo(0, 0, 550);
					if (onCustomTouchListenter != null)
						onCustomTouchListenter.close();
					// smoothScrollToPrePage();
				} else {
					nowX = getScroll_x();
					smoothScrollTo(getScroll_x(), 0, 550);
					if (onCustomTouchListenter != null)
						onCustomTouchListenter.open();
					// smoothScrollToNextPage();
				}
			} else {
				smoothScrollTo(nowX, 0, 550);
				// smoothScrollToCurrent();
			}
			return true;
		}
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mScrollable)
			return super.onInterceptTouchEvent(ev);
		else
			return false;
	}

	private interface ScrollerEx {

		void create(Context context, Interpolator interpolator);

		Object getScroller();

		void abortAnimation();

		void startScroll(int startX, int startY, int dx, int dy, int duration);

		boolean isFinished();

	}

	public interface OnCustomTouchListenter {
		void open();

		void close();

		void click();

		void prePage();
		
		void touch();
	}
}