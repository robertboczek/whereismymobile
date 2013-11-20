package com.kucowka.whereismymobile.controllers;

import java.util.Arrays;
import java.util.Map;

import javax.validation.Valid;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

	@Qualifier("memcached.client")
	@Autowired()
	private MemcachedClient memcached;

	private static final String errorMessage = "errorMessage";

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Login login, BindingResult result, Model model) {
		model.asMap().put("login", login);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(@Valid Login login, BindingResult result, Model model) {
		logger.info("DEVICE ID: " + login.getDeviceId());

		if (!result.hasErrors()) {
			Credentials credentials = getCredentials(login.getDeviceId());
			if (credentials != null) {
				if (credentials.getPassword() != null
						&& credentials.getPassword()
								.equals(login.getPassword())) {
					return "redirect:welcome";
				}
			}
			model.asMap()
					.put(errorMessage, "Device Id or password was invalid");
		} else {
			model.asMap().put(errorMessage, "Please fix the errors and retry!");
		}
		model.asMap().put("login", login);
		return "login";
	}

	private Credentials getCredentials(String id) {
		Credentials credentials = null;

		if (memcached != null) {
			Object cachedObject = memcached.get(id);
			if (cachedObject != null && cachedObject instanceof Credentials) {
				credentials = (Credentials) cachedObject;
				logger.info("Using cached version of credentials");
			}
		}

		if (credentials == null) {
			try {
				Key key = new Key();
				key.setHashKeyElement(new AttributeValue().withS(id));
				GetItemRequest getItemRequest = new GetItemRequest()
						.withTableName("Devices").withKey(key)
						.withAttributesToGet(Arrays.asList("Id", "Password"));

				GetItemResult result = dbClientProvider.getClient().getItem(
						getItemRequest);

				Map<String, AttributeValue> resultItemMap = result.getItem();
				if (resultItemMap != null) {
					credentials = new Credentials();
					credentials.setId(id);
					credentials.setPassword(resultItemMap.get("Password")
							.getS());
					if (memcached != null) {
						logger.info("Caching credentials for id: " + id);
						memcached.set(id, 3600, credentials);
					}
				}

			} catch (AmazonServiceException ase) {
				logger.warn("Failed to retrieve item in Devices", ase);
			} catch (Exception e) {
				logger.error("Exception: ", e);
			}
		}
		return credentials;
	}
}
