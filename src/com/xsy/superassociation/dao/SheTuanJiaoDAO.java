package com.xsy.superassociation.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xsy.superassociation.bean.SheTuanJiao;
import com.xsy.superassociation.db.DBConnection;

public class SheTuanJiaoDAO {
	private DBConnection dbConnection;
	private SQLiteDatabase db;
	private Cursor cursor;
	private Context context;
	public SheTuanJiaoDAO(Context context) {
		this.context = context;
	}
	private void create(){
		dbConnection = new DBConnection(context);
		db = dbConnection.getWritableDatabase();
	}
	
	public boolean save(SheTuanJiao sheTuanJiao) {
		create();
		String sql = "insert into SheTuanJiao(stjid,stjtext,stjsign,name)"  
				+ "values(?,?,?,?)";
		try {
			db.execSQL(
					sql,
					new String[] {"" + sheTuanJiao.getStjid(),sheTuanJiao.getStjtext(),sheTuanJiao.getStjsign(),sheTuanJiao.getName()});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	
	public boolean deleteAll() {
		create();
		String sql = "delete from SheTuanJiao ";
		try {
			db.execSQL(sql,new String[] {});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public SheTuanJiao findSheTuanJiao() {
		create();
		String sql = "select * from SheTuanJiao";
		cursor = db.rawQuery(sql, new String[]{});
		SheTuanJiao sheTuanJiao = null;
		if (cursor.moveToNext()) {
			sheTuanJiao = new SheTuanJiao(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		}
		close();
		return sheTuanJiao;
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
