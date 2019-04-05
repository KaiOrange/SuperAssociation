package com.xsy.superassociation.action;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.xsy.superassociation.action.usermain.UserMainAgeActivity;
import com.xsy.superassociation.action.usermain.UserMainBirthdayActivity;
import com.xsy.superassociation.action.usermain.UserMainNiChenActivity;
import com.xsy.superassociation.action.usermain.UserMainSexActivity;
import com.xsy.superassociation.action.usermain.UserMainTelphoneActivity;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;

public class UserMainDetailActivity extends BaseActivity{
	@ViewInject(value=R.id.userMain_touxiang)
	private ImageView touxiang;
	@ViewInject(value=R.id.userMain_name)
	private TextView name;
	@ViewInject(value=R.id.userMain_nicheng)
	private TextView nicheng;
	@ViewInject(value=R.id.userMain_telphone)
	private TextView telphone;
	@ViewInject(value=R.id.userMain_sex)
	private TextView sex;
	@ViewInject(value=R.id.userMain_age)
	private TextView age;
	@ViewInject(value=R.id.userMain_birthday)
	private TextView birthday;
	@ViewInject(value=R.id.userMain_exit)
	private ImageView exit;
	//布局
	@ViewInject(value=R.id.userMain_layout_touxiang)
	private RelativeLayout layout_touxiang;
	@ViewInject(value=R.id.userMain_layout_nichen)
	private RelativeLayout layout_nichen;
	@ViewInject(value=R.id.userMain_layout_telphone)
	private RelativeLayout layout_telphone;
	@ViewInject(value=R.id.userMain_layout_sex)
	private RelativeLayout layout_sex;
	@ViewInject(value=R.id.userMain_layout_age)
	private RelativeLayout layout_age;
	@ViewInject(value=R.id.userMain_layout_birthday)
	private RelativeLayout layout_birthday;
	
	
	private UserMain userMain;
	
