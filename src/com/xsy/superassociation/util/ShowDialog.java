package com.xsy.superassociation.util;

import android.app.Activity;
import android.app.ProgressDialog;

public class ShowDialog {
	public static ProgressDialog getDialogProgress(Activity activity,String title,String content){
		ProgressDialog dialog = new ProgressDialog(activity); 
		//���ý�������񣬷��ΪԲ�Σ���ת�� 
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		//����ProgressDialog ���� 
		dialog.setTitle(title); 
		//����ProgressDialog ��ʾ��Ϣ 
		dialog.setMessage(content); 
		//����ProgressDialog ����ͼ�� 
		dialog.setIcon(android.R.drawable.ic_dialog_map); 
		//����ProgressDialog �Ľ������Ƿ���ȷ 
		dialog.setIndeterminate(false); 
		//����ProgressDialog �Ƿ���԰��˻ذ���ȡ�� 
		dialog.setCancelable(false);
		return dialog;
	}
	public static void dismissDialog(ProgressDialog dialog){
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
