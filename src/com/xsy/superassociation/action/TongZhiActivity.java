package com.xsy.superassociation.action;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.action.tongzhi.TongZhiType0Activity;
import com.xsy.superassociation.action.tongzhi.TongZhiType1Activity;
import com.xsy.superassociation.action.tongzhi.TongZhiType3Activity;
import com.xsy.superassociation.bean.ShetuanAdmin;
import com.xsy.superassociation.bean.Tongzhi;
import com.xsy.superassociation.dao.SheTuanAdminDAO;
import com.xsy.superassociation.dao.TongZhiDAO;
import com.xsy.superassociation.util.TongZhiCountUtils;

public class TongZhiActivity extends BaseActivity {
	@ViewInject(value=R.id.listView1)
	private ListView listView;
	private List<Tongzhi> tongzhis;
	private MyAdapter adapter;
	private TongZhiDAO tongZhiDAO;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tongzhi);
		ViewUtils.inject(this);
		tongZhiDAO = new TongZhiDAO(this);
		tongzhis = tongZhiDAO.findByUmid(userMain.getUmid());
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
	}
	
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return tongzhis.size();
		}
		@Override
		public Object getItem(int arg0) {
			return tongzhis.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (view == null) {
				view = View.inflate(TongZhiActivity.this, R.layout.list_item_tongzhi, null);
				viewHolder = new ViewHolder();
				viewHolder.tongzhi_title = (TextView) view.findViewById(R.id.tongzhi_title);
				viewHolder.tongzhi_text = (TextView) view.findViewById(R.id.tongzhi_text);
				viewHolder.tongzhi_date = (TextView) view.findViewById(R.id.tongzhi_date);
				viewHolder.tongzhi_layout = (LinearLayout) view.findViewById(R.id.tongzhi_layout);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			final Tongzhi tongzhi = tongzhis.get(arg0);
			if (!TextUtils.isEmpty(tongzhi.getTztitle())) {
				viewHolder.tongzhi_title.setText(tongzhi.getTztitle());
			}
			if (!TextUtils.isEmpty(tongzhi.getTztext())) {
				viewHolder.tongzhi_text.setText(tongzhi.getTztext());				
			}
			if (!TextUtils.isEmpty(tongzhi.getTzdate())) {
				viewHolder.tongzhi_date.setText(tongzhi.getTzdate());				
			}
			if (tongzhi.getStatus() == 1) {
				viewHolder.tongzhi_layout.setBackgroundResource(R.drawable.tongzhi_have);
			} else {
				viewHolder.tongzhi_layout.setBackgroundResource(R.drawable.tongzhi_empty);				
			}
			viewHolder.tongzhi_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View layout) {
					boolean status = tongZhiDAO.updateStatus(tongzhi.getTzid());
					if (status) {
						TongZhiCountUtils.addTongZhiCount(TongZhiActivity.this, -1);
						layout.setBackgroundResource(R.drawable.tongzhi_empty);						
					}
					switch (tongzhi.getTzType()) {
					case 0:{//普通通知
						Intent intent = new Intent(TongZhiActivity.this, TongZhiType0Activity.class);
						intent.putExtra("tongzhi", tongzhi);
						startActivity(intent);
						break;
					}
					case 1:{//加好友通知
						Intent intent = new Intent(TongZhiActivity.this, TongZhiType1Activity.class);
						intent.putExtra("tongzhi", tongzhi);
						startActivity(intent);
						break;
					}
					case 2:{//管理员审核通知
						if (!TextUtils.isEmpty(userMain.getStid())) {
							List<ShetuanAdmin> shetuanAdmins = new SheTuanAdminDAO(TongZhiActivity.this).findByStid(userMain.getStid());
							for (ShetuanAdmin shetuanAdmin : shetuanAdmins) {
								if (shetuanAdmin.getUmid() == userMain.getUmid()) {
									Intent intent = new Intent(TongZhiActivity.this, UserShenHeActivity.class);
									startActivity(intent);
									finish();
									return;
								}
							}							
							Toast.makeText(TongZhiActivity.this, "您的权限不足,不能处理该消息", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(TongZhiActivity.this, "您没有社团,不能处理该消息", Toast.LENGTH_SHORT).show();
						}
						break;
					}
					case 3:{//聊天通知
						Intent intent = new Intent(TongZhiActivity.this, TongZhiType3Activity.class);
						intent.putExtra("umidYou", tongzhi.getAdditional());
						intent.putExtra("title", tongzhi.getTztitle());
						startActivity(intent);
						break;
					}
					default:
						break;
					}
				}
			});
			return view;
		}
	}
	class ViewHolder{
		TextView tongzhi_title;
		TextView tongzhi_text;
		TextView tongzhi_date;
		LinearLayout tongzhi_layout;
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		tongzhis = tongZhiDAO.findByUmid(userMain.getUmid());
		adapter.notifyDataSetChanged();
	}

}
