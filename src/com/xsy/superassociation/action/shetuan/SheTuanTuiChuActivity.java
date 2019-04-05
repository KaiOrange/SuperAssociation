package com.xsy.superassociation.action.shetuan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;

public class SheTuanTuiChuActivity extends BaseActivity {
	private SheTuan sheTuan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuichu_shetuan);
		sheTuan = new SheTuanDAO(this).findSheTuan(userMain.getStid());
	}
	public void tuichu(View v){
		finish();
	}
	public void tuichushetuan(View v){
		if (sheTuan == null) {
			Toast.makeText(this, "����û�м�������Ŷ", Toast.LENGTH_SHORT).show();
			return;
		} else if(sheTuan.getUmid() == userMain.getUmid()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(SheTuanTuiChuActivity.this);
			builder.setMessage("���Ǳ����ŵĴ�����,�������˳�������,�����Ҫ�˳�������\"���Ź���\"�н�ɢ�����Ż��߽�����ת�ø�������,Ȼ���˳�.");
			builder.setNegativeButton("�뿪", null);
			builder.create();
			builder.show();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(SheTuanTuiChuActivity.this);
			builder.setTitle("�˳�����");
			builder.setMessage("���õ��������Ҫ��ȥ,��ȷ��Ҫ�˳��Լ���������?");
			builder.setNegativeButton("ȡ��", null);
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					tuishuSheTuanFromService();
				}

			});
			builder.create();
			builder.show();
		}
	}
	private void tuishuSheTuanFromService() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sheTuan.stid", "" + sheTuan.getStid());
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!tuichuSheTuan.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanTuiChuActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanTuiChuActivity.this, "�˳�����ʧ�����i�n�i...", Toast.LENGTH_SHORT).show();
				} else if("createdMan".equals(result)){
					Toast.makeText(SheTuanTuiChuActivity.this, "����Ա�����˳����Ũi�n�i...", Toast.LENGTH_SHORT).show();					
				} else {
					UserMainDAO userMainDAO = new UserMainDAO(SheTuanTuiChuActivity.this);
					UserMain findUserMain = userMainDAO.findUserMain();
					findUserMain.setStid("");
					userMainDAO.update(findUserMain);
					SheTuanDAO sheTuanDAO = new SheTuanDAO(SheTuanTuiChuActivity.this);
					sheTuanDAO.deleteSheTuan(""+sheTuan.getStid());
					Toast.makeText(SheTuanTuiChuActivity.this, "�˳����ųɹ�/(��o��)/~~", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
}
