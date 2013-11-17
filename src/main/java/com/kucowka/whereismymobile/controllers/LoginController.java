package com.kucowka.whereismymobile.controllers;

import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;
import com.amazonaws.services.dynamodb.model.Key;
import com.kucowka.whereismymobile.aws.Credentials;
import com.kucowka.whereismymobile.aws.DynamoDbClientProvider;
import com.kucowka.whereismymobile.models.Login;

@Controller
public class LoginController {

	private static final Logger logger = Logger
			.getLogger(LoginController.class);
	
	@Autowired
	private DynamoDbClientProvider dbClientProvider;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Login login, Model model) {
		model.asMap().put("login", login);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(Login login, Model model) {
		logger.info("DEVICE ID: " + login.getDeviceId());

		Credentials credentials = getCredentials(login.getDeviceId());
		if (credentials != null) {
			if (credentials.getPassword() != null
					&& credentials.getPassword().equals(login.getPassword())) {
				return "redirect:welcome";
			}
		}

		model.asMap().put("login", login);
		return "login";
	}

	private Credentials getCredentials(String id) {
		Credentials credentials = null;
		try {
			Key key = new Key();
			key.setHashKeyElement(new AttributeValue().withS(id));
			GetItemRequest getItemRequest = new GetItemRequest()
					.withTableName("Devices")
					.withKey(key)
					.withAttributesToGet(
							Arrays.asList("Id", "Password"));

			GetItemResult result = dbClientProvider.getClient().getItem(
					getItemRequest);

			Map<String, AttributeValue> resultItemMap = result.getItem();
			if (resultItemMap != null) {
				credentials = new Credentials();
				credentials.setId(id);
				credentials.setPassword(resultItemMap.get("Password").getS());
			}

		} catch (AmazonServiceException ase) {
			logger.warn("Failed to retrieve item in Devices", ase);
		} catch (Exception e) {
			logger.error("Exception: ", e);
		}
		return credentials;
	}
}
