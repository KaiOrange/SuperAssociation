package com.xsy.superassociation.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollingViewPager extends ViewPager{
	public NoScrollingViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollingViewPager(Context context) {
		super(context);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}
}
