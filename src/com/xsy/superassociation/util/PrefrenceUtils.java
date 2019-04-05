package com.xsy.superassociation.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceUtils {
	public static final String fileName = "config";
	public static boolean getBoolean (Context context,String name,boolean initValue){
		SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return preferences.getBoolean(name, initValue);
	}
	public static void setBoolean(Context context,String name, boolean setValue){
		SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		preferences.edit().putBoolean(name, setValue).commit();
	}
	public static String getString (Context context,String name,String initValue){
		SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return preferences.getString(name, initValue);
	}
	public static void setString(Context context,String name, String setValue){
		SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		preferences.edit().putString(name, setValue).commit();
	}
	public static int getInt (Context context,String name,int initValue){
		SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return preferences.getInt(name, initValue);
	}
	public static void setInt(Context context,String name, int setValue){
		SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		preferences.edit().putInt(name, setValue).commit();
	}
}
