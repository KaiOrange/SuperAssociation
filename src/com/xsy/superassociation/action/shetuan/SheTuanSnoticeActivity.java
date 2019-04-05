package com.xsy.superassociation.action.shetuan;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.global.Global;

public class SheTuanSnoticeActivity extends BaseActivity {
	@ViewInject(value=R.id.exit)
	private ImageView exit;
	@ViewInject(value=R.id.myedittext)
	private TextView myedittext;
	@ViewInject(value=R.id.queren)
	private Button queren;
	private SheTuan findSheTuan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_snotice);
		ViewUtils.inject(this);
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		findSheTuan = new SheTuanDAO(this).findSheTuan(userMain.getStid());
		if (findSheTuan != null) {			
			String s = "";
			if (TextUtils.isEmpty(findSheTuan.getSnotice())) {
				s = findSheTuan.getSnotice();
			}
			myedittext.setText(s);
		}
		queren.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("sheTuan.stid", "" + findSheTuan.getStid());
				params.addBodyParameter("sheTuan.snotice", myedittext.getText().toString());
				httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!updateSnotice.action", params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(SheTuanSnoticeActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						if (!"failure".equals(result)) {
							Toast.makeText(SheTuanSnoticeActivity.this, "恭喜你,社团公告已修改完成\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
							SheTuan jsonSheTuan = new Gson().fromJson(result, SheTuan.class);
							new SheTuanDAO(SheTuanSnoticeActivity.this).update(jsonSheTuan);
							finish();
						} else {
							Toast.makeText(SheTuanSnoticeActivity.this, "更改社团公告失败。。", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
}
