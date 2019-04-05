package com.xsy.superassociation.util;

public class IntUtils {
	public static String to2String(int i){
		String s = null;
		if (i < 10) {
			s = "0" + i;
		} else {
			s = "" + i;
		}
		return s;
	}
}
