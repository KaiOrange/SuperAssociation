package com.xsy.superassociation.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.action.CreateSheTuanJiaoActivity;
import com.xsy.superassociation.action.MainActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.SheTuanJiao;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.SheTuanJiaoDAO;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;

public class LeftFragment extends BaseFragment{
	@ViewInject(R.id.left_touxiang)
	private ImageView left_touxiang;
	@ViewInject(R.id.left_write)
	private ImageView left_write;
	@ViewInject(R.id.left_reload)
	private ImageView left_reload;
	@ViewInject(R.id.imageView2)
	private ImageView imageView2;
	
	@ViewInject(R.id.left_name)
	private TextView left_name;
	
	@ViewInject(R.id.left_text)
	private TextView left_text;
	
	@ViewInject(R.id.left_sign)
	private TextView left_sign;

	private UserMain userMain;

	private BitmapUtils bitmapUtils;
	private SheTuanJiaoDAO sheTuanJiaoDAO;
	private SheTuanJiao sheTuanJiao;
	
	
	@Override
	public View initView() {
		View view = View.inflate(myActivity, R.layout.left_fregment_layout, null);
		ViewUtils.inject(this, view);
		bitmapUtils = new BitmapUtils(myActivity);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//ƒ¨»œ±≥æ∞Õº∆¨  
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//º”‘ÿ ß∞‹Õº∆¨  
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//…Ë÷√Õº∆¨—πÀı¿‡–Õ  
		left_write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, CreateSheTuanJiaoActivity.class);
				startActivity(intent);
				toggleSlidingMenu();
			}
		});
		left_reload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("name", "" +userMain.getName());
				httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanJiaoAction!reloadSheTuanJiao.action", params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						SheTuanJiao sheTuanJiao = new SheTuanJiao(0, "ƒæ”–Õ¯¬Á", "", "");
						updateSheTuanJiao(sheTuanJiao);
					}
					
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						if ("failure".equals(result)) {
							Toast.makeText(myActivity, "À¢–¬ ß∞‹¿≤®i©n®i...", Toast.LENGTH_SHORT).show();
						} else {
							SheTuanJiao sheTuanJiao = new Gson().fromJson(result, SheTuanJiao.class);
							sheTuanJiaoDAO.deleteAll();
							sheTuanJiaoDAO.save(sheTuanJiao);
							updateSheTuanJiao(sheTuanJiao);
						}
					}
				});
			}
		});
		sheTuanJiaoDAO = new SheTuanJiaoDAO(myActivity);
		sheTuanJiao = sheTuanJiaoDAO.findSheTuanJiao();
		if (sheTuanJiao != null) {
			updateSheTuanJiao(sheTuanJiao);
		}
		imageView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toggleSlidingMenu();
			}
		});
		return view;
	}
	private void updateSheTuanJiao(SheTuanJiao sheTuanJiao) {
		if (!TextUtils.isEmpty(sheTuanJiao.getStjtext())) {
			left_text.setText(sheTuanJiao.getStjtext());
		}
		if (!TextUtils.isEmpty(sheTuanJiao.getStjsign())) {
			left_sign.setText("°™°™" + sheTuanJiao.getStjsign());
		} else {
			left_sign.setText("");
		}
	}
	@Override
	public void initData() {
		userMain = new UserMainDAO(myActivity).findUserMain();
		if (userMain != null) {
			String s = "(Œ¥√¸√˚)";
			if (!TextUtils.isEmpty(userMain.getNichen())) {
				s = userMain.getNichen();
			}
			if (left_name != null) {
				left_name.setText(s);
			}
			if (userMain.getTouxiang() != null && left_touxiang != null) {
				bitmapUtils.display(left_touxiang, Global.BASE_TOUXIANG_URL
						+ userMain.getTouxiang());
			}
		}
		
	}
	
	public void toggleSlidingMenu(){
		MainActivity mainActivity = (MainActivity) myActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		slidingMenu.toggle();
	}
	/*public void setCurrentMenuDetailPager(int position){
		MainActivity mainActivity = (MainActivity) myActivity;;
		ContentFragment contentFragment = mainActivity.getContentMenu();
		SheTuanPager sheTuanPager = contentFragment.getSheTuanPager();
		sheTuanPager.setCurrentMenuPager(position);
	}*/
}
