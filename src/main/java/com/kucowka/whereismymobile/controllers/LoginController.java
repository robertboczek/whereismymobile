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
import org.springframework.util.StringUtils;
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
public class LoginController extends AbstractController {

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

		boolean validCaptchaCode = true;
		Object captchaObject = session.getAttribute(CaptchaController.CAPTCHA_SESSION_KEY);
		if (login.getCaptchaCode() == null || !login.getCaptchaCode().equals(captchaObject)) {
			model.asMap().put(errorMessage, "Invalid code from the image");
			validCaptchaCode = false;
		} else {
			if (validCaptchaCode && !result.hasErrors()) {
				List<Credentials> credentialsList = credentialsDao.getById(login.getEmail());
				Credentials credentials = credentialsList.size() > 0 ? credentialsList.get(0) : null;
				if (credentials != null) {
					if (credentials.getPassword() != null
							&& credentials.getPassword().equals(login.getPassword())) {
						User user = new User(credentials);
						saveUserInSession(session, user);
					
						return "redirect:welcome";
					}
				}
				model.asMap().put(errorMessage, "Email or password was invalid");
			} else {
				model.asMap().put(errorMessage, "Please fix the errors and retry!");
			}
		}
		model.asMap().put("login", login);
		return "login";
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

		List<Credentials> credentialsList = credentialsDao.getById(fbUser.getEmail());
		Credentials credentials = null;
		boolean shouldSave = false;
		if (credentialsList.size() == 1) {
			credentials = credentialsList.get(0);
			shouldSave = setFields(credentials, fbUser);
		} else {
			credentials = new Credentials();
			credentials.setEmail(fbUser.getEmail());
			shouldSave = setFields(credentials, fbUser);
		}
		if (shouldSave) {
			credentialsDao.save(credentials);
		}
		User user = new User(credentials);
		saveUserInSession(session, user);
		
		return "redirect:welcome";
	}

	private boolean setFields(Credentials credentials, FbUser fbUser) {
		if (!fbUser.getFirst_name().equals(credentials.getFirstName()) ||
				!fbUser.getLast_name().equals(credentials.getLastName()) ||
				StringUtils.isEmpty(credentials.getImgSrc())) {
			credentials.setFirstName(fbUser.getFirst_name());
			credentials.setLastName(fbUser.getLast_name());
			credentials.setImgSrc("http://graph.facebook.com/" + fbUser.getId() + "/picture");
			return true;
		} else {
			return false;
		}
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
