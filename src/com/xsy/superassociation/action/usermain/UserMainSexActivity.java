package com.xsy.superassociation.action.usermain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class UserMainSexActivity extends BaseActivity {
	@ViewInject(value=R.id.exit)
	private ImageView exit;
	@ViewInject(value=R.id.activity_sex)
	private RadioGroup sex;
	@ViewInject(value=R.id.queren)
	private Button queren;
	@ViewInject(value=R.id.radio0)
	private RadioButton radioMan;
	@ViewInject(value=R.id.radio1)
	private RadioButton radioWoman;
	private boolean sexBoolean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_sex);
		ViewUtils.inject(this);
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		if (userMain.isSex()) {
			radioMan.setChecked(true);
			sexBoolean = true;
		} else {
			radioWoman.setChecked(true);
			sexBoolean = false;
		}
		sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.radio1) {
					sexBoolean = false;
				} else {
					sexBoolean = true;
				}
			}
		});
		queren.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("userMain.umid", "" + userMain.getUmid());
				params.addBodyParameter("userMain.name", userMain.getName());
				params.addBodyParameter("userMain.nichen", userMain.getNichen());
				params.addBodyParameter("userMain.sex", sexBoolean + "");
				params.addBodyParameter("userMain.age", userMain.getAge() + "");
				params.addBodyParameter("userMain.brithday", userMain.getBrithday());
				params.addBodyParameter("userMain.telphone", userMain.getTelphone());
				httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!updateUserMainButTXAndMM.action", params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(UserMainSexActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						if (!"failure".equals(result)) {
							Toast.makeText(UserMainSexActivity.this, "恭喜你,您的性别已更换\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
							UserMain jsonUserMain = new Gson().fromJson(result, UserMain.class);
							new UserMainDAO(UserMainSexActivity.this).update(jsonUserMain);
							finish();
						} else {
							Toast.makeText(UserMainSexActivity.this, "性别修改失败。。", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
}
