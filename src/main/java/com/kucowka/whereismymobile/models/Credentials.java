package com.kucowka.whereismymobile.models;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.kucowka.whereismymobile.ui.models.NewUser;

@DynamoDBTable(tableName="Credentials")
public class Credentials extends Entity {

	private String email, password, firstName, lastName, imgSrc;

	public Credentials() {
	}
	
	public Credentials(NewUser newUser) {
		this.email = newUser.getEmail();
		this.password = newUser.getPassword();
		this.firstName = newUser.getFirstName();
		this.lastName = newUser.getLastName();
	}

	@DynamoDBHashKey(attributeName="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DynamoDBAttribute
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@DynamoDBAttribute
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@DynamoDBAttribute
	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	@DynamoDBAttribute
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
