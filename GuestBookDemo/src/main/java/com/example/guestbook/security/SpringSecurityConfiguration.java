package com.example.guestbook.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.authorizeRequests()
	                .antMatchers("/admin").hasRole("ADMIN")
	                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
	                .antMatchers("/").permitAll()
	                .and().formLogin();
	    }

}
