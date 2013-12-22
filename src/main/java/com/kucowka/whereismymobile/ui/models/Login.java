package com.kucowka.whereismymobile.ui.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.kucowka.whereismymobile.validation.annotations.RequiredPasswordFormat;

public class Login {

	@Length(min=8)
	@Email
	private String email;
	
	@Length(min=8)
	@RequiredPasswordFormat
	private String password;
	
	private String captchaCode;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}
}
