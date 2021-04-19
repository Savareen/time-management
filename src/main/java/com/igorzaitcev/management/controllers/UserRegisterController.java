package com.igorzaitcev.management.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.igorzaitcev.management.dto.UserDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.UserDTOManagement;

@Controller
@RequestMapping
public class UserRegisterController {
	private GroupManagement groupManagement;
	private UserDTOManagement userDTOManagement;

	public UserRegisterController(GroupManagement groupManagement, UserDTOManagement userDTOManagement) {
		this.groupManagement = groupManagement;
		this.userDTOManagement = userDTOManagement;
	}

	@GetMapping("/register")
	public String getRegisterForm(Model model) {
		addPositions(model);
		model.addAttribute("userDTO", new UserDTO());
		return "guest/register";
	}

	@PostMapping("/registration")
	public String getRegistration(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			addPositions(model);
			return "guest/register";
		}
		try {
			userDTOManagement.registerClient(userDTO);
		} catch (InvalidArgumentException | MissingArgumentException ex) {
			model.addAttribute("message", ex.getMessage());
			addPositions(model);
			return "guest/register";
		}
		return "guest/welcome";
	}

	private Model addPositions(Model model) {
		model.addAttribute("positions", UserType.values());
		model.addAttribute("groups", groupManagement.getGroups());
		return model;
	}

}
