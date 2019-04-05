package com.xsy.superassociation.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xsy.superassociation.bean.ShetuanAdmin;
import com.xsy.superassociation.db.DBConnection;

public class SheTuanAdminDAO {
	private DBConnection dbConnection;
	private SQLiteDatabase db;
	private Cursor cursor;
	private Context context;
	public SheTuanAdminDAO(Context context) {
		this.context = context;
	}
	private void create(){
		dbConnection = new DBConnection(context);
		db = dbConnection.getWritableDatabase();
	}
	
	public boolean save(ShetuanAdmin sheTuanAdmin) {
		create();
		String sql = "insert into ShetuanAdmin(staid,stid,umid)"  
			+ "values(?,?,?)";
		try {
			db.execSQL(
					sql,
					new String[] { "" + sheTuanAdmin.getStaid(),
							"" + sheTuanAdmin.getStid(),
							"" + sheTuanAdmin.getUmid()});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	

	
	public boolean deleteAll() {
		create();
		String sql = "delete from ShetuanAdmin ";
		try {
			db.execSQL(sql,new String[] {});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	public boolean deleteByStaid(String staid) {
		create();
		String sql = "delete ShetuanAdmin where" +
			" staid = ? ";
		try {
			db.execSQL(sql,new String[] {staid});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public List<ShetuanAdmin> findByStid(String stid) {
		create();
		String sql = "select * from ShetuanAdmin where stid = ? ";
		cursor = db.rawQuery(sql, new String[]{stid});
		List<ShetuanAdmin> list = new ArrayList<ShetuanAdmin>();
		while (cursor.moveToNext()) {
			ShetuanAdmin sheTuanAdmin = new ShetuanAdmin(cursor.getInt(0), cursor.getInt(1),cursor.getInt(2));
			list.add(sheTuanAdmin);
		}
		close();
		return list;
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
