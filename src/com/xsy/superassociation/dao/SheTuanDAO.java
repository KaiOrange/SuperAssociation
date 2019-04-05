package com.xsy.superassociation.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xsy.superassociation.bean.SheTuan;
import com.xsy.superassociation.db.DBConnection;

public class SheTuanDAO {
	private DBConnection dbConnection;
	private SQLiteDatabase db;
	private Cursor cursor;
	private Context context;
	public SheTuanDAO(Context context) {
		this.context = context;
	}
	private void create(){
		dbConnection = new DBConnection(context);
		db = dbConnection.getWritableDatabase();
	}
	
	public boolean save(SheTuan sheTuan) {
		create();
		String sql = "insert into SheTuan(stid,sname,snotice,sdescribe,cjdate,touxiang,umid,umName)"  
			+ "values(?,?,?,?,?,?,?,?)";
		try {
			db.execSQL(
					sql,
					new String[] { sheTuan.getStid() + "", 
							sheTuan.getSname(),sheTuan.getSnotice(),
							sheTuan.getSdescribe(),sheTuan.getCjdate(),
							sheTuan.getTouxiang(),sheTuan.getUmid() + "",
							sheTuan.getUmName()});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public boolean update(SheTuan sheTuan) {
		create();
		System.out.println(sheTuan);
		String sql = "update SheTuan set sname = ?,snotice = ?,sdescribe = ?,touxiang = ?,umid = ?,umName = ? where stid = ?";
		try {
			db.execSQL(
					sql,
					new String[] { 
							sheTuan.getSname(),sheTuan.getSnotice(),
							sheTuan.getSdescribe(),
							sheTuan.getTouxiang(),sheTuan.getUmid() + "",
							sheTuan.getUmName(),
							sheTuan.getStid() + ""});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public boolean deleteSheTuan(String stid) {
		create();
		String sql = "delete from SheTuan where stid = ?";
		try {
			db.execSQL(sql,new String[] {stid});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public SheTuan findSheTuan(String stid) {
		create();
		SheTuan sheTuan = null;
		String sql = "select stid,sname,snotice,sdescribe,cjdate,touxiang,umid,umName from SheTuan where stid = ?";
		cursor = db.rawQuery(sql, new String[]{stid});
		if (cursor.moveToNext()) {
			sheTuan = new SheTuan(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),cursor.getString(7));
		}
		close();
		return sheTuan;
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
