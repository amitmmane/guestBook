package com.example.guestbook.entity;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.guestbook.entity.Feedback;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackTest {
	
	@InjectMocks
	 Feedback feedback;
	
	
	@Test
	public void testFeedBack_toString() {
		
		Feedback fb = new Feedback();
		byte[] feedbackImage = null;
		fb.setFeedbackImage(feedbackImage);
		fb.setFeedbackImageName("feedbackImageName");
		fb.setFeedbackText("feedbackText");
		Date feedbackTime = new Date();
		fb.setFeedbackTime(feedbackTime);
		fb.setFirstName("firstName");
		fb.setId( 1);
		fb.setFeedbackApproved(true);
		fb.setUserId("userId");
		feedback.toString();
	}
}
