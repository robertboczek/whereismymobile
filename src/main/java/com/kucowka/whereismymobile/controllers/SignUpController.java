package com.kucowka.whereismymobile.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kucowka.whereismymobile.dao.CredentialsDao;
import com.kucowka.whereismymobile.models.Credentials;
import com.kucowka.whereismymobile.ui.models.Login;
import com.kucowka.whereismymobile.ui.models.NewUser;
import com.kucowka.whereismymobile.ui.models.User;
import com.kucowka.whereismymobile.util.SendMail;
import com.kucowka.whereismymobile.util.models.MailMessage;

@Controller
public class SignUpController extends AbstractController {

	private Logger logger = Logger.getLogger(SignUpController.class);
	
	@Autowired
	private CredentialsDao credentialsDao;
	
	@Autowired
	private SendMail sendMail;
	
	@RequestMapping(value="signup", method = RequestMethod.GET)
	public String getSignUp(NewUser newUser, BindingResult result, Model model) {
		
		model.asMap().put("newUser", newUser);
		return "signup";
	}
	
	@RequestMapping(value="signup", method = RequestMethod.POST)
	public String doSignUp(@Valid NewUser newUser, BindingResult result, Model model, HttpSession session) {
		
		String message = null;
		if (!result.hasErrors()) {
			if (!newUser.getPassword().equals(newUser.getRepeatPassword())) {
				message = "Passwords do not match";
			}
			
			List<Credentials> credentialsList = credentialsDao.getById(newUser.getEmail());
			if (credentialsList.size() != 0) {
				message = "Account with this email already exists"; 
			}
			
			if (message == null) {
				Credentials credentials = new Credentials(newUser);
				try {
					credentialsDao.save(credentials);
					User user = new User(credentials);
					saveUserInSession(session, user);
					
					MailMessage mailMessage = new MailMessage();
					mailMessage.setTo(credentials.getEmail());
					mailMessage.setSubject("Welcome to whereIsMyMobile");
					mailMessage.setBody("");
					sendMail.sendMail(mailMessage);
					
					return "redirect:welcome";
				} catch (Exception e) {
					logger.error("Error while creating new account: " + credentials);
					message = "Error while creating new account";
				}
			}
		}
		
		model.asMap().put(errorMessage, message);
		model.asMap().put("newUser", newUser);
		return "signup";
	}
	
	@RequestMapping(value="forgotPassword")
	public String getForgotPassword(@Valid Login login, BindingResult result, Model model) {
		return "forgotPassword";
	}
}
