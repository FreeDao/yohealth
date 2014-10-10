package com.vc.util;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

public class DensityHelper {
	 
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    public static int getScreenWidth(Context context)
    {
    	return ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
    }
    
     
    public static int getScreenHeight(Context context)
    {
    	return ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
    }
    
    

	
	public static int[] getScreenSize(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(
						    Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();//��Ļ���?
		int height = wm.getDefaultDisplay().getHeight();//��Ļ�߶�
		int[] size = {width,height};
		
		return size;
	}
	
	
	public static int getStatusBarHeight(Context context){
		int statusBarHeight = 0;
		
		int screenHeight = getScreenSize(context)[1];
		
		switch(screenHeight){
		case 240:
			statusBarHeight = 20;
			break;
		case 480:
			statusBarHeight = 25;
			break;
		case 800:
			statusBarHeight = 38;
			break;
		default:
			break;
		}
		
		return statusBarHeight;
	}
}
