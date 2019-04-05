package com.xsy.superassociation.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;
import com.xsy.superassociation.util.ShowDialog;

public class CreateSheTuanActivity extends BaseActivity {
	@ViewInject(value=R.id.create_shetuan_sname)
	private EditText shetuan_sname;
	@ViewInject(value=R.id.create_shetuan_snotice)
	private EditText shetuan_snotice;
	@ViewInject(value=R.id.create_shetuan_sdescribe)
	private EditText shetuan_sdescribe;
	@ViewInject(value=R.id.create_shetuan_touxiang)
	private ImageView shetuan_touxiang;
	@ViewInject(value=R.id.create_shetuan_view_sname)
	private View view_sname;
	@ViewInject(value=R.id.create_shetuan_view_snotice)
	private View view_snotice;
	@ViewInject(value=R.id.create_shetuan_view_sdescribe)
	private View view_sdescribe;
	
	private Uri imageUri;
	public static final int CROP_PHOTO = 1;
	public static final int TAKE_PHOTO = 2;
	private String touxiangPath = "superAssociationSTTX.jpg";
	private ProgressDialog dialogProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_shetuan);
		ViewUtils.inject(this);
		dialogProgress = ShowDialog.getDialogProgress(this, "创建社团", "正在创建社团");
		final EditText[] editTexts = new EditText[3];
		editTexts[0] = shetuan_sname;
		editTexts[1] = shetuan_snotice;
		editTexts[2] = shetuan_sdescribe;
		final View[] views = new View[3];
		views[0] = view_sname;
		views[1] = view_snotice;
		views[2] = view_sdescribe;
		
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
		shetuan_touxiang.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for (int j = 1; j < views.length; j++) {							
					views[j].setBackgroundColor(Color.GRAY);
				}
				views[0].setBackgroundColor(Color.RED);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(CreateSheTuanActivity.this);
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
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inScaled = true;
					options.inSampleSize = 6;
					Bitmap bitmap = BitmapFactory.decodeStream (getContentResolver()
							.openInputStream(imageUri),null,options);
					shetuan_touxiang.setImageBitmap(bitmap);// 将裁剪后的照片显示出来
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
	public void tuichu(View v){
		finish();
	}
	public void complete(View v){
		String snameStr = shetuan_sname.getText().toString();
		String snoticeStr = shetuan_snotice.getText().toString();
		String sdescribeStr = shetuan_sdescribe.getText().toString();
		if (TextUtils.isEmpty(snameStr)) {
			Toast.makeText(CreateSheTuanActivity.this, "社团名称不能为空!!!", Toast.LENGTH_SHORT).show();
			return;
		}
		dialogProgress.show();
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sheTuan.sname", "" + snameStr);
		params.addBodyParameter("sheTuan.snotice", "" + snoticeStr);
		params.addBodyParameter("sheTuan.sdescribe", "" + sdescribeStr);
		params.addBodyParameter("sheTuan.umid", "" + userMain.getUmid());
		File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
		if (file.exists()) {
			params.addBodyParameter("sheTuan.pic", file);	
		}
		
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!createSheTuan.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(CreateSheTuanActivity.this, "网络出错啦╥﹏╥...", Toast.LENGTH_SHORT).show();
				ShowDialog.dismissDialog(dialogProgress);
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(CreateSheTuanActivity.this, "创建失败啦╥﹏╥...", Toast.LENGTH_SHORT).show();
				} else if("created".equals(result)){
					Toast.makeText(CreateSheTuanActivity.this, "改社团名已经被注册", Toast.LENGTH_SHORT).show();					
				} else if("joined".equals(result)){
					Toast.makeText(CreateSheTuanActivity.this, "您已经加入社团了", Toast.LENGTH_SHORT).show();					
				}else {
					SheTuan sheTuan = new Gson().fromJson(result, SheTuan.class);
					SheTuanDAO sheTuanDAO = new SheTuanDAO(CreateSheTuanActivity.this);
					sheTuanDAO.save(sheTuan);
					UserMainDAO userMainDAO = new UserMainDAO(CreateSheTuanActivity.this);
					UserMain findUserMain = userMainDAO.findUserMain();
					findUserMain.setStid("" + sheTuan.getStid());
					userMainDAO.update(findUserMain);
					Toast.makeText(CreateSheTuanActivity.this, "创建社团成功( ^_^ )", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(CreateSheTuanActivity.this,
							MainActivity.class);
					startActivity(intent);
					deleteImage();
				}
				ShowDialog.dismissDialog(dialogProgress);
			}
		});
		
		
	}
	private void deleteImage(){
		File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
		if (file.exists()) {
			file.delete();
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
