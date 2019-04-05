package com.xsy.superassociation.action.xiakeliao;

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
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.global.Global;

public class CreateXiaKeLiaoActivity extends BaseActivity {
	@ViewInject(value=R.id.xiakeliao_xkltitle)
	private EditText xiakeliao_xkltitle;
	@ViewInject(value=R.id.xiakeliao_xkltext)
	private EditText xiakeliao_xkltext;
	@ViewInject(value=R.id.xiakeliao_view_xkltitle)
	private View xiakeliao_view_xkltitle;
	@ViewInject(value=R.id.xiakeliao_view_xkltext)
	private View xiakeliao_view_xkltext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_xiakeliao);
		ViewUtils.inject(this);
		
		final EditText[] editTexts = new EditText[2];
		editTexts[0] = xiakeliao_xkltitle;
		editTexts[1] = xiakeliao_xkltext;
		final View[] views = new View[2];
		views[0] = xiakeliao_view_xkltitle;
		views[1] = xiakeliao_view_xkltext;
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
	}
	public void complete(View v){
		String title = xiakeliao_xkltitle.getText().toString();
		final String text = xiakeliao_xkltext.getText().toString();
		if (TextUtils.isEmpty(text)) {
			Toast.makeText(CreateXiaKeLiaoActivity.this, "下课聊内容不能为空!!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(title)) {
			Toast.makeText(CreateXiaKeLiaoActivity.this, "下课聊标题不能为空!!", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("xiakeliao.xkltitle",title);
		params.addBodyParameter("xiakeliao.xkltext", text);
		params.addBodyParameter("xiakeliao.umid", "" +userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"xiaKeLiaoAction!createXiaKeLiao.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(CreateXiaKeLiaoActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(CreateXiaKeLiaoActivity.this, "发表失败啦╥﹏╥...", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(CreateXiaKeLiaoActivity.this, "发表成功( ^_^ )", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
}
