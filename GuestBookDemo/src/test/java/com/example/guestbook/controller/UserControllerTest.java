package com.example.guestbook.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.guestbook.entity.Feedback;
import com.example.guestbook.entity.User;
import com.example.guestbook.service.FeedbackService;
import com.example.guestbook.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@InjectMocks
	UserController appController;
	
	@Mock
	UserService userService;

	@Mock
	FeedbackService feedbackService;

	@Captor
	ArgumentCaptor<String> stringCaptor;

	@Captor
	ArgumentCaptor<Integer> intCaptor;

	private MockMvc mockMvc;

	@Before
	public void onSetUp() {

		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("aName");

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(appController).build();
		
		ReflectionTestUtils.setField(appController, "errorMessage", "Some error occurred please try again");
		ReflectionTestUtils.setField(appController, "userFeebackPending", "Your Feedback Is Pending For Admin Approval");
	}

	@Test
	public void testWelcomeUser_Sucess() throws Exception {
		
		User user= new User() ;
		user.setFirstName("aName");
		Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
		mockMvc.perform(get("/user/welcomeUser")).andExpect(status().isOk()).andExpect(view().name("welcome-user"))
				.andExpect(model().attribute("loggedinUserName", "aName")).andReturn();
	}

	@Test
	public void testWelcomeUse_Failure() throws Exception {
		mockMvc.perform(get("/user/welcomeAdmin")).andExpect(status().isNotFound()).andReturn();
	}

	@Test
	public void testViewFeedbackUser_Success() throws Exception {
		/* When Feedbacks are present */

		List<Feedback> value = new ArrayList<>();
		Feedback f1 = new Feedback();
		f1.setId(1);
		Mockito.when(feedbackService.findFeebackByUserId("aName")).thenReturn(value);

		mockMvc.perform(get("/user/viewFeedbackUser")).andExpect(status().isOk())
				.andExpect(view().name("user-feedback")).andExpect(model().attribute("loggedinUserName", "aName"))
				.andExpect(model().attributeExists("feedbackList")).andExpect(model().attribute("feedbackList", value))
				.andReturn();
	}

	@Test
	public void testViewFeedbackUser_Success1() throws Exception {
		/* When Feedbacks returned null from feedbackService */

		List<Feedback> value = null;
		Mockito.when(feedbackService.findFeebackByUserId("aName")).thenReturn(value);

		mockMvc.perform(get("/user/viewFeedbackUser")).andExpect(status().isOk())
				.andExpect(view().name("user-feedback")).andExpect(model().attribute("loggedinUserName", "aName"))
				.andExpect(model().attributeDoesNotExist("feedbackList")).andReturn();
	}

	@Test
	public void testViewFeedbackUser_Failure() throws Exception {
		/* When feedbackService throws exception */
		Mockito.when(feedbackService.findFeebackByUserId("aName")).thenThrow(NullPointerException.class);

		mockMvc.perform(get("/user/viewFeedbackUser")).andExpect(status().isOk())
				.andExpect(view().name("user-feedback")).andExpect(model().attribute("loggedinUserName", "aName"))
				.andExpect(model().attributeDoesNotExist("feedbackList")).andReturn();
	}

	@Test
	public void testAddFeedback_Sucess() throws Exception {
		mockMvc.perform(get("/user/addFeedback")).andExpect(status().isOk()).andExpect(view().name("feedback-form"));
	}

	@Test
	public void testSubmitFeedback_Success() throws Exception {

		/* When feedback is added successfully by User with option Image */
		doNothing().when(feedbackService).saveFeedback(Mockito.any(Feedback.class));
		
		User user= new User() ;
		user.setFirstName("Amit");
		Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);

		MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
		mockMvc.perform(multipart("/user/submitFeedback/image").file(file).param("id", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Your Feedback Is Pending For Admin Approval"));

		verify(feedbackService, times(1)).saveFeedback(Mockito.any(Feedback.class));
	}
	
	
	@Test
	public void testSubmitFeedback_Success1() throws Exception {

		/* When feedback is added successfully by User with option text */
		doNothing().when(feedbackService).saveFeedback(Mockito.any(Feedback.class));
		
		User user= new User() ;
		user.setFirstName("Amit");
		Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);

		mockMvc.perform(post("/user/submitFeedback/text").param("id", "1").param("feedbacktext", "sample"))
				.andExpect(status().is3xxRedirection()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Your Feedback Is Pending For Admin Approval"));

		verify(feedbackService, times(1)).saveFeedback(Mockito.any(Feedback.class));
	}

	@Test
	public void testSubmitFeedback_Failure() throws Exception {

		/*
		 * When while adding feedback exception is thrown by
		 * feedbackService.saveFeedback()
		 */
		doThrow(NullPointerException.class).when(feedbackService).saveFeedback(Mockito.any(Feedback.class));
		User user= new User() ;
		user.setFirstName("Amit");
		Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
		MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
		mockMvc.perform(multipart("/user/submitFeedback/image").file(file).param("id", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some error occurred please try again"));

		verify(feedbackService, times(1)).saveFeedback(Mockito.any(Feedback.class));
	}

	@Test
	public void testGetFeedbackImage_Success() throws Exception {

		/* get image from DB */

		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);
		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);

		mockMvc.perform(get("/user/getFeedbackImage").param("id", "1")).andExpect(status().is3xxRedirection());

		verify(feedbackService, times(1)).findFeedbackById(1);
	}

	@Test
	public void testGetFeedbackImage_Failure() throws Exception {

		/* Exception is thrown while getting image from DB */
		Feedback f1 = new Feedback();
		f1.setId(1);
		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenThrow(NullPointerException.class);

		mockMvc.perform(get("/user/getFeedbackImage").param("id", "1")).andExpect(status().is3xxRedirection());

		verify(feedbackService, times(1)).findFeedbackById(1);
	}
}
