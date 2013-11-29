package com.kucowka.whereismymobile.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SignUpController {

	@RequestMapping(value="signup")
	public String getSignUp(Model model) {
		
		return "signup";
	}
	
	@RequestMapping(value="forgotPassword")
	public String getForgotPassword(Model model) {
		return "forgotPassword";
	}
}
