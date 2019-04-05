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
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//Ĭ�ϱ���ͼƬ  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//����ʧ��ͼƬ  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//����ͼƬѹ������  
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
				Toast.makeText(UserShenHeActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(UserShenHeActivity.this, "��ʱ��û��ͯЬҪ������������Ŷ...", Toast.LENGTH_SHORT).show();
				} else if("notAdmin".equals(result)){
					Toast.makeText(UserShenHeActivity.this, "Ȩ�޲���:�ܱ�Ǹ���Ѿ����ǹ���Ա��...", Toast.LENGTH_SHORT).show();					
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
				Toast.makeText(UserShenHeActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(UserShenHeActivity.this, "�ܾ�����ʧ��...", Toast.LENGTH_SHORT).show();
				} else {
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					for (UserMain userMain : fromJson) {
						userMains.remove(userMain);						
					}
					adapter.notifyDataSetChanged();
					Toast.makeText(UserShenHeActivity.this, "�ѳɹ��ܾ�" + fromJson.length + "λͯЬ�ļ���...", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(UserShenHeActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(UserShenHeActivity.this, "ͬ�����ʧ��...", Toast.LENGTH_SHORT).show();
				} else {
					UserMain[] fromJson = new Gson().fromJson(result, UserMain[].class);
					for (UserMain userMain : fromJson) {
						userMains.remove(userMain);						
					}
					adapter.notifyDataSetChanged();
					Toast.makeText(UserShenHeActivity.this, "��ͬ��" + fromJson.length + "λͯЬ�ļ���...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private boolean checkIsHaveUser(boolean b) {//b Ϊtrue ��Ϊȷ�ϼ��� falseΪ�ܾ�����
		boolean goAhead = true;
		if (checkedIds.size() < 1) {
			Toast.makeText(UserShenHeActivity.this, "��û��" + (b?"ȷ��":"�ܾ�") + "�κ�ͬѧ����", Toast.LENGTH_SHORT).show();			
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
			viewHolder.shehe_loginname.setText("�˺ţ�" + currUserMain.getName());
			String s = "(δ��д)";
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
					if (arg1) {//ѡ��
						checkedIds.add(Integer.valueOf(currUserMain.getUmid()));
					} else {//δѡ��
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
