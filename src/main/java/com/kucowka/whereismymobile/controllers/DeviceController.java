package com.kucowka.whereismymobile.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeviceController {

	@Autowired
	private DeviceResolver deviceResolver;
	
	@RequestMapping(value = "deviceTester")
	public @ResponseBody String detectDevice(HttpServletRequest request) {
		
		Device device = deviceResolver.resolveDevice(request);
		if (device == null) {
			return "null";
		}
		String deviceType = "unknown";
		if (device.isNormal()) {
			deviceType = "normal";
		} else if (device.isMobile()) {
			deviceType = "mobile";
		} else if (device.isTablet()) {
			deviceType = "tablet";
		}
		return "Hello " + deviceType + " browser!";
	}
}
