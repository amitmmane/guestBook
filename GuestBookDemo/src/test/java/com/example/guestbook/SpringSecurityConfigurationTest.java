package com.example.guestbook;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class SpringSecurityConfigurationTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private FilterChainProxy filterChainProxy;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).dispatchOptions(true).addFilters(filterChainProxy)
				.apply(springSecurity()).build();
	}


	@Test
	@WithMockUser(username = "Komal", roles = { "USER" })

	/*
	 * When person with ADMIN role tries to access the /admin/** uri/  He is not
	 * allowed to access
	 */

	public void givenAuthRequestOnPrivateService_shouldSucceedWith403() throws Exception {
		mvc.perform(get("/admin/welcomeAdmin").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}


	@Test
	@WithMockUser(username = "Amit", roles = { "NA" })

	/*
	 * When person with Undefined role tries to access the /user/**  uri  He is
	 * not allowed to access
	 */

	public void givenAuthRequestOnPrivateService_WithAnonymousRole() throws Exception {
		mvc.perform(get("/user/welcomeUser").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}
}
