package com.example.guestbook.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.guestbook.entity.User;
import com.example.guestbook.helper.UserRegistrationDto;
import com.example.guestbook.service.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

	@Autowired
	private UserService userService;

	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	/* Display the registration-form to user */
	@GetMapping
	public String showRegistrationForm() {
		
		logger.info("Entered into registerUserAccount.showRegistrationForm()");
		return "registration-form";
	}
	

	/* returns the new registered user upon validating the user details */
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
			BindingResult bindingResult) {

		logger.info("Entered into registerUserAccount.registerUserAccount() with User as{}", registrationDto);
		try {
			
			User existing = userService.findByEmail(registrationDto.getEmail());
			if (existing != null) {
				bindingResult.rejectValue("email", null, "There is already an account registered with that email");
				logger.debug("User already exisits with email {}", existing.getEmail());
			}

			if (bindingResult.hasErrors()) {
				logger.debug("Some errors occured while validating the user details{}", bindingResult.getAllErrors().toString());
				return "registration-form";
				
			}
			userService.save(registrationDto);
		} catch (Exception e) {
			logger.error("Some exception occured  while registerUserAccount.registerUserAccount() {}",e.getMessage(), e );
			return "redirect:/registration?error";
		}
		return "redirect:/login?success";
	}
}
