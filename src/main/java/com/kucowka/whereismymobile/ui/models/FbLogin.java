package com.kucowka.whereismymobile.ui.models;

public class FbLogin {

	private String code,state,access_token,expires_in;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "FbLogin [code=" + code + ", state=" + state + ", access_token="
				+ access_token + ", expires_in=" + expires_in + "]";
	}
}
