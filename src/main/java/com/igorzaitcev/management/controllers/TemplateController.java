package com.igorzaitcev.management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class TemplateController {

	@GetMapping(value = { "/", "index" })
	public String getIndex() {
		return "index";
	}

	@GetMapping("/login")
	public String getLogin() {
		return "guest/login";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "guest/login";
	}


}
