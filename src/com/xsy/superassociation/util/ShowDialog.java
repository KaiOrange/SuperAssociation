package com.xsy.superassociation.util;

import android.app.Activity;
import android.app.ProgressDialog;

public class ShowDialog {
	public static ProgressDialog getDialogProgress(Activity activity,String title,String content){
		ProgressDialog dialog = new ProgressDialog(activity); 
		//设置进度条风格，风格为圆形，旋转的 
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		//设置ProgressDialog 标题 
		dialog.setTitle(title); 
		//设置ProgressDialog 提示信息 
		dialog.setMessage(content); 
		//设置ProgressDialog 标题图标 
		dialog.setIcon(android.R.drawable.ic_dialog_map); 
		//设置ProgressDialog 的进度条是否不明确 
		dialog.setIndeterminate(false); 
		//设置ProgressDialog 是否可以按退回按键取消 
		dialog.setCancelable(false);
		return dialog;
	}
	public static void dismissDialog(ProgressDialog dialog){
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
