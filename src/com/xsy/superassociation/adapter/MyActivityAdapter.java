package com.xsy.superassociation.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.Activity;
import com.xsy.superassociation.util.DateConvert;

public class MyActivityAdapter extends BaseAdapter{
	private Context context;
	private List<Activity> activitys;
	private Calendar calendar1;
	private Calendar calendar2;
	
	public MyActivityAdapter(Context context, List<Activity> activitys) {
		super();
		this.context = context;
		this.activitys = activitys;
		calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		calendar2 = Calendar.getInstance();
	}
	@Override
	public int getCount() {
		return activitys.size();
	}
	@Override
	public Object getItem(int arg0) {
		return activitys.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (view == null) {
			view = View.inflate(context, R.layout.list_item_activity, null);
			viewHolder = new ViewHolder();
			viewHolder.item_atitle = (TextView) view.findViewById(R.id.item_atitle);
			viewHolder.item_sdate = (TextView) view.findViewById(R.id.item_sdate);
			viewHolder.item_edate = (TextView) view.findViewById(R.id.item_edate);
			viewHolder.item_adescribe = (TextView) view.findViewById(R.id.item_adescribe);
			viewHolder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		Activity activity = activitys.get(arg0);
		if (!TextUtils.isEmpty(activity.getAtitle())) {
			viewHolder.item_atitle.setText(activity.getAtitle());
		}
		if (!TextUtils.isEmpty(activity.getSdate())) {
			viewHolder.item_sdate.setText(activity.getSdate());				
		}
		if (!TextUtils.isEmpty(activity.getEdate())) {
			viewHolder.item_edate.setText(activity.getEdate());				
		}
		if (!TextUtils.isEmpty(activity.getAdescribe())) {
			viewHolder.item_adescribe.setText(activity.getAdescribe());				
		}
		calendar2.setTime(DateConvert.convertToDate(activity.getEdate()));
		if (calendar1.compareTo(calendar2) > 0) {
			viewHolder.item_layout.setBackgroundResource(R.drawable.activity_stop);
		} else {
			viewHolder.item_layout.setBackgroundResource(R.drawable.activity_run);
		}
		return view;
	}
}
class ViewHolder{
	TextView item_atitle;
	TextView item_sdate;
	TextView item_edate;
	TextView item_adescribe;
	LinearLayout item_layout;
}
