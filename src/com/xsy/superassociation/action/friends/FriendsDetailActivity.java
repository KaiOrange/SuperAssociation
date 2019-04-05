package com.xsy.superassociation.action.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
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
import com.xsy.superassociation.action.tongzhi.TongZhiType3Activity;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.FriendsDAO;
import com.xsy.superassociation.dao.TongZhiDAO;
import com.xsy.superassociation.global.Global;

public class FriendsDetailActivity extends BaseActivity{
	@ViewInject(value=R.id.userMain_touxiang)
	private ImageView touxiang;
	@ViewInject(value=R.id.userMain_name)
	private TextView name;
	@ViewInject(value=R.id.userMain_nicheng)
	private TextView nicheng;
	@ViewInject(value=R.id.userMain_telphone)
	private TextView telphone;
	@ViewInject(value=R.id.userMain_sex)
	private TextView sex;
	@ViewInject(value=R.id.userMain_age)
	private TextView age;
	@ViewInject(value=R.id.userMain_birthday)
	private TextView birthday;
	@ViewInject(value=R.id.userMain_stid)
	private TextView stid;
	@ViewInject(value=R.id.userMain_exit)
	private ImageView exit;
	//����
	@ViewInject(value=R.id.userMain_layout_telphone)
	private RelativeLayout layout_telphone;
	
	private UserMain friend;
	private BitmapUtils bitmapUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_info);
		ViewUtils.inject(this);
		bitmapUtils = new BitmapUtils(this);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//Ĭ�ϱ���ͼƬ  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//����ʧ��ͼƬ  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//����ͼƬѹ������  
    	friend = (UserMain) getIntent().getSerializableExtra("friend");
		updateUserMain();
		
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		
		layout_telphone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(friend.getTelphone())) {
					Toast.makeText(FriendsDetailActivity.this, "�ú���Ϊ��,���ɲ���", Toast.LENGTH_SHORT).show();
					return;
				}
				//����绰���Ͷ���
				AlertDialog.Builder builder = new AlertDialog.Builder(FriendsDetailActivity.this);
				builder.setItems(new String[]{"���е绰","���Ͷ���"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {//���е绰
							Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+friend.getTelphone())); 
							startActivity(intent);
						} else if(arg1 == 1) {//���Ͷ���
							Uri uri = Uri.parse("smsto:" + friend.getTelphone());
							Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
							startActivity(intent);
						}
					}
				});
				builder.create();
				builder.show();
			}
		});
		
		
	}

	private void updateUserMain() {
		if (friend.getTouxiang() != null) {
	    	bitmapUtils.display(touxiang, Global.BASE_TOUXIANG_URL + friend.getTouxiang());
		}
		name.setText(friend.getName());
		if (TextUtils.isEmpty(friend.getNichen())) {
			nicheng.setText("(δ��д)");
			nicheng.setTextColor(Color.DKGRAY);
		} else {
			nicheng.setText(friend.getNichen());			
		}
		if (TextUtils.isEmpty(friend.getTelphone())) {
			telphone.setText("(δ��д)");
			telphone.setTextColor(Color.DKGRAY);
		} else {
			telphone.setText(friend.getTelphone());
		}
		sex.setText(friend.isSex() ? "��":"Ů");
		age.setText("" + friend.getAge());
		String s1 = "(δ����)";
		if (!TextUtils.isEmpty(friend.getStid())) {
			s1 = "" + friend.getStid();
		}
		stid.setText(s1);
		if (TextUtils.isEmpty(friend.getBrithday())) {
			birthday.setText("(δ��д)");
			birthday.setTextColor(Color.DKGRAY);
		} else {
			String s = friend.getBrithday().split(" ")[0];
			birthday.setText(s);
		}
	}
	
	public void gototongzhitype3(View v){
		String nicheng = friend.getNichen();
		if (!TextUtils.isEmpty(nicheng)) {
			nicheng = "���û�[" + nicheng + "]����";
		} else {						
			nicheng = "���˺�[" + friend.getName() + "]����";
		}
		Intent intent = new Intent(FriendsDetailActivity.this, TongZhiType3Activity.class);
		intent.putExtra("umidYou", "" + friend.getUmid());
		intent.putExtra("title", nicheng);
		startActivity(intent);
	}
	public void deleteFriend(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(FriendsDetailActivity.this);
		builder.setTitle("ɾ������");
		String s = null;
		if (!TextUtils.isEmpty(friend.getNichen())) {
			s = friend.getNichen();
		} else {
			s = friend.getName();			
		}
		builder.setMessage("��ȷ��Ҫɾ����ĺ���[" + s + "]��?");
		builder.setNegativeButton("����", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				deleteFromServece();
			}
		});
		builder.create();
		builder.show();
	}
	private void deleteFromServece() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		params.addBodyParameter("umid2", "" + friend.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"friendsAction!deleteFriend.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(FriendsDetailActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("seccess".equals(result)) {
					new FriendsDAO(FriendsDetailActivity.this).delete(friend.getUmid());
					new TongZhiDAO(FriendsDetailActivity.this).deleteLiaoTianByUmid(userMain.getUmid(),"" + friend.getUmid());
					Toast.makeText(FriendsDetailActivity.this, "ɾ�����ѳɹ�", Toast.LENGTH_SHORT).show();
					finish();
				} else if("failure".equals(result)){
					Toast.makeText(FriendsDetailActivity.this, "ɾ������ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
