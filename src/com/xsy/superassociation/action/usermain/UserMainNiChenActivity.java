package com.xsy.superassociation.action.usermain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;

public class UserMainNiChenActivity extends BaseActivity {
	@ViewInject(value=R.id.exit)
	private ImageView exit;
	@ViewInject(value=R.id.activity_nicheng)
	private TextView nicheng;
	@ViewInject(value=R.id.queren)
	private Button queren;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_nicheng);
		ViewUtils.inject(this);
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		nicheng.setText(userMain.getNichen());
		queren.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				UserMain userMain = new UserMainDAO(UserMainNiChenActivity.this).findUserMain();
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("userMain.umid", "" + userMain.getUmid());
				params.addBodyParameter("userMain.name", userMain.getName());
				params.addBodyParameter("userMain.nichen", nicheng.getText().toString());
				params.addBodyParameter("userMain.sex", userMain.isSex() + "");
				params.addBodyParameter("userMain.age", userMain.getAge() + "");
				params.addBodyParameter("userMain.brithday", userMain.getBrithday());
				params.addBodyParameter("userMain.telphone", userMain.getTelphone());
				httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!updateUserMainButTXAndMM.action", params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(UserMainNiChenActivity.this, "ÍøÂç³ö´íÀ²¨i©n¨i...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						if (!"failure".equals(result)) {
							Toast.makeText(UserMainNiChenActivity.this, "¹§Ï²Äã,êÇ³ÆÒÑÐÞ¸ÄÍê³É\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
							UserMain jsonUserMain = new Gson().fromJson(result, UserMain.class);
							new UserMainDAO(UserMainNiChenActivity.this).update(jsonUserMain);
							finish();
						} else {
							Toast.makeText(UserMainNiChenActivity.this, "¸ü¸ÄêÇ³ÆÊ§°Ü¡£¡£", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
}
