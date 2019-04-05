package com.xsy.superassociation.action.xiakeliao;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

public class CreateXiaKeLiaoReplyActivity extends BaseActivity {
	@ViewInject(value=R.id.xkl_create_title)
	private TextView xkl_create_title;
	@ViewInject(value=R.id.xkl_create_text)
	private EditText xkl_create_text;
	private int xklid;
	private int umidyou;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_xiakeliaoreply);
		ViewUtils.inject(this);
		Intent intent = getIntent();
		xklid = intent.getIntExtra("xklid", 0);
		umidyou = intent.getIntExtra("umidyou", 0);
		if (umidyou != 0) {
			String nichen = intent.getStringExtra("nichen");
			xkl_create_title.setText("回复 " + nichen);
		}
	}
	public void complete(View v){
		final String text = xkl_create_text.getText().toString();
		if (TextUtils.isEmpty(text)) {
			Toast.makeText(CreateXiaKeLiaoReplyActivity.this, "回复内容不能为空!!", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("reply.xklid","" + xklid);
		params.addBodyParameter("reply.umidme", "" + userMain.getUmid());
		params.addBodyParameter("reply.umidyou", "" + umidyou);
		params.addBodyParameter("reply.xklrtext", text);
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"xiaKeLiaoAction!createXiaKeLiaoReply.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(CreateXiaKeLiaoReplyActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(CreateXiaKeLiaoReplyActivity.this, "回复失败啦╥﹏╥...", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(CreateXiaKeLiaoReplyActivity.this, "回复成功( ^_^ )", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
}
