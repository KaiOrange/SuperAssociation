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
	//����
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
		bitmapUtils.configDefaultLoadingImage(R.drawable.touxiang_user);//Ĭ�ϱ���ͼƬ  
    	bitmapUtils.configDefaultLoadFailedImage(R.drawable.touxiang_user);//����ʧ��ͼƬ  
    	bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//����ͼƬѹ������  
    	
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
				builder.setItems(new String[]{"��       ��","����ͼ��"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {
							//�������
							//����File�������ڴ洢���պ��ͼƬ  
			                //����ͼƬ�洢��SD���ĸ�Ŀ¼��  
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
			                //��File����ת����Uri����  
			                //Uri���ʶ��ͼƬ�ĵ�ַ  
			                imageUri = Uri.fromFile(outputImage);  
			                //��ʽ�������������  
			                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");  
			                //���µ���Ƭ�ᱻ�����output_image.jpg��ȥ  
			                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
			                //�˴���ʹ�õ�startActivityForResult����  
			                //��������������н�����ص�onActivityResult������ȥ������ֵ��Ϊ<span style="font-size: 13.3333px; font-family: Arial, Helvetica, sans-serif;">CUT_PICTURE</span>  
			                //onActivityResult��������Ҫ��ʵ��ͼƬ�ü�  
			                startActivityForResult(intent, TAKE_PHOTO); 
						} else if(arg1 == 1) {
							//����ͼ���е�ͼƬ:
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
			                //�˴�������ͼƬѡ����  
			                //���ֱ��дintent.setDataAndType("image/*");  
			                //���õ���ϵͳͼ��  
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
			nicheng.setText("(δ��д)");
			nicheng.setTextColor(Color.DKGRAY);
		} else {
			nicheng.setText(userMain.getNichen());			
		}
		if (TextUtils.isEmpty(userMain.getTelphone())) {
			telphone.setText("(δ��д)");
			telphone.setTextColor(Color.DKGRAY);
		} else {
			telphone.setText(userMain.getTelphone());
		}
		sex.setText(userMain.isSex() ? "��":"Ů");
		age.setText("" + userMain.getAge());
	
		if (TextUtils.isEmpty(userMain.getBrithday())) {
			birthday.setText("(δ��д)");
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
				// outputX , outputY : �ü�ͼƬ���
				intent.putExtra("outputX", 600);
				intent.putExtra("outputY", 600);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO); // �����ü�����
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
							Toast.makeText(UserMainDetailActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							String result = info.result;
							if (!"failure".equals(result)) {
								Toast.makeText(UserMainDetailActivity.this, "��ϲ��,�޸�ͷ��ɹ�!!\\(^o^)/YES!", Toast.LENGTH_SHORT).show();
								UserMain myUserMain = new Gson().fromJson(result, UserMain.class);
								new UserMainDAO(UserMainDetailActivity.this).update(myUserMain);
								
						    	bitmapUtils.display(touxiang, Global.BASE_TOUXIANG_URL + myUserMain.getTouxiang());
								
//									BitmapFactory.Options options = new BitmapFactory.Options();
//									options.inScaled = true;
//									options.inSampleSize = 6;
//									Bitmap bitmap = BitmapFactory.decodeStream (UserMainDetailActivity.this.getContentResolver()
//											.openInputStream(imageUri),null,options);
//									touxiang.setImageBitmap(bitmap);// ���ü������Ƭ��ʾ����
							} else {
								Toast.makeText(UserMainDetailActivity.this, "�޸�ͷ��ʧ�ܡ���", Toast.LENGTH_SHORT).show();
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