	private Uri imageUri;
	public static final int CROP_PHOTO = 1;
	public static final int TAKE_PHOTO = 2;
	private String touxiangPath = "superAssociationTX.jpg";
	private BitmapUtils bitmapUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usermain);
		ViewUtils.inject(this);
		bitmapUtils = new BitmapUtils(this);  
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//默认背景图片  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//加载失败图片  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型  
    	
		updateUserMain();
		
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		layout_touxiang.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(UserMainDetailActivity.this);
				builder.setItems(new String[]{"拍       照","本地图库"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {
							//调用相机
							//创建File对象，用于存储拍照后的图片  
			                //将此图片存储于SD卡的根目录下  
			                File outputImage = new File(Environment.getExternalStorageDirectory(),  
			                        touxiangPath);  
			                try {  
			                    if (outputImage.exists()) {  
			                        outputImage.delete();  
			                    }  
			                    outputImage.createNewFile();  
			                } catch (IOException e) {  
			                    e.printStackTrace();  
			                }  
			                //将File对象转换成Uri对象  
			                //Uri表标识着图片的地址  
			                imageUri = Uri.fromFile(outputImage);  
			                //隐式调用照相机程序  
			                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");  
			                //拍下的照片会被输出到output_image.jpg中去  
			                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
			                //此处是使用的startActivityForResult（）  
			                //因此在拍照完后悔有结果返回到onActivityResult（）中去，返回值即为<span style="font-size: 13.3333px; font-family: Arial, Helvetica, sans-serif;">CUT_PICTURE</span>  
			                //onActivityResult（）中主要是实现图片裁剪  
			                startActivityForResult(intent, TAKE_PHOTO); 
						} else if(arg1 == 1) {
							//调用图册中的图片:
							File outputImage = new File(Environment.getExternalStorageDirectory(),  
			                        touxiangPath);  
			                try {  
			                    if (outputImage.exists()) {  
			                        outputImage.delete();  
			                    }  
			                    outputImage.createNewFile();  
			                } catch (IOException e) {  
			                    e.printStackTrace();  
			                }  
			                imageUri = Uri.fromFile(outputImage);  
			                Intent intent = new Intent(Intent.ACTION_PICK,null);  
			                //此处调用了图片选择器  
			                //如果直接写intent.setDataAndType("image/*");  
			                //调用的是系统图库  
			                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
			                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
			                startActivityForResult(intent, TAKE_PHOTO);  
						}
					}
				});
				builder.create();
				builder.show();
			}
		});
		layout_nichen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserMainDetailActivity.this, UserMainNiChenActivity.class);
				startActivity(intent);
			}
		});
		layout_telphone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserMainDetailActivity.this, UserMainTelphoneActivity.class);
				startActivity(intent);
			}
		});
		layout_sex.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserMainDetailActivity.this, UserMainSexActivity.class);
				startActivity(intent);
			}
		});
		layout_age.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserMainDetailActivity.this, UserMainAgeActivity.class);
				startActivity(intent);
			}
		});
		layout_birthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserMainDetailActivity.this, UserMainBirthdayActivity.class);
				startActivity(intent);
			}
		});
	}

	private void updateUserMain() {
		userMain = new UserMainDAO(this).findUserMain();
		if (userMain.getTouxiang() != null) {
	    	bitmapUtils.display(touxiang, Global.BASE_TOUXIANG_URL + userMain.getTouxiang());
		}
		name.setText(userMain.getName());
		if (TextUtils.isEmpty(userMain.getNichen())) {
			nicheng.setText("(未填写)");
			nicheng.setTextColor(Color.DKGRAY);
		} else {
			nicheng.setText(userMain.getNichen());			
		}
		if (TextUtils.isEmpty(userMain.getTelphone())) {
			telphone.setText("(未填写)");
			telphone.setTextColor(Color.DKGRAY);
		} else {
			telphone.setText(userMain.getTelphone());
		}
		sex.setText(userMain.isSex() ? "男":"女");
		age.setText("" + userMain.getAge());
	
		if (TextUtils.isEmpty(userMain.getBrithday())) {
			birthday.setText("(未填写)");
			birthday.setTextColor(Color.DKGRAY);
		} else {
			String s = userMain.getBrithday().split(" ")[0];
			birthday.setText(s);
		}
	}
	@Override
	protected void onRestart() {
		super.onStart();
		updateUserMain();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(data.getData(), "image/*"); 
				intent.putExtra("scale", true);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				// outputX , outputY : 裁剪图片宽高
				intent.putExtra("outputX", 600);
				intent.putExtra("outputY", 600);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				
					if (userMain == null) {
						userMain = new UserMainDAO(UserMainDetailActivity.this).findUserMain();
					}
					HttpUtils httpUtils = new HttpUtils();
					RequestParams params = new RequestParams();
					params.addBodyParameter("userMain.umid", "" + userMain.getUmid());
					params.addBodyParameter("userMain.name", userMain.getName());
					if (!TextUtils.isEmpty(userMain.getPassword())) {
						params.addBodyParameter("userMain.password",
								userMain.getPassword());
					}
					if (!TextUtils.isEmpty(userMain.getNichen())) {
						params.addBodyParameter("userMain.nichen",
								userMain.getNichen());
					}
					params.addBodyParameter("userMain.sex", userMain.isSex() + "");
					params.addBodyParameter("userMain.age", userMain.getAge() + "");
					if (!TextUtils.isEmpty(userMain.getBrithday())) {
						params.addBodyParameter("userMain.brithday",
								userMain.getBrithday());
					}
					if (!TextUtils.isEmpty(userMain.getTelphone())) {
						params.addBodyParameter("userMain.telphone",
								userMain.getTelphone());
					}
					File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
					if (file.exists()) {
						params.addBodyParameter("userMain.pic", file);	
					}
					httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!updateTouXiang.action", params, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(UserMainDetailActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							String result = info.result;
							if (!"failure".equals(result)) {
								Toast.makeText(UserMainDetailActivity.this, "恭喜你,修改头像成功!!\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
								UserMain myUserMain = new Gson().fromJson(result, UserMain.class);
								new UserMainDAO(UserMainDetailActivity.this).update(myUserMain);
								
						    	bitmapUtils.display(touxiang, Global.BASE_TOUXIANG_URL + myUserMain.getTouxiang());
								
//									BitmapFactory.Options options = new BitmapFactory.Options();
//									options.inScaled = true;
//									options.inSampleSize = 6;
//									Bitmap bitmap = BitmapFactory.decodeStream (UserMainDetailActivity.this.getContentResolver()
//											.openInputStream(imageUri),null,options);
//									touxiang.setImageBitmap(bitmap);// 将裁剪后的照片显示出来
							} else {
								Toast.makeText(UserMainDetailActivity.this, "修改头像失败。。", Toast.LENGTH_SHORT).show();
							}
						}
					});
			}
			break;
		default:
			break;
		}
	}
	
}
