package com.example.guestbook.controller;


import javax.validation.Valid;

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
   
	@Autowired
	private UserService userService;

	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration-form";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto registrationDto,BindingResult bindingResult) {
		 User existing = userService.findByEmail(registrationDto.getEmail());
	        if (existing != null){
	        	bindingResult.rejectValue("email", null, "There is already an account registered with that email");
	        }

	        if (bindingResult.hasErrors()){
	            return "registration-form";
	        }

	        try {
				userService.save(registrationDto);
			} catch (Exception e) {
				return "redirect:/registration?error";
			}
	        return "redirect:/login?success";
	}
}
