package com.example.guestbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.guestbook.entity.Feedback;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {
	
	public List<Feedback> findAllByUserId(String userId)throws Exception;
	
}
