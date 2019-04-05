package com.xsy.superassociation.action;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CreateSheTuanFailureActivity extends BaseActivity{
	@ViewInject(value=R.id.shetuan_tv)
	private TextView shetuan_tv;
	@ViewInject(value=R.id.shetuan_tv_title)
	private TextView shetuan_tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_shetuan_failure);
		ViewUtils.inject(this);
		boolean b = getIntent().getBooleanExtra("isCreate", false);
		if (!b) {
			shetuan_tv.setText("加入社团");
			shetuan_tv_title.setText("加入失败");
		}
	}
	public void tuichu(View v){
		finish();
	}
}
