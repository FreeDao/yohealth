package com.vc.cloudbalance.common;

import com.vc.cloudbalance.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogHelper {

	static Dialog dialog;
	static TextView tipTextView;
	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static void ShowLoadingDialog(Context context, String msg) {
		if (dialog == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
			LinearLayout layout = (LinearLayout) v
					.findViewById(R.id.dialog_view);// 加载布局
			// main.xml中的ImageView
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
			tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
					context, R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			tipTextView.setText(msg);// 设置加载信息

			dialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

			dialog.setCancelable(false);// 不可以用“返回键”取消
			dialog.setContentView(layout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		}
		else
		{
			tipTextView.setText(msg);
		}
		dialog.show();

	}

	public static void CloseLoadingDialog() {
		if(dialog!=null){
			dialog.dismiss();
			dialog=null;
		}
		

	}
	public static void showTost(Context context, String mess) {
		try {
			Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	public static void showComfrimDialog(Context c, String title, String msg,
			String okTitle, DialogInterface.OnClickListener okClickListener,
			String cancleTitle,
			DialogInterface.OnClickListener cancleClickListener) {
		Builder dialog = new AlertDialog.Builder(c).setTitle(title)
				.setMessage(msg).setPositiveButton(okTitle, okClickListener);
		if (cancleClickListener != null)
			dialog.setNegativeButton(cancleTitle, cancleClickListener);

		dialog.show();
	}

	public static void showComfrimDialog(Context c, String title, String msg,
			DialogInterface.OnClickListener okClickListener,
			DialogInterface.OnClickListener cancleClickListener) {
		showComfrimDialog(c, title, msg, "确定", okClickListener, "取消",
				cancleClickListener);
	}
}
