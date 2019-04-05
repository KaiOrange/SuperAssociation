package com.xsy.superassociation.bean;

public class XiakeliaoImg {
	private int xklimgid;
	private String imgpath;
	private int xklid;
	public XiakeliaoImg() {
	}
	public XiakeliaoImg(int xklimgid, String imgpath, int xklid) {
		super();
		this.xklimgid = xklimgid;
		this.imgpath = imgpath;
		this.xklid = xklid;
	}
	public int getXklimgid() {
		return xklimgid;
	}
	public void setXklimgid(int xklimgid) {
		this.xklimgid = xklimgid;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public int getXklid() {
		return xklid;
	}
	public void setXklid(int xklid) {
		this.xklid = xklid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imgpath == null) ? 0 : imgpath.hashCode());
		result = prime * result + xklid;
		result = prime * result + xklimgid;
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
		XiakeliaoImg other = (XiakeliaoImg) obj;
		if (imgpath == null) {
			if (other.imgpath != null)
				return false;
		} else if (!imgpath.equals(other.imgpath))
			return false;
		if (xklid != other.xklid)
			return false;
		if (xklimgid != other.xklimgid)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "XiakeliaoImg [imgpath=" + imgpath + ", xklid=" + xklid
				+ ", xklimgid=" + xklimgid + "]";
	}
}
