package com.xsy.superassociation.action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.global.Global;

public class ShowSheTuanInfoActivity extends BaseActivity{
	@ViewInject(value=R.id.shetuan_stid)
	private TextView shetuan_stid;
	@ViewInject(value=R.id.shetuan_sname)
	private TextView shetuan_sname;
	@ViewInject(value=R.id.shetuan_cjdate)
	private TextView shetuan_cjdate;
	@ViewInject(value=R.id.shetuan_sdescribe)
	private TextView shetuan_sdescribe;
	@ViewInject(value=R.id.shetuan_username)
	private TextView shetuan_username;
	@ViewInject(value=R.id.join_button)
	private Button join_button;
	private SheTuan sheTuan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_shetuaninfo);
		ViewUtils.inject(this);
		sheTuan = (SheTuan) getIntent().getSerializableExtra("sheTuan");
		if (sheTuan != null) {
			//��ť��ʾ
			join_button.setVisibility(View.VISIBLE);
		} else {
			//��ťӰ��	
			SheTuanDAO sheTuanDAO = new SheTuanDAO(this);
			sheTuan = sheTuanDAO.findSheTuan(userMain.getStid());
			join_button.setVisibility(View.GONE);
		}
		if (sheTuan != null) {
			shetuan_stid.setText("" + sheTuan.getStid());
			shetuan_sname.setText("" + sheTuan.getSname());
			shetuan_cjdate.setText("" + sheTuan.getCjdate());
			shetuan_sdescribe.setText("" + sheTuan.getSdescribe());
			shetuan_username.setText("" + sheTuan.getUmName());
		}
	}
	public void exit(View v){finish();}
	public void joinSheTuan(View v){
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!isJoinSheTuan.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(ShowSheTuanInfoActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("true".equals(result)) {
					Intent intent = new Intent(ShowSheTuanInfoActivity.this,
							CreateSheTuanFailureActivity.class);
					intent.putExtra("isCreate", false);
					startActivity(intent);					
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(ShowSheTuanInfoActivity.this);
					builder.setTitle("��������");
					builder.setMessage("��ȷ��Ҫ����[" + sheTuan.getSname() + "]������?" );
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							joinSheTuan();
						}
					});
					builder.setNegativeButton("����", null);
					builder.create();
					builder.show();
				}
			}
		});
	}
	private void joinSheTuan(){
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("u2st.umid", "" + userMain.getUmid());
		params.addBodyParameter("u2st.stid", "" + sheTuan.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"u2stAction!joinSheTuan.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(ShowSheTuanInfoActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("seccess".equals(result)) {
					Toast.makeText(ShowSheTuanInfoActivity.this, "�ȴ����:�Ѿ������Ա�ύ������������...", Toast.LENGTH_SHORT).show();
				} else if("hava".equals(result)){
					Toast.makeText(ShowSheTuanInfoActivity.this, "���Ѿ�����������������ٴ�����...", Toast.LENGTH_SHORT).show();					
				} else  {
					Toast.makeText(ShowSheTuanInfoActivity.this, "�������ʧ��...", Toast.LENGTH_SHORT).show();					
				}
			}
		});
	}
	
}
