package com.example.guestbook.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.guestbook.controller.LoginController;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

	@InjectMocks
	LoginController appController;

	private MockMvc mockMvc;

	@Before
	public void onSetUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(appController).build();
	}

	@Test
	public void testInitialLogin_Sucess() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login-page"));
	}

	@Test
	public void testInitialLogin_404() throws Exception {
		mockMvc.perform(get("/loginadmin")).andExpect(status().isNotFound());
	}

	@Test
	public void testInitialLogin_404_Post() throws Exception {
		mockMvc.perform(post("/loginadmin")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testInitialLogin_403() throws Exception {
		mockMvc.perform(get("/403")).andExpect(status().isOk()).andExpect(view().name("403-forbidden"));
	}

}
