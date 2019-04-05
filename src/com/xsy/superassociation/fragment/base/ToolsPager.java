package com.xsy.superassociation.fragment.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xsy.superassociation.action.JoinSheTuanActivity;
import com.xsy.superassociation.action.LoginActivity;
import com.xsy.superassociation.action.R;
import com.xsy.superassociation.action.UserMainDetailActivity;
import com.xsy.superassociation.action.shetuan.SheTuanTuiChuActivity;
import com.xsy.superassociation.action.xiakeliao.MyXiaKeLiaoActivity;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.ActivityCollector;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.MD5Utils;

public class ToolsPager extends BasePager{
	private LinearLayout tools_xiugaimima;
	private LinearLayout tools_guanyu;
	private LinearLayout tools_zhuxiao;
	private LinearLayout tools_jiarushetuan;
	private LinearLayout tools_tuichushetuan;
	private RelativeLayout tools_userMain;
	private ImageView touxiang;
	private TextView nicheng;
	private TextView name;
	private BitmapUtils bitmapUtils;
	private LinearLayout tools_myxiakeliao;

	public ToolsPager(Activity myActivity) {
		super(myActivity);
	}
	@Override
	public void initData() {
		super.initData();
		setScrollingTouch(true);
		View view = View.inflate(myActivity, R.layout.activity_tools, null);
		tools_xiugaimima = (LinearLayout) view.findViewById(R.id.tools_xiugaimima);
		tools_guanyu = (LinearLayout) view.findViewById(R.id.tools_guanyu);
		tools_zhuxiao = (LinearLayout) view.findViewById(R.id.tools_zhuxiao);
		tools_jiarushetuan = (LinearLayout) view.findViewById(R.id.tools_jiarushetuan);
		tools_tuichushetuan = (LinearLayout) view.findViewById(R.id.tools_tuichushetuan);
		tools_myxiakeliao = (LinearLayout) view.findViewById(R.id.tools_myxiakeliao);
		tools_userMain = (RelativeLayout) view.findViewById(R.id.tools_userMain);
		touxiang = (ImageView) view.findViewById(R.id.tools_touxiang);
		nicheng = (TextView) view.findViewById(R.id.tools_nicheng);
		name = (TextView) view.findViewById(R.id.tools_name);
		shetuan_framelayoLayout.addView(view);
		bitmapUtils = new BitmapUtils(myActivity);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//默认背景图片  
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//加载失败图片  
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型  
	    updateUserInfo();
		
		tools_xiugaimima.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
				View view = View.inflate(myActivity, R.layout.dialog_repwd, null);
				final TextView repwd = (TextView) view.findViewById(R.id.dialog_repwd_repwd);
				final TextView password = (TextView) view.findViewById(R.id.dialog_repwd_password);
				final TextView confimpwd = (TextView) view.findViewById(R.id.dialog_repwd_confimpwd);
				builder.setTitle("修改密码");
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String repwdStr = repwd.getText().toString();
						String passwordStr = password.getText().toString();
						String confimpwdStr = confimpwd.getText().toString();
						if (TextUtils.isEmpty(repwdStr)) {
							Toast.makeText(myActivity, "修改密码失败:原密码不能为空！", Toast.LENGTH_SHORT).show();
						} else if (TextUtils.isEmpty(passwordStr)) {
							Toast.makeText(myActivity, "修改密码失败:新密码不能为空！", Toast.LENGTH_SHORT).show();
						} else if (TextUtils.isEmpty(confimpwdStr)) {
							Toast.makeText(myActivity, "修改密码失败:没有确认密码!！", Toast.LENGTH_SHORT).show();
						} else if(!passwordStr.equals(confimpwdStr)){
							Toast.makeText(myActivity, "修改密码失败:两次输入的新密码不一致！", Toast.LENGTH_SHORT).show();
						} else if (passwordStr.length() > 20){
							Toast.makeText(myActivity, "修改密码失败:新密码的长度不能超过15！", Toast.LENGTH_SHORT).show();
						} else if (passwordStr.length() < 6){
							Toast.makeText(myActivity, "修改密码失败:新密码的长度不能少于6！", Toast.LENGTH_SHORT).show();
						} else {
							HttpUtils httpUtils = new HttpUtils();
							RequestParams params = new RequestParams();
							UserMain userMain = new UserMainDAO(myActivity).findUserMain();
							//设置密码MD5加密
							userMain.setPassword(MD5Utils.md5Password(passwordStr));
							params.addBodyParameter("userMain.umid", "" + userMain.getUmid());
							params.addBodyParameter("userMain.name", userMain.getName());
							params.addBodyParameter("userMain.password", userMain.getPassword());
							params.addBodyParameter("userMain.nichen", userMain.getNichen());
							params.addBodyParameter("userMain.sex", userMain.isSex() + "");
							params.addBodyParameter("userMain.age", userMain.getAge() + "");
							params.addBodyParameter("userMain.brithday", userMain.getBrithday());
							params.addBodyParameter("userMain.telphone", userMain.getTelphone());
							params.addBodyParameter("password", MD5Utils.md5Password(repwdStr));
							httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!repassword.action", params, new RequestCallBack<String>() {
								@Override
								public void onFailure(HttpException arg0, String arg1) {
									Toast.makeText(myActivity, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onSuccess(ResponseInfo<String> info) {
									String result = info.result;
									if ("seccess".equals(result)) {
										Toast.makeText(myActivity, "恭喜你,修改密码成功,请重新登录。。O(∩_∩)O~~", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(myActivity,
												LoginActivity.class);
										myActivity.startActivity(intent);
										myActivity.finish();
									} else if("passwor can't pass".equals(result)){
										Toast.makeText(myActivity, "密码不正确╮(╯_╰)╭", Toast.LENGTH_SHORT).show();										
									} else {
										Toast.makeText(myActivity, "修改密码失败╮(╯_╰)╭", Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
					}
				});
				builder.setView(view);
				builder.create();
				builder.show();
			}
		});
		tools_tuichushetuan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, SheTuanTuiChuActivity.class);
				myActivity.startActivity(intent);
			}
		});
		tools_myxiakeliao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, MyXiaKeLiaoActivity.class);
				myActivity.startActivity(intent);
			}
		});
		tools_zhuxiao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
				builder.setItems(new String[]{"注销账号","关闭软件"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {
							//注销账号
							Intent intent = new Intent(myActivity, LoginActivity.class);
							myActivity.startActivity(intent);
							myActivity.finish();
							UserMainDAO userMainDAO = new UserMainDAO(myActivity);
							userMainDAO.deleteUserMain(userMainDAO.findUserMain().getName());
						} else {
							ActivityCollector.finishAll();
						}
					}
				});
				builder.create();
				builder.show();
			}
		});
		tools_userMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, UserMainDetailActivity.class);
				myActivity.startActivity(intent);
			}
		});
		tools_jiarushetuan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(myActivity, JoinSheTuanActivity.class);
				myActivity.startActivity(intent);
			}
		});
		tools_guanyu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
				View v = View.inflate(myActivity, R.layout.about, null);
				TextView textView = (TextView) v.findViewById(R.id.about_close);
				builder.setView(v);
				builder.create();
				final AlertDialog dialog = builder.show();
				textView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
			}
		});
	}
	public void updateUserInfo() {
		userMain = new UserMainDAO(myActivity).findUserMain();
		if (userMain.getTouxiang() != null && touxiang != null) {
	    	bitmapUtils.display(touxiang, Global.BASE_TOUXIANG_URL + userMain.getTouxiang());
		}
		String s = "(未命名)";
		if (!TextUtils.isEmpty(userMain.getNichen())) {
			s = userMain.getNichen();
		}
		if (nicheng != null) {
			nicheng.setText(s);
		}
		String s2 = "(未获取到账号)";
		if (!TextUtils.isEmpty(userMain.getName())) {
			s2 = "账号：" + userMain.getName();
		}
		if (name != null) {
			name.setText(s2);
		}
	}
	@Override
	public void onRestart() {
		updateUserInfo();
		super.onRestart();
	}
}
