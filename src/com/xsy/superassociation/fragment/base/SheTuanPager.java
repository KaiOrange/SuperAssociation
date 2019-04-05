package com.xsy.superassociation.fragment.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xsy.superassociation.action.JoinSheTuanActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.action.SheTuanGuanLiActivity;
import com.xsy.superassociation.action.ShowSheTuanInfoActivity;
import com.xsy.superassociation.action.activity.ActivityMoreActivity;
import com.xsy.superassociation.action.shetuan.SheTuanUsersActivity;
import com.xsy.superassociation.adapter.MyActivityAdapter;
import com.xsy.superassociation.bean.Activity;
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.bean.ShetuanAdmin;
import com.xsy.superassociation.dao.ActivityDAO;
import com.xsy.superassociation.dao.SheTuanAdminDAO;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.Utility;
import com.xsy.superassociation.view.FocusedTextView;

public class SheTuanPager extends BasePager{
	private FocusedTextView shetuan_snotice;
	private ImageView shetuan_touxiang;
	private TextView shetuan_sname;
	private TextView shetuan_stid;
	private TextView shetuan_moreActivity;
	private LinearLayout shetuan_info;
	private ListView shetuan_lv_activity;
	private BitmapUtils bitmapUtils;
	private SheTuan sheTuan;
	private SheTuanDAO sheTuanDAO;
	private TextView shetuan_guanli;
	private HttpUtils httpUtils;
	private View view;
	private List<Activity> activitys = new ArrayList<Activity>() ;
	private MyActivityAdapter adapter;
	private ActivityDAO activityDAO;
	private LinearLayout shetuan_lookUser;

