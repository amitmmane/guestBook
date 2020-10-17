package com.example.guestbook.service;

import java.util.List;
import java.util.Optional;

import com.example.guestbook.entity.Feedback;



public interface FeedbackService {
	
	public void deleteFeedbackById(int id)throws Exception;
	public List<Feedback> findFeebackByUserId(String userId)throws Exception;
	public List<Feedback> findAllFeedbacks()throws Exception;
	public Optional<Feedback> findFeedbackById(int id)throws Exception;
	public void saveFeedback(Feedback user)throws Exception;

}
