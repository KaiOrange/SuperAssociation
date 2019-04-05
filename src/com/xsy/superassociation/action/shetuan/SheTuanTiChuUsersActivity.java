package com.xsy.superassociation.action.shetuan;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
import com.xsy.superassociation.adapter.WithCheckUserMainAdapter;
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.global.Global;

public class SheTuanTiChuUsersActivity extends BaseActivity {
	@ViewInject(value=R.id.users_lv)
	private ListView users_lv;
	private List<UserMain> users = new ArrayList<UserMain>();
	private WithCheckUserMainAdapter adapterUsers;
	private SheTuan sheTuan;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_users);
		ViewUtils.inject(this);
		sheTuan = new SheTuanDAO(this).findSheTuan(userMain.getStid());
		adapterUsers = new WithCheckUserMainAdapter(this, users);
		users_lv.setAdapter(adapterUsers);
		findUsersFromService();
		
	}
	
	private void findUsersFromService() {
		//从服务器获取数据
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sheTuan.stid", userMain.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!getAllSheTuanUsers.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanTiChuUsersActivity.this, "您现在没有开网络哦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanTiChuUsersActivity.this, "加载社团成员失败...", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
					Toast.makeText(SheTuanTiChuUsersActivity.this, "该社团还没有成员", Toast.LENGTH_SHORT).show();
				} else {
//					Toast.makeText(SheTuanAdminGuanLiActivity.this, "更新好友成功...", Toast.LENGTH_SHORT).show();
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					users.clear();
					for (UserMain um : fromJson) {
						if (um.getUmid() != sheTuan.getUmid() && um.getUmid() != userMain.getUmid()) {
							users.add(um);
						}
					}
					adapterUsers.notifyDataSetChanged();
				}
			}
		});
	}

	public void updateFromService(View v){
		final List<UserMain> checkedUsers = adapterUsers.getCheckedUsers();
		if (checkedUsers.size() < 0) {
			Toast.makeText(SheTuanTiChuUsersActivity.this, "您没有选择要删除的成员", Toast.LENGTH_SHORT).show();			
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(SheTuanTiChuUsersActivity.this);
		builder.setTitle("踢出成员");
		builder.setMessage("你确定要踢出该社团的这" + checkedUsers.size() + "位成员吗?");
		builder.setNegativeButton("放弃", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				deleteFromServece(checkedUsers);
			}
		});
		builder.create();
		builder.show();
		
	}
	private void deleteFromServece(final List<UserMain> checkedUsers) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		for (int i = 0; i < checkedUsers.size(); i++) {
			params.addBodyParameter("u2sts[" + i + "].umid", "" + checkedUsers.get(i).getUmid());
			params.addBodyParameter("u2sts[" + i + "].stid", userMain.getStid());
		}
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"u2stAction!removeUsers.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanTiChuUsersActivity.this, "您现在没有开网络哦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanTiChuUsersActivity.this, "你的没有选择要踢出的成员...", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SheTuanTiChuUsersActivity.this, "成功踢出" + checkedUsers.size() + "位好友", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}

	
}
