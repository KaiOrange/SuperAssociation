package com.xsy.superassociation.action;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.global.Global;

public class CreateSheTuanJiaoActivity extends BaseActivity {
	@ViewInject(value=R.id.shetuanjiao_sign)
	private EditText shetuanjiao_sign;
	@ViewInject(value=R.id.shetuanjiao_name)
	private EditText shetuanjiao_name;
	@ViewInject(value=R.id.shetuanjiao_text)
	private EditText shetuanjiao_text;
	@ViewInject(value=R.id.shetuanjiao_view_sign)
	private View shetuanjiao_view_sign;
	@ViewInject(value=R.id.shetuanjiao_view_name)
	private View shetuanjiao_view_name;
	@ViewInject(value=R.id.shetuanjiao_view_text)
	private View shetuanjiao_view_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shetuanjiao_create);
		ViewUtils.inject(this);
		
		final EditText[] editTexts = new EditText[3];
		editTexts[0] = shetuanjiao_sign;
		editTexts[1] = shetuanjiao_name;
		editTexts[2] = shetuanjiao_text;
		final View[] views = new View[3];
		views[0] = shetuanjiao_view_sign;
		views[1] = shetuanjiao_view_name;
		views[2] = shetuanjiao_view_text;
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
		if (!TextUtils.isEmpty(userMain.getNichen())) {
			shetuanjiao_sign.setText(userMain.getNichen());
		}
	}
	public void complete(View v){
		String sign = shetuanjiao_sign.getText().toString();
		final String name = shetuanjiao_name.getText().toString();
		String text = shetuanjiao_text.getText().toString();
		if (TextUtils.isEmpty(text)) {
			Toast.makeText(CreateSheTuanJiaoActivity.this, "社团角语录不能为空!!", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sheTuanJiao.stjsign",sign);
		params.addBodyParameter("sheTuanJiao.name", name);
		params.addBodyParameter("sheTuanJiao.stjtext", text);
		params.addBodyParameter("umid", "" +userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanJiaoAction!createSheTuanJiao.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(CreateSheTuanJiaoActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(CreateSheTuanJiaoActivity.this, "创建失败啦╥﹏╥...", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
					Toast.makeText(CreateSheTuanJiaoActivity.this, "你和账号为[" + name + "]的童鞋不是好友", Toast.LENGTH_SHORT).show();					
				} else {
					Toast.makeText(CreateSheTuanJiaoActivity.this, "新增社团角成功( ^_^ )", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
}
