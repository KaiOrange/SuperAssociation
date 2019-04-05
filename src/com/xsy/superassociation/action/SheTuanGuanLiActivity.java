package com.xsy.superassociation.action;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
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
import com.xsy.superassociation.action.activity.ActivityAdminActivity;
import com.xsy.superassociation.action.shetuan.JieSanZhuanRangActivity;
import com.xsy.superassociation.action.shetuan.SheTuanAdminGuanLiActivity;
import com.xsy.superassociation.action.shetuan.SheTuanSdescribeActivity;
import com.xsy.superassociation.action.shetuan.SheTuanSnoticeActivity;
import com.xsy.superassociation.action.shetuan.SheTuanTiChuUsersActivity;
import com.xsy.superassociation.action.tongzhi.SheTuanTongZhiActivity;
import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.dao.SheTuanDAO;
import com.xsy.superassociation.dao.UserMainDAO;
import com.xsy.superassociation.global.Global;

public class SheTuanGuanLiActivity extends BaseActivity {
	@ViewInject(value=R.id.admin_shenHe)
	private LinearLayout admin_shenHe;
	@ViewInject(value=R.id.shezhuLayout)
	private LinearLayout shezhuLayout;
	
	private Uri imageUri;
	public static final int CROP_PHOTO = 1;
	public static final int TAKE_PHOTO = 2;
	private String touxiangPath = "superAssociationSTTX.jpg";
	private SheTuan sheTuan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		ViewUtils.inject(this);
		sheTuan = new SheTuanDAO(this).findSheTuan(userMain.getStid());
		if (userMain.getUmid() == sheTuan.getUmid()) {
			shezhuLayout.setVisibility(View.VISIBLE);
		} else {
			shezhuLayout.setVisibility(View.GONE);			
		}
	}
	public void shenhe(View v){
		Intent intent = new Intent(this, UserShenHeActivity.class);
		startActivity(intent);
	}
	public void tichuUser(View v){
		Intent intent = new Intent(this, SheTuanTiChuUsersActivity.class);
		startActivity(intent);
	}
	public void updateSheTuanSdescrib(View v){
		Intent intent = new Intent(this, SheTuanSdescribeActivity.class);
		startActivity(intent);
	}
	public void updateSheTuanSnotice(View v){
		Intent intent = new Intent(this, SheTuanSnoticeActivity.class);
		startActivity(intent);		
	}
	public void gotoQunFaTongzhiPage(View v){
		Intent intent = new Intent(this, SheTuanTongZhiActivity.class);
		startActivity(intent);		
	}
	public void updateSheTuanTX(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(SheTuanGuanLiActivity.this);
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
	public void tuichu(View v){finish();}
	public void gotoActivityPage(View v){
		Intent intent = new Intent(this, ActivityAdminActivity.class);
		startActivity(intent);
	}
	public void gotoAdminList(View v){
		Intent intent = new Intent(this, SheTuanAdminGuanLiActivity.class);
		startActivity(intent);
	}
	public void gotoJieSanAndZhuanRang(View v){
		Intent intent = new Intent(this, JieSanZhuanRangActivity.class);
		startActivity(intent);
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
				updateSheTuanTXFromService();
			}
			break;
		default:
			break;
		}
	}
	private void updateSheTuanTXFromService() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		SheTuan sheTuan = new SheTuanDAO(this).findSheTuan(userMain.getStid());
		params.addBodyParameter("sheTuan.stid", "" + sheTuan.getStid());
		File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
		if (file.exists()) {
			params.addBodyParameter("sheTuan.pic", file);	
		}
		httpUtils.send(HttpMethod.POST, Global.BASE_URL+"sheTuanAction!updateSheTuanTX.action", params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(SheTuanGuanLiActivity.this, "����������i�n�i...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				if ("failure".equals(result)) {
					Toast.makeText(SheTuanGuanLiActivity.this, "��������ͷ��ʧ�����i�n�i...", Toast.LENGTH_SHORT).show();
				} else {
					SheTuan sheTuan = new Gson().fromJson(result, SheTuan.class);
					SheTuanDAO sheTuanDAO = new SheTuanDAO(SheTuanGuanLiActivity.this);
					sheTuanDAO.update(sheTuan);
					UserMainDAO userMainDAO = new UserMainDAO(SheTuanGuanLiActivity.this);
					UserMain findUserMain = userMainDAO.findUserMain();
					findUserMain.setStid("" + sheTuan.getStid());
					userMainDAO.update(findUserMain);
					Toast.makeText(SheTuanGuanLiActivity.this, "��������ͷ��ɹ�( ^_^ )", Toast.LENGTH_SHORT).show();
					deleteImage();
				}
			}
		});
	}
	private void deleteImage(){
		File file = new File(Environment. getExternalStorageDirectory(), touxiangPath);
		if (file.exists()) {
			file.delete();
		}
	}
}
