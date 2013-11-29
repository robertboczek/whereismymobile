package com.kucowka.whereismymobile.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SecurityFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SecurityFilter.class);
	public static final String AUTHORIZED_KEY = "authorized";
	private static String loginUrl;
	private static String openDir;
	private String context;

	public void destroy() {
		logger.info("SecurityFilter destroy...");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		
		HttpSession session = ((HttpServletRequest) request).getSession();
		logger.info("Received sessionId: " + session.getId());
		
		// /whereismymobile/resources/bootstrap/js/bootstrap.js
		String url = ((HttpServletRequest) request).getRequestURI().toString();
		logger.info("URL: " + url);
		
		// check if request is authorized, if not redirect to loginUrl
		Boolean authorizedValue = (Boolean) (session.getAttribute(AUTHORIZED_KEY));
        boolean authorized = authorizedValue != null && authorizedValue;
		if (url.startsWith(openDir) || url.startsWith(loginUrl) 
				|| (context + "/fbLogin").equals(url) || authorized) {
			filterChain.doFilter(request, response);
		} else {
			logger.info("Redirecting to login page");
			((HttpServletResponse)response).sendRedirect(loginUrl);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		loginUrl = config.getInitParameter("loginUrl");
		openDir = config.getInitParameter("open");
		context = config.getInitParameter("context");
		loginUrl = context + loginUrl;
		openDir = context + openDir;
	}
}
