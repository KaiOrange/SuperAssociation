package com.xsy.superassociation.action.tongzhi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.xsy.superassociation.global.Global;

public class SheTuanTongZhiActivity extends BaseActivity {
	@ViewInject(value=R.id.exit)
	private ImageView exit;
	@ViewInject(value=R.id.myedittext)
	private TextView myedittext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shetuan_tongzhi);
		ViewUtils.inject(this);
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}
	
	public void sendTongzhi2All(View v){
		if (TextUtils.isEmpty(myedittext.getText().toString())) {
			Toast.makeText(SheTuanTongZhiActivity.this, "通知内容不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("stid", "" + userMain.getStid());
		params.addBodyParameter("tztext", myedittext.getText().toString());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"tongZhiAction!sendMessage2AllUser.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanTongZhiActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if (!"failure".equals(result)) {
					Toast.makeText(SheTuanTongZhiActivity.this, "恭喜你,通知已经群发成功\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(SheTuanTongZhiActivity.this, "群发通知失败。。", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
