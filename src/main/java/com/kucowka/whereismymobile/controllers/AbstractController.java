package com.kucowka.whereismymobile.controllers;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.kucowka.whereismymobile.filters.SecurityFilter;
import com.kucowka.whereismymobile.ui.models.User;

public abstract class AbstractController {
	
	private static final Logger logger = Logger.getLogger(AbstractController.class);
	
	protected static final String errorMessage = "errorMessage";

	protected void saveUserInSession(HttpSession session, User user) {
		session.setAttribute(SecurityFilter.AUTHORIZED_KEY, true);
		session.setAttribute("user", user);	
		logger.info("Saving session");
	}
}