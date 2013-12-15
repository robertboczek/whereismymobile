package com.kucowka.whereismymobile.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kucowka.whereismymobile.ui.models.ResetPassword;

@Component
public class ResetPasswordController extends AbstractController {
	
	@RequestMapping(value="resetPassword", method = RequestMethod.GET)
	public String getPasswordReset(ResetPassword resetPassword, Model model) {
		
		model.asMap().put("resetPassword", resetPassword);
		return "resetPassword";
	}
	
	@RequestMapping(value="resetPassword", method = RequestMethod.POST)
	public String doResetPassword(@Valid ResetPassword resetPassword, BindingResult result, Model model, HttpSession session) {
		
		String message = null;
		if (!result.hasErrors()) {
			
		}
		
		model.asMap().put(errorMessage, message);
		model.asMap().put("resetPassword", resetPassword);
		return "resetPassword";
	}
}
