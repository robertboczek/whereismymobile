package com.kucowka.whereismymobile.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kucowka.whereismymobile.filters.SecurityFilter;

@Controller
public class Logout {

	@RequestMapping(value="/logout")
	public String logout(HttpSession session) {
		session.setAttribute(SecurityFilter.AUTHORIZED_KEY, false);
		session.invalidate();
		
		return "redirect:login";
	}
}
