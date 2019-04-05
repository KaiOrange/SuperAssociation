package com.xsy.superassociation.action;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.ActivityCollector;

public class BaseActivity extends Activity{
	protected UserMain userMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.d("BaseActivity", getClass().getSimpleName());
		ActivityCollector.addActivity(this);
		userMain = new UserMainDAO(this).findUserMain();
	}
	
	public void tuichu(View v){finish();}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
