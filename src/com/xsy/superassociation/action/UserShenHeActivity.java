package com.xsy.superassociation.action;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.global.Global;

public class UserShenHeActivity extends BaseActivity {
	@ViewInject(value=R.id.shenhe_lv)
	private ListView shenhe_lv;
	private HttpUtils httpUtils;
	private List<UserMain> userMains = new ArrayList<UserMain>();
	private BitmapUtils bitmapUtils;
	private List<Integer> checkedIds = new ArrayList<Integer>();
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_shenhe);
		ViewUtils.inject(this);
		httpUtils = new HttpUtils();
		bitmapUtils = new BitmapUtils(this);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//默认背景图片  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//加载失败图片  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型  
    	adapter = new MyAdapter();
    	shenhe_lv.setAdapter(adapter);
		getWantJoinUsers();
	}
	private void getWantJoinUsers() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("u2st.stid", "" + userMain.getStid());
		params.addBodyParameter("u2st.umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"u2stAction!getWantJoinedUser.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(UserShenHeActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(UserShenHeActivity.this, "暂时还没有童鞋要加入你们社团哦...", Toast.LENGTH_SHORT).show();
				} else if("notAdmin".equals(result)){
					Toast.makeText(UserShenHeActivity.this, "权限不足:很抱歉您已经不是管理员了...", Toast.LENGTH_SHORT).show();					
				} else {
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					for (UserMain userMain : fromJson) {
						userMains.add(userMain);						
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	public void refuseJoin(View v){
		if (!checkIsHaveUser(false)) {
			return;
		}
		RequestParams params = new RequestParams();
		final Integer[] array = checkedIds.toArray(new Integer[]{});
		checkedIds.clear();
		params.addBodyParameter("u2st.stid", "" + userMain.getStid());
		params.addBodyParameter("checkedIds", new Gson().toJson(array));
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"u2stAction!refuseJoin.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				for (Integer integer : array) {
					if (!checkedIds.contains(integer)) {
						checkedIds.add(integer);
					}
				}
				Toast.makeText(UserShenHeActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(UserShenHeActivity.this, "拒绝加入失败...", Toast.LENGTH_SHORT).show();
				} else {
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					for (UserMain userMain : fromJson) {
						userMains.remove(userMain);						
					}
					adapter.notifyDataSetChanged();
					Toast.makeText(UserShenHeActivity.this, "已成功拒绝" + fromJson.length + "位童鞋的加入...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	public void confirmJoin(View v){
		if (!checkIsHaveUser(true)) {
			return;
		}
		RequestParams params = new RequestParams();
		final Integer[] array = checkedIds.toArray(new Integer[]{});
		checkedIds.clear();
		params.addBodyParameter("u2st.stid", "" + userMain.getStid());
		params.addBodyParameter("checkedIds", new Gson().toJson(array));
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"u2stAction!confirmJoin.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				for (Integer integer : array) {
					if (!checkedIds.contains(integer)) {
						checkedIds.add(integer);
					}
				}
				Toast.makeText(UserShenHeActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(UserShenHeActivity.this, "同意加入失败...", Toast.LENGTH_SHORT).show();
				} else {
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					for (UserMain userMain : fromJson) {
						userMains.remove(userMain);						
					}
					adapter.notifyDataSetChanged();
					Toast.makeText(UserShenHeActivity.this, "已同意" + fromJson.length + "位童鞋的加入...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private boolean checkIsHaveUser(boolean b) {//b 为true 则为确认加入 false为拒绝加入
		boolean goAhead = true;
		if (checkedIds.size() < 1) {
			Toast.makeText(UserShenHeActivity.this, "您没有" + (b?"确认":"拒绝") + "任何同学加入", Toast.LENGTH_SHORT).show();			
			goAhead = false;
		}
		return goAhead;
	}
	public void tuichu(View v){finish();}
	
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
			if (view == null) {
				view = View.inflate(UserShenHeActivity.this, R.layout.list_item_user_shenhe, null);
				viewHolder = new ViewHolder();
				viewHolder.shehe_checkBox = (CheckBox) view.findViewById(R.id.shehe_checkBox);
				viewHolder.shehe_touxiang = (ImageView) view.findViewById(R.id.shehe_touxiang);
				viewHolder.shehe_nichen = (TextView) view.findViewById(R.id.shehe_nichen);
				viewHolder.shehe_loginname = (TextView) view.findViewById(R.id.shehe_loginname);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			final UserMain currUserMain = userMains.get(arg0);
			viewHolder.shehe_loginname.setText("账号：" + currUserMain.getName());
			String s = "(未填写)";
			if (!TextUtils.isEmpty(currUserMain.getNichen())) {
				s = currUserMain.getNichen();
			}
			viewHolder.shehe_nichen.setText(s);				
			if (!TextUtils.isEmpty(currUserMain.getTouxiang())) {
				bitmapUtils.display(viewHolder.shehe_touxiang,
						Global.BASE_TOUXIANG_URL + currUserMain.getTouxiang());
			} else {
				viewHolder.shehe_touxiang.setImageResource(R.drawable.touxiang_user);
			}
			viewHolder.shehe_checkBox.setChecked(false);
			viewHolder.shehe_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (arg1) {//选中
						checkedIds.add(Integer.valueOf(currUserMain.getUmid()));
					} else {//未选中
						checkedIds.remove(Integer.valueOf(currUserMain.getUmid()));
					}
				}
			});
			return view;
		}
	}
	class ViewHolder{
		CheckBox shehe_checkBox;
		ImageView shehe_touxiang;
		TextView shehe_nichen;
		TextView shehe_loginname;
	}
}
