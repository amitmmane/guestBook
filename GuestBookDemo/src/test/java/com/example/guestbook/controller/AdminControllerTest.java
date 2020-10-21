package com.example.guestbook.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.guestbook.entity.Feedback;
import com.example.guestbook.entity.User;
import com.example.guestbook.service.FeedbackService;
import com.example.guestbook.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

	@InjectMocks
	AdminController appController;

	@Mock
	FeedbackService feedbackService;

	@Mock
	UserService userService;

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
	}

	@Test
	public void testWelcomeAdmin_Sucess() throws Exception {
		
		User user= new User() ;
		user.setFirstName("aName");
		Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(user);
		mockMvc.perform(get("/admin/welcomeAdmin")).andExpect(status().isOk()).andExpect(view().name("welcome-admin"))
				.andExpect(model().attribute("loggedinUserName", "aName")).andReturn();

	}

	@Test
	public void testWelcomeAdmin_Failure() throws Exception {

		mockMvc.perform(get("/welcomeAdmin")).andExpect(status().isNotFound()).andReturn();
	}

	@Test
	public void testViewFeedbackAdmin_Success() throws Exception {

		/* When Feedbacks are present */

		List<Feedback> value = new ArrayList<>();
		Feedback f1 = new Feedback();
		f1.setId(1);

		Mockito.when(feedbackService.findAllFeedbacks()).thenReturn(value);

		mockMvc.perform(get("/admin/viewFeedbackAdmin")).andExpect(status().isOk())
				.andExpect(view().name("admin-feedback")).andExpect(model().attribute("loggedinUserName", "aName"))
				.andExpect(model().attributeExists("feedbackList")).andExpect(model().attribute("feedbackList", value))
				.andReturn();
	}

	@Test
	public void testViewFeedbackAdmin_Success1() throws Exception {

		/* When Feedbacks returned null from feedbackService */

		List<Feedback> value = null;
		Mockito.when(feedbackService.findAllFeedbacks()).thenReturn(value);

		mockMvc.perform(get("/admin/viewFeedbackAdmin")).andExpect(status().isOk())
				.andExpect(view().name("admin-feedback")).andExpect(model().attribute("loggedinUserName", "aName"))
				.andExpect(model().attributeDoesNotExist("feedbackList")).andReturn();
	}

	@Test
	public void testViewFeedbackAdmin_Failure() throws Exception {

		/* When feedbackService throws exception */
		Mockito.when(feedbackService.findAllFeedbacks()).thenThrow(NullPointerException.class);

		mockMvc.perform(get("/admin/viewFeedbackAdmin")).andExpect(status().isOk())
				.andExpect(view().name("admin-feedback")).andExpect(model().attribute("loggedinUserName", "aName"))
				.andExpect(model().attributeDoesNotExist("feedbackList")).andReturn();
	}

	@Test
	public void testDeleteFeedback_Success() throws Exception {

		/* When deleteFeedbackById is success */

		doNothing().when(feedbackService).deleteFeedbackById(intCaptor.capture());

		mockMvc.perform(get("/admin/deleteFeedback").param("id", "1")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/viewFeedbackAdmin"))
				.andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Feedback Has Been Deleted Successfully")).andReturn();

		verify(feedbackService, times(1)).deleteFeedbackById(1);
		int id = intCaptor.getValue();
		assertEquals(1, id);
	}

	@Test
	public void testDeleteFeedback_Failure() throws Exception {

		/* When feedbackService.deleteFeedbackById throws exception */

		doThrow(NullPointerException.class).when(feedbackService).deleteFeedbackById(intCaptor.capture());

		mockMvc.perform(get("/admin/deleteFeedback").param("id", "1")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/viewFeedbackAdmin"))
				.andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some Problem Occured while deleting the Feedback"))
				.andReturn();

		verify(feedbackService, times(1)).deleteFeedbackById(1);
		int id = intCaptor.getValue();
		assertEquals(1, id);
	}

	@Test
	public void testApproveFeedback_Success() throws Exception {

		/* When Feedbacks is approved successfully by admin */

		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);
		doNothing().when(feedbackService).saveFeedback(f1);

		mockMvc.perform(get("/admin/approveFeedback").param("id", "1")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/viewFeedbackAdmin"))
				.andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Feedback Approved Succeesfully")).andReturn();

		verify(feedbackService, times(1)).findFeedbackById(1);
		verify(feedbackService, times(1)).saveFeedback(f1);
	}

	@Test
	public void testApproveFeedback_Failure() throws Exception {

		/* When feedbackService.saveFeedback throws exception */

		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);
		doThrow(NullPointerException.class).when(feedbackService).saveFeedback(f1);

		mockMvc.perform(get("/admin/approveFeedback").param("id", "1")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/viewFeedbackAdmin"))
				.andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some problem occured while approving the feedback"))
				.andReturn();

		verify(feedbackService, times(1)).findFeedbackById(1);
		verify(feedbackService, times(1)).saveFeedback(f1);
	}

	@Test
	public void testApproveFeedback_Failure1() throws Exception {

		/* When feedbackService.findFeedbackById throws exception */
		Feedback f1 = new Feedback();
		f1.setId(1);
		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenThrow(NullPointerException.class);

		/* Then */
		mockMvc.perform(get("/admin/approveFeedback").param("id", "1")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/viewFeedbackAdmin"))
				.andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some problem occured while approving the feedback"))
				.andReturn();

		/* Assert */
		verify(feedbackService, times(1)).findFeedbackById(1);
		verify(feedbackService, times(0)).saveFeedback(f1);
	}

	@Test
	public void testEditFeedback_Success() throws Exception {

		/* When Feedbacks is selected for Edit by admin */

		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);

		mockMvc.perform(get("/admin/editFeedback").param("id", "1")).andExpect(status().isOk())
				.andExpect(view().name("edit-feedback")).andExpect(model().attributeExists("feedback"))
				.andExpect(model().attribute("feedback", f1)).andReturn();

		verify(feedbackService, times(1)).findFeedbackById(1);
	}

	@Test
	public void testEditFeedback_Failure() throws Exception {

		/* When feedbackService.findFeedbackById returns null */

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(null);

		mockMvc.perform(get("/admin/editFeedback").param("id", "1")).andExpect(status().isOk())
				.andExpect(view().name("edit-feedback")).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some problem occured")).andReturn();

		verify(feedbackService, times(1)).findFeedbackById(1);
	}

	@Test
	public void testEditFeedback_Failure1() throws Exception {

		/* When feedbackService.findFeedbackById throws exception */
		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenThrow(NullPointerException.class);

		mockMvc.perform(get("/admin/editFeedback").param("id", "1")).andExpect(status().isOk())
				.andExpect(view().name("edit-feedback")).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some problem occured")).andReturn();

		verify(feedbackService, times(1)).findFeedbackById(1);
	}

	@Test
	public void testEditedFeedback_Success() throws Exception {

		/* When feedback is edited successfully by Admin */
		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);
		doNothing().when(feedbackService).saveFeedback(f1);

		MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", "a123"))
				.andExpect(status().is3xxRedirection()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Feedback Edited Successfully"));

		verify(feedbackService, times(1)).saveFeedback(f1);
	}
	

	@Test
	public void testEditedFeedback_Failure() throws Exception {

		/*
		 * When feedbackService.findFeedbackById throws exception while editing the
		 * feedback by Admin
		 */

		Feedback f1 = new Feedback();
		f1.setId(1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenThrow(NullPointerException.class);

		MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", "a123"))
				.andExpect(status().is3xxRedirection()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some problem occured while editing the feedback"));

	}

	@Test
	public void testEditedFeedback_Failure1() throws Exception {

		/*
		 * When feedbackService.saveFeedback throws exception while editing the feedback
		 * by Admin
		 */
		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);
		doThrow(NullPointerException.class).when(feedbackService).saveFeedback(f1);

		MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", "a123"))
				.andExpect(status().is3xxRedirection()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some problem occured while editing the feedback"));

		verify(feedbackService, times(1)).saveFeedback(f1);
	}
	

	@Test
	public void testEditedFeedback_Failure2() throws Exception {

		/* When Admin does not modifies any of feedback options 
		 * he will be redirected to same page */
		MockMultipartFile file = new MockMultipartFile("file", "", null, "bar".getBytes());
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", ""))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/editFeedback?error=true&id=1"));
	}
	
	
	@Test
	public void testEditedFeedback_Failure3() throws Exception {

		/* When feedbackService.findFeedbackById returns null */

		Optional<Feedback> optional = Optional.empty();
		Mockito.when(feedbackService.findFeedbackById(1)).thenReturn(optional);

		MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", "a123"))
				.andExpect(status().is3xxRedirection()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", "Some problem occured while editing the feedback"));

	}
	
	@Test
	public void testEditedFeedback_Success1() throws Exception {

		/* When Admin modifies any of feedback text 
		 * feedback will be successfully modified */
		MockMultipartFile file = new MockMultipartFile("file", "", null, "bar".getBytes());
		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);
		doNothing().when(feedbackService).saveFeedback(f1); 
		
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", "1234"))
		.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/viewFeedbackAdmin?message=Feedback+Edited+Successfully"))
		.andExpect(model().attributeExists("message"))
		.andExpect(model().attribute("message", "Feedback Edited Successfully"));
	}
	
	
	@Test
	public void testEditedFeedback_Success2() throws Exception {

		/* When Admin modifies any of feedback image 
		 * feedback will be successfully modified */
		MockMultipartFile file = new MockMultipartFile("file", "origi", null, "bar".getBytes());
		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);
		doNothing().when(feedbackService).saveFeedback(f1); 
		
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", ""))
		.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/viewFeedbackAdmin?message=Feedback+Edited+Successfully"))
		.andExpect(model().attributeExists("message"))
		.andExpect(model().attribute("message", "Feedback Edited Successfully"));
	}
	
	
	@Test
	public void testEditedFeedback_Success3() throws Exception {

		/* When Admin modifies any of feedback image and Feedback text
		 * feedback will be successfully modified */
		MockMultipartFile file = new MockMultipartFile("file", "origi", null, "bar".getBytes());
		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);

		Mockito.when(feedbackService.findFeedbackById(Mockito.anyInt())).thenReturn(optional);
		doNothing().when(feedbackService).saveFeedback(f1); 
		
		mockMvc.perform(multipart("/admin/editedFeedback").file(file).param("id", "1").param("feedbacktext", "1234"))
		.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/viewFeedbackAdmin?message=Feedback+Edited+Successfully"))
		.andExpect(model().attributeExists("message"))
		.andExpect(model().attribute("message", "Feedback Edited Successfully"));
	}
	

}
