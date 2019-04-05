package com.xsy.superassociation.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xsy.superassociation.bean.Activity;
import com.xsy.superassociation.db.DBConnection;
import com.xsy.superassociation.util.DateConvert;

public class ActivityDAO {
	private DBConnection dbConnection;
	private SQLiteDatabase db;
	private Cursor cursor;
	private Context context;
	public ActivityDAO(Context context) {
		this.context = context;
	}
	private void create(){
		dbConnection = new DBConnection(context);
		db = dbConnection.getWritableDatabase();
	}
	
	public boolean save(Activity activity,int umid) {
		create();
		String sql = "insert into Activity(aid,stid,adescribe,atitle,sdate,edate,umid)"  
			+ "values(?,?,?,?,?,?,?)";
		try {
			db.execSQL(
					sql,
					new String[] {"" + activity.getAid(),"" + activity.getStid(),activity.getAdescribe(),
							activity.getAtitle(),activity.getSdate(),activity.getEdate(),"" + umid});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	public boolean saveAll(List<Activity> list,int umid) {
		try {
			for (Activity activity : list) {
				save(activity, umid);
			}
		} catch (Exception e) {
			return false;
		} 
		return true;
	}
	
	public boolean update(Activity activity,int umid) {
		create();
		System.out.println(activity);
		String sql = "update Activity set adescribe = ?,sdate = ?,edate = ? where aid = ? and umid = ?";
		try {
			db.execSQL(
					sql,
					new String[] { 
							activity.getAdescribe(),
							activity.getSdate(),
							activity.getEdate(),
							"" + activity.getAid(),"" + umid});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public boolean deleteAllActivity(String stid,int umid) {
		create();
		String sql = "delete from Activity where stid = ? and umid = ?";
		try {
			db.execSQL(sql,new String[] {stid,"" + umid});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	public boolean delete(int aid) {
		create();
		String sql = "delete from Activity where aid = ? ";
		try {
			db.execSQL(sql,new String[] {"" + aid});
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
		return true;
	}
	
	public Activity findActivity(String aid) {
		create();
		Activity activity = null;
		String sql = "select aid,stid,adescribe,atitle,sdate,edate,umid from Activity " +
				"where aid = ? ";
		cursor = db.rawQuery(sql, new String[]{aid});
		if (cursor.moveToNext()) {
			activity = new Activity(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
		}
		close();
		return activity;
	}
	
	public List<Activity> findUserActivity(String stid,int uid) {
		create();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		Calendar calendar2 = Calendar.getInstance();
		String sql = "select aid,stid,adescribe,atitle,sdate,edate,umid from Activity " +
				"where stid = ? and umid = ? order by aid desc";
		cursor = db.rawQuery(sql, new String[]{stid , "" + uid});
		List<Activity> list = new ArrayList<Activity>();
		List<Activity> listYes = new ArrayList<Activity>();
		List<Activity> listNo = new ArrayList<Activity>();
		while (cursor.moveToNext()) {
			calendar2.setTime(DateConvert.convertToDate(cursor.getString(5)));
			Activity activity = new Activity(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
			if (calendar1.compareTo(calendar2) < 0) {
				listYes.add(activity);
			} else {
				listNo.add(activity);
			}
		}
		close();
		list.addAll(listYes);
		list.addAll(listNo);
		return list;
	}
	/*
	 * public List<Activity> findUserActivity(String stid,int uid) {
		create();
		String sql = "select aid,stid,adescribe,atitle,sdate,edate,umid from Activity " +
				"where stid = ? and umid = ? order by aid desc";
		cursor = db.rawQuery(sql, new String[]{stid , "" + uid});
		List<Activity> list = new ArrayList<Activity>();
		while (cursor.moveToNext()) {
			Activity activity = new Activity(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
			list.add(activity);
		}
		close();
		return list;
	}*/
	
	public List<Activity> findUserNowActivity(String stid,int uid) {
		create();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		Calendar calendar2 = Calendar.getInstance();
		String sql = "select aid,stid,adescribe,atitle,sdate,edate,umid from Activity " +
				"where stid = ? and umid = ? order by aid desc";
		cursor = db.rawQuery(sql, new String[]{stid , "" + uid});
		List<Activity> list = new ArrayList<Activity>();
		while (cursor.moveToNext()) {
			calendar2.setTime(DateConvert.convertToDate(cursor.getString(5)));
			if (calendar1.compareTo(calendar2) < 0) {
				Activity activity = new Activity(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
				list.add(activity);
			}
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
