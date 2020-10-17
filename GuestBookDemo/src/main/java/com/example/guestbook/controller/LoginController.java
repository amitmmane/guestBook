package com.example.guestbook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);
	@GetMapping("/login")
	public String showMyLoginPage() {
		logger.info("Entered into LoginController.showMyLoginPage()");
		return "login-page";

	}
	
	@GetMapping("/403")
	public String showAccessDenied() {
		return "403-forbidden";
	}
	
}
