package com.loadtrend.web.mobile.dao.model;

import java.io.Serializable;
import java.util.Date;

public class UserRecorder implements Serializable {
	private String id = null;

	private Date loginTime = null;

	private String mobile = null;

	private String ip = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
