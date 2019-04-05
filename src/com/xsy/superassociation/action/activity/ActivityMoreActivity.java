package com.xsy.superassociation.action.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.adapter.MyActivityAdapter;
import com.xsy.superassociation.bean.Activity;
import com.xsy.superassociation.dao.ActivityDAO;

public class ActivityMoreActivity extends BaseActivity {
	@ViewInject(value=R.id.activiity_add)
	private ImageView activiity_add;
	@ViewInject(value=R.id.activity_lv)
	private ListView activity_lv;
	private List<Activity> activitys;
	private MyActivityAdapter adapter;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_more);
		ViewUtils.inject(this);
		activitys = new ActivityDAO(this).findUserActivity(userMain.getStid(), userMain.getUmid());
		adapter = new MyActivityAdapter(this, activitys);
		activity_lv.setAdapter(adapter);
	}
}
