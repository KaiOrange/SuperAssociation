package com.xsy.superassociation.action;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.UserMainDAO;


public class SplashActivity extends BaseActivity{
//	RelativeLayout frameLayout;
	private UserMain userMain;
	private TextView splash_title;
	private TextView splash_des;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		userMain = new UserMainDAO(SplashActivity.this).findUserMain();
		
		splash_title = (TextView) findViewById(R.id.splash_title);
		splash_des = (TextView) findViewById(R.id.splash_des);
//		frameLayout = (RelativeLayout) findViewById(R.id.splash_frame);
		startAnimation();
	}
	/*private void startAnimation(){
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.618f);
		alpha.setDuration(1000);
		AlphaAnimation reAlpha = new AlphaAnimation(0.618f, 1.0f);
		reAlpha.setDuration(1000);
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(alpha);
		set.addAnimation(reAlpha);
		set.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
//				boolean welcome_finished = PrefrenceUtils.getBoolean(SplashActivity.this, "welcome_finished", false);
				
				if (userMain == null) {
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
		frameLayout.startAnimation(set);
	}*/
	private void startAnimation(){
		TranslateAnimation animation1 = new TranslateAnimation(-100, 0, 0, 0);
		animation1.setDuration(2000);
		TranslateAnimation animation2 = new TranslateAnimation(120,0, 0, 0);
		animation2.setDuration(2000);
		animation1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
//				boolean welcome_finished = PrefrenceUtils.getBoolean(SplashActivity.this, "welcome_finished", false);
				if (userMain == null) {
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
		splash_title.startAnimation(animation1);
		splash_des.startAnimation(animation2);
	}

}
