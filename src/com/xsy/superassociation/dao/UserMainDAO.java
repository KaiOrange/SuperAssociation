package com.xsy.superassociation.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xsy.superassociation.bean.UserMain;
import com.xsy.superassociation.db.DBConnection;

public class UserMainDAO {
	private DBConnection dbConnection;
	private SQLiteDatabase db;
	private Cursor cursor;
	private Context context;
	public UserMainDAO(Context context) {
		this.context = context;
	}
	private void create(){
		dbConnection = new DBConnection(context);
		db = dbConnection.getWritableDatabase();
	}
	
	public boolean save(UserMain userMain) {
		create();
		String sql = "insert into UserMain(umid,name,nichen,Touxiang,sex,age,brithday,telphone,myclass,stid)"  
			+ "values(?,?,?,?,?,?,?,?,?,?)";
		try {
			db.execSQL(
					sql,
					new String[] { userMain.getUmid() + "",userMain.getName(), userMain.getNichen(),
							userMain.getTouxiang(),
							userMain.isSex() + "",
							"" + userMain.getAge(), userMain.getBrithday(),
							userMain.getTelphone(), userMain.getMyclass(),userMain.getStid() });
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public boolean update(UserMain userMain) {
		create();
		System.out.println(userMain);
		String sql = "update UserMain set name = ?,nichen = ?,Touxiang = ?,sex = ?,age = ?,brithday = ?,telphone = ?,myclass = ?,stid = ? where umid = ?";
		try {
			db.execSQL(
					sql,
					new String[] { userMain.getName(), userMain.getNichen(),
							userMain.getTouxiang(),
							userMain.isSex() + "",
							"" + userMain.getAge(), userMain.getBrithday(),
							userMain.getTelphone(), userMain.getMyclass(),userMain.getStid(),userMain.getUmid() + "" });
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public boolean deleteUserMain(String userName) {
		create();
		String sql = "delete from UserMain where" +
			" name = ? ";
		try {
			db.execSQL(sql,new String[] { userName });
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public UserMain findUserMain() {
		create();
		UserMain userMain = null;
		String sql = "select * from UserMain";
		cursor = db.rawQuery(sql, new String[]{});
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
