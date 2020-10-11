package com.example.guestbook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
	
    
    @GetMapping("/login")
	public ModelAndView  showMyLoginPage() {
		
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("guestBookLogin");
	    return modelAndView;
		
	}

}
