package com.xsy.superassociation.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.ShowDialog;

@SuppressLint("SimpleDateFormat")
public class Register2Activity extends BaseActivity{
	@ViewInject(value=R.id.register_nicheng)
	private EditText nicheng;
	@ViewInject(value=R.id.register_sex_radioGroup)
	private RadioGroup sex_radioGroup;
	@ViewInject(value=R.id.register_age)
	private EditText age;
	@ViewInject(value=R.id.register_birthday)
	private EditText birthday;
	@ViewInject(value=R.id.register_phone)
	private EditText phone;
	
	@ViewInject(value=R.id.register_view_nicheng)
	private View view_nicheng;
	@ViewInject(value=R.id.register_view_sex)
	private View view_sex;
	@ViewInject(value=R.id.register_view_age)
	private View view_age;
	@ViewInject(value=R.id.register_view_birthday)
	private View view_birthday;
	@ViewInject(value=R.id.register_view_phone)
	private View view_phone;
	
	@ViewInject(value=R.id.login_btn)
	private Button login_btn;
	@ViewInject(value=R.id.register_touxiang)
	private ImageView touxiang;
	
	private boolean sex;
	private UserMain userMain;
	private Uri imageUri;
	public static final int CROP_PHOTO = 1;
	public static final int TAKE_PHOTO = 2;
	private String touxiangPath = "superAssociationTX.jpg";
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register2);
		ViewUtils.inject(this);
		userMain = (UserMain) getIntent().getSerializableExtra("userMain");
		dialog = ShowDialog.getDialogProgress(this, "��д��Ϣ", "���ڱ�����Ϣ...");
		
		final EditText[] editTexts = new EditText[4];
		editTexts[0] = nicheng;
		editTexts[1] = age;
		editTexts[2] = birthday;
		editTexts[3] = phone;
		final View[] views = new View[4];
		views[0] = view_nicheng;
		views[1] = view_age;
		views[2] = view_birthday;
		views[3] = view_phone;
		
		for (int i = 0; i < editTexts.length; i++) {
			final int k = i;
			editTexts[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					for (int j = 0; j < 4; j++) {							
						views[j].setBackgroundColor(Color.GRAY);
					}
					view_sex.setBackgroundColor(Color.GRAY);
					views[k].setBackgroundColor(Color.RED);
				}
			});
		}
		sex_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.radio1) {
					sex = false;
				} else {
					sex = true;
				}
				for (int j = 0; j < 4; j++) {							
					views[j].setBackgroundColor(Color.GRAY);
				}
				view_sex.setBackgroundColor(Color.RED);
//				login_btn.setFocusable(true);
//				login_btn.requestFocus();
			}
		});
//		birthday.setText("1990-1-1");
		birthday.setHint("���磺"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		birthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dp = new DatePickerDialog(
						Register2Activity.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								String sdate = arg1 + "-" + (arg2 + 1) + "-"
										+ arg3;
								birthday.setText(sdate);
							}
						}, 1990, 0, 1);
				dp.show();
			}
		});
		touxiang.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for (int j = 1; j < 4; j++) {							
					views[j].setBackgroundColor(Color.GRAY);
				}
				views[0].setBackgroundColor(Color.RED);
				editTexts[0].setFocusable(true);
				editTexts[0].requestFocus();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(Register2Activity.this);
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
		
	}
	
	public void completeRegister(View v){
		try {
			new SimpleDateFormat("yyyy-MM-dd").parse(birthday.getText().toString());
		} catch (ParseException e) {
			e.printStackTrace();
			Toast.makeText(Register2Activity.this, "���ڸ�ʽ����ȷ,���޸ĺ����ύ..", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog.show();
		String nichengStr = nicheng.getText().toString();
		userMain.setAge(Integer.parseInt(age.getText().toString()));
		userMain.setNichen(nichengStr);
		userMain.setSex(sex);
		userMain.setBrithday(birthday.getText().toString());
		userMain.setTelphone(phone.getText().toString());
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
//		params.addBodyParameter("userGson", new Gson().toJson(userMain));
		params.addBodyParameter("userMain.umid", "" + userMain.getUmid());
		params.addBodyParameter("userMain.name", userMain.getName());
		params.addBodyParameter("userMain.password", userMain.getPassword());
		params.addBodyParameter("userMain.nichen", userMain.getNichen());
		params.addBodyParameter("userMain.sex", userMain.isSex() + "");
		params.addBodyParameter("userMain.age", userMain.getAge() + "");
		params.addBodyParameter("userMain.brithday", userMain.getBrithday());
		params.addBodyParameter("userMain.telphone", userMain.getTelphone());
		File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
		if (file.exists()) {
			params.addBodyParameter("userMain.pic", file);	
		}
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"loginAction!updateUserMain.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(Register2Activity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
				ShowDialog.dismissDialog(dialog);
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("seccess".equals(result)) {
					Toast.makeText(Register2Activity.this, "��ϲ��,������������д���\\(^o^)/YES!", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Register2Activity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(Register2Activity.this, "��д��Ϣʧ�ܡ���", Toast.LENGTH_LONG).show();
				}
				ShowDialog.dismissDialog(dialog);
				File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
				if (file.exists()) {
					file.delete();
				}
			}
		});
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
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inScaled = true;
					options.inSampleSize = 6;
					Bitmap bitmap = BitmapFactory.decodeStream (getContentResolver()
							.openInputStream(imageUri),null,options);
					touxiang.setImageBitmap(bitmap);// ���ü������Ƭ��ʾ����
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
		if (file.exists()) {
			file.delete();
		}
	}
}
