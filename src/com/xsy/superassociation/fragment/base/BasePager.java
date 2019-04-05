package com.xsy.superassociation.fragment.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xsy.superassociation.action.MainActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.action.TongZhiActivity;
import com.xsy.superassociation.bean.Tongzhi;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.ChatInfosDAO;
import com.xsy.superassociation.dao.TongZhiDAO;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;

public class BasePager {
	public Activity myActivity;
	public TextView shetuan_tv;
	public ImageButton shetuan_xiaoxi;
	public FrameLayout shetuan_framelayoLayout;
	public View myView;
	public UserMain userMain;
	private HttpUtils httpUtils;
	private TongZhiDAO tongZhiDAO;
	private ChatInfosDAO chatInfosDAO;

	public BasePager(Activity myActivity) {
		super();
		this.myActivity = myActivity;
		userMain = new UserMainDAO(myActivity).findUserMain();
		initView();
	}
	
	public View initView(){
		myView = View.inflate(myActivity, R.layout.basepager_layout, null);
		shetuan_tv = (TextView) myView.findViewById(R.id.shetuan_tv);
		shetuan_xiaoxi = (ImageButton) myView.findViewById(R.id.shetuan_xiaoxi);
		shetuan_framelayoLayout = (FrameLayout) myView.findViewById(R.id.shetuan_framelayout);
		httpUtils = new HttpUtils();
		shetuan_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleSlidingMenu();
			}
		});
		tongZhiDAO = new TongZhiDAO(myActivity);
		chatInfosDAO = new ChatInfosDAO(myActivity);
		shetuan_xiaoxi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, TongZhiActivity.class);
				myActivity.startActivity(intent);
			}
		});
		return myView;
	}
	public void initData(){
		//联网查询消息
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"tongZhiAction!getTongZhisByUmid.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(myActivity, "查找消息时网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if (!"notFound".equals(result)) {
					Tongzhi[] fromJson = new Gson().fromJson(result, Tongzhi[].class);
//					int i = 0;
					for (Tongzhi tongzhi : fromJson) {
						if (tongzhi.getTzType() == 3) {
							chatInfosDAO.save(tongzhi);
							Tongzhi tz = tongZhiDAO.isHasChatInfo(tongzhi.getUmid(), tongzhi.getAdditional());
							if (tz != null) {
								if (tz.getStatus() == 0) {
//									i++;
								}
								tongZhiDAO.updateChatInfo(tongzhi);							
							} else {								
								tongZhiDAO.save(tongzhi);							
//								i++;
							}
						} else {							
							tongZhiDAO.save(tongzhi);
//							i++;
						}
					}
//					TongZhiCountUtils.addTongZhiCount(myActivity, i);
					initTongZhiIcon();
				}
			}
		});
		initTongZhiIcon();
	}
	public void onRestart(){
		initTongZhiIcon();
	}

	private void initTongZhiIcon() {
//		int count = TongZhiCountUtils.getTongZhiCount(myActivity);
		int count = tongZhiDAO.findUnReadCount(userMain.getUmid());
//		Toast.makeText(myActivity, "count = " + count, 1).show();
		if (count > 0) {
			shetuan_xiaoxi.setImageResource(R.drawable.xinfenghave);
		} else {
			shetuan_xiaoxi.setImageResource(R.drawable.xinfengempty);
		}
	}
	public void setScrollingTouch(boolean isScrolling){
		MainActivity mainActivity = (MainActivity)myActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		if (isScrolling) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			shetuan_tv.setEnabled(true);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			shetuan_tv.setEnabled(false);
		}
	}
	public void toggleSlidingMenu(){
		MainActivity mainActivity = (MainActivity) myActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		slidingMenu.toggle();
	}
}
