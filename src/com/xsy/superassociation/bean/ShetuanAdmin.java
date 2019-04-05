package com.xsy.superassociation.bean;

public class ShetuanAdmin {
	private int staid;
	private int stid;
	private int umid;
	public ShetuanAdmin() {
	}
	public ShetuanAdmin(int staid, int stid, int umid) {
		super();
		this.staid = staid;
		this.stid = stid;
		this.umid = umid;
	}
	public int getStaid() {
		return staid;
	}
	public void setStaid(int staid) {
		this.staid = staid;
	}
	public int getStid() {
		return stid;
	}
	public void setStid(int stid) {
		this.stid = stid;
	}
	public int getUmid() {
		return umid;
	}
	public void setUmid(int umid) {
		this.umid = umid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + staid;
		result = prime * result + stid;
		result = prime * result + umid;
		return result;
	}
	@Override
	public String toString() {
		return "ShetuanAdmin [staid=" + staid + ", stid=" + stid + ", umid="
				+ umid + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShetuanAdmin other = (ShetuanAdmin) obj;
		if (staid != other.staid)
			return false;
		if (stid != other.stid)
			return false;
		if (umid != other.umid)
			return false;
		return true;
	}
	
}
