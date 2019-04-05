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
		//�ӷ�������ȡ����
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sheTuan.stid", userMain.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!getAllSheTuanUsers.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanTiChuUsersActivity.this, "������û�п�����Ŷ�i�n�i...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanTiChuUsersActivity.this, "�������ų�Աʧ��...", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
					Toast.makeText(SheTuanTiChuUsersActivity.this, "�����Ż�û�г�Ա", Toast.LENGTH_SHORT).show();
				} else {
//					Toast.makeText(SheTuanAdminGuanLiActivity.this, "���º��ѳɹ�...", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(SheTuanTiChuUsersActivity.this, "��û��ѡ��Ҫɾ���ĳ�Ա", Toast.LENGTH_SHORT).show();			
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(SheTuanTiChuUsersActivity.this);
		builder.setTitle("�߳���Ա");
		builder.setMessage("��ȷ��Ҫ�߳������ŵ���" + checkedUsers.size() + "λ��Ա��?");
		builder.setNegativeButton("����", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
				Toast.makeText(SheTuanTiChuUsersActivity.this, "������û�п�����Ŷ�i�n�i...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanTiChuUsersActivity.this, "���û��ѡ��Ҫ�߳��ĳ�Ա...", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SheTuanTiChuUsersActivity.this, "�ɹ��߳�" + checkedUsers.size() + "λ����", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}

	
}
