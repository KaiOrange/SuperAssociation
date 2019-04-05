package com.xsy.superassociation.action;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.MD5Utils;

public class Register1Activity extends BaseActivity{
	@ViewInject(value=R.id.userName)
	private EditText userName;
	@ViewInject(value=R.id.password)
	private EditText password1;
	@ViewInject(value=R.id.password2)
	private EditText password2;
	
	@ViewInject(value=R.id.register_view_name)
	private View view_name;
	@ViewInject(value=R.id.register_view_password)
	private View view_password;
	@ViewInject(value=R.id.register_view_repassword)
	private View view_repassword;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register1);
		ViewUtils.inject(this);
		
		final EditText[] editTexts = new EditText[3];
		editTexts[0] = userName;
		editTexts[1] = password1;
		editTexts[2] = password2;
		final View[] views = new View[3];
		views[0] = view_name;
		views[1] = view_password;
		views[2] = view_repassword;
		for (int i = 0; i < editTexts.length; i++) {
			final int k = i;
			editTexts[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					for (int j = 0; j < views.length; j++) {							
						views[j].setBackgroundColor(Color.GRAY);
					}
					views[k].setBackgroundColor(Color.RED);
				}
			});
		}
		view_name.setBackgroundColor(Color.RED);
	}
	public void next(View v){
		final String userName_Text = userName.getText().toString();
		final String password_Text = password1.getText().toString();
		String password2_Text = password2.getText().toString();
		if (TextUtils.isEmpty(userName_Text)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(password_Text)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(password2_Text)) {
			Toast.makeText(this, "请重复填写你的密码！", Toast.LENGTH_SHORT).show();
		} else if (!password_Text.equals(password2_Text)) {
			Toast.makeText(this, "两次填写的密码不一致！", Toast.LENGTH_SHORT).show();
		} else if (userName_Text.length() > 15 || userName_Text.length() < 6 ){
			Toast.makeText(this, "用户名的长度必须为6~15位！", Toast.LENGTH_SHORT).show();
		} else if (password_Text.length() > 15 || password_Text.length() < 6){
			Toast.makeText(this, "密码的长度必须为6~15位！", Toast.LENGTH_SHORT).show();
		} else {
			HttpUtils httpUtils = new HttpUtils();
			RequestParams params = new RequestParams();
			params.addBodyParameter("userName", userName_Text);
			//MD5加密
			params.addBodyParameter("password", MD5Utils.md5Password(password_Text));
			httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!register.action", params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(Register1Activity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(ResponseInfo<String> info) {
					String result = info.result;
					if (!"null".equals(result)) {
						Toast.makeText(Register1Activity.this, "恭喜你,注册成功(*^▽^*)。。", Toast.LENGTH_LONG).show();
						UserMain userMain = new Gson().fromJson(result,
								UserMain.class);
						Intent intent = new Intent(Register1Activity.this, Register2Activity.class);
						intent.putExtra("userMain", userMain);
						startActivity(intent);
					} else {
						Toast.makeText(Register1Activity.this, "此用户已经被注册,请更换用户名。。", Toast.LENGTH_LONG).show();
					}
				}
			});
			
		}
	}
}
