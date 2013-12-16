package com.kucowka.whereismymobile.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kucowka.whereismymobile.dao.CredentialsDao;
import com.kucowka.whereismymobile.dao.TimeBoundFeaturesDao;
import com.kucowka.whereismymobile.models.Credentials;
import com.kucowka.whereismymobile.models.TimeBoundFeatures;
import com.kucowka.whereismymobile.ui.models.ResetPassword;
import com.kucowka.whereismymobile.util.DateTimeUtil;
import com.kucowka.whereismymobile.util.SendMail;
import com.kucowka.whereismymobile.util.models.MailMessage;

@Controller
public class ResetPasswordController extends AbstractController {
	
	private static final Logger logger = Logger.getLogger(ResetPasswordController.class);

	@Autowired
	private TimeBoundFeaturesDao timeBoundFeaturesDao;

	@Autowired
	private DateTimeUtil dateTimeUtil;

	@Autowired
	private SendMail sendMail;

	@Autowired
	private CredentialsDao credentialsDao;

	@RequestMapping(value = "resetPassword", method = RequestMethod.GET)
	public String getPasswordReset(ResetPassword resetPassword, Model model) {

		if (!StringUtils.isEmpty(resetPassword.getCode())) {
			List<TimeBoundFeatures> list = timeBoundFeaturesDao.getById(resetPassword.getCode());
			if (list.size() == 1) {
				TimeBoundFeatures timeBoundFeatures = list.get(0);
				logger.info(timeBoundFeatures);
				if (isExpired(timeBoundFeatures)) {
					model.asMap().put(errorMessage, "Reset link has expired. Please repeat the reset procedure.");
					resetPassword.setCode(null);
				} else {
					resetPassword.setAccountId(timeBoundFeatures.getFeatureContent());
					model.asMap().put(infoMessage, "Type your new password.");
				}
			} else {
				resetPassword.setCode(null);
				model.asMap().put(errorMessage, "Unknown password reset code. Please request password reset once again");
			}
			
		}
		
		model.asMap().put("resetResult", false);
		model.asMap().put("resetPassword", resetPassword);
		return "resetPassword";
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public String doResetPassword(@Valid ResetPassword resetPassword,
			BindingResult result, Model model, HttpSession session) {

		String error = null;
		String info = null;
		boolean resetResult = false;
		
		if (!result.hasErrors()) {
			if (StringUtils.isEmpty(resetPassword.getCode())) {
				List<Credentials> credentialsList = credentialsDao
					.getById(resetPassword.getAccountId());

				if (credentialsList.size() > 0) {
					TimeBoundFeatures t = new TimeBoundFeatures();
					t.setId(UUID.randomUUID().toString().replace("-", ""));
					t.setExpireTime(dateTimeUtil.getDate(1).getTime()); // valid for
					t.setValid(true);												// a day
					t.setFeatureContent(resetPassword.getAccountId());
					timeBoundFeaturesDao.save(t);

					boolean emailSentResult = sendMessage(t);
					if (emailSentResult) {
						info = "An email with a link has been sent. Please check your mail. Link will be valid for the next 24 hours.";
					} else {
						error = "Email sent failure. Please try again.";
					}
				} else {
					error = "Sorry, but there is no account with this email.";
				}
			} else {
				// load credentials and try to reset password
				if (!resetPassword.getPassword().equals(resetPassword.getRepeatPassword())) {
					error = "Passwords do not match";
				} else {
					List<TimeBoundFeatures> list = timeBoundFeaturesDao.getById(resetPassword.getCode());
					TimeBoundFeatures timeBoundFeatures = null;
					if (list.size() == 1) {
						timeBoundFeatures = list.get(0);
						if (isExpired(timeBoundFeatures)) {
							model.asMap().put(errorMessage, "Reset link has expired. Please repeat the reset procedure.");
							resetPassword.setCode(null);
						} else {
							List<Credentials> credentialsList = credentialsDao.getById(resetPassword.getAccountId());
							if (credentialsList.size() == 0) {
								error = "Account for this email was not found."; 
							} else {
								Credentials credentials = credentialsList.get(0);
								if (!resetPassword.getAccountId().equals(timeBoundFeatures.getFeatureContent())) {
									error = "Can't reset password for different account: " + resetPassword.getAccountId(); 
								} else {
									credentials.setPassword(resetPassword.getPassword());
									credentialsDao.save(credentials);
									// invalidate
									timeBoundFeatures.setValid(false);
									timeBoundFeaturesDao.save(timeBoundFeatures);
									resetResult = true;
								}
							}
						}
					}
				}
			}
		}

		model.asMap().put("resetResult", resetResult );
		model.asMap().put(infoMessage, info);
		model.asMap().put(errorMessage, error);
		model.asMap().put("resetPassword", resetPassword);
		return "resetPassword";
	}

	protected boolean isExpired(TimeBoundFeatures timeBoundFeatures) {
		return new Date().getTime() > timeBoundFeatures.getExpireTime() 
				|| !timeBoundFeatures.isValid();
	}

	protected boolean sendMessage(TimeBoundFeatures t) {
		MailMessage mailMessage = new MailMessage();
		mailMessage.setSubject("Password reset request");
		mailMessage.setTo(t.getFeatureContent());
		StringBuilder sb = new StringBuilder();
		sb.append("<p>To reset your password click on this link: <a href=\"http://whereismymobile.com/resetPassword?code=");
		sb.append(t.getId());
		sb.append("\">reset link</a>.");
		sb.append("</p>");
		sb.append("<p>Ignore this email if you have not requested email reset.</p>");
		mailMessage.setBody(sb.toString());
		return sendMail.sendMail(mailMessage);
	}
}
