package com.xsy.superassociation.bean;

import java.io.Serializable;

public class Tongzhi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int tzid;
	private String tztext;
	private String tztitle;
	private int umid;
	private String tzdate;
	private int status;//1 未读 0已读
	private int tzType;//通知类型 0普通通知 1加好友通知 2管理员审核通知 3聊天通知
	private String additional;//附加字段 -1表示已经同意 -2表示已经拒绝 还可以添加userName
	
	public Tongzhi() {
	}
	

	public Tongzhi(int tzid, String tztext, String tztitle, int umid,
			String tzdate, int status, int tzType, String additional) {
		super();
		this.tzid = tzid;
		this.tztext = tztext;
		this.tztitle = tztitle;
		this.umid = umid;
		this.tzdate = tzdate;
		this.status = status;
		this.tzType = tzType;
		this.additional = additional;
	}


	public int getTzid() {
		return tzid;
	}
	public void setTzid(int tzid) {
		this.tzid = tzid;
	}
	public String getTztext() {
		return tztext;
	}
	public void setTztext(String tztext) {
		this.tztext = tztext;
	}
	public String getTztitle() {
		return tztitle;
	}
	public void setTztitle(String tztitle) {
		this.tztitle = tztitle;
	}
	public String getAdditional() {
		return additional;
	}


	public void setAdditional(String additional) {
		this.additional = additional;
	}


	public int getUmid() {
		return umid;
	}
	public void setUmid(int umid) {
		this.umid = umid;
	}
	
	public String getTzdate() {
		return tzdate;
	}
	public void setTzdate(String tzdate) {
		this.tzdate = tzdate;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTzType() {
		return tzType;
	}

	public void setTzType(int tzType) {
		this.tzType = tzType;
	}


	@Override
	public String toString() {
		return "Tongzhi [tzid=" + tzid + ", tztext=" + tztext + ", tztitle="
				+ tztitle + ", umid=" + umid + ", tzdate=" + tzdate
				+ ", status=" + status + ", tzType=" + tzType + ", additional="
				+ additional + "]";
	}



}
