package com.xsy.superassociation.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.global.Global;

public class WithCheckUserMainAdapter extends BaseAdapter {
	private Context context;
	private List<UserMain> userMains;
	private List<UserMain> checkedUsers = new ArrayList<UserMain>();
	private BitmapUtils bitmapUtils;
	
	public WithCheckUserMainAdapter(Context context, List<UserMain> userMains) {
		super();
		this.context = context;
		this.userMains = userMains;
		bitmapUtils = new BitmapUtils(context);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//Ĭ�ϱ���ͼƬ  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//����ʧ��ͼƬ  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//����ͼƬѹ������  
	}
	@Override
	public int getCount() {
		return userMains.size();
	}
	@Override
	public Object getItem(int arg0) {
		return userMains.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (view == null) {
			view = View.inflate(context, R.layout.list_item_user_shenhe, null);
			viewHolder = new ViewHolder();
			viewHolder.shehe_checkBox = (CheckBox) view.findViewById(R.id.shehe_checkBox);
			viewHolder.shehe_touxiang = (ImageView) view.findViewById(R.id.shehe_touxiang);
			viewHolder.shehe_nichen = (TextView) view.findViewById(R.id.shehe_nichen);
			viewHolder.shehe_loginname = (TextView) view.findViewById(R.id.shehe_loginname);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final UserMain currUserMain = userMains.get(arg0);
		viewHolder.shehe_loginname.setText("�˺ţ�" + currUserMain.getName());
		String s = "(δ��д)";
		if (!TextUtils.isEmpty(currUserMain.getNichen())) {
			s = currUserMain.getNichen();
		}
		viewHolder.shehe_nichen.setText(s);				
		if (!TextUtils.isEmpty(currUserMain.getTouxiang())) {
			bitmapUtils.display(viewHolder.shehe_touxiang,
					Global.BASE_TOUXIANG_URL + currUserMain.getTouxiang());
		} else {
			viewHolder.shehe_touxiang.setImageResource(R.drawable.touxiang_user);
		}
		viewHolder.shehe_checkBox.setChecked(false);
		viewHolder.shehe_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {//ѡ��
					checkedUsers.add(currUserMain);
				} else {//δѡ��
					checkedUsers.remove(currUserMain);
				}
			}
		});
		return view;
	}
	class ViewHolder{
		CheckBox shehe_checkBox;
		ImageView shehe_touxiang;
		TextView shehe_nichen;
		TextView shehe_loginname;
	}
	public List<UserMain> getCheckedUsers() {
		return checkedUsers;
	}
	
	
}
