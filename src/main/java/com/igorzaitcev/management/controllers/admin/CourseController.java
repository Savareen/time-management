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
import com.igorzaitcev.management.model.Course;
import com.igorzaitcev.management.service.CourseManagement;

@Controller
@RequestMapping("/admin/")
public class CourseController {
	CourseManagement management;
	private static final String URL = "admin/view/course-view";
	private static final String NEWURL = "admin/edit/course-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "course";
	private static final String PRINT = "courses";

	public CourseController(CourseManagement management) {
		this.management = management;
	}

	@GetMapping("view/course")
	public String getCourse(Model model) {
		return URL;
	}

	@GetMapping("view/findcourse")
	public String findCourse(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
		} else {
			management.findCourse(id.get()).ifPresentOrElse(course -> {
				model.addAttribute(PRINT, course);
			}, () -> {
				model.addAttribute(MESSAGE, "Course not found...");
			});
		}
		return URL;
	}

	@GetMapping(value = "edit/deletecourse/{id}")
	public String deleteCourse(@PathVariable Long id, Model model) {
		boolean deleted = false;
		try {
			deleted = management.deleteCourse(id);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}
		if (deleted) {
			model.addAttribute(MESSAGE, "Course deleted...");
		} else {
			model.addAttribute(MESSAGE, "Course not found...");
		}
		return URL;
	}

	@GetMapping("view/showcourse")
	public String showCourse(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit,
			Model model) {
		if (offset.isEmpty() || limit.isEmpty()) {
			model.addAttribute(PRINT, management.getCoursesByLimit(1, 50));
		} else {
			model.addAttribute(PRINT, management.getCoursesByLimit(offset.get(), limit.get()));
		}
		return URL;
	}

	@GetMapping("view/addcourse")
	public String newCourseForm(Model model) {
		model.addAttribute(ATTRIBUTE, new Course());
		return NEWURL;
	}

	@GetMapping(value = "view/editcourse/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		Optional<Course> course = management.findCourse(id);
		model.addAttribute(ATTRIBUTE, course.get());
		return NEWURL;
	}

	@PostMapping("edit/addcourse")
	public String addCourse(@Valid @ModelAttribute(ATTRIBUTE) Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return NEWURL;
		}
		try {
			Course addedCourse = management.addCourse(course);
			model.addAttribute(PRINT, addedCourse);
		} catch (MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return NEWURL;
		}
		return getCourse(model);
	}
}
