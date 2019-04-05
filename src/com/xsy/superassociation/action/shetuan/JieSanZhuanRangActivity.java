package com.xsy.superassociation.action.shetuan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.xsy.superassociation.action.MainActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.ShetuanAdmin;
import com.xsy.superassociation.dao.SheTuanAdminDAO;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.MD5Utils;

public class JieSanZhuanRangActivity extends BaseActivity {
	@ViewInject(value=R.id.password)
	private EditText password;
	private boolean isZhuanRang = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiesan_zhuanrang);
		ViewUtils.inject(this);
	}
	
	private void checkPassword() {
		if (TextUtils.isEmpty(password.getText().toString())) {
			Toast.makeText(JieSanZhuanRangActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userName", userMain.getName());
		//MD5加密
		params.addBodyParameter("password", MD5Utils.md5Password(password.getText().toString()));
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!login.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(JieSanZhuanRangActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if (!"null".equals(result)) {
					if (isZhuanRang) {
						doZhuanRang();
					} else {
						doJieSan();
					}
				} else {
					Toast.makeText(JieSanZhuanRangActivity.this, "密码或用户名错误。。", Toast.LENGTH_SHORT).show();
				}
			}

		});
	}
	private void doJieSan() {
		AlertDialog.Builder builder = new AlertDialog.Builder(JieSanZhuanRangActivity.this);
		builder.setTitle("解散社团");
		builder.setMessage("你确定要解散该社团吗,此操作可要慎用啊?");
		builder.setNegativeButton("放弃", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				jiesanFromServece();
			}
		});
		builder.create();
		builder.show();
	}

	private void doZhuanRang() {
		AlertDialog.Builder builder = new AlertDialog.Builder(JieSanZhuanRangActivity.this);
		builder.setTitle("转让社团");
		View view = View.inflate(this, R.layout.dialog_zhuanrang, null);
		final EditText tv_name = (EditText) view.findViewById(R.id.dialog_name);
		builder.setView(view);
		builder.setNegativeButton("放弃", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (TextUtils.isEmpty(tv_name.getText().toString())) {
					Toast.makeText(JieSanZhuanRangActivity.this, "对方账号不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				zhuanrangFromService(tv_name.getText().toString());
			}
		});
		builder.create();
		builder.show();
	}
	private void jiesanFromServece() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		params.addBodyParameter("stid", userMain.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!jiesan.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(JieSanZhuanRangActivity.this, "您现在没有开网络哦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(JieSanZhuanRangActivity.this, "你的没有选择要踢出的成员...", Toast.LENGTH_SHORT).show();
				} else if ("notCreater".equals(result)){
					Toast.makeText(JieSanZhuanRangActivity.this, "你不是该社团的社老大,没有这么大的权力", Toast.LENGTH_SHORT).show();
				} else{
					Toast.makeText(JieSanZhuanRangActivity.this, "解散社团成功", Toast.LENGTH_SHORT).show();
					SheTuanAdminDAO adminDAO = new SheTuanAdminDAO(JieSanZhuanRangActivity.this);
					adminDAO.deleteAll();
					userMain.setStid("");
					new UserMainDAO(JieSanZhuanRangActivity.this).update(userMain);
					Intent intent = new Intent(JieSanZhuanRangActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}
	private void zhuanrangFromService(String name) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		params.addBodyParameter("stid", userMain.getStid());
		params.addBodyParameter("name", name);
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!zhuanrang.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(JieSanZhuanRangActivity.this, "您现在没有开网络哦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(JieSanZhuanRangActivity.this, "你的没有选择要踢出的成员...", Toast.LENGTH_SHORT).show();
				} else if ("notCreater".equals(result)){
					Toast.makeText(JieSanZhuanRangActivity.this, "你不是该社团的社老大,没有这么大的权力", Toast.LENGTH_SHORT).show();
				} else if ("userNotFount".equals(result)){
					Toast.makeText(JieSanZhuanRangActivity.this, "您没有找到你填写的账号所对应的用户", Toast.LENGTH_SHORT).show();
				} else if ("notInSheTuan".equals(result)){
					Toast.makeText(JieSanZhuanRangActivity.this, "该用户不是社团中的成员不能转让", Toast.LENGTH_SHORT).show();
				} else if ("cannotbeyou".equals(result)){
					
				}else{
					Toast.makeText(JieSanZhuanRangActivity.this, "社团转让成功,您已经不是该社团的老大了", Toast.LENGTH_SHORT).show();
					ShetuanAdmin[] array = new Gson().fromJson(result, ShetuanAdmin[].class);
					SheTuanAdminDAO adminDAO = new SheTuanAdminDAO(JieSanZhuanRangActivity.this);
					adminDAO.deleteAll();
					for (ShetuanAdmin shetuanAdmin2 : array) {
						adminDAO.save(shetuanAdmin2);
					}
					Intent intent = new Intent(JieSanZhuanRangActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}
	public void zhuanrang(View v){
		isZhuanRang = true;
		checkPassword();
	}
	public void jiesan(View v){
		isZhuanRang = false;
		checkPassword();
	}
}
