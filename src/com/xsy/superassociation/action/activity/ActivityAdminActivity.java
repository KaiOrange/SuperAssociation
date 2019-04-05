package com.xsy.superassociation.action.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.adapter.MyActivityAdapter;
import com.xsy.superassociation.bean.Activity;
import com.xsy.superassociation.dao.ActivityDAO;
import com.xsy.superassociation.global.Global;

public class ActivityAdminActivity extends BaseActivity {
	@ViewInject(value=R.id.activiity_add)
	private ImageView activiity_add;
	@ViewInject(value=R.id.activity_lv)
	private ListView activity_lv;
	private List<Activity> activitys;
	private MyActivityAdapter adapter;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_admin);
		ViewUtils.inject(this);
		activitys = new ActivityDAO(this).findUserActivity(userMain.getStid(), userMain.getUmid());
		adapter = new MyActivityAdapter(this, activitys);
		activity_lv.setAdapter(adapter);
		activity_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Activity activity = activitys.get(arg2);
				Intent intent = new Intent(ActivityAdminActivity.this, ActivityUpdateActivity.class);
				intent.putExtra("activity", activity);
				startActivity(intent);
			}
		});
		activity_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final Activity a = activitys.get(arg2);
				AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdminActivity.this);
				builder.setTitle("删除活动");
				builder.setMessage("删除该活动将会把所有成员发送的说说全部删除,请谨慎操作!!\n你确定要删除活动[" + a.getAtitle() + "]吗?");
				builder.setNegativeButton("放弃", null);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						HttpUtils httpUtils = new HttpUtils();
						System.out.println(a);
						RequestParams params = new RequestParams();
						params.addBodyParameter("activityId", "" + a.getAid());
						httpUtils.send(HttpMethod.POST, Global.BASE_URL+"activityAction!deleteThisActivity.action", params, new RequestCallBack<String>() {
							@Override
							public void onFailure(HttpException arg0, String arg1) {
								Toast.makeText(ActivityAdminActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(ResponseInfo<String> info) {
								String result = info.result;
								if ("seccess".equals(result)) {
									Toast.makeText(ActivityAdminActivity.this, "删除活动成功", Toast.LENGTH_SHORT).show();
									new ActivityDAO(ActivityAdminActivity.this).delete(a.getAid());
									activitys.remove(a);
									adapter.notifyDataSetChanged();
								} else if("failure".equals(result)){
									Toast.makeText(ActivityAdminActivity.this, "删除活动失败", Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				});
				builder.create();
				builder.show();
				return true;
			}
		});
	}
	public void addActivity(View v){
		Intent intent = new Intent(this, ActivityAddActivity.class);
		startActivity(intent);
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		updateActivityDate();
	}
	private void updateActivityDate() {
		if (TextUtils.isEmpty(userMain.getStid())) {
			return;
		}
		HttpUtils httpUtils = new HttpUtils(); 
		RequestParams params = new RequestParams();
		params.addBodyParameter("stid", "" + userMain.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"activityAction!getSheTuanActivity.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(ActivityAdminActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(ActivityAdminActivity.this, "活动获取失败...", Toast.LENGTH_SHORT).show();					
				}  else if("notFound".equals(result)) {
					Toast.makeText(ActivityAdminActivity.this, "没有还没有活动...", Toast.LENGTH_SHORT).show();					
				} else {
					Activity[] activityArray = new Gson().fromJson(result, Activity[].class);
					if (activityArray.length > 0) {
						List<Activity> list = new ArrayList<Activity>();
						for (Activity a : activityArray) {
							list.add(a);
						}
						ActivityDAO activityDAO = new ActivityDAO(ActivityAdminActivity.this); 
						activityDAO.deleteAllActivity(userMain.getStid(), userMain.getUmid());
						activityDAO.saveAll(list, userMain.getUmid());
						List<Activity> newAll = activityDAO.findUserActivity(userMain.getStid(), userMain.getUmid());
						activitys.clear();
						activitys.addAll(newAll);
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
}
