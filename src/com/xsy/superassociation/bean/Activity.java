package com.xsy.superassociation.bean;

import java.io.Serializable;



public class Activity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private int aid;
	private int stid;
	private String adescribe;
	private String atitle;
	private String sdate;
	private String edate;
	
	private int umid;
	
	public Activity() {
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getStid() {
		return stid;
	}
	public void setStid(int stid) {
		this.stid = stid;
	}
	public String getAdescribe() {
		return adescribe;
	}
	public void setAdescribe(String adescribe) {
		this.adescribe = adescribe;
	}
	public String getAtitle() {
		return atitle;
	}
	public void setAtitle(String atitle) {
		this.atitle = atitle;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}


	public Activity(int aid, int stid, String adescribe, String atitle,
			String sdate, String edate, int umid) {
		super();
		this.aid = aid;
		this.stid = stid;
		this.adescribe = adescribe;
		this.atitle = atitle;
		this.sdate = sdate;
		this.edate = edate;
		this.umid = umid;
	}
	public int getUmid() {
		return umid;
	}
	public void setUmid(int umid) {
		this.umid = umid;
	}
	@Override
	public String toString() {
		return "Activity [adescribe=" + adescribe + ", aid=" + aid
				+ ", atitle=" + atitle + ", edate=" + edate + ", sdate="
				+ sdate + ", stid=" + stid + "]";
	}
	
	
	
}
