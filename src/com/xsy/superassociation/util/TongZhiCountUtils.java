package com.xsy.superassociation.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class TongZhiCountUtils {
	public static int getTongZhiCount(Activity activity){
		SharedPreferences sharedPreferences = activity.getSharedPreferences("sa", Context.MODE_PRIVATE);
		return sharedPreferences.getInt("tongZhiCount", 0);
	}
	public static void addTongZhiCount(Activity activity,int number){
		SharedPreferences sharedPreferences = activity.getSharedPreferences("sa", Context.MODE_PRIVATE);
		int count = sharedPreferences.getInt("tongZhiCount", 0);
		sharedPreferences.edit().putInt("tongZhiCount", count + number).commit();
	}
}
