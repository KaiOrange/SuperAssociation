package com.xsy.superassociation.bean;

import java.io.Serializable;



public class UserMain implements Serializable{
	private static final long serialVersionUID = 1L;
	private int umid;
	private String name;
	private String password;
	private String nichen ;
	private String touxiang;
	private boolean sex = true;
	private int age;	
	private String brithday;
	private String telphone;
	private String myclass;
	private String stid;
	public UserMain() {
	}
	
	

	public UserMain(int umid, String name, String password, String nichen,
			String touxiang, boolean sex, int age, String brithday,
			String telphone, String myclass, String stid) {
		super();
		this.umid = umid;
		this.name = name;
		this.password = password;
		this.nichen = nichen;
		this.touxiang = touxiang;
		this.sex = sex;
		this.age = age;
		this.brithday = brithday;
		this.telphone = telphone;
		this.myclass = myclass;
		this.stid = stid;
	}



	public int getUmid() {
		return umid;
	}
	public void setUmid(int umid) {
		this.umid = umid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNichen() {
		return nichen;
	}
	public void setNichen(String nichen) {
		this.nichen = nichen;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getBrithday() {
		return brithday;
	}
	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getMyclass() {
		return myclass;
	}
	public void setMyclass(String myclass) {
		this.myclass = myclass;
	}

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	@Override
	public String toString() {
		return "UserMain [umid=" + umid + ", name=" + name + ", password="
				+ password + ", nichen=" + nichen + ", touxiang=" + touxiang
				+ ", sex=" + sex + ", age=" + age + ", brithday=" + brithday
				+ ", telphone=" + telphone + ", myclass=" + myclass + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + umid;
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
		UserMain other = (UserMain) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (umid != other.umid)
			return false;
		return true;
	}



	
	
}
