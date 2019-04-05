package com.xsy.superassociation.bean;

import java.io.Serializable;

public class Tongzhi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int tzid;
	private String tztext;
	private String tztitle;
	private int umid;
	private String tzdate;
	private int status;//1 δ�� 0�Ѷ�
	private int tzType;//֪ͨ���� 0��֪ͨͨ 1�Ӻ���֪ͨ 2����Ա���֪ͨ 3����֪ͨ
	private String additional;//�����ֶ� -1��ʾ�Ѿ�ͬ�� -2��ʾ�Ѿ��ܾ� ���������userName
	
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
