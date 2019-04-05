package com.xsy.superassociation.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xsy.superassociation.bean.Tongzhi;
import com.xsy.superassociation.db.DBConnection;

public class ChatInfosDAO {
	private DBConnection dbConnection;
	private SQLiteDatabase db;
	private Cursor cursor;
	private Context context;
	public ChatInfosDAO(Context context) {
		this.context = context;
	}
	private void create(){
		dbConnection = new DBConnection(context);
		db = dbConnection.getWritableDatabase();
	}
	
	public boolean save(Tongzhi tongzhi) {
		create();
		String sql = "insert into ChatInfos(tzid,tztext,tztitle,umid,tzdate,status,tzType,additional)"  
			+ "values(?,?,?,?,?,?,?,?)";
		try {
			db.execSQL(
					sql,
					new String[] {""+tongzhi.getTzid(), tongzhi.getTztext(),
							tongzhi.getTztitle(),""+tongzhi.getUmid(),
							tongzhi.getTzdate(),""+tongzhi.getStatus(),
							"" + tongzhi.getTzType(),tongzhi.getAdditional()});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public boolean updateStatus(int tzid) {
		create();
		String sql = "update ChatInfos set status = 0 where tzid = ?";
		try {
			db.execSQL(
					sql,
					new String[] { 
							""+tzid});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public boolean deleteLiaoTianByUmid(int umid,String additional) {
		create();
		String sql = "delete from ChatInfos where umid = ? and additional = ? and tzType = 3";
		try {
			db.execSQL(sql,new String[] {"" + umid,additional});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	
	public List<Tongzhi> findByUmid(int umid) {
		create();
		String sql = "select * from ChatInfos where umid = ? and tzid > 0 order by tzid desc";
		cursor = db.rawQuery(sql, new String[]{""+umid});
		List<Tongzhi> list = new ArrayList<Tongzhi>();
		while (cursor.moveToNext()) {
			Tongzhi tongZhi = new Tongzhi(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4),cursor.getInt(5),cursor.getInt(6),cursor.getString(7));
			list.add(tongZhi);
		}
		close();
		return list;
	}
	public List<Tongzhi> findLiaoTianByIds(int umid, String additional) {
		create();
		String sql = "select * from ChatInfos where ((umid = ? and tzType = 3 and tzid != -1 and additional = ?) or (additional = ? and umid = ? and tzType = 3 and tzid = -1))  order by tzdate asc";
		cursor = db.rawQuery(sql, new String[]{""+umid,additional,""+umid,additional});
		List<Tongzhi> list = new ArrayList<Tongzhi>();
		while (cursor.moveToNext()) {
			Tongzhi tongZhi = new Tongzhi(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4),cursor.getInt(5),cursor.getInt(6),cursor.getString(7));
			list.add(tongZhi);
		}
		close();
		return list;
	}
	public Tongzhi findByTzid(int tzid) {
		create();
		if (tzid < 0) {
			return null;
		}
		String sql = "select * from ChatInfos where tzid = ? ";
		cursor = db.rawQuery(sql, new String[]{""+tzid});
		Tongzhi tongZhi = null;
		if (cursor.moveToNext()) {
			tongZhi = new Tongzhi(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4),cursor.getInt(5),cursor.getInt(6),cursor.getString(7));
		}
		close();
		return tongZhi;
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
