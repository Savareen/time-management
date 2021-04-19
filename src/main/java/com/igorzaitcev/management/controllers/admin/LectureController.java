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
import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.service.LectureManagement;

@Controller
@RequestMapping("/admin/")
public class LectureController {
	LectureManagement management;
	private static final String URL = "admin/view/class-view";
	private static final String NEWURL = "admin/edit/class-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "lecture";
	private static final String PRINT = "lectures";

	public LectureController(LectureManagement management) {
		this.management = management;
	}

	@GetMapping("view/class")
	public String getClass(Model model) {
		return URL;
	}

	@GetMapping("view/findclass")
	public String findClass(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
		} else {
			management.findLecture(id.get()).ifPresentOrElse(lecture -> {
				model.addAttribute(PRINT, lecture);
			}, () -> {
				model.addAttribute(MESSAGE, "Class not found...");
			});
		}
		return URL;
	}

	@GetMapping(value = "edit/deleteclass/{id}")
	public String deleteClass(@PathVariable Long id, Model model) {
		boolean deleted = false;
		try {
			deleted = management.deleteLecture(id);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}
		if (deleted) {
			model.addAttribute(MESSAGE, "Class deleted...");
		} else {
			model.addAttribute(MESSAGE, "Class not found...");
		}
		return URL;
	}

	@GetMapping("view/showclass")
	public String showClass(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit,
			Model model) {
		if (offset.isEmpty() || limit.isEmpty()) {
			model.addAttribute(PRINT, management.getLecturesByLimit(1, 50));
		} else {
			model.addAttribute(PRINT, management.getLecturesByLimit(offset.get(), limit.get()));
		}
		return URL;
	}

	@GetMapping("view/addclass")
	public String newClassForm(Model model) {
		model.addAttribute(ATTRIBUTE, new Lecture());
		return NEWURL;
	}

	@GetMapping(value = "view/editclass/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		Optional<Lecture> lecture = management.findLecture(id);
		model.addAttribute(ATTRIBUTE, lecture.get());
		return NEWURL;
	}

	@PostMapping("edit/addclass")
	public String addClass(@Valid @ModelAttribute(ATTRIBUTE) Lecture lecture, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return NEWURL;
		}
		try {
			Lecture addedLecture = management.addLecture(lecture);
			model.addAttribute(PRINT, addedLecture);
		} catch (MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return NEWURL;
		}
		return getClass(model);
	}

}
