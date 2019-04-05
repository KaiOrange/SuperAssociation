package com.xsy.superassociation.action.shetuan;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
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
import com.xsy.superassociation.bean.ShetuanAdmin;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.SheTuanAdminDAO;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.ShowDialog;

public class SheTuanAdminGuanLiActivity extends BaseActivity {
	@ViewInject(value=R.id.admin_lv)
	private ListView admin_lv;
	@ViewInject(value=R.id.users_lv)
	private ListView users_lv;
	private List<UserMain> admins = new ArrayList<UserMain>();;
	private List<UserMain> users = new ArrayList<UserMain>();
	private WithCheckUserMainAdapter adapterAdmins;
	private WithCheckUserMainAdapter adapterUsers;
	private List<ShetuanAdmin> sheTuanAdmins;
	private ProgressDialog dialogProgress;;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_guanli);
		ViewUtils.inject(this);
		dialogProgress = ShowDialog.getDialogProgress(this, "更新管理员", "正在更新管理员...");
		sheTuanAdmins = new SheTuanAdminDAO(this).findByStid(userMain.getStid());
		adapterAdmins = new WithCheckUserMainAdapter(this, admins);
		adapterUsers = new WithCheckUserMainAdapter(this, users);
		admin_lv.setAdapter(adapterAdmins);
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
				Toast.makeText(SheTuanAdminGuanLiActivity.this, "您现在没有开网络哦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanAdminGuanLiActivity.this, "加载社团成员失败...", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
					Toast.makeText(SheTuanAdminGuanLiActivity.this, "该社团还没有成员", Toast.LENGTH_SHORT).show();
				} else {
//					Toast.makeText(SheTuanAdminGuanLiActivity.this, "更新好友成功...", Toast.LENGTH_SHORT).show();
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					admins.clear();
					users.clear();
					for (UserMain um : fromJson) {
						if (um.getUmid() != userMain.getUmid()) {
							boolean isAdmin = false;
							for (ShetuanAdmin sta : sheTuanAdmins) {
								if (sta.getUmid() == um.getUmid()) {
									isAdmin = true;
									break;
								} 
							}
							if (isAdmin) {
								admins.add(um);
							} else {
								users.add(um);
							}
						}
					}
					adapterAdmins.notifyDataSetChanged();
					adapterUsers.notifyDataSetChanged();
				}
			}
		});
	}

	public void updateFromService(View v){
		if (admins.size() > 3) {
			Toast.makeText(SheTuanAdminGuanLiActivity.this, "一个社团最多可以分配三个管理员...", Toast.LENGTH_SHORT).show();
			return;
		}
		dialogProgress.show();
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("stid", userMain.getStid());
		for (int i = 0; i < admins.size(); i++) {
			params.addBodyParameter("ids["+i+"]","" + admins.get(i).getUmid());
		}
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAdminAction!createSheTuanAdmin.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanAdminGuanLiActivity.this, "您现在没有开网络哦╥﹏╥...", Toast.LENGTH_SHORT).show();
				ShowDialog.dismissDialog(dialogProgress);
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanAdminGuanLiActivity.this, "没有找到你的社团...", Toast.LENGTH_SHORT).show();
				} else {
					ShetuanAdmin[] fromJson = new Gson().fromJson(result, ShetuanAdmin[].class);
					SheTuanAdminDAO sheTuanAdminDAO = new SheTuanAdminDAO(SheTuanAdminGuanLiActivity.this);
					sheTuanAdminDAO.deleteAll();
					for (ShetuanAdmin st : fromJson) {
						sheTuanAdminDAO.save(st);
					}
					finish();
					Toast.makeText(SheTuanAdminGuanLiActivity.this, "更新管理员成功", Toast.LENGTH_SHORT).show();
				}
				ShowDialog.dismissDialog(dialogProgress);
			}
		});
	}
	public void addAdmin(View v){
		List<UserMain> checkedUsers = adapterUsers.getCheckedUsers();
		if (admins.size() + checkedUsers.size() > 4) {
			Toast.makeText(SheTuanAdminGuanLiActivity.this, "管理员最多只能有三人!!", Toast.LENGTH_SHORT).show();			
		}
		if (checkedUsers.size() < 1) {
			Toast.makeText(SheTuanAdminGuanLiActivity.this, "在非管理员列表中没有选中任何人", Toast.LENGTH_SHORT).show();
		} else {
			users.removeAll(checkedUsers);
			admins.addAll(checkedUsers);
			checkedUsers.clear();
			adapterAdmins.notifyDataSetChanged();
			adapterUsers.notifyDataSetChanged();
		}
	}
	public void removeAdmin(View v){
		List<UserMain> checkedUsers = adapterAdmins.getCheckedUsers();
		if (checkedUsers.size() < 1) {
			Toast.makeText(SheTuanAdminGuanLiActivity.this, "在管理员列表中没有选中任何人", Toast.LENGTH_SHORT).show();
		} else {
			admins.removeAll(checkedUsers);
			users.addAll(checkedUsers);
			checkedUsers.clear();
			adapterAdmins.notifyDataSetChanged();
			adapterUsers.notifyDataSetChanged();
		}
	}
	
}
