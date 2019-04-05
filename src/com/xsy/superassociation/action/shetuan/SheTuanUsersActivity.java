package com.xsy.superassociation.action.shetuan;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import com.xsy.superassociation.action.friends.FriendsDetailActivity;
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.bean.ShetuanAdmin;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.SheTuanAdminDAO;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.global.Global;

public class SheTuanUsersActivity extends BaseActivity {
	@ViewInject(value=R.id.shentuan_users_lv)
	private ListView listView;
	private List<UserMain> userMains;
	private MyAdapter adapter;
	private BitmapUtils bitmapUtils;
	private SheTuan sheTuan;
	private List<ShetuanAdmin> admins;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shentuan_users);
		ViewUtils.inject(this);
		userMains = new ArrayList<UserMain>();
		sheTuan = new SheTuanDAO(this).findSheTuan(userMain.getStid());	
		admins = new SheTuanAdminDAO(this).findByStid(userMain.getStid());
		//�������ȡ��Դ
		getUsersFromService();
		bitmapUtils = new BitmapUtils(this);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//Ĭ�ϱ���ͼƬ  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//����ʧ��ͼƬ  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//����ͼƬѹ������  
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
	}
	
	private void getUsersFromService() {
		//�ӷ�������ȡ����
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sheTuan.stid", userMain.getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!getAllSheTuanUsers.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanUsersActivity.this, "������û�п�����Ŷ�i�n�i...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanUsersActivity.this, "�������ų�Աʧ��...", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
					Toast.makeText(SheTuanUsersActivity.this, "�����Ż�û�г�Ա", Toast.LENGTH_SHORT).show();
				} else {
//					Toast.makeText(myActivity, "���º��ѳɹ�...", Toast.LENGTH_SHORT).show();
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					userMains.clear();
					for (UserMain um : fromJson) {
						userMains.add(um);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return userMains.size();
		}
		@Override
		public Object getItem(int arg0) {
			return userMains.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (view == null || view.getTag() == null) {
				view = View.inflate(SheTuanUsersActivity.this, R.layout.list_item_st_users, null);
				viewHolder = new ViewHolder();
				viewHolder.lv_item_touxiang = (ImageView) view.findViewById(R.id.lv_item_touxiang);
				viewHolder.lv_item_nichen = (TextView) view.findViewById(R.id.lv_item_nichen);
				viewHolder.lv_item_name = (TextView) view.findViewById(R.id.lv_item_name);
				viewHolder.lv_item_isadmin = (TextView) view.findViewById(R.id.lv_item_isadmin);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			final UserMain friend = userMains.get(arg0);
			String s = "(δ��д)";
			if (!TextUtils.isEmpty(friend.getNichen())) {
				s = friend.getNichen();
			}
			viewHolder.lv_item_nichen.setText(s);
			if (!TextUtils.isEmpty(friend.getName())) {
				viewHolder.lv_item_name.setText("�˺ţ�" + friend.getName());				
			}
			if (!TextUtils.isEmpty(friend.getTouxiang())) {
				bitmapUtils.display(viewHolder.lv_item_touxiang, Global.BASE_TOUXIANG_URL + friend.getTouxiang());
			} else {
				viewHolder.lv_item_touxiang.setImageResource(R.drawable.touxiang_user);
			}
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(SheTuanUsersActivity.this, FriendsDetailActivity.class);
					intent.putExtra("friend", friend);
					SheTuanUsersActivity.this.startActivity(intent);
				}
			});
			if (sheTuan.getUmid() == friend.getUmid()) {
				viewHolder.lv_item_isadmin.setVisibility(View.VISIBLE);
				viewHolder.lv_item_isadmin.setText("������");
			} else {
				boolean isAdmin = false;
				if (admins.size() > 0) {
					for (ShetuanAdmin admin : admins) {
						if (admin.getUmid() == friend.getUmid()) {
							viewHolder.lv_item_isadmin
									.setVisibility(View.VISIBLE);
							viewHolder.lv_item_isadmin.setText("����Ա");
							isAdmin = true;
							break;
						}
					}
				}
				if (!isAdmin) {
					viewHolder.lv_item_isadmin.setVisibility(View.GONE);
				}
			}
			return view;
		
		}
	}
	class ViewHolder{
		ImageView lv_item_touxiang;
		TextView lv_item_nichen;
		TextView lv_item_name;
		TextView lv_item_isadmin;
	}

}
