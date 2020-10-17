package com.example.guestbook.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.guestbook.entity.Feedback;
import com.example.guestbook.repository.FeedbackRepo;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackRepo feedbackRepo;

	@Override
	public void deleteFeedbackById(int id)throws Exception {
		feedbackRepo.deleteById(id);
	}

	@Override
	public List<Feedback> findFeebackByUserId(String userId)throws Exception {
		return feedbackRepo.findAllByUserId(userId);
	}

	@Override
	public List<Feedback> findAllFeedbacks()throws Exception  {
		return feedbackRepo.findAll();
	}

	@Override
	public Optional<Feedback> findFeedbackById(int id)throws Exception {
		return feedbackRepo.findById(id);
	}

	@Override
	public void saveFeedback(Feedback user)throws Exception {
		feedbackRepo.save(user);
	}

}
