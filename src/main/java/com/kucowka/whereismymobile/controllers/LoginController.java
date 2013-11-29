package com.kucowka.whereismymobile.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
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

import com.kucowka.whereismymobile.dao.CredentialsDao;
import com.kucowka.whereismymobile.filters.SecurityFilter;
import com.kucowka.whereismymobile.models.Credentials;
import com.kucowka.whereismymobile.ui.models.FbLogin;
import com.kucowka.whereismymobile.ui.models.FbUser;
import com.kucowka.whereismymobile.ui.models.Login;
import com.kucowka.whereismymobile.ui.models.User;
import com.kucowka.whereismymobile.util.HttpClientUtil;
import com.kucowka.whereismymobile.util.ObjectMapperUtil;

@Controller
public class LoginController {

	private static final Logger logger = Logger
			.getLogger(LoginController.class);

	@Autowired
	private CredentialsDao credentialsDao;

	@Autowired
	private HttpClientUtil httpClientUtil;
	
	@Autowired
	private ObjectMapperUtil fbUserMapperUtil;

	@Qualifier("memcached.client")
	@Autowired()
	private MemcachedClient memcached;

	private static final String errorMessage = "errorMessage";
	private static final String accessToken = "access_token=";
	private static final String expiresKey = "&expires=";
	private static final String FbUserDetailsUrl = "https://graph.facebook.com/me?access_token=";

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Login login, BindingResult result, Model model) {
		model.asMap().put("login", login);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(@Valid Login login, BindingResult result, Model model, HttpSession session) {
		logger.info("Email: " + login.getEmail());

		if (!result.hasErrors()) {
			Credentials credentials = getCredentials(login.getEmail());
			if (credentials != null) {
				if (credentials.getPassword() != null
						&& credentials.getPassword()
								.equals(login.getPassword())) {
					User user = new User();
					user.setEmail(credentials.getEmail());
					if (user.getName() == null) {
						user.setName("unknown");
					}
					saveUserInSession(session, user);
					
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
				cacheCredentials(credentials);
			}
		}
		return credentials;
	}

	private void cacheCredentials(Credentials credentials) {
		if (memcached != null) {
			logger.info("Caching credentials for id: " + credentials.getEmail());
			memcached.set(credentials.getEmail(), 3600, credentials);
		}		
	}

	@RequestMapping(value = "/fbLogin")
	public String fbLogin(FbLogin fbLogin, HttpSession session) {
		logger.info("Received FB login: " + fbLogin);
		FbUser fbUser = null;
		try {
			fbUser = getFbUser("https://graph.facebook.com/oauth/access_token?client_id=374675276002381&redirect_uri=http://ec2-50-16-158-177.compute-1.amazonaws.com:8080/whereismymobile/fbLogin&client_secret=b004fbcaa2bdfe6fa7bc43bd9e563b3e&code="
					+ fbLogin.getCode());
		} catch (Exception e) {
			logger.error("Error while getting fb token", e);
			return "redirect:login";
		}
		// TODO set session id as logged and save it in memcached
		List<Credentials> credentialsList = credentialsDao.getById(fbUser.getEmail());
		if (credentialsList.size() == 1) {
			Credentials credentials = credentialsList.get(0);
			credentials.setName(fbUser.getName());
			credentialsDao.save(credentials);
			cacheCredentials(credentials);
		}
		User user = new User(fbUser);
		saveUserInSession(session, user);
		
		return "redirect:welcome";
	}

	private void saveUserInSession(HttpSession session, User user) {
		session.setAttribute(SecurityFilter.AUTHORIZED_KEY, true);
		session.setAttribute("user", user);		
	}

	private FbUser getFbUser(String address) throws Exception {

		String response = httpClientUtil.getRequest(address);
		String token = response.substring(
				response.indexOf(accessToken) + accessToken.length(),
				response.indexOf(expiresKey));
		// TODO save session
		String expires = response.substring(response.indexOf(expiresKey) + expiresKey.length());

		String userDetailsUrl = FbUserDetailsUrl + token;
		String userDetails = httpClientUtil.getRequest(userDetailsUrl);
		logger.info("FB User details: " + userDetails);
		
		return fbUserMapperUtil.readValue(userDetails, FbUser.class);
	}
}
