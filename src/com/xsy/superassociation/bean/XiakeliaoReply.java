package com.xsy.superassociation.bean;


public class XiakeliaoReply {
	private int xklrid;
	private int xklid;
	private int umidme;
	private int umidyou;
	private String xklrtext;
	private String cjdate;
	
	//自己的信息
	private UserMain userMainMe;
	//别人的信息
	private UserMain userMainYou;
	
	public XiakeliaoReply() {
	}
	public XiakeliaoReply(int xklrid, int xklid, int umidme, int umidyou,
			String xklrtext, String cjdate) {
		super();
		this.xklrid = xklrid;
		this.xklid = xklid;
		this.umidme = umidme;
		this.umidyou = umidyou;
		this.xklrtext = xklrtext;
		this.cjdate = cjdate;
	}
	public int getXklrid() {
		return xklrid;
	}
	public void setXklrid(int xklrid) {
		this.xklrid = xklrid;
	}
	public int getXklid() {
		return xklid;
	}
	public void setXklid(int xklid) {
		this.xklid = xklid;
	}
	public int getUmidme() {
		return umidme;
	}
	public void setUmidme(int umidme) {
		this.umidme = umidme;
	}
	public int getUmidyou() {
		return umidyou;
	}
	public void setUmidyou(int umidyou) {
		this.umidyou = umidyou;
	}
	public String getXklrtext() {
		return xklrtext;
	}
	public void setXklrtext(String xklrtext) {
		this.xklrtext = xklrtext;
	}
	public String getCjdate() {
		return cjdate;
	}
	public void setCjdate(String cjdate) {
		this.cjdate = cjdate;
	}
	
	public UserMain getUserMainMe() {
		return userMainMe;
	}
	public void setUserMainMe(UserMain userMainMe) {
		this.userMainMe = userMainMe;
	}
	public UserMain getUserMainYou() {
		return userMainYou;
	}
	public void setUserMainYou(UserMain userMainYou) {
		this.userMainYou = userMainYou;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cjdate == null) ? 0 : cjdate.hashCode());
		result = prime * result + umidme;
		result = prime * result + umidyou;
		result = prime * result + xklid;
		result = prime * result + xklrid;
		result = prime * result
				+ ((xklrtext == null) ? 0 : xklrtext.hashCode());
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
		XiakeliaoReply other = (XiakeliaoReply) obj;
		if (cjdate == null) {
			if (other.cjdate != null)
				return false;
		} else if (!cjdate.equals(other.cjdate))
			return false;
		if (umidme != other.umidme)
			return false;
		if (umidyou != other.umidyou)
			return false;
		if (xklid != other.xklid)
			return false;
		if (xklrid != other.xklrid)
			return false;
		if (xklrtext == null) {
			if (other.xklrtext != null)
				return false;
		} else if (!xklrtext.equals(other.xklrtext))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "XiakeliaoReply [cjdate=" + cjdate + ", umidme=" + umidme
				+ ", umidyou=" + umidyou + ", xklid=" + xklid + ", xklrid="
				+ xklrid + ", xklrtext=" + xklrtext + "]";
	}
	
}
