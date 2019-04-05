package com.xsy.superassociation.fragment.base;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.action.friends.AddFriendsActivity;
import com.xsy.superassociation.action.friends.FriendsDetailActivity;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.FriendsDAO;
import com.xsy.superassociation.global.Global;

public class FriendsPager extends BasePager{
	private ListView friends_lv;
	private MyAdapter adapter;
	private List<UserMain> friends;
	private BitmapUtils bitmapUtils;
	private FriendsDAO friendsDAO;
	
	public FriendsPager(Activity myActivity) {
		super(myActivity);
	}
	@Override
	public void initData() {
		super.initData();
		setScrollingTouch(true);
		View view = View.inflate(myActivity, R.layout.activity_friends, null);
		friends_lv = (ListView) view.findViewById(R.id.friends_lv);
		friendsDAO = new FriendsDAO(myActivity);
		shetuan_framelayoLayout.addView(view);
		bitmapUtils = new BitmapUtils(myActivity);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//默认背景图片  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//加载失败图片  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型  
		friends = friendsDAO.findByUmidMe(userMain.getUmid());
		getFirendsFromService();
		adapter = new MyAdapter();
		friends_lv.setAdapter(adapter);
	}
	
	private void getFirendsFromService() {
		//从服务器获取数据
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"friendsAction!getMyAllFriends.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(myActivity, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
//					Toast.makeText(myActivity, "加载好友失败...", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
//					Toast.makeText(myActivity, "您还没有添加朋友呢", Toast.LENGTH_SHORT).show();
				} else {
//					Toast.makeText(myActivity, "更新好友成功...", Toast.LENGTH_SHORT).show();
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					friendsDAO.deleteByUmidMe(userMain.getUmid());
					friends.clear();
					for (UserMain um : fromJson) {
						friendsDAO.save(um, userMain.getUmid());
						friends.add(um);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	@Override
	public void onRestart() {
		super.onRestart();
		if (friendsDAO != null && adapter != null) {
			friends = friendsDAO.findByUmidMe(userMain.getUmid());
			adapter.notifyDataSetChanged();
		}
	}
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return friends.size() + 1;
		}
		@Override
		public Object getItem(int arg0) {
			return friends.get(arg0 - 1);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			if (arg0 == 0) {
				View v = View.inflate(myActivity, R.layout.list_item_friends_add, null);
				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						//跳转至添加好友页
						Intent intent = new Intent(myActivity, AddFriendsActivity.class);
						myActivity.startActivity(intent);
					}
				});
				return v;
			} else {
				ViewHolder viewHolder;
				if (view == null || view.getTag() == null) {
					view = View.inflate(myActivity, R.layout.list_item_friends, null);
					viewHolder = new ViewHolder();
					viewHolder.lv_item_touxiang = (ImageView) view.findViewById(R.id.lv_item_touxiang);
					viewHolder.lv_item_nichen = (TextView) view.findViewById(R.id.lv_item_nichen);
					viewHolder.lv_item_name = (TextView) view.findViewById(R.id.lv_item_name);
					viewHolder.lv_item_sname = (TextView) view.findViewById(R.id.lv_item_sname);
					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}
				final UserMain friend = friends.get(arg0 - 1);
				String s = "(未填写)";
				if (!TextUtils.isEmpty(friend.getNichen())) {
					s = friend.getNichen();
				}
				viewHolder.lv_item_nichen.setText(s);
				if (!TextUtils.isEmpty(friend.getName())) {
					viewHolder.lv_item_name.setText("账号：" + friend.getName());				
				}
				if (!TextUtils.isEmpty(friend.getTouxiang())) {
					bitmapUtils.display(viewHolder.lv_item_touxiang, Global.BASE_TOUXIANG_URL + friend.getTouxiang());
				}
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(myActivity, FriendsDetailActivity.class);
						intent.putExtra("friend", friend);
						myActivity.startActivity(intent);
					}
				});
				return view;
			}
			
		}
	}
	class ViewHolder{
		ImageView lv_item_touxiang;
		TextView lv_item_nichen;
		TextView lv_item_name;
		TextView lv_item_sname;
	}
	
}
