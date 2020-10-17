package com.example.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.guestbook.entity.Feedback;
import com.example.guestbook.repository.FeedbackRepo;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceTest {

	@InjectMocks
	private FeedbackServiceImpl feedbackService;

	@Mock
	private FeedbackRepo feedbackRepo;

	@Test
	public void testDeleteFeedbackById_Sucess() throws Exception {

		doNothing().when(feedbackRepo).deleteById(Mockito.anyInt());
		feedbackService.deleteFeedbackById(1);
		verify(feedbackRepo, times(1)).deleteById(1);
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteFeedbackById_Failure() throws Exception {

		doThrow(NullPointerException.class).when(feedbackRepo).deleteById(Mockito.anyInt());
		feedbackService.deleteFeedbackById(1);
	}

	@Test
	public void testfindFeebackByUserId_Sucess() throws Exception {

		List<Feedback> expectedvalue = new ArrayList<>();
		Mockito.when(feedbackRepo.findAllByUserId(Mockito.anyString())).thenReturn(expectedvalue);

		List<Feedback> actulaValue = feedbackService.findFeebackByUserId("Amit");

		verify(feedbackRepo, times(1)).findAllByUserId(Mockito.anyString());
		assertEquals(expectedvalue, actulaValue);
	}

	@Test(expected = NullPointerException.class)
	public void testFindFeebackByUserId_Failure() throws Exception {

		Mockito.when(feedbackRepo.findAllByUserId(Mockito.anyString())).thenThrow(NullPointerException.class);
		feedbackService.findFeebackByUserId("Amit");
	}

	@Test
	public void testFindAllFeedbacks_Sucess() throws Exception {

		List<Feedback> expectedvalue = new ArrayList<>();
		Mockito.when(feedbackRepo.findAll()).thenReturn(expectedvalue);

		List<Feedback> actulaValue = feedbackService.findAllFeedbacks();

		verify(feedbackRepo, times(1)).findAll();
		assertEquals(expectedvalue, actulaValue);
	}

	@Test(expected = NullPointerException.class)
	public void testFindAllFeedbacks_Failure() throws Exception {

		Mockito.when(feedbackRepo.findAll()).thenThrow(NullPointerException.class);
		feedbackService.findAllFeedbacks();

	}

	@Test
	public void testFindFeedbackById_Sucess() throws Exception {

		Feedback f1 = new Feedback();
		f1.setId(1);
		Optional<Feedback> optional = Optional.of(f1);
		Mockito.when(feedbackRepo.findById(Mockito.anyInt())).thenReturn(optional);

		Optional<Feedback> actulaValue = feedbackService.findFeedbackById(1);

		verify(feedbackRepo, times(1)).findById(Mockito.anyInt());
		assertEquals(optional, actulaValue);
	}

	@Test(expected = NullPointerException.class)
	public void testFindFeedbackById_Failure() throws Exception {

		Mockito.when(feedbackRepo.findById(Mockito.anyInt())).thenThrow(NullPointerException.class);
		feedbackService.findFeedbackById(1);
	}
	
	@Test
	public void testSaveFeedback_Sucess() throws Exception {
		Feedback user = new Feedback();
		Mockito.when(feedbackRepo.save(Mockito.any())).thenReturn(user);
		feedbackService.saveFeedback(user);;
		verify(feedbackRepo, times(1)).save(user);
	}

}
