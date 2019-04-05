package com.xsy.superassociation.bean;

public class SheTuanJiao {
	private int stjid;
	private String stjtext ;
	private String stjsign ;
	private String name;
	public SheTuanJiao() {
	}
	
	public SheTuanJiao(int stjid, String stjtext, String stjsign, String name) {
		super();
		this.stjid = stjid;
		this.stjtext = stjtext;
		this.stjsign = stjsign;
		this.name = name;
	}

	public int getStjid() {
		return stjid;
	}
	public void setStjid(int stjid) {
		this.stjid = stjid;
	}
	public String getStjtext() {
		return stjtext;
	}
	public void setStjtext(String stjtext) {
		this.stjtext = stjtext;
	}
	public String getStjsign() {
		return stjsign;
	}
	public void setStjsign(String stjsign) {
		this.stjsign = stjsign;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
