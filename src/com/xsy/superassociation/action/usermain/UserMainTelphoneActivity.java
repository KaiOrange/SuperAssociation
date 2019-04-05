package com.xsy.superassociation.action.usermain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UserMainTelphoneActivity extends BaseActivity {
	@ViewInject(value=R.id.exit)
	private ImageView exit;
	@ViewInject(value=R.id.activity_telphone)
	private EditText telphone;
	@ViewInject(value=R.id.queren)
	private Button queren;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_telphone);
		ViewUtils.inject(this);
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		telphone.setText(userMain.getTelphone());
		queren.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				UserMain userMain = new UserMainDAO(UserMainTelphoneActivity.this).findUserMain();
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("userMain.umid", "" + userMain.getUmid());
				params.addBodyParameter("userMain.name", userMain.getName());
				params.addBodyParameter("userMain.nichen", userMain.getNichen());
				params.addBodyParameter("userMain.sex", userMain.isSex() + "");
				params.addBodyParameter("userMain.age", userMain.getAge() + "");
				params.addBodyParameter("userMain.brithday", userMain.getBrithday());
				params.addBodyParameter("userMain.telphone", telphone.getText().toString());
				httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!updateUserMainButTXAndMM.action", params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(UserMainTelphoneActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						if (!"failure".equals(result)) {
							Toast.makeText(UserMainTelphoneActivity.this, "恭喜你,电话号码已修改完成\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
							UserMain jsonUserMain = new Gson().fromJson(result, UserMain.class);
							new UserMainDAO(UserMainTelphoneActivity.this).update(jsonUserMain);
							finish();
						} else {
							Toast.makeText(UserMainTelphoneActivity.this, "更改电话号码失败。。", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
}
