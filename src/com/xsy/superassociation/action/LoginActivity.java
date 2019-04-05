package com.xsy.superassociation.action;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.MD5Utils;
import com.xsy.superassociation.util.ShowDialog;

public class LoginActivity extends BaseActivity{
	private EditText userName;
	private EditText password;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		userName = (EditText) findViewById(R.id.userName);
		password = (EditText) findViewById(R.id.password);
		dialog = ShowDialog.getDialogProgress(this, "��¼", "���ڵ�¼��...");
	}
	public void login(View v){
		String userName_Text = userName.getText().toString();
		String password_Text = password.getText().toString();
		if (TextUtils.isEmpty(userName_Text)) {
			Toast.makeText(this, "�û�������Ϊ�գ�", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(password_Text)) {
			Toast.makeText(this, "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
		} else if (userName_Text.length() < 6 || password_Text.length() < 6){
			Toast.makeText(this, "�û���������ĳ���Ӧ�ö���6λ��", Toast.LENGTH_SHORT).show();
		} else {
			dialog.show();
			//Toast.makeText(this, "ע��ɹ���", Toast.LENGTH_SHORT).show();
			HttpUtils httpUtils = new HttpUtils();
			RequestParams params = new RequestParams();
			params.addBodyParameter("userName", userName_Text);
			//MD5����
			params.addBodyParameter("password", MD5Utils.md5Password(password_Text));
			httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!login.action", params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(LoginActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
					ShowDialog.dismissDialog(dialog);
				}

				@Override
				public void onSuccess(ResponseInfo<String> info) {
					String result = info.result;
					if (!"null".equals(result)) {
						UserMain userMain = new Gson().fromJson(result,
								UserMain.class);
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						startActivity(intent);
						new UserMainDAO(LoginActivity.this).save(userMain);
						finish();
					} else {
						Toast.makeText(LoginActivity.this, "������û������󡣡�", Toast.LENGTH_SHORT).show();
					}
					ShowDialog.dismissDialog(dialog);
				}
			});
		}
	}
	public void zhuce(View v){
		Intent intent = new Intent(this, Register1Activity.class);
		startActivity(intent);
	}
	
}
