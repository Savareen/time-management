package com.igorzaitcev.management.controllers.admin;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.service.UserManagement;

@Controller
@RequestMapping("/admin/")
public class UserController {
	UserManagement management;
	private static final String URL = "admin/view/user-view";
	private static final String NEWURL = "admin/edit/user-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "user";
	private static final String PRINT = "users";

	public UserController(UserManagement management) {
		this.management = management;
	}

	@GetMapping("view/user")
	public String getUser(Model model) {
		return URL;
	}

	@GetMapping("view/finduser")
	public String findUser(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
		} else {
			management.findUser(id.get()).ifPresentOrElse(user -> {
				model.addAttribute(PRINT, user);
			}, () -> {
				model.addAttribute(MESSAGE, "User not found...");
			});
		}
		return URL;
	}

	@GetMapping(value = "edit/deleteuser/{id}")
	public String deleteUser(@PathVariable Long id, Model model) {
		boolean deleted = false;
		try {
			deleted = management.deleteUser(id);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}
		if (deleted) {
			model.addAttribute(MESSAGE, "User deleted...");
		} else {
			model.addAttribute(MESSAGE, "User not found...");
		}
		return URL;
	}

	@GetMapping("view/showuser")
	public String showUser(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit,
			Model model) {
		if (offset.isEmpty() || limit.isEmpty()) {
			model.addAttribute(PRINT, management.getUsersByLimit(1, 50));
		} else {
			model.addAttribute(PRINT, management.getUsersByLimit(offset.get(), limit.get()));
		}
		return URL;
	}

	@GetMapping("view/adduser")
	public String addUserForm(Model model) {
		model.addAttribute(ATTRIBUTE, new User());
		addTypesRoles(model);
		return NEWURL;
	}

	@GetMapping(value = "view/edituser/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		Optional<User> user = management.findUser(id);
		model.addAttribute(ATTRIBUTE, user.get());
		addTypesRoles(model);
		return NEWURL;
	}

	@PostMapping("edit/adduser")
	public String addUser(@Valid @ModelAttribute(ATTRIBUTE) User user, BindingResult result, Model model) {

		if (result.hasErrors()) {
			addTypesRoles(model);
			return NEWURL;
		}
		try {
			User addedUser = management.addUser(user);
			model.addAttribute(PRINT, addedUser);
		} catch (InvalidArgumentException | MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			addTypesRoles(model);
			return NEWURL;
		}
		return getUser(model);

	}

	private Model addTypesRoles(Model model) {
		model.addAttribute("roles", UserRole.values());
		model.addAttribute("types", UserType.values());
		return model;
	}

}
