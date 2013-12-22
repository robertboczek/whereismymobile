package com.kucowka.whereismymobile.controllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CaptchaController {
	
	public static final String CAPTCHA_SESSION_KEY = "catpchaKey";
	
	private static final Logger logger = Logger.getLogger(CaptchaController.class);

	@RequestMapping(value = "/captcha.png", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.GET)
	public @ResponseBody BufferedImage getCatchaImage(Boolean refresh, Model model, HttpSession session) {
		BufferedImage image = null;
		String randomText = null;
		if ((refresh != null && refresh) || session.getAttribute(CAPTCHA_SESSION_KEY) == null) {
			randomText = createNewCaptchaText(session);
		} else {
			randomText = session.getAttribute(CAPTCHA_SESSION_KEY).toString();
		}
		try {
			image = ImageIO.read(this.getClass().getResourceAsStream("/captcha.png"));
			Graphics g = image.getGraphics();
		    g.setFont(g.getFont().deriveFont(27f));
		    g.setColor(Color.BLACK);
		    g.drawString(randomText, 5, 38);
		    g.dispose();
		} catch (IOException e) {
			logger.error("Exception while loading captcha image", e);
		}
		return image;
	}
	
	@RequestMapping(value = "/refreshCaptcha", method = RequestMethod.POST)
	public @ResponseBody String refreshCatcha(HttpSession session) {
		logger.info("Refreshing captcha");
		createNewCaptchaText(session);
		
		return "";
	}

	private String createNewCaptchaText(HttpSession session) {
		String randomText = RandomStringUtils.randomAlphabetic(6);
		session.setAttribute(CAPTCHA_SESSION_KEY, randomText);
		
		return randomText;
	}
}
