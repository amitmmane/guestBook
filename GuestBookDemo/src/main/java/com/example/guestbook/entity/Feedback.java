package com.example.guestbook.entity;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="feedbackdetails")
public class Feedback {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	@Column(name="id")
	private int id;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="first_name")
	private String firstName;
	

	@Column(name="feedback_text")
	private String feedbackText;
	
	@Column(name="feedback_image_name")
	private String feedbackImageName;
	
	@Lob
	@Column(name="feedback_image")
	private byte[] feedbackImage;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="feedback_time")
	private Date feedbackTime;
	
	@Column(nullable = false, columnDefinition = "BIT", length = 1)
	private boolean feedbackApproved = false;;


	public boolean isFeedbackApproved() {
		return feedbackApproved;
	}

	public void setFeedbackApproved(boolean feedbackApproved) {
		this.feedbackApproved = feedbackApproved;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public String getFeedbackImageName() {
		return feedbackImageName;
	}

	public void setFeedbackImageName(String feedbackImageName) {
		this.feedbackImageName = feedbackImageName;
	}

	public byte[] getFeedbackImage() {
		return feedbackImage;
	}

	public void setFeedbackImage(byte[] feedbackImage) {
		this.feedbackImage = feedbackImage;
	}

	public Date getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "Feedback [id=" + id + ", userId=" + userId + ", firstName=" + firstName + ", feedbackText="
				+ feedbackText + ", feedbackImageName=" + feedbackImageName + ", feedbackImage="
				+ Arrays.toString(feedbackImage) + ", feedbackTime=" + feedbackTime + ", feedbackApproved="
				+ feedbackApproved + "]";
	}


	
	
	
}
