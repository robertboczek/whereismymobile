package com.kucowka.whereismymobile.ui.models;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.kucowka.whereismymobile.validation.annotations.RequiredPasswordFormat;

public class ResetPassword implements Serializable {

	@Length(min=8)
	@Email
	private String accountId;
	
	private String code;
	
	@Length(min=8)
	@RequiredPasswordFormat
	private String password, repeatPassword;
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
}
