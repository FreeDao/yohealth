package com.vc.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vc.lib.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewWindow {
	ListView lvHP;
	Button btn_cancel;
	private Context mContext;
	List<String> datas;
	OnItemClickListener clickListener;

	String title;
	String cancel;
	Dialog dialog;
	String[] arrayString;
	private OnSelectListener onSelectListener;

	public ListViewWindow(Context context, OnSelectListener clickListener,
			List<String> items, String title,String cancel) {

		mContext = context;
		datas=items;
		arrayString = new String[items.size()];
		arrayString=items.toArray(arrayString);
		this.onSelectListener = clickListener;
		this.title = title;
		this.cancel=cancel;
		init();
	}
	public ListViewWindow(Context context, OnSelectListener clickListener,
			List<String> items, String title) {

		mContext = context;
		datas=items;
		arrayString = new String[items.size()];
		arrayString=items.toArray(arrayString);
		this.onSelectListener = clickListener;
		this.title = title;
		this.cancel="È¡Ïû";
		init();
	}
	public ListViewWindow(Context context, OnSelectListener clickListener,
			String[] items, String title) {

		mContext = context;
		arrayString = items;
		this.onSelectListener = clickListener;
		this.title = title;
		init();
	}
	public ListViewWindow(Context context, OnSelectListener clickListener,
			String[] items, String title,String cancel) {

		mContext = context;
		arrayString = items;
		this.onSelectListener = clickListener;
		this.title = title;
		this.cancel=cancel;
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		

		// OnClickListener listener = null;
		dialog = new AlertDialog.Builder(mContext)
				.setTitle(title)
				.setItems(arrayString,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (onSelectListener != null)
									onSelectListener.onSelect(dialog, which);
							}
						})
				.setNegativeButton(cancel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).create();
	}

	public interface OnSelectListener {
		void onSelect(DialogInterface dialog, int which);
	}

	public void setOnSelectListener(OnSelectListener l) {
		onSelectListener = l;
	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}

}
