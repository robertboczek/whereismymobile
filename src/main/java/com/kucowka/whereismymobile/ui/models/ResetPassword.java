package com.kucowka.whereismymobile.ui.models;

import java.io.Serializable;

public class ResetPassword implements Serializable {

	private String accountId;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
}
