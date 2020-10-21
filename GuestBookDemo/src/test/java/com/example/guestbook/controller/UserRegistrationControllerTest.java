package com.example.guestbook.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import com.example.guestbook.entity.User;
import com.example.guestbook.helper.UserRegistrationDto;
import com.example.guestbook.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistrationControllerTest {

	
	@InjectMocks
	UserRegistrationController appController;
	
	@Mock
	UserService userService;
	
	@Mock
	BindingResult bindingResult;
	
	private MockMvc mockMvc;
	
	@Before
	public void onSetUp() {

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(appController).build();
	}
	
	@Test
	public void testRegisterUserAccountr_Sucess() throws Exception {
		
		/*
		 * When there is existing user with same email id
		 * user is not allowed to register and redirected to registration-form
		 */
		
		User value = new User();
		value.setEmail("aEmail");
		value.setFirstName("aFirstName");
		value.setId((long) 1);
		value.setLastName("aLastName");
		value.setPassword("aPwd");
		
		UserRegistrationDto dto =  new UserRegistrationDto();
		dto.setEmail("aName");
		when(userService.findByEmail(Mockito.anyString())).thenReturn(value);
		mockMvc.perform(post("/registration").flashAttr("user", dto)).andExpect(status().isOk()).andExpect(view().name("registration-form"))
		.andReturn();
		
	}
	
	
	@Test
	public void testRegisterUserAccountr_Sucess1() throws Exception {
		
		/*
		 * When there is no existing user with  email id
		 * user is  allowed to register and redirected to login form
		 * But binding result has errors.
		 */
		
		UserRegistrationDto dto =  new UserRegistrationDto();
		dto.setEmail("aName");
		mockMvc.perform(post("/registration").flashAttr("user", dto)).andExpect(status().isOk()).andExpect(view().name("registration-form"))
		.andReturn();
		
	}
	
	
	@Test
	public void testRegisterUserAccountr_Sucess2() throws Exception {
		
		/*
		 * When there is no existing user with  email id
		 * user is  allowed to register  
		 * No binding result has errors.
		 */
		

		User value = new User();
		value.setEmail("aEmail");
		value.setFirstName("aFirstName");
		value.setId((long) 1);
		value.setLastName("aLastName");
		value.setPassword("aPwd");
		
		when(userService.save(Mockito.any(UserRegistrationDto.class))).thenReturn(value);
		
		UserRegistrationDto dto =  new UserRegistrationDto();
		dto.setEmail("a@bt.com");
		mockMvc.perform(post("/registration").flashAttr("user", dto)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?success"))
		.andReturn();
		
	}
	
	@Test
	public void testRegisterUserAccountr_Error() throws Exception {
		
		/*
		 * When there is no existing user with  email id
		 * user is  allowed to register  
		 * No binding result has errors.
		 */
		

		User value = new User();
		value.setEmail("aEmail");
		value.setFirstName("aFirstName");
		value.setId((long) 1);
		value.setLastName("aLastName");
		value.setPassword("aPwd");
		
		when(userService.save(Mockito.any(UserRegistrationDto.class))).thenThrow(NullPointerException.class);
		
		UserRegistrationDto dto =  new UserRegistrationDto();
		dto.setEmail("a@bt.com");
		mockMvc.perform(post("/registration").flashAttr("user", dto)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/registration?error"))
		.andReturn();
		
	}
	
	
	@Test
	public void testRegisterUserAccount_Sucess_GET_methode() throws Exception {
		
		/*
		 * Use requests for the registration page
		 */
		mockMvc.perform(get("/registration")).andExpect(status().isOk()).andExpect(view().name("registration-form"))
		.andReturn();
		
	}
}
