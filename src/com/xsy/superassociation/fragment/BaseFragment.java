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
		super.onCreate(savedInstanceState);//���������ʡ�԰� ....  ���˰������ 
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
//		initData();//���ﲻӦ���ٳ�ʼ�������� ��Ȼûһ�ζ���������һҳ
	}
	public abstract View initView();
	public void initData(){}
}
