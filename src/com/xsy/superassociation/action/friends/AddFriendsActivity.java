package com.xsy.superassociation.action.friends;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.ShowDialog;

public class AddFriendsActivity extends BaseActivity {
	@ViewInject(value=R.id.friends_name)
	private TextView friends_name;
	@ViewInject(value=R.id.queren)
	private Button queren;
	private ProgressDialog dialogProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_add);
		ViewUtils.inject(this);
		dialogProgress = ShowDialog.getDialogProgress(this, "��Ӻ���", "���ڷ�����Ӻ�������");
		queren.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String userName = friends_name.getText().toString();
				if (TextUtils.isEmpty(userName)) {
					Toast.makeText(AddFriendsActivity.this, "�˺Ų���Ϊ��!!!", Toast.LENGTH_SHORT).show();
					return;
				}
				dialogProgress.show();
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("userName", userName);
				params.addBodyParameter("umid", "" + userMain.getUmid());
				httpUtils.send(HttpMethod.POST, Global.BASE_URL+"friendsAction!callFriends.action", params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(AddFriendsActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
						ShowDialog.dismissDialog(dialogProgress);
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						if ("seccess".equals(result)) {
							Toast.makeText(AddFriendsActivity.this, "��ϲ��,�Ѿ�����ĺ��ѷ��������������,�뾲������\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
							finish();
						} else if("failure".equals(result)){
							Toast.makeText(AddFriendsActivity.this, "��Ӻ���ʧ��", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(AddFriendsActivity.this, "���˺Ų�����,����ϸ�˶��˺�", Toast.LENGTH_SHORT).show();
						}
						ShowDialog.dismissDialog(dialogProgress);
					}
				});
			}
		});
	}
}