	public SheTuanPager(android.app.Activity myActivity) {
		super(myActivity);
	}
	@Override
	public void initData() {
		super.initData();
		setScrollingTouch(true);
		sheTuanDAO = new SheTuanDAO(myActivity);
		httpUtils = new HttpUtils();
		userMain = new UserMainDAO(myActivity).findUserMain();
		if (TextUtils.isEmpty(userMain.getStid())) {
			if (view != null) {
				view.setVisibility(View.GONE);
			}
			View myView = View.inflate(myActivity, R.layout.activity_shetuan_noshetuan, null);
			ImageView jiaru_iv = (ImageView) myView.findViewById(R.id.jiaru_iv);
			int [] resIds = {R.drawable.jiarushetuan0,R.drawable.jiarushetuan1,R.drawable.jiarushetuan2,R.drawable.jiarushetuan3,R.drawable.jiarushetuan4,R.drawable.jiarushetuan5};
			int next = new Random().nextInt(6);
			jiaru_iv.setImageResource(resIds[next]);
			Button jiaru_button = (Button) myView.findViewById(R.id.jiaru_button);
			jiaru_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(myActivity, JoinSheTuanActivity.class);
					myActivity.startActivity(intent);
				}
			});
			shetuan_framelayoLayout.addView(myView);
			monitorSheTuanDate();
		} else {
			view = View.inflate(myActivity, R.layout.activity_shetuan, null);
			shetuan_snotice = (FocusedTextView) view.findViewById(R.id.shetuan_snotice);
			shetuan_touxiang = (ImageView) view.findViewById(R.id.shetuan_touxiang);
			shetuan_sname = (TextView) view.findViewById(R.id.shetuan_sname);
			shetuan_stid = (TextView) view.findViewById(R.id.shetuan_stid);
			shetuan_moreActivity = (TextView) view.findViewById(R.id.shetuan_moreActivity);
			shetuan_info = (LinearLayout) view.findViewById(R.id.shetuan_info);
			shetuan_lookUser = (LinearLayout) view.findViewById(R.id.shetuan_lookUser);
			shetuan_lv_activity = (ListView) view.findViewById(R.id.shetuan_lv_activity);
			shetuan_guanli = (TextView) view.findViewById(R.id.shetuan_guanli);
			shetuan_framelayoLayout.addView(view);
			bitmapUtils = new BitmapUtils(myActivity);  
			bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//默认背景图片  
	    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//加载失败图片  
	    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型  
	    	activityDAO = new ActivityDAO(myActivity);
	    	shetuan_lv_activity.setFocusable(false);
	    	activitys = activityDAO.findUserNowActivity(userMain.getStid(), userMain.getUmid());
	    	adapter = new MyActivityAdapter(myActivity, activitys);
	    	shetuan_lv_activity.setAdapter(adapter);
	    	Utility.setListViewHeightBasedOnChildren(shetuan_lv_activity);
	    	updateActivityDate();
			fillSheTuanDate();
			initGuanLiText();
			shetuan_info.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(myActivity, ShowSheTuanInfoActivity.class);
					myActivity.startActivity(intent);
				}
			});
			shetuan_guanli.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(myActivity, SheTuanGuanLiActivity.class);
					myActivity.startActivity(intent);
				}
			});
			shetuan_moreActivity.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(myActivity, ActivityMoreActivity.class);
					myActivity.startActivity(intent);
				}
			});
			shetuan_lookUser.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(myActivity, SheTuanUsersActivity.class);
					myActivity.startActivity(intent);					
				}
			});
		}
	}
	
	private void updateActivityDate() {
		if (TextUtils.isEmpty(userMain.getStid())) {
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("stid", "" + userMain.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"activityAction!getSheTuanActivity.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
//					Toast.makeText(myActivity, "活动获取失败...", Toast.LENGTH_SHORT).show();					
				}  else if("notFound".equals(result)) {
//					Toast.makeText(myActivity, "没有还没有活动...", Toast.LENGTH_SHORT).show();					
				} else {
					Activity[] activityArray = new Gson().fromJson(result, Activity[].class);
					if (activityArray.length > 0) {
						List<Activity> list = new ArrayList<Activity>();
						for (Activity a : activityArray) {
							list.add(a);
						}
						activityDAO.deleteAllActivity(userMain.getStid(), userMain.getUmid());
						activityDAO.saveAll(list, userMain.getUmid());
						List<Activity> findAll = activityDAO.findUserNowActivity(userMain.getStid(), userMain.getUmid());
						activitys.clear();
						activitys.addAll(findAll);
						adapter.notifyDataSetChanged();
						Utility.setListViewHeightBasedOnChildren(shetuan_lv_activity);
					}
				}
			}
		});
	}
	private void initGuanLiText() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("stid", "" + userMain.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAdminAction!getSheTuanAdmin.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(myActivity, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if (!"failure".equals(result)) {
					ShetuanAdmin[] shetuanAdmin = new Gson().fromJson(result, ShetuanAdmin[].class);
					if (shetuanAdmin.length > 0) {
						SheTuanAdminDAO sheTuanAdminDAO = new SheTuanAdminDAO(myActivity);
						sheTuanAdminDAO.deleteAll();
						for (int i = 0; i < shetuanAdmin.length; i++) {
							sheTuanAdminDAO.save(shetuanAdmin[i]);		
							if (shetuanAdmin[i].getUmid() == userMain.getUmid()) {
								shetuan_guanli.setVisibility(View.VISIBLE);
							}
						}
					}
				}  else  {
//					Toast.makeText(myActivity, "管理员获取失败...", Toast.LENGTH_SHORT).show();					
				}
			}
		});
	}
	private void fillSheTuanDate() {
		loadSheTuanInfo();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!findSheTuanByUid.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(myActivity, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if (!"failure".equals(result)) {
//					Toast.makeText(myActivity, "恭喜你,社团信息更新成功\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
					sheTuan = new Gson().fromJson(result, SheTuan.class);
					if (sheTuanDAO.findSheTuan(userMain.getStid()) != null && userMain.getStid().equals("" + sheTuan.getStid())) {
						sheTuanDAO.update(sheTuan);
					} else {
						sheTuanDAO.deleteSheTuan(userMain.getStid());
						sheTuanDAO.save(sheTuan);						
						userMain.setStid(sheTuan.getStid() + "");
						new UserMainDAO(myActivity).update(userMain);
					}
					updateSheTuanInfo();
				} else {
//					Toast.makeText(myActivity, "社团信息更新失败。。", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		sheTuan = new SheTuanDAO(myActivity).findSheTuan(userMain.getStid());
		
	}
	public void loadSheTuanInfo() {
		if (!TextUtils.isEmpty(userMain.getStid())) {
			sheTuan = new SheTuanDAO(myActivity)
					.findSheTuan(userMain.getStid());
			List<Activity> newAll = activityDAO.findUserNowActivity(userMain.getStid(), userMain.getUmid());
			activitys.clear();
			activitys.addAll(newAll);
			adapter.notifyDataSetChanged();
			Utility.setListViewHeightBasedOnChildren(shetuan_lv_activity);
		}
		if (view != null) {
			updateSheTuanInfo();
		}
	}
	private void monitorSheTuanDate() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!findSheTuanByUid.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(myActivity, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if (!"failure".equals(result)) {
//					Toast.makeText(myActivity, "恭喜你,加入社团成功\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
					sheTuan = new Gson().fromJson(result, SheTuan.class);
					sheTuanDAO.save(sheTuan);
					UserMainDAO userMainDAO = new UserMainDAO(myActivity);
					userMain = userMainDAO.findUserMain();
					userMain.setStid(sheTuan.getStid() + "");
					userMainDAO.update(userMain);
					initData();
				} else {
//					Toast.makeText(myActivity, "社团信息更新失败。。", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private void updateSheTuanInfo(){
		if (sheTuan != null) {
			String snoticeStr = "暂无公告";
			if (!TextUtils.isEmpty(sheTuan.getSnotice())) {
				snoticeStr = sheTuan.getSnotice();
				shetuan_snotice.setText(snoticeStr);
			}
			if (!TextUtils.isEmpty(sheTuan.getSname())) {
				shetuan_sname.setText(sheTuan.getSname());
			}
			shetuan_stid.setText("社团号：" + sheTuan.getStid());
			if (!TextUtils.isEmpty(sheTuan.getTouxiang())) {
				bitmapUtils.display(shetuan_touxiang, Global.BASE_SHETUANTX_URL + sheTuan.getTouxiang());
			}
		}
	}
	@Override
	public void onRestart() {
		super.onRestart();
		loadSheTuanInfo();
	}
	public void setCurrentMenuPager(int position){}
}
