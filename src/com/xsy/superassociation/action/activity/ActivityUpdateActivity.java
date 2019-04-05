package com.xsy.superassociation.action.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.xsy.superassociation.bean.Activity;
import com.xsy.superassociation.dao.ActivityDAO;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.DateConvert;
import com.xsy.superassociation.util.IntUtils;

public class ActivityUpdateActivity extends BaseActivity {
	@ViewInject(value=R.id.create_activity_atitle)
	private TextView activity_atitle;
	@ViewInject(value=R.id.create_activity_sdate)
	private EditText activity_sdate;
	@ViewInject(value=R.id.create_activity_edate)
	private EditText activity_edate;
	@ViewInject(value=R.id.create_activity_adescribe)
	private EditText activity_adescribe;
	
	@ViewInject(value=R.id.create_activity_view_atitle)
	private View view_atitle;
	@ViewInject(value=R.id.create_activity_view_sdate)
	private View view_sdate;
	@ViewInject(value=R.id.create_activity_view_edate)
	private View view_edate;
	@ViewInject(value=R.id.create_activity_view_adescribe)
	private View view_adescribe;
	
	private String sdate;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_update);
		ViewUtils.inject(this);
		activity = (Activity) getIntent().getSerializableExtra("activity");
		activity_atitle.setText(activity.getAtitle());
		activity_adescribe.setText(activity.getAdescribe());
		final EditText[] editTexts = new EditText[3];
		editTexts[0] = activity_sdate;
		editTexts[1] = activity_edate;
		editTexts[2] = activity_adescribe;
		final View[] views = new View[3];
		views[0] = view_sdate;
		views[1] = view_edate;
		views[2] = view_adescribe;
		
		for (int i = 0; i < editTexts.length; i++) {
			final int k = i;
			editTexts[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					for (int j = 0; j < views.length; j++) {							
						views[j].setBackgroundColor(Color.GRAY);
					}
					views[k].setBackgroundColor(Color.RED);
				}
			});
		}
		
		
		sdate = activity.getSdate();
		activity_sdate.setText(sdate);
		final Calendar calendar1 = Calendar.getInstance();
		
		activity_sdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dp = new DatePickerDialog(
						ActivityUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, final int arg1,
									final int arg2, final int arg3) {
								Date date = null;
								try {
									date = DateConvert.convertToDate(activity_sdate.getText().toString());
								} catch (Exception e) {
									date = new Date();
								}
								calendar1.setTime(date);
								TimePickerDialog tp = new TimePickerDialog(ActivityUpdateActivity.this,new TimePickerDialog.OnTimeSetListener() {
									@Override
									public void onTimeSet(TimePicker arg0, int h, int m) {
										sdate = arg1 + "-" + (arg2 + 1) + "-"
												+ arg3 + " " + IntUtils.to2String(h) + ":" + IntUtils.to2String(m) + ":00";
										activity_sdate.setText(sdate);
									}
								},calendar1.get(Calendar.HOUR_OF_DAY),calendar1.get(Calendar.MINUTE),true);
								tp.show();
							}
						}, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
				dp.show();
			}
		});
		activity_edate.setText(activity.getEdate());
		final Calendar calendar2 = Calendar.getInstance();
		activity_edate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Date date2 = null;
				try {
					date2 = DateConvert.convertToDate(activity_edate.getText().toString());
				} catch (Exception e) {
					date2 = new Date();
				}
				calendar2.setTime(date2);
				DatePickerDialog dp = new DatePickerDialog(
						ActivityUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, final int arg1,
									final int arg2, final int arg3) {
								TimePickerDialog tp = new TimePickerDialog(ActivityUpdateActivity.this,new TimePickerDialog.OnTimeSetListener() {
									@Override
									public void onTimeSet(TimePicker arg0, int h, int m) {
										sdate = arg1 + "-" + (arg2 + 1) + "-"
												+ arg3 + " " + IntUtils.to2String(h) + ":" + IntUtils.to2String(m) + ":00";
										activity_edate.setText(sdate);
									}
								},calendar2.get(Calendar.HOUR_OF_DAY),calendar2.get(Calendar.MINUTE),true);
								tp.show();
							}
						}, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
				dp.show();
			}
		});
	}
	public void complete(View v){
		String sdateStr = activity_sdate.getText().toString();
		String edateStr = activity_edate.getText().toString();
		String adescribeStr = activity_adescribe.getText().toString();
		if (TextUtils.isEmpty(sdateStr)
				|| TextUtils.isEmpty(edateStr) || TextUtils.isEmpty(adescribeStr)) {
			Toast.makeText(this, "所有数据不能为空", Toast.LENGTH_SHORT).show();
			return;
		} 
		try {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(DateConvert.convertToDate(sdateStr));
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(DateConvert.convertToDate(edateStr));
			if(calendar1.compareTo(calendar2) > 0) {
				Toast.makeText(this, "开始日期不能晚于结果日期", Toast.LENGTH_SHORT).show();			
				return;
			}
		} catch (Exception e) {
			Toast.makeText(this, "日期格式有误...", Toast.LENGTH_SHORT).show();			
			return;
		}
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("activity.aid", "" + activity.getAid());
		params.addBodyParameter("activity.stid", "" + activity.getStid());
		params.addBodyParameter("activity.atitle", activity.getAtitle());
		params.addBodyParameter("activity.sdate", sdateStr);
		params.addBodyParameter("activity.edate", edateStr);
		params.addBodyParameter("activity.adescribe", adescribeStr);
		activity.setSdate(sdateStr);
		activity.setEdate(sdateStr);
		activity.setAdescribe(sdateStr);
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"activityAction!updateActivity.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(ActivityUpdateActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(ActivityUpdateActivity.this, "修改活动失败...", Toast.LENGTH_SHORT).show();
				} else if("notFound".equals(result)) {
					Toast.makeText(ActivityUpdateActivity.this, "系统没有找到你所属的社团", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ActivityUpdateActivity.this, "修改活动成功", Toast.LENGTH_SHORT).show();
					new ActivityDAO(ActivityUpdateActivity.this).update(activity, userMain.getUmid());
					finish();
				}
			}
		});
	}
	
}
