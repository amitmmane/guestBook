package com.example.guestbook.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.example.guestbook.helper.GusetbookConstants;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;

	@Autowired
	private DataSource securityDataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().dataSource(securityDataSource);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/").hasRole(GusetbookConstants.ADMIN).antMatchers("/admin/**")
				.hasRole(GusetbookConstants.ADMIN).antMatchers("/user/**")
				.hasAnyRole(GusetbookConstants.USER, GusetbookConstants.ADMIN).and().formLogin().loginPage("/login")
				.loginProcessingUrl("/intialLogin").successHandler(successHandler).permitAll().and().logout()
				.permitAll().and().exceptionHandling().accessDeniedPage("/invalid");

				http.headers().defaultsDisabled().cacheControl();
		
		
	}

}
