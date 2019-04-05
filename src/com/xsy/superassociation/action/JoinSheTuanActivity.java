package com.xsy.superassociation.action;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.view.RefreshListView;

public class JoinSheTuanActivity extends BaseActivity {
	@ViewInject(value=R.id.join_create_shetuan)
	private ImageView join_create_shetuan;
	@ViewInject(value=R.id.join_create_write)
	private ImageView join_create_write;
	@ViewInject(value=R.id.lv_shetuan)
	private RefreshListView lv_shetuan;
	
	private List<SheTuan> sheTuans = new LinkedList<SheTuan>();
	private BitmapUtils bitmapUtils;
	private MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_shetuan);
		ViewUtils.inject(this);
		join_create_shetuan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gotoCreateSheTuanActivity();				
			}
		});
		join_create_write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(JoinSheTuanActivity.this);
				builder.setTitle("精确查找");
				View view = View.inflate(JoinSheTuanActivity.this, R.layout.dialog_join_shetuan, null);
				final EditText text = (EditText) view.findViewById(R.id.dialog_stid);
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String idStr = text.getText().toString();
						findSheTuanFromService(idStr);
					}
				});
				builder.setView(view);
				builder.create();
				builder.show();
			}
		});
		bitmapUtils = new BitmapUtils(this);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//默认背景图片  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//加载失败图片  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型  
    	myAdapter = new MyAdapter();
    	lv_shetuan.setAdapter(myAdapter);
    	initDataFromService(0, Global.SHETUAN_PAGESIZE);
    	lv_shetuan.setMyListener(new RefreshListView.RefreshListViewListener() {
			@Override
			public void refreshData() {
				initDataFromService(0, Global.SHETUAN_PAGESIZE);
			}
			@Override
			public void londMoreData() {
				initDataFromFooter(Global.SHETUAN_PAGESIZE);
			}
		});
	}
	private void findSheTuanFromService(String idStr) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("stid", idStr);
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!findSheTuanBySid.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(JoinSheTuanActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(JoinSheTuanActivity.this, "没有找到该社团", Toast.LENGTH_SHORT).show();
				} else {
					SheTuan fromJson = new Gson().fromJson(result, SheTuan.class);
					gotoSheTuanDetailActivity(fromJson);
				}
			}
		});
		
	}
	
	private void gotoCreateSheTuanActivity() {
		//判断是否加入社团如果没有加入则可以创建
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!isJoinSheTuan.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(JoinSheTuanActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("true".equals(result)) {
					Intent intent = new Intent(JoinSheTuanActivity.this,
							CreateSheTuanFailureActivity.class);
					intent.putExtra("isCreate", true);
					startActivity(intent);
				} else {
					Intent intent = new Intent(JoinSheTuanActivity.this,
							CreateSheTuanActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	public void tuichu(View v){
		finish();
	}
	
	private void initDataFromService(int nowPage,int pageSize) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("nowPage", "" +nowPage);
		params.addBodyParameter("pageSize", "" + pageSize);
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!findSheTuanUsePageDate.action", params,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				if ("failure".equals(result)) {
					Toast.makeText(JoinSheTuanActivity.this, "加载社团失败...", Toast.LENGTH_SHORT).show();
					lv_shetuan.recoverPullRefresh(true);
					return;
				}
				SheTuan[] sheTuanArray = new Gson().fromJson(result, SheTuan[].class);
				if (sheTuans.size() == 0) {
					for (int i = 0; i < sheTuanArray.length; i++) {
						sheTuans.add(sheTuanArray[i]);
					}
				} else {
					for (int i = sheTuanArray.length - 1; i > -1; i--) {
						if (!sheTuans.contains(sheTuanArray[i])) {
							sheTuans.add(0, sheTuanArray[i]);
						}
					}
				}
				lv_shetuan.recoverPullRefresh(true);
				myAdapter.notifyDataSetChanged();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(JoinSheTuanActivity.this, "网络连接失败...", Toast.LENGTH_SHORT).show();
				lv_shetuan.recoverPullRefresh(false);					
			}
		});
	}
	private void initDataFromFooter(int pageSize) {
		if (sheTuans.size() == 0) {
			lv_shetuan.recoverFooter();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("nowPage", "0");
		params.addBodyParameter("pageSize", "" + pageSize);
		params.addBodyParameter("stid", "" + sheTuans.get(sheTuans.size() - 1).getStid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!findSheTuanUsePageDateAndStid.action", params,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				if ("failure".equals(result)) {
					Toast.makeText(JoinSheTuanActivity.this, "加载社团失败...", Toast.LENGTH_SHORT).show();
					lv_shetuan.recoverPullRefresh(true);
					return;
				}
				SheTuan[] sheTuanArray = new Gson().fromJson(result, SheTuan[].class);
				if (sheTuanArray.length > 0) {
					for (int i = 0; i < sheTuanArray.length; i++) {
						if (!sheTuans.contains(sheTuanArray[i])) {
							sheTuans.add(sheTuanArray[i]);
						}
					}
				} else {
					Toast.makeText(JoinSheTuanActivity.this, "已经加载到底了", Toast.LENGTH_SHORT).show();
				}
				lv_shetuan.recoverFooter();
				myAdapter.notifyDataSetChanged();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(JoinSheTuanActivity.this, "网络连接失败...", Toast.LENGTH_SHORT).show();
				lv_shetuan.recoverFooter();
			}
		});
	}
	
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return sheTuans.size();
		}
		@Override
		public Object getItem(int arg0) {
			return sheTuans.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (view == null) {
				view = View.inflate(JoinSheTuanActivity.this, R.layout.list_item_shetuan, null);
				viewHolder = new ViewHolder();
				viewHolder.lv_item_touxiang = (ImageView) view.findViewById(R.id.lv_item_touxiang);
				viewHolder.lv_item_sname = (TextView) view.findViewById(R.id.lv_item_sname);
				viewHolder.lv_item_sdescribe = (TextView) view.findViewById(R.id.lv_item_sdescribe);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			final SheTuan currSheTuan = (SheTuan)sheTuans.get(arg0);
			viewHolder.lv_item_sname.setText(currSheTuan.getSname());
			if (!TextUtils.isEmpty(currSheTuan.getSdescribe())) {
				viewHolder.lv_item_sdescribe.setText(currSheTuan.getSdescribe());				
			}
			if (!TextUtils.isEmpty(currSheTuan.getTouxiang())) {
				bitmapUtils.display(viewHolder.lv_item_touxiang,
						Global.BASE_SHETUANTX_URL + currSheTuan.getTouxiang());
			}
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					gotoSheTuanDetailActivity(currSheTuan);
				}
			});
			return view;
		}
	}
	class ViewHolder{
		ImageView lv_item_touxiang;
		TextView lv_item_sname;
		TextView lv_item_sdescribe;
	}
	private void gotoSheTuanDetailActivity(SheTuan shetuan) {
		Intent intent = new Intent(JoinSheTuanActivity.this, ShowSheTuanInfoActivity.class);
		intent.putExtra("sheTuan", shetuan);
		startActivity(intent);
	}
		
}
