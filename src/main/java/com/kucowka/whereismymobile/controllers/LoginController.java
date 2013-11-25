package com.kucowka.whereismymobile.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.validation.Valid;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kucowka.whereismymobile.dao.CredentialsDao;
import com.kucowka.whereismymobile.models.Credentials;
import com.kucowka.whereismymobile.ui.models.FbLogin;
import com.kucowka.whereismymobile.ui.models.FbUser;
import com.kucowka.whereismymobile.ui.models.Login;

@Controller
public class LoginController {

	private static final Logger logger = Logger
			.getLogger(LoginController.class);

	@Autowired
	private CredentialsDao credentialsDao;

	/*
	 * @Qualifier("memcached.client")
	 * 
	 * @Autowired()
	 */
	private MemcachedClient memcached;

	private static final String errorMessage = "errorMessage";

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Login login, BindingResult result, Model model) {
		model.asMap().put("login", login);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(@Valid Login login, BindingResult result, Model model) {
		logger.info("Email: " + login.getEmail());

		if (!result.hasErrors()) {
			Credentials credentials = getCredentials(login.getEmail());
			if (credentials != null) {
				if (credentials.getPassword() != null
						&& credentials.getPassword()
								.equals(login.getPassword())) {
					return "redirect:welcome";
				}
			}
			model.asMap().put(errorMessage, "Email or password was invalid");
		} else {
			model.asMap().put(errorMessage, "Please fix the errors and retry!");
		}
		model.asMap().put("login", login);
		return "login";
	}

	private Credentials getCredentials(String email) {
		Credentials credentials = null;

		if (memcached != null) {
			Object cachedObject = memcached.get(email);
			if (cachedObject != null && cachedObject instanceof Credentials) {
				credentials = (Credentials) cachedObject;
				logger.info("Using cached version of credentials");
			}
		}

		if (credentials == null) {
			List<Credentials> credentialsList = credentialsDao.getById(email);
			if (credentialsList.size() == 1) {
				credentials = credentialsList.get(0);
				if (memcached != null) {
					logger.info("Caching credentials for id: " + email);
					memcached.set(email, 3600, credentials);
				}
			}
		}
		return credentials;
	}

	@RequestMapping(value = "/fbLogin")
	public String fbLogin(FbLogin fbLogin) {
		logger.info("Received FB login: " + fbLogin);

		if (!StringUtils.isEmpty(fbLogin.getCode())) {
			try {
				getToken("https://graph.facebook.com/oauth/access_token?client_id=374675276002381&redirect_uri=http://ec2-50-16-158-177.compute-1.amazonaws.com:8080/whereismymobile/fbLogin&client_secret=b004fbcaa2bdfe6fa7bc43bd9e563b3e&code="
						+ fbLogin.getCode());
			} catch (Exception e) {
				logger.error("Error while getting fb token", e);
				return "redirect:login";
			}
		}
		return "redirect:welcome";
	}

	private void getToken(String address) throws Exception {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		FbUser fbUser = null;
		logger.info("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			logger.info("Received output: " + output);
			String token = output.substring(
					output.indexOf("access_token=") + 13,
					output.indexOf("&expires"));
			String expires = output.substring(output.indexOf("expires=") + 8);
			logger.info("Token: " + token + ", expires: " + expires);
		}
		conn.disconnect();
		
		// todo get email from fb
	}
}
