package com.example.guestbook.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.guestbook.entity.Feedback;
import com.example.guestbook.entity.User;
import com.example.guestbook.helper.GusetbookConstants;
import com.example.guestbook.service.FeedbackService;
import com.example.guestbook.service.UserService;

@PropertySource(value ="classpath:message.properties")
@Controller
@RequestMapping("/user")
public class UserController extends GusetbookConstants {

	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private UserService userService;
	
	 @Value("${error.message}")
	 private String errorMessage;
	 
	 @Value("${user.feedback.pending}")
	 private String userFeebackPending;
	 
	 

	Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * Returns the the welcome screen for the logged in user
	 * @param model
	 * @return
	 */
	@GetMapping("/welcomeUser")
	public String welcomeUser(Model model) {
		logger.info("Entered into UserController.welcome()");
		
		User user = userService.findByEmail(getLoggedUserName());
		model.addAttribute(LOGGED_IN_USERNAME, user.getFirstName());
		return "welcome-user";
	}

	/**
	 * Returns the list of feedbacks for the the logged in user
	 * @param model
	 * @return
	 */
	@GetMapping("/viewFeedbackUser")
	public String viewFeedbackUser(Model model) {
		logger.info("Entered into UserController.viewFeedbackUser()");

		List<Feedback> feedbackList =null;
		try {
			feedbackList = feedbackService.findFeebackByUserId(getLoggedUserName());
			logger.debug("List of Feedbacks obtained from UserController.viewFeedbackUser() is {}", feedbackList);
		} catch (Exception e) {
			logger.error("Exception occured in UserController.viewFeedbackUser(){}{}", e.getMessage(), e);
		}
		model.addAttribute(FEEDBACK_LIST, feedbackList);
		model.addAttribute(LOGGED_IN_USERNAME, getLoggedUserName());
		return "user-feedback";
	}

	
	/**
	 * Directs user to feedback form for logged in user
	 * @return
	 */
	@GetMapping("/addFeedback")
	public String addFeedback() {
		logger.info("Entered into UserController.addFeedback()");
		return "feedback-form";
	}

	/**
	 * Adds the new feedback image for logged in user
	 * @param feedbackImage
	 * @param model
	 * @return
	 */
	@PostMapping("/submitFeedback/image")
	public String submitFeedbackImage(@RequestParam("file" ) MultipartFile feedbackImage, Model model) {
		logger.info("Entered into UserController.submitFeedback()");

		try {
			
			User user = userService.findByEmail(getLoggedUserName());
			String filname = StringUtils.cleanPath(feedbackImage.getOriginalFilename());
			Feedback feedback = new Feedback();
			feedback.setFeedbackImageName(filname);
			feedback.setUserId(getLoggedUserName());
			feedback.setFeedbackImage(feedbackImage.getBytes());
			feedback.setFeedbackTime(new Date());
			feedback.setFirstName(user.getFirstName());
			feedbackService.saveFeedback(feedback);
			model.addAttribute(MESSAGE, userFeebackPending);
			logger.debug("Feedback Added Successfully for user{}", feedback.getUserId());
		} catch (Exception e) {
			logger.error("Exception occured in UserController.submitFeedback(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE, errorMessage);
		}
		return "redirect:/user/viewFeedbackUser";
	}
	
	
	/**
	 * Adds the new feedback image for logged in user
	 * @param feedbacktext
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/submitFeedback/text")
	public String submitFeedbacktext(@RequestParam("feedbacktext") String feedbacktext, Model model) {
		logger.info("Entered into UserController.submitFeedback()");

		try {
			User user = userService.findByEmail(getLoggedUserName());
			Feedback feedback = new Feedback();
			feedback.setUserId(getLoggedUserName());
			feedback.setFeedbackText(feedbacktext);
			feedback.setFeedbackTime(new Date());
			feedback.setFirstName(user.getFirstName());
			feedbackService.saveFeedback(feedback);
			model.addAttribute(MESSAGE, userFeebackPending);
			logger.debug("Feedback Added Successfully for user{}", feedback.getUserId());
		} catch (Exception e) {
			logger.error("Exception occured in UserController.submitFeedback(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE, errorMessage);
		}
		return "redirect:/user/viewFeedbackUser";
	}

	
	/**
	 * To get feedback image from db to browser
	 * @param id
	 * @param response
	 * @param model
	 * @return
	 */
	@GetMapping("/getFeedbackImage")
	public String getFeedbackImage(@Param("id") int id, HttpServletResponse response, Model model) {
		logger.info("Entered into UserController.getFeedbackImage() for id{}", id);

		try {
			Optional<Feedback> result = feedbackService.findFeedbackById(id);
			if (!result.isPresent()) {
				logger.info("No feedback obtained from current id{}", id);
				model.addAttribute(MESSAGE,errorMessage);
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
			model.addAttribute(MESSAGE,errorMessage);
		}
		return "redirect:/user/viewFeedbackUser";
	}

	/**
	 * This method returns the logged in user name
	 * @return
	 */
	private String getLoggedUserName() {
		String loggedUser = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			loggedUser = authentication.getName();
		}
		logger.info("Logged in User on UserController is {}", loggedUser);
		return loggedUser;
	}
}
