package com.xsy.superassociation.action.tongzhi;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.Tongzhi;
import com.xsy.superassociation.dao.ChatInfosDAO;
import com.xsy.superassociation.dao.TongZhiDAO;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.DateConvert;

public class TongZhiType3Activity extends BaseActivity {
	
	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private MsgAdapter adapter;
	private List<Tongzhi> tongzhis;
	private TongZhiDAO tongZhiDAO;
	private String umidYou;
	private HttpUtils httpUtils;
	private TextView title_name;
	private boolean isRunning = true;
	private ChatInfosDAO chatInfosDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend_talk);
		title_name = (TextView) findViewById(R.id.friend_name);
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		initMsgs();
		adapter = new MsgAdapter(TongZhiType3Activity.this, R.layout.list_item_msg, tongzhis);
		msgListView = (ListView) findViewById(R.id.msg_list_view);
		msgListView.setAdapter(adapter);
		msgListView.setSelection(tongzhis.size());
		httpUtils = new HttpUtils();
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = inputText.getText().toString();
				if (!"".equals(content)) {
					String nicheng = userMain.getNichen();
					if (!TextUtils.isEmpty(nicheng)) {
						nicheng = "与用户[" + nicheng + "]聊天";
					} else {						
						nicheng = "与账号[" + userMain.getName() + "]聊天";
					}
					Tongzhi tongzhi = new Tongzhi(-1, content,nicheng , Integer.parseInt(umidYou), DateConvert.getDateStr(), 0, 3, "" +userMain.getUmid());
					//联网发送消息
					sendToService(tongzhi);
				}
			}

		});
		//获取消息
		whileEndStop();
	}
	private void whileEndStop() {
		new Thread(){
			public void run() {
				while (isRunning) {
					getMsgFromService();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	private void sendToService(final Tongzhi tongzhi) {
		httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("tongzhi.tztext", tongzhi.getTztext());
		params.addBodyParameter("tongzhi.tztitle", tongzhi.getTztitle());
		params.addBodyParameter("tongzhi.umid", "" + tongzhi.getUmid());
		params.addBodyParameter("tongzhi.tzType", "" + tongzhi.getTzType());
		params.addBodyParameter("tongzhi.additional", tongzhi.getAdditional());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"tongZhiAction!sendMessage.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(TongZhiType3Activity.this, "木有网络...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(TongZhiType3Activity.this, "发送失败", Toast.LENGTH_SHORT).show();					
				} else {
					tongzhi.setTzdate(result);
					tongzhis.add(tongzhi);
					//成功时保存
					chatInfosDAO.save(tongzhi);
					adapter.notifyDataSetChanged();
					msgListView.setSelection(tongzhis.size());
					inputText.setText("");
				}
			}
		});
		
	}
	private void getMsgFromService() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"tongZhiAction!getTongZhisByUmid.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(TongZhiType3Activity.this, "查找消息时网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if (!"notFound".equals(result)) {
					Tongzhi[] fromJson = new Gson().fromJson(result, Tongzhi[].class);
//					int i = 0;
					boolean flag = false;
					for (Tongzhi tongzhi : fromJson) {
						if (tongzhi.getTzType() == 3 && tongzhi.getAdditional().equals(umidYou)) {
							tongzhi.setStatus(0);
							tongzhis.add(tongzhi);
							chatInfosDAO.save(tongzhi);
							tongZhiDAO.updateChatInfo(tongzhi);
							flag = true;
						} else {
							if (tongzhi.getTzType() == 3) {
								chatInfosDAO.save(tongzhi);
								Tongzhi tz = tongZhiDAO.isHasChatInfo(tongzhi.getUmid(), tongzhi.getAdditional());
								if (tz != null) {
									if (tz.getStatus() == 0) {
//										i++;
									}
									tongZhiDAO.updateChatInfo(tongzhi);							
								} else {								
									tongZhiDAO.save(tongzhi);							
//									i++;
								}						
							} else {							
								tongZhiDAO.save(tongzhi);
//								i++;
							}
						}
					}
					if (flag) {
						adapter.notifyDataSetChanged();
						msgListView.setSelection(tongzhis.size());
					}
//					TongZhiCountUtils.addTongZhiCount(TongZhiType3Activity.this, i);
				}
			}
		});
	}

	private void initMsgs() {
		Intent intent = getIntent();
		umidYou = intent.getStringExtra("umidYou");
		title_name.setText(intent.getStringExtra("title"));
		tongZhiDAO = new TongZhiDAO(TongZhiType3Activity.this); 
		chatInfosDAO = new ChatInfosDAO(TongZhiType3Activity.this); 
		tongzhis = chatInfosDAO.findLiaoTianByIds(userMain.getUmid(), umidYou);
	}
	
	
	class MsgAdapter extends ArrayAdapter<Tongzhi> {
		private int resourceId;
		public MsgAdapter(Context context, int textViewResourceId, List<Tongzhi> objects) {
			super(context, textViewResourceId, objects);
			resourceId = textViewResourceId;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Tongzhi tongzhi = getItem(position);
			View view;
			ViewHolder viewHolder;
			if (convertView == null) {
				view = LayoutInflater.from(getContext()).inflate(resourceId, null);
				viewHolder = new ViewHolder();
				viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
				viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
				viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
				viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			//别人给我发送的 应该显示在左边
			if (tongzhi.getUmid() == userMain.getUmid() && tongzhi.getTzid() != -1) {
				viewHolder.leftLayout.setVisibility(View.VISIBLE);
				viewHolder.rightLayout.setVisibility(View.GONE);
				viewHolder.leftMsg.setText(tongzhi.getTztext());
			} else if(("" + userMain.getUmid()).equals(tongzhi.getAdditional()) && tongzhi.getTzid() == -1){//我给别人发送的消息 显示在右边
				viewHolder.rightLayout.setVisibility(View.VISIBLE);
				viewHolder.leftLayout.setVisibility(View.GONE);
				viewHolder.rightMsg.setText(tongzhi.getTztext());
			}
			return view;
		}

		class ViewHolder {
			LinearLayout leftLayout;
			LinearLayout rightLayout;
			TextView leftMsg;
			TextView rightMsg;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRunning = false;
	}
}
