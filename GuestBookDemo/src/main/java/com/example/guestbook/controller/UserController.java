package com.example.guestbook.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.guestbook.entity.Feedback;
import com.example.guestbook.helper.GusetbookConstants;
import com.example.guestbook.service.FeedbackService;

@Controller
public class UserController extends GusetbookConstants {

	@Autowired
	private FeedbackService feedbackService;
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/user/welcomeUser")
	public String welcomeUser(Model model) {

		logger.info("Entered into UserController.welcome()");
		model.addAttribute(LOGGED_IN_USERNAME, getLoggedUseName());
		return "welcome-user";
	}

	@GetMapping("/user/viewFeedbackUser")
	public String viewFeedbackUser(Model model) {
		logger.info("Entered into UserController.viewFeedbackUser()");

		List<Feedback> feedbackList =null;
		try {
			feedbackList = feedbackService.findAllFeedbacks();
			logger.debug("List of Feedbacks obtained from UserController.viewFeedbackUser() is {}", feedbackList);
		} catch (Exception e) {
			logger.error("Exception occured in UserController.viewFeedbackUser(){}{}", e.getMessage(), e);
		}
		model.addAttribute(FEEDBACK_LIST, feedbackList);
		model.addAttribute(LOGGED_IN_USERNAME, getLoggedUseName());
		return "user-feedback";
	}

	@GetMapping("/user/addFeedback")
	public String addFeedback(Model model) {
		logger.info("Entered into UserController.addFeedback()");
		return "feedback-form";
	}

	@PostMapping("/user/submitFeedback")
	public String submitFeedback(@RequestParam("file") MultipartFile feedbackImage,
			@RequestParam("feebacktext") String feebackText, Model model) throws IOException {
		logger.info("Entered into UserController.submitFeedback()");

		try {
			String filname = StringUtils.cleanPath(feedbackImage.getOriginalFilename());
			Feedback feedback = new Feedback();
			feedback.setFeedbackImageName(filname);
			feedback.setUserId(getLoggedUseName());
			feedback.setFeedbackText(feebackText);
			feedback.setFeedbackImage(feedbackImage.getBytes());
			feedback.setFeedbackTime(new Date());
			feedbackService.saveFeedback(feedback);
			model.addAttribute(MESSAGE, "Your Feedback Is Pending For Admin Approval");
			logger.debug("Feedback Added Successfully for user{}", feedback.getUserId());
		} catch (Exception e) {
			logger.error("Exception occured in UserController.submitFeedback(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE, "Error While Adding Feedback");
		}
		return "redirect:/user/viewFeedbackUser";
	}

	@GetMapping("/getFeedbackImage")
	public String getFeedbackImage(@Param("id") int id, HttpServletResponse response, Model model) {
		logger.info("Entered into UserController.getFeedbackImage() for id{}", id);

		try {
			Optional<Feedback> result = feedbackService.findFeedbackById(id);
			if (!result.isPresent()) {
				logger.info("No feedback obtained from current id{}", id);
				model.addAttribute(MESSAGE,"Some Error occured");
			} else {
				Feedback userInformation = result.get();
				response.setContentType("APPLICATION/OCTET-STREAM");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + userInformation.getFeedbackImageName() + "\"");
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(userInformation.getFeedbackImage());
				outputStream.close();
			}
		} catch (Exception e) {
			logger.error("Exception occured in UserController.getFeedbackImage(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE,"Some Error occured");
		}
		return "redirect:/user/viewFeedbackUser";
	}

	private String getLoggedUseName() {
		String loggedUser = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			loggedUser = authentication.getName();
		}
		logger.info("Logged in User on UserController is {}", loggedUser);
		return loggedUser;
	}
}
