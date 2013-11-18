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

import org.apache.log4j.Logger;

public class SecurityFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SecurityFilter.class);
	private static String loginUrl;
	private static String openDir;
	private String context;

	public void destroy() {
		logger.info("SecurityFilter destroy...");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		// /whereismymobile/resources/bootstrap/js/bootstrap.js
		String url = ((HttpServletRequest) request).getRequestURI().toString();
		logger.info("URL: " + url);
		
		// check if authenticated if not redirect to loginUrl
		boolean authenticated = (context + "/welcome").equals(url);
		if (url.startsWith(openDir) || url.startsWith(loginUrl) || authenticated) {
			logger.info("Authenticated");
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
