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

import com.igorzaitcev.management.dto.TeacherDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.service.TeacherManagement;

@Controller
@RequestMapping("/admin/")
public class TeacherController {
	TeacherManagement management;
	private static final String URL = "admin/view/teacher-view";
	private static final String NEWURL = "admin/edit/teacher-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "teacherDTO";
	private static final String PRINT = "teacherDTOs";

	public TeacherController(TeacherManagement management) {
		this.management = management;
	}

	@GetMapping("view/teacher")
	public String getTeacher(Model model) {
		return URL;
	}

	@GetMapping("view/findteacher")
	public String findTeacher(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
			return URL;
		}
		try {
			TeacherDTO teacherDTO = management.findTeacher(id.get());
			model.addAttribute(PRINT, teacherDTO);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
		}
		return URL;
	}

	@GetMapping(value = "edit/deleteteacher/{id}")
	public String deleteTeacher(@PathVariable Long id, Model model) {
		boolean deleted = false;
		try {
			deleted = management.deleteTeacher(id);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}
		if (deleted) {
			model.addAttribute(MESSAGE, "Teacher deleted...");
		} else {
			model.addAttribute(MESSAGE, "Teacher not found...");
		}
		return URL;
	}

	@GetMapping("view/showteacher")
	public String showTeacher(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit, Model model) {
		try {
			if (offset.isEmpty() || limit.isEmpty()) {
				model.addAttribute(PRINT, management.getTeachersByLimit(1, 50));
			} else {
				model.addAttribute(PRINT, management.getTeachersByLimit(offset.get(), limit.get()));
			}
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
		}
		return URL;
	}

	@GetMapping("view/addteacher")
	public String newTeacherForm(Model model) {
		model.addAttribute(ATTRIBUTE, new TeacherDTO());
		return NEWURL;
	}

	@GetMapping(value = "view/editteacher/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		TeacherDTO teacherDTO = management.findTeacher(id);
		model.addAttribute(ATTRIBUTE, teacherDTO);
		return NEWURL;
	}

	@PostMapping("edit/addteacher")
	public String addTeacher(@Valid @ModelAttribute(ATTRIBUTE) TeacherDTO teacherDTO, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return NEWURL;
		}
		try {
			TeacherDTO addedTeacher = management.addTeacher(teacherDTO);
			model.addAttribute(PRINT, addedTeacher);
		} catch (InvalidArgumentException | MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return NEWURL;
		}
		return getTeacher(model);
	}
}
