package com.xsy.superassociation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	protected FragmentActivity myActivity;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//坚决不可以省略啊 ....  找了半天错误 
		myActivity = getActivity();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return initView();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	@Override
	public void onResume() {
		super.onResume();
//		initData();//这里不应该再初始化数据了 不然没一次都会跳到第一页
	}
	public abstract View initView();
	public void initData(){}
}
