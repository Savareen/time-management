package com.igorzaitcev.management.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.igorzaitcev.management.dto.TimeTableRequestDTO;
import com.igorzaitcev.management.dto.UserDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.TimeTableRequestDTOMapper;
import com.igorzaitcev.management.service.UserDTOManagement;

@Controller
@RequestMapping("/user")
public class UserTableController {
	private static final String MESSAGE = "message";
	private static final String URL = "user/user-table";
	private static final String EDIT = "user/edit-profile";
	private static final String PRINT = "studentDTOs";

	private GroupManagement groupManagement;
	private GetUserController getUser;
	private TimeTableRequestDTOMapper mapper;
	private UserDTOManagement userDTOManagement;

	public UserTableController(GroupManagement groupManagement, GetUserController getUser, TimeTableRequestDTOMapper mapper,
			UserDTOManagement userDTOManagement) {
		this.groupManagement = groupManagement;
		this.getUser = getUser;
		this.mapper = mapper;
		this.userDTOManagement = userDTOManagement;
	}

	@GetMapping("/")
	public String openTableForm(Model model) {
		model.addAttribute("username", getUserName());
		return URL;
	}

	private String getUserName() {
		UserDTO userDTO = userDTOManagement.getUserDTO(getUser.currentUserName());
		String name = userDTO.getFirstName() + " " + userDTO.getLastName();
		return name;
	}

	@GetMapping("/findtable")
	public String getTimeTable(@RequestParam("interval") Long interval, Model model) {
		try {
			List<TimeTableRequestDTO> readyTable = mapper.getSchedule(interval, getUser.currentUserName());
			model.addAttribute(PRINT, readyTable);
			model.addAttribute("username", getUserName());
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			model.addAttribute("username", getUserName());
		}
		return URL;
	}

	@GetMapping("/editprofile/")
	public String editProfile(Model model) {
		addViewAttributes(model);
		model.addAttribute("username", getUserName());
		return EDIT;
	}

	@PostMapping("/update")
	public String getRegistration(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			addViewAttributes(model);
			model.addAttribute("username", getUserName());
			return EDIT;
		}
		try {
			userDTOManagement.updateClient(userDTO, getUser.currentUserName());
			model.addAttribute("username", getUserName());
		} catch (InvalidArgumentException | MissingArgumentException ex) {
			model.addAttribute("message", ex.getMessage());
			addViewAttributes(model);
			model.addAttribute("username", getUserName());
			return EDIT;
		}
		return "user/update";
	}

	private Model addViewAttributes(Model model) {
		model.addAttribute("userDTO", userDTOManagement.getUserDTO(getUser.currentUserName()));
		model.addAttribute("groups", groupManagement.getGroups());
		return model;
	}
}
