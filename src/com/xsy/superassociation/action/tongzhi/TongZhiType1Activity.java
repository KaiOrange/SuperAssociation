package com.xsy.superassociation.action.tongzhi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.xsy.superassociation.bean.Tongzhi;
import com.xsy.superassociation.dao.TongZhiDAO;
import com.xsy.superassociation.global.Global;

public class TongZhiType1Activity extends BaseActivity {
	@ViewInject(value=R.id.tongzhi_title)
	private TextView tongzhi_title;
	@ViewInject(value=R.id.tongzhi_date)
	private TextView tongzhi_date;
	@ViewInject(value=R.id.tongzhi_text)
	private TextView tongzhi_text;
	@ViewInject(value=R.id.tongzhi_status)
	private TextView tongzhi_status;
	@ViewInject(value=R.id.tongzhi_button)
	private LinearLayout tongzhi_button;
	private Tongzhi tongzhi;
	private TongZhiDAO tongZhiDAO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tongzhi_friends);
		ViewUtils.inject(this);
		tongZhiDAO = new TongZhiDAO(this);
		tongzhi = (Tongzhi) getIntent().getSerializableExtra("tongzhi");
		tongzhi = tongZhiDAO.findByTzid(tongzhi.getTzid());
		if (!TextUtils.isEmpty(tongzhi.getTztitle())) {
			tongzhi_title.setText(tongzhi.getTztitle());
		}
		if (!TextUtils.isEmpty(tongzhi.getTzdate())) {
			tongzhi_date.setText(tongzhi.getTzdate());
		}
		if (!TextUtils.isEmpty(tongzhi.getTztext())) {
			tongzhi_text.setText(tongzhi.getTztext());
		}
		if ("-1".equals(tongzhi.getAdditional())) {
			tongzhi_status.setText("已确认");
		} else if ("-2".equals(tongzhi.getAdditional())){
			tongzhi_status.setText("已拒绝");			
		} else {
			tongzhi_status.setText("未处理");
			tongzhi_button.setVisibility(View.VISIBLE);
		}
	}
	
	public void refuseAddFriend(View v){
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		params.addBodyParameter("userName", tongzhi.getAdditional());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"friendsAction!refuseAddFriend.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(TongZhiType1Activity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(TongZhiType1Activity.this, "拒绝失败...", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
					Toast.makeText(TongZhiType1Activity.this, "您拒绝的好友已经找不到了", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TongZhiType1Activity.this, "成功拒绝该好友...", Toast.LENGTH_SHORT).show();
					Toast.makeText(TongZhiType1Activity.this, "成功添加该好友...", Toast.LENGTH_SHORT).show();
					tongzhi.setAdditional("-2");
					tongZhiDAO.update(tongzhi);
					finish();
				}
			}
		});
	}
	public void confimAddFriend(View v){
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("umid", "" + userMain.getUmid());
		params.addBodyParameter("userName", tongzhi.getAdditional());
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"friendsAction!confimAddFriend.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(TongZhiType1Activity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(TongZhiType1Activity.this, "同意失败...", Toast.LENGTH_SHORT).show();
				} else if("confimed".equals(result)){
					Toast.makeText(TongZhiType1Activity.this, "您已经同意过该好友申请", Toast.LENGTH_SHORT).show();
				} else if("notFriend".equals(result)){
					Toast.makeText(TongZhiType1Activity.this, "您同意的好友已经找不到了", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TongZhiType1Activity.this, "成功添加该好友...", Toast.LENGTH_SHORT).show();
					tongzhi.setAdditional("-1");
					tongZhiDAO.update(tongzhi);
					finish();
				}
			}
		});
	}
}
