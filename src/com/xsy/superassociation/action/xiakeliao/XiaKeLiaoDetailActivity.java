package com.xsy.superassociation.action.xiakeliao;

import java.util.ArrayList;
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
import com.xsy.superassociation.bean.Xiakeliao;
import com.xsy.superassociation.bean.XiakeliaoReply;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.DateConvert;

public class XiaKeLiaoDetailActivity extends BaseActivity {
	@ViewInject(value=R.id.xiakeliaodetail_reply)
	private ImageView xiakeliaodetail_reply;
	@ViewInject(value=R.id.xiakeliaodetail_lv)
	private ListView xiakeliaodetail_lv;
	
	private List<XiakeliaoReply> xiakeliaoReplies = new ArrayList<XiakeliaoReply>();
	private BitmapUtils bitmapUtils;
	private MyAdapter myAdapter;
	private Xiakeliao xiakeliao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiakeliao_reply);
		ViewUtils.inject(this);
		xiakeliao = (Xiakeliao) getIntent().getSerializableExtra("xiakeliao");
		xiakeliaodetail_reply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gotoCreateXiaKeLiaoReplyActivity(0,"");				
			}
		});
		bitmapUtils = new BitmapUtils(this);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//ƒ¨»œ±≥æ∞Õº∆¨  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//…Ë÷√Õº∆¨—πÀı¿‡–Õ  
    	myAdapter = new MyAdapter();
    	xiakeliaodetail_lv.setAdapter(myAdapter);
    	initDataFromService();
	}
	
	private void gotoCreateXiaKeLiaoReplyActivity(int umidYou,String nichen) {
		Intent intent = new Intent(XiaKeLiaoDetailActivity.this,
				CreateXiaKeLiaoReplyActivity.class);
		intent.putExtra("xklid", xiakeliao.getXklid());
		intent.putExtra("umidyou", umidYou);
		intent.putExtra("nichen", nichen);
		startActivity(intent);
	}
	
	public void tuichu(View v){
		finish();
	}
	
	private void initDataFromService() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("xklid", "" +xiakeliao.getXklid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"xiaKeLiaoAction!findXiaKeLiaoReply.action", params,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				if ("failure".equals(result)) {
					Toast.makeText(XiaKeLiaoDetailActivity.this, "ƒæ”–∆¿¬€...", Toast.LENGTH_SHORT).show();
					return;
				}
				XiakeliaoReply[] array = new Gson().fromJson(result, XiakeliaoReply[].class);
				xiakeliaoReplies.clear();
				for (int i = 0; i < array.length; i++) {
					xiakeliaoReplies.add(array[i]);
				}
				myAdapter.notifyDataSetChanged();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(XiaKeLiaoDetailActivity.this, "Õ¯¬Á¡¨Ω” ß∞‹...", Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return xiakeliaoReplies.size() + 1;
		}
		@Override
		public Object getItem(int arg0) {
			return xiakeliaoReplies.get(arg0 - 1);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (arg0 == 0) {
				View v = View.inflate(XiaKeLiaoDetailActivity.this, R.layout.list_item_xiakeliao, null);
				ImageView xiakeliao_touxiang = (ImageView) v.findViewById(R.id.xiakeliao_touxiang);
				TextView xiakeliao_nichen = (TextView) v.findViewById(R.id.xiakeliao_nichen);
				TextView xiakeliao_date = (TextView) v.findViewById(R.id.xiakeliao_date);
				TextView xiakeliao_title = (TextView) v.findViewById(R.id.xiakeliao_title);
				TextView xiakeliao_text = (TextView) v.findViewById(R.id.xiakeliao_text);
				TextView xiakeliao_pingluncount = (TextView) v.findViewById(R.id.xiakeliao_pingluncount);
				xiakeliao_pingluncount.setVisibility(View.GONE);
				xiakeliao_date.setText(DateConvert.convertToSimpleStr(xiakeliao.getCjdate()));
				if (xiakeliao.getUserMain() != null) {
					String s = "(Œ¥ÃÓ–¥)";
					if (!TextUtils.isEmpty(xiakeliao.getUserMain().getNichen())) {
						s = xiakeliao.getUserMain().getNichen();
					}
					xiakeliao_nichen.setText(s);
					if (!TextUtils.isEmpty(xiakeliao.getUserMain().getTouxiang())) {
						bitmapUtils.display(xiakeliao_touxiang,
								Global.BASE_TOUXIANG_URL + xiakeliao.getUserMain().getTouxiang());
					}
				}
				xiakeliao_title.setText(xiakeliao.getXkltitle());
				xiakeliao_text.setText(xiakeliao.getXkltext());
				return v;
			} else if (view == null || view.getTag() == null) {
				view = View.inflate(XiaKeLiaoDetailActivity.this, R.layout.list_item_xiakeliaoreply, null);
				viewHolder = new ViewHolder();
				viewHolder.item_touxiang = (ImageView) view.findViewById(R.id.item_touxiang);
				viewHolder.item_lou = (TextView) view.findViewById(R.id.item_lou);
				viewHolder.item_nichen = (TextView) view.findViewById(R.id.item_nichen);
				viewHolder.item_text = (TextView) view.findViewById(R.id.item_text);
				viewHolder.item_date = (TextView) view.findViewById(R.id.item_date);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			final XiakeliaoReply xiakeliaoReply = (XiakeliaoReply)xiakeliaoReplies.get(arg0 - 1);
			viewHolder.item_date.setText(DateConvert.convertToSimpleStr(xiakeliaoReply.getCjdate()));
			viewHolder.item_lou.setText("µ⁄" + (xiakeliaoReplies.size() + 1 - arg0) + "¬•");
			if (xiakeliaoReply.getUserMainMe() != null) {
				String s = "(Œ¥ÃÓ–¥)";
				if (!TextUtils.isEmpty(xiakeliaoReply.getUserMainMe().getNichen())) {
					s = xiakeliaoReply.getUserMainMe().getNichen();
				}
				viewHolder.item_nichen.setText(s);
				if (!TextUtils.isEmpty(xiakeliaoReply.getUserMainMe().getTouxiang())) {
					bitmapUtils.display(viewHolder.item_touxiang,
							Global.BASE_TOUXIANG_URL + xiakeliaoReply.getUserMainMe().getTouxiang());
				}
			}
			String s = "";
			if (xiakeliaoReply.getUserMainYou()!=null) {
				s = "Œ¥ÃÓ–¥";
				if (!TextUtils.isEmpty(xiakeliaoReply.getUserMainYou().getNichen())) {
					s = xiakeliaoReply.getUserMainYou().getNichen();
				}
				s = "∂‘[" + s + "]ªÿ∏¥£∫";
			}
			s +=xiakeliaoReply.getXklrtext(); 
			viewHolder.item_text.setText(s);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (xiakeliaoReply.getUmidme() == userMain.getUmid()) {
						//µØ≥ˆ…æ≥˝øÚ
						showDeleteDialog(xiakeliaoReply.getXklrid());
					} else {
						//µØ≥ˆªÿ∏¥øÚ
						String nichen = xiakeliaoReply.getUserMainMe().getNichen();
						if (TextUtils.isEmpty(nichen)) {
							nichen = "’À∫≈[" + xiakeliaoReply.getUserMainMe().getName() + "]";
						}
						showHuiFuDialog(xiakeliaoReply.getUmidme(),nichen);
					}
				}
			});
			return view;
		}
	}
	class ViewHolder{
		ImageView item_touxiang;
		TextView item_lou;
		TextView item_nichen;
		TextView item_text;
		TextView item_date;
	}
	private void showHuiFuDialog(final int id,final String nichen) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[]{"ªÿ∏¥"}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
				case 0:
					gotoCreateXiaKeLiaoReplyActivity(id,nichen);	
				default:
					break;
				}
			}
		});
		builder.create();
		builder.show();
	}
	private void showDeleteDialog(int xklrid){
		final int id = xklrid;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[]{"…æ≥˝"}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
				case 0:
					HttpUtils httpUtils = new HttpUtils();
					RequestParams params = new RequestParams();
					params.addBodyParameter("xklrid", "" +id);
					httpUtils.send(HttpMethod.POST, Global.BASE_URL+"xiaKeLiaoAction!deleteXiaKeLiaoReply.action", params,new RequestCallBack<String>() {
						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							String result = responseInfo.result;
							if ("failure".equals(result)) {
								Toast.makeText(XiaKeLiaoDetailActivity.this, "…æ≥˝ ß∞‹...", Toast.LENGTH_SHORT).show();
								return;
							} else {
								Toast.makeText(XiaKeLiaoDetailActivity.this, "…æ≥˝≥…π¶...", Toast.LENGTH_SHORT).show();
								initDataFromService();
							}
						}
						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(XiaKeLiaoDetailActivity.this, "Õ¯¬Á¡¨Ω” ß∞‹...", Toast.LENGTH_SHORT).show();
						}
					});
					break;

				default:
					break;
				}
			}
		});
		builder.create();
		builder.show();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		initDataFromService();
	}
		
}
