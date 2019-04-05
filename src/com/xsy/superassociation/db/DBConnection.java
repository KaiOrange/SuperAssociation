package com.xsy.superassociation.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

	private final static String dbName = "superAssociation.db";
	private final static int version = 1;
	
	public DBConnection(Context context) {
		super(context, dbName, null, version);
	}
	@Override
	public void onCreate(SQLiteDatabase sqlitedatabase) {
		String sql1 = "create table UserMain(" + 
				"umid integer," +
				"name Varchar(20)," + 
				"nichen Varchar(20)," + 
				"Touxiang Varchar(100)," +
				"sex Varchar(9)," + 
				"age integer," +
				"brithday Varchar(20)," + 
				"telphone varchar(20)," + 
				"myclass varchar(20)," + 
				"stid varchar(20)" + 
			");";
		sqlitedatabase.execSQL(sql1);
		
		String sql2 = "create table SheTuan(" + 
				"stid integer ," + 
				"sname Varchar(20)," + 
				"snotice Varchar(500)," + 
				"sdescribe Varchar(500)," + 
				"cjdate datetime," + 
				"touxiang Varchar(100)," + 
				"umid integer," + 
				"umName Varchar(100)" + 
			");";
		sqlitedatabase.execSQL(sql2);
		
		String sql3 = "create table ShetuanAdmin("+
				"staid integer not null,"+
				"stid integer not null,"+
				"umid integer not null"+
			");";
		sqlitedatabase.execSQL(sql3);
		
		String sql4 = "create table Tongzhi(" + 
				"tzid integer ," + 
				"tztext varchar(500) ," + 
				"tztitle	varchar(50) ," + 
				"umid integer," + 
				"tzdate varchar(50)," + 
				"status integer," + 
				"tzType integer," +
				"additional varchar(50)" + //附加字段 
			");";
		sqlitedatabase.execSQL(sql4);
		
		//朋友表
		String sql5 = "create table Firends(" + 
				"umid integer," +
				"name Varchar(20)," + 
				"nichen Varchar(20)," + 
				"Touxiang Varchar(100)," +
				"sex Varchar(9)," + 
				"age integer," +
				"brithday Varchar(20)," + 
				"telphone varchar(20)," + 
				"myclass varchar(20)," + 
				"stid integer," + 
				"umidMe integer(20)" + 
			");";
		sqlitedatabase.execSQL(sql5);
		//社团表
		String sql6 = "create table Activity(" +
				"aid integer," +
				"stid integer," +
				"adescribe varchar(500)," +
				"atitle varchar(50)," +
				"sdate varchar(50)," + 
				"edate varchar(50)," + 
				"umid integer" + 
			")";
		sqlitedatabase.execSQL(sql6);
		//社团角
		String sql7 = "create table SheTuanJiao(" + 
				"stjid integer," + 
				"stjtext varchar(200)," +
				"stjsign varchar(50)," +
				"name varchar(50)" +
			");";
		sqlitedatabase.execSQL(sql7);
		
		String sql8 = "create table ChatInfos(" + 
				"tzid integer ," + 
				"tztext varchar(500) ," + 
				"tztitle varchar(50) ," + 
				"umid integer," + 
				"tzdate varchar(50)," + 
				"status integer," + 
				"tzType integer," +
				"additional varchar(50)" + //附加字段 
			");";
		sqlitedatabase.execSQL(sql8);
		
		System.out.println("执行成功!!!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
		sqlitedatabase.execSQL("drop table UserMain");
		sqlitedatabase.execSQL("drop table SheTuan");
		onCreate(sqlitedatabase);
	}

}