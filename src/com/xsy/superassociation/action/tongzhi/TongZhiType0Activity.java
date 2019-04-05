package com.xsy.superassociation.action.tongzhi;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.action.BaseActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.Tongzhi;

public class TongZhiType0Activity extends BaseActivity {
	@ViewInject(value=R.id.tongzhi_title)
	private TextView tongzhi_title;
	@ViewInject(value=R.id.tongzhi_date)
	private TextView tongzhi_date;
	@ViewInject(value=R.id.tongzhi_text)
	private TextView tongzhi_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tongzhi_putong);
		ViewUtils.inject(this);
		Tongzhi tongzhi = (Tongzhi) getIntent().getSerializableExtra("tongzhi");
		if (!TextUtils.isEmpty(tongzhi.getTztitle())) {
			tongzhi_title.setText(tongzhi.getTztitle());
		}
		if (!TextUtils.isEmpty(tongzhi.getTzdate())) {
			tongzhi_date.setText(tongzhi.getTzdate());
		}
		if (!TextUtils.isEmpty(tongzhi.getTztext())) {
			tongzhi_text.setText(tongzhi.getTztext());
		}
	}
}
