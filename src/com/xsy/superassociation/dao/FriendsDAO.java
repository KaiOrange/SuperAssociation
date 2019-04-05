package com.xsy.superassociation.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.db.DBConnection;

public class FriendsDAO {
	private DBConnection dbConnection;
	private SQLiteDatabase db;
	private Cursor cursor;
	private Context context;
	public FriendsDAO(Context context) {
		this.context = context;
	}
	private void create(){
		dbConnection = new DBConnection(context);
		db = dbConnection.getWritableDatabase();
	}
	
	public boolean save(UserMain userMain,int umidMe) {
		create();
		String sql = "insert into Firends(umid,name,nichen,Touxiang,sex,age,brithday,telphone,myclass,stid,umidMe)"  
			+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			db.execSQL(
					sql,
					new String[] { userMain.getUmid() + "",userMain.getName(), userMain.getNichen(),
							userMain.getTouxiang(),
							userMain.isSex() + "",
							"" + userMain.getAge(), userMain.getBrithday(),
							userMain.getTelphone(), userMain.getMyclass(),userMain.getStid(),"" + umidMe });
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	
	public boolean deleteByUmidMe(int umidMe) {
		create();
		String sql = "delete from Firends where" +
			" umidMe = ? ";
		try {
			db.execSQL(sql,new String[] { "" + umidMe });
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	public boolean delete(int umid) {
		create();
		String sql = "delete from Firends where" +
				" umid = ? ";
		try {
			db.execSQL(sql,new String[] { "" + umid });
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public List<UserMain> findByUmidMe(int umidMe) {
		create();
//		String sql = "select * from Firends where umidMe = ? order by nichen ";
		String sql = "select * from Firends where umidMe = ? ";
		List<UserMain> list = new ArrayList<UserMain>();
		cursor = db.rawQuery(sql, new String[]{"" + umidMe});
		while (cursor.moveToNext()) {
			UserMain userMain = new UserMain(cursor.getInt(0), cursor.getString(1), "", cursor.getString(2), cursor.getString(3), Boolean.parseBoolean(cursor.getString(4)), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),cursor.getString(9));
			list.add(userMain);
		}
		close();
		return list;
	}
	public UserMain findByFriendUmid(int umid) {
		create();
		String sql = "select * from Firends where umid = ? ";
		cursor = db.rawQuery(sql, new String[]{"" + umid});
		UserMain userMain = null;
		if (cursor.moveToNext()) {
			userMain = new UserMain(cursor.getInt(0), cursor.getString(1), "", cursor.getString(2), cursor.getString(3), Boolean.parseBoolean(cursor.getString(4)), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),cursor.getString(9));
		}
		close();
		return userMain;
	}
	
	
	private void close(){
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		if (db != null) {
			db.close();
			db = null;
		}
		if (dbConnection != null) {
			dbConnection.close();
			dbConnection = null;
		}
	}
}
