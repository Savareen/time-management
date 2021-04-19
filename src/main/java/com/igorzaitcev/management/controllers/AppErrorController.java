package com.igorzaitcev.management.controllers;

import java.util.Optional;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.service.UserManagement;

@Controller
public class AppErrorController implements ErrorController {
	private GetUserController getUser;
	private UserManagement userManagement;

	public AppErrorController(GetUserController getUser, UserManagement userManagement) {
		this.getUser = getUser;
		this.userManagement = userManagement;
	}

	@RequestMapping("/error")
	public String handleError() {
		UserRole userRole = getUserRole();
		if (userRole.equals("admin")) {
			return "/index";
		} else {
			return "/index";
		}
	}

	private UserRole getUserRole() {
		Optional<User> user = userManagement.findByEmail(getUser.currentUserName());
		if (user.isEmpty()) {
			return UserRole.GUEST;
		}
		return user.get().getRole();
	}

	@Override
	public String getErrorPath() {
		return null;
	}

}
