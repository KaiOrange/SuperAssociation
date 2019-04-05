package com.xsy.superassociation.action;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xsy.superassociation.fragment.ContentFragment;
import com.xsy.superassociation.fragment.LeftFragment;
import com.xsy.superassociation.global.ActivityCollector;

public class MainActivity extends SlidingFragmentActivity {

	private static final String LEFT_FRAGMENT = "left_fragment";
	private static final String CONTENT_FRAGMENT = "content_fragment";
	private FragmentManager pm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_sliding_layout);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(200);
		initFragment();
		ActivityCollector.addActivity(this);
	}

	private void initFragment() {
		pm = getSupportFragmentManager();
		FragmentTransaction transaction = pm.beginTransaction();
		transaction.replace(R.id.left_layout, new LeftFragment(),LEFT_FRAGMENT);
		transaction.replace(R.id.content_layout, new ContentFragment(),CONTENT_FRAGMENT);
		transaction.commit();
	}
	public LeftFragment getLeftMenu(){
		return (LeftFragment) pm.findFragmentByTag(LEFT_FRAGMENT);
	}
	public ContentFragment getContentMenu(){
		return (ContentFragment) pm.findFragmentByTag(CONTENT_FRAGMENT);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getContentMenu().getToolsPager().onRestart();
		getContentMenu().getFriendsPager().onRestart();
		getContentMenu().getDiscoverPager().onRestart();
		getContentMenu().getSheTuanPager().onRestart();
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		getContentMenu().getSheTuanPager().initData();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
