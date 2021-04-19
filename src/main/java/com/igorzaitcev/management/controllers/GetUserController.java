package com.igorzaitcev.management.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.igorzaitcev.management.security.IAuthenticationFacade;

@Controller
public class GetUserController {
	private IAuthenticationFacade authenticationFacade;

	public GetUserController(IAuthenticationFacade authenticationFacade) {
		this.authenticationFacade = authenticationFacade;
	}

	@GetMapping(value = "/username")
	@ResponseBody
	public String currentUserName() {
		Authentication authentication = authenticationFacade.getAuthentication();
		return authentication.getName();
	}

}
