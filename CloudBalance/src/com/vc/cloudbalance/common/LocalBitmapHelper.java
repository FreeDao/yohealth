package com.vc.cloudbalance.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LocalBitmapHelper {

	public static Bitmap ReadBgImg(Context context, int resId) {
		Bitmap bm = BitmapFactory.decodeStream(context.getResources().openRawResource(
				resId));
		
		return bm;

	}
}
