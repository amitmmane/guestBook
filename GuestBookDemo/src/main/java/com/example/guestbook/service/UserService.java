package com.example.guestbook.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.guestbook.entity.User;
import com.example.guestbook.helper.UserRegistrationDto;


public interface UserService extends UserDetailsService{
	public User save(UserRegistrationDto registrationDto);
	public User findByEmail(String email);
       
   
}
