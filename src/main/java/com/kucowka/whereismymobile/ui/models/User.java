package com.kucowka.whereismymobile.ui.models;

import java.io.Serializable;

public class User implements Serializable {

	private String name, email;

	public User() {
	}

	public User(FbUser fbUser) {
		this.name = fbUser.getName();
		this.email = fbUser.getEmail();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
