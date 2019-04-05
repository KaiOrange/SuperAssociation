package com.xsy.superassociation.action.usermain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

public class UserMainBirthdayActivity extends BaseActivity {
	@ViewInject(value=R.id.exit)
	private ImageView exit;
	@ViewInject(value=R.id.activity_birthday)
	private TextView birthday;
	@ViewInject(value=R.id.queren)
	private Button queren;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_birthday);
		ViewUtils.inject(this);
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		birthday.setText(userMain.getBrithday());
		birthday.setHint("例如："+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		birthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dp = new DatePickerDialog(
						UserMainBirthdayActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								String sdate = arg1 + "-" + (arg2 + 1) + "-"
										+ arg3;
								birthday.setText(sdate);
							}
						}, 1990, 0, 1);
				dp.show();
			}
		});
		queren.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					new SimpleDateFormat("yyyy-MM-dd").parse(birthday.getText().toString());
				} catch (ParseException e) {
					e.printStackTrace();
					Toast.makeText(UserMainBirthdayActivity.this, "更新失败:日期格式不正确..", Toast.LENGTH_SHORT).show();
					return;
				}
				
				UserMain userMain = new UserMainDAO(UserMainBirthdayActivity.this).findUserMain();
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("userMain.umid", "" + userMain.getUmid());
				params.addBodyParameter("userMain.name", userMain.getName());
				params.addBodyParameter("userMain.nichen", userMain.getNichen());
				params.addBodyParameter("userMain.sex", userMain.isSex() + "");
				params.addBodyParameter("userMain.age", userMain.getAge() + "");
				params.addBodyParameter("userMain.brithday", birthday.getText().toString());
				params.addBodyParameter("userMain.telphone", userMain.getTelphone());
				httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!updateUserMainButTXAndMM.action", params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(UserMainBirthdayActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						if (!"failure".equals(result)) {
							Toast.makeText(UserMainBirthdayActivity.this, "恭喜你,生日已修改完成\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
							UserMain jsonUserMain = new Gson().fromJson(result, UserMain.class);
							new UserMainDAO(UserMainBirthdayActivity.this).update(jsonUserMain);
							finish();
						} else {
							Toast.makeText(UserMainBirthdayActivity.this, "更改生日失败。。", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
}
