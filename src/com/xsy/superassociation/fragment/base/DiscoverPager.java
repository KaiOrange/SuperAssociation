package com.xsy.superassociation.fragment.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.xsy.superassociation.action.CreateSheTuanJiaoActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.action.xiakeliao.XiaKeLiaoActivity;

public class DiscoverPager extends BasePager{

	private LinearLayout discover_shetuanjiao;
	private LinearLayout discover_xiakeliao;
	
	public DiscoverPager(Activity myActivity) {
		super(myActivity);
	}
	@Override
	public void initData() {
		super.initData();
		setScrollingTouch(true);
		View view = View.inflate(myActivity, R.layout.activity_discover, null);
		discover_shetuanjiao = (LinearLayout) view.findViewById(R.id.discover_shetuanjiao);
		discover_xiakeliao = (LinearLayout) view.findViewById(R.id.discover_xiakeliao);
		
		discover_xiakeliao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, XiaKeLiaoActivity.class);
				myActivity.startActivity(intent);
			}
		});
		discover_shetuanjiao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, CreateSheTuanJiaoActivity.class);
				myActivity.startActivity(intent);
			}
		});
		shetuan_framelayoLayout.addView(view);
	}
	
}
