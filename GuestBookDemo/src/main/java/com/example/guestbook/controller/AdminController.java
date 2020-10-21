package com.example.guestbook.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.guestbook.entity.Feedback;
import com.example.guestbook.entity.User;
import com.example.guestbook.helper.GusetbookConstants;
import com.example.guestbook.service.FeedbackService;
import com.example.guestbook.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController extends GusetbookConstants {

	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private UserService userService;
	
	Logger logger = LoggerFactory.getLogger(AdminController.class);

	/*returns the the welcome screen for the logged in Admin*/
	@GetMapping("/welcomeAdmin")
	public String welcome(Model model) {

		User user = userService.findByEmail(getLoggedUserName());
		model.addAttribute(LOGGED_IN_USERNAME,user.getFirstName());
		return "welcome-admin";

	}

	/*returns the list of feedbacks for the the logged in Admin*/
	@GetMapping("/viewFeedbackAdmin")
	public String viewFeedbackadmin(Model model) {
		logger.info("Entered into AdminController.viewFeedbackadmin()");

		List<Feedback> feedbackList = null;
		try {
			feedbackList = feedbackService.findAllFeedbacks();
			logger.debug("List of Feedbacks obtained from AdminController.viewFeedbackadmin() is {}", feedbackList);
		} catch (Exception e) {
			logger.error("Exception occured in AdminController.viewFeedbackadmin(){}{}", e.getMessage(), e);
		}
		model.addAttribute(FEEDBACK_LIST, feedbackList);
		model.addAttribute(LOGGED_IN_USERNAME, getLoggedUserName());
		return "admin-feedback";
	}

	/*Admin can delete the feedback for selected ID*/ 
	@GetMapping("/deleteFeedback")
	public String deleteFeedback(@Param("id") int id, Model model) {
		logger.info("Entered into AdminController.deleteFeedback() with id{}", id);

		try {
			feedbackService.deleteFeedbackById(id);
			logger.info("Deleted successfully Feedback with id {}", id);
			model.addAttribute(MESSAGE, "Feedback Has Been Deleted Successfully");
		} catch (Exception e) {
			logger.error("Exception occured in AdminController.deleteFeedback(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE, "Some Problem Occured while deleting the Feedback");
		}
		return REDIRECT_ADMIN_FEEDBACK;
	}

	/*Admin can approve the feedback for selected ID*/ 
	@GetMapping("/approveFeedback")
	public String approveFeedback(@Param("id") int id, Model model) {
		logger.info("Entered into AdminController.approveFeedback() with id{}", id);

		try {
			Optional<Feedback> result = feedbackService.findFeedbackById(id);
			if (!result.isPresent()) {
				logger.info("No feedback obtained from current id{}", id);
				model.addAttribute(MESSAGE, "Some problem occured while approving the feedback");
			} else {
				Feedback feedback = result.get();
				feedback.setFeedbackApproved(true);
				feedbackService.saveFeedback(feedback);
				model.addAttribute(MESSAGE, "Feedback Approved Succeesfully");
				logger.debug("Approved Feedback successfully for user {}", feedback.getUserId());
			}
		} catch (Exception e) {
			logger.error("Exception occured in AdminController.approveFeedback(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE, "Some problem occured while approving the feedback");
		}
		return REDIRECT_ADMIN_FEEDBACK;
	}
	/*Admin will be redirected to the edit feedback form*/ 
	@GetMapping("/editFeedback")
	public String editFeedback(@RequestParam("id") int id,@RequestParam(value = "error" ,required = false) String error, Model model) {
		logger.info("Entered into AdminController.editFeedback() with id{}", id);

		try {
			
			if (!StringUtils.isEmpty(error)) {
				model.addAttribute("error", "Please edit atleast one option");
			}
			Optional<Feedback> result = feedbackService.findFeedbackById(id);
			if (!result.isPresent()) {
				logger.info("No feedback obtained for current id{}", id);
				model.addAttribute(MESSAGE, "Some problem occured");
			} else {
				Feedback feedback = result.get();
				model.addAttribute(FEEDBACK, feedback);
				logger.debug("Edit Feedback  for user {}", feedback.getUserId());
			}
		} catch (Exception e) {
			logger.error("Exception occured in AdminController.editFeedback(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE, "Some problem occured");
		}
		return "edit-feedback";
	}

	/*Admin can edit the feedback for selected ID and submit*/ 
	@PostMapping("/editedFeedback")
	public String editedFeedback(@RequestParam("id") int id, @RequestParam(value ="feedbacktext", required=false) String feedbacktext,
			@RequestParam(value ="file", required=false) MultipartFile file, Model model) throws Exception {
		logger.info("Entered into AdminController.editedFeedback() with id{}", id);

		try {
			if (StringUtils.isEmpty(feedbacktext) && StringUtils.isEmpty(file.getOriginalFilename())) {
				return "redirect:/admin/editFeedback?error=true&id="+id;
			}
			String filname = StringUtils.cleanPath(file.getOriginalFilename());
			
			Optional<Feedback> result = feedbackService.findFeedbackById(id);
			if (!result.isPresent()) {
				logger.info("No feedback obtained for current id{}", id);
				model.addAttribute(MESSAGE, "Some problem occured while editing the feedback");
			} else {
				Feedback feedback = result.get();
				if(!StringUtils.isEmpty(feedbacktext) && StringUtils.isEmpty(file.getOriginalFilename())) {
					feedback.setFeedbackText(feedbacktext);
					feedback.setFeedbackTime(new Date());
					feedbackService.saveFeedback(feedback);
				}else if(StringUtils.isEmpty(feedbacktext) && !StringUtils.isEmpty(file.getOriginalFilename())) {
					feedback.setFeedbackImageName(filname);
					feedback.setFeedbackImage(file.getBytes());
					feedback.setFeedbackTime(new Date());
					feedbackService.saveFeedback(feedback);
				}else {
					
					feedback.setFeedbackText(feedbacktext);
					feedback.setFeedbackTime(new Date());
					feedback.setFeedbackImageName(filname);
					feedback.setFeedbackImage(file.getBytes());
					feedbackService.saveFeedback(feedback);
				}
				
				logger.debug("Edited Feedback for user {}", feedback.getUserId());
				model.addAttribute(MESSAGE, "Feedback Edited Successfully");
			}
		} catch (Exception e) {
			logger.error("Exception occured in AdminController.editedFeedback(){}{}", e.getMessage(), e);
			model.addAttribute(MESSAGE, "Some problem occured while editing the feedback");
		}
		return REDIRECT_ADMIN_FEEDBACK;
	}

	/*This method returns the logged in user name*/
	private String getLoggedUserName() {
		String loggedUser = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			loggedUser = authentication.getName();
		}
		logger.info("Logged in User on AdminController is {}", loggedUser);
		return loggedUser;
	}

}
