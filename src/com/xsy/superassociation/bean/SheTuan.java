package com.xsy.superassociation.bean;

import java.io.Serializable;


public class SheTuan implements Serializable {
	private static final long serialVersionUID = 1L;
	private int stid;
	private String sname;
	private String snotice;
	private String sdescribe;
	private String cjdate;
	private String touxiang;
	private int umid;
	
	//创始人姓名
	private String umName;
	
	public SheTuan() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + stid;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SheTuan other = (SheTuan) obj;
		if (stid != other.stid)
			return false;
		return true;
	}

	public SheTuan(int stid, String sname, String snotice, String sdescribe,
			String cjdate, String touxiang, int umid, String umName) {
		super();
		this.stid = stid;
		this.sname = sname;
		this.snotice = snotice;
		this.sdescribe = sdescribe;
		this.cjdate = cjdate;
		this.touxiang = touxiang;
		this.umid = umid;
		this.umName = umName;
	}
	public String getUmName() {
		return umName;
	}
	public void setUmName(String umName) {
		this.umName = umName;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	public int getStid() {
		return stid;
	}
	public void setStid(int stid) {
		this.stid = stid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSnotice() {
		return snotice;
	}
	public void setSnotice(String snotice) {
		this.snotice = snotice;
	}
	public String getSdescribe() {
		return sdescribe;
	}
	public void setSdescribe(String sdescribe) {
		this.sdescribe = sdescribe;
	}
	public String getCjdate() {
		return cjdate;
	}
	public void setCjdate(String cjdate) {
		this.cjdate = cjdate;
	}
	public int getUmid() {
		return umid;
	}
	public void setUmid(int umid) {
		this.umid = umid;
	}

	@Override
	public String toString() {
		return "SheTuan [cjdate=" + cjdate + ", sdescribe=" + sdescribe
				+ ", sname=" + sname + ", snotice=" + snotice + ", stid="
				+ stid + ", touxiang=" + touxiang + ", umid=" + umid + "]";
	}

}
