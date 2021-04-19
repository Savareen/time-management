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
import com.igorzaitcev.management.model.Auditorium;
import com.igorzaitcev.management.service.AuditoriumManagement;

@Controller
@RequestMapping("/admin/")
public class AuditoriumController {
	AuditoriumManagement management;
	private static final String URL = "admin/view/auditorium-view";
	private static final String NEWURL = "admin/edit/auditorium-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "auditorium";
	private static final String PRINT = "auditoriums";

	public AuditoriumController(AuditoriumManagement management) {
		this.management = management;
	}

	@GetMapping("view/auditorium")
	public String getAuditorium(Model model) {
		return URL;
	}

	@GetMapping("view/findauditorium")
	public String findAuditorium(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
		} else {
			management.findAuditorium(id.get()).ifPresentOrElse(auditorium -> {
				model.addAttribute(PRINT, auditorium);
			}, () -> {
				model.addAttribute(MESSAGE, "Auditorium not found...");
			});
		}
		return URL;
	}

	@GetMapping(value = "edit/deleteauditorium/{id}")
	public String deleteAuditorium(@PathVariable Long id, Model model) {
		boolean deleted = false;
		try {
			deleted = management.deleteAuditorium(id);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}
		if (deleted) {
			model.addAttribute(MESSAGE, "Auditorium deleted...");
		} else {
			model.addAttribute(MESSAGE, "Auditorium not found...");
		}
		return URL;
	}

	@GetMapping("view/showauditorium")
	public String showAuditorium(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit, Model model) {
		if (offset.isEmpty() || limit.isEmpty()) {
			model.addAttribute(PRINT, management.getAuditoriumsByLimit(1, 50));
		} else {
			model.addAttribute(PRINT, management.getAuditoriumsByLimit(offset.get(), limit.get()));
		}
		return URL;
	}

	@GetMapping("view/addauditorium")
	public String newAuditoriumForm(Model model) {
		model.addAttribute(ATTRIBUTE, new Auditorium());
		return NEWURL;
	}

	@GetMapping(value = "view/editauditorium/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		Optional<Auditorium> auditorium = management.findAuditorium(id);
		model.addAttribute(ATTRIBUTE, auditorium.get());
		return NEWURL;
	}

	@PostMapping("edit/addauditorium")
	public String addAuditorium(@Valid @ModelAttribute(ATTRIBUTE) Auditorium auditorium, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return NEWURL;
		}
		try {
			Auditorium addedAuditorium = management.addAuditorium(auditorium);
			model.addAttribute(PRINT, addedAuditorium);
		} catch (MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return NEWURL;
		}
		return getAuditorium(model);
	}
}
