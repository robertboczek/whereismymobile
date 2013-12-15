package com.kucowka.whereismymobile.ui.models;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.kucowka.whereismymobile.validation.annotations.RequiredPasswordFormat;

public class NewUser implements Serializable {

	@Length(min=8)
	@Email
	private String email;
	
	@Length(min=8)
	@RequiredPasswordFormat
	private String password, repeatPassword;
	
	@Length(min=2)
	private String firstName, lastName;
	
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

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "NewUser [email=" + email + ", password=" + password
				+ ", repeatPassword=" + repeatPassword + ", firstName="
				+ firstName + ", lastName=" + lastName + "]";
	}
}
