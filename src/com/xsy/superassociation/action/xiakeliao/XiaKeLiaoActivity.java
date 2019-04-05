package com.xsy.superassociation.action.xiakeliao;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.Xiakeliao;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.DateConvert;
import com.xsy.superassociation.view.RefreshListView;

public class XiaKeLiaoActivity extends BaseActivity {
	@ViewInject(value=R.id.xiakeliao_add)
	private ImageView xiakeliao_add;
	@ViewInject(value=R.id.xiakeliao_lv)
	private RefreshListView xiakeliao_lv;
	
	private List<Xiakeliao> xiaKeLiaos = new LinkedList<Xiakeliao>();
	private BitmapUtils bitmapUtils;
	private MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiakeliao);
		ViewUtils.inject(this);
		xiakeliao_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gotoCreateXiaKeLiaoActivity();				
			}
		});
		bitmapUtils = new BitmapUtils(this);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//默认背景图片  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型  
    	myAdapter = new MyAdapter();
    	xiakeliao_lv.setAdapter(myAdapter);
    	initDataFromService(0, Global.XIAKELIAO_PAGESIZE);
    	xiakeliao_lv.setMyListener(new RefreshListView.RefreshListViewListener() {
			@Override
			public void refreshData() {
				initDataFromService(0, Global.XIAKELIAO_PAGESIZE);
			}
			@Override
			public void londMoreData() {
				initDataFromServiceFooter(Global.XIAKELIAO_PAGESIZE);
			}
		});
	}
	
	private void gotoCreateXiaKeLiaoActivity() {
		Intent intent = new Intent(XiaKeLiaoActivity.this,
				CreateXiaKeLiaoActivity.class);
		startActivity(intent);
	}
	
	public void tuichu(View v){
		finish();
	}
	
	private void initDataFromService(int nowPage,int pageSize) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("nowPage", "" +nowPage);
		params.addBodyParameter("pageSize", "" + pageSize);
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"xiaKeLiaoAction!findXiaKeLiaoUserPageData.action", params,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				if ("failure".equals(result)) {
					Toast.makeText(XiaKeLiaoActivity.this, "木有获取到内容...", Toast.LENGTH_SHORT).show();
					xiakeliao_lv.recoverPullRefresh(true);
					return;
				}
				Xiakeliao[] xiakeliaoArray = new Gson().fromJson(result, Xiakeliao[].class);
				if (xiaKeLiaos.size() == 0) {
					for (int i = 0; i < xiakeliaoArray.length; i++) {
						xiaKeLiaos.add(xiakeliaoArray[i]);
					}
				} else {
					for (int i = xiakeliaoArray.length - 1; i > -1; i--) {
						if (!xiaKeLiaos.contains(xiakeliaoArray[i])) {
							xiaKeLiaos.add(0, xiakeliaoArray[i]);
						}
					}
				}
				xiakeliao_lv.recoverPullRefresh(true);
				myAdapter.notifyDataSetChanged();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(XiaKeLiaoActivity.this, "网络连接失败...", Toast.LENGTH_SHORT).show();
				xiakeliao_lv.recoverPullRefresh(false);					
			}
		});
	}
	private void initDataFromServiceFooter(int pageSize) {
		if (xiaKeLiaos.size() == 1) {
			xiakeliao_lv.recoverFooter();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("nowPage", "0");
		params.addBodyParameter("pageSize", "" + pageSize);
		Xiakeliao xiakeliao = xiaKeLiaos.get(xiaKeLiaos.size() - 1);
		params.addBodyParameter("xklid", "" + xiakeliao.getXklid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"xiaKeLiaoAction!findXiaKeLiaoUserPageDataByXklid.action", params,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				if ("failure".equals(result)) {
					Toast.makeText(XiaKeLiaoActivity.this, "木有获取到内容...", Toast.LENGTH_SHORT).show();
					xiakeliao_lv.recoverFooter();
					return;
				}
				Xiakeliao[] xiakeliaoArray = new Gson().fromJson(result, Xiakeliao[].class);
				if (xiakeliaoArray.length > 0) {
					for (int i = 0; i < xiakeliaoArray.length; i++) {
						if (!xiaKeLiaos.contains(xiakeliaoArray[i])) {
							xiaKeLiaos.add(xiakeliaoArray[i]);
						}
					}
				} else {
					Toast.makeText(XiaKeLiaoActivity.this, "已经滑到低了", Toast.LENGTH_SHORT).show();
				}
				xiakeliao_lv.recoverFooter();
				myAdapter.notifyDataSetChanged();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(XiaKeLiaoActivity.this, "网络连接失败...", Toast.LENGTH_SHORT).show();
				xiakeliao_lv.recoverFooter();
			}
		});
	}
	
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return xiaKeLiaos.size();
		}
		@Override
		public Object getItem(int arg0) {
			return xiaKeLiaos.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (view == null) {
				view = View.inflate(XiaKeLiaoActivity.this, R.layout.list_item_xiakeliao, null);
				viewHolder = new ViewHolder();
				viewHolder.xiakeliao_touxiang = (ImageView) view.findViewById(R.id.xiakeliao_touxiang);
				viewHolder.xiakeliao_nichen = (TextView) view.findViewById(R.id.xiakeliao_nichen);
				viewHolder.xiakeliao_date = (TextView) view.findViewById(R.id.xiakeliao_date);
				viewHolder.xiakeliao_title = (TextView) view.findViewById(R.id.xiakeliao_title);
				viewHolder.xiakeliao_text = (TextView) view.findViewById(R.id.xiakeliao_text);
				viewHolder.xiakeliao_pingluncount = (TextView) view.findViewById(R.id.xiakeliao_pingluncount);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			final Xiakeliao xiakeliao = (Xiakeliao)xiaKeLiaos.get(arg0);
			viewHolder.xiakeliao_date.setText(DateConvert.convertToSimpleStr(xiakeliao.getCjdate()));
			if (xiakeliao.getUserMain() != null) {
				String s = "(未填写)";
				if (!TextUtils.isEmpty(xiakeliao.getUserMain().getNichen())) {
					s = xiakeliao.getUserMain().getNichen();
				}
				viewHolder.xiakeliao_nichen.setText(s);
				if (!TextUtils.isEmpty(xiakeliao.getUserMain().getTouxiang())) {
					bitmapUtils.display(viewHolder.xiakeliao_touxiang,
							Global.BASE_TOUXIANG_URL + xiakeliao.getUserMain().getTouxiang());
				}
			}
			viewHolder.xiakeliao_title.setText(xiakeliao.getXkltitle());
			viewHolder.xiakeliao_text.setText(xiakeliao.getXkltext());
			viewHolder.xiakeliao_pingluncount.setText("" + xiakeliao.getReplyCount());
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(XiaKeLiaoActivity.this, XiaKeLiaoDetailActivity.class);
					intent.putExtra("xiakeliao", xiakeliao);
					startActivity(intent);
				}
			});
			return view;
		}
	}
	class ViewHolder{
		ImageView xiakeliao_touxiang;
		TextView xiakeliao_nichen;
		TextView xiakeliao_date;
		TextView xiakeliao_title;
		TextView xiakeliao_text;
		TextView xiakeliao_pingluncount;
	}
	
		
}
