package com.kucowka.whereismymobile.aws;

import java.io.Serializable;

public class Credentials implements Serializable {

	private String id, password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
