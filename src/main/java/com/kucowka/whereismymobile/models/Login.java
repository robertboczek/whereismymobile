package com.kucowka.whereismymobile.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.kucowka.whereismymobile.validation.annotations.RequiredPasswordFormat;

public class Login {

	@NotNull
	@Length(min=8)
	private String deviceId;
	
	@NotNull
	@Length(min=8)
	@RequiredPasswordFormat
	private String password;
	
	private boolean rememberMe;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
}
