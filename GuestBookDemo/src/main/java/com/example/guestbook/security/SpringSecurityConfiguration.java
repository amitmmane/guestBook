package com.example.guestbook.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
			.antMatchers("/").hasAnyRole("USER","ADMIN")
			.antMatchers("/user/**").hasRole("USER")
			.and().formLogin().loginPage("/login").loginProcessingUrl("/intialLogin").permitAll()
			.and().logout().permitAll()
			.and().exceptionHandling().accessDeniedPage("");
	    }

}
