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

import com.igorzaitcev.management.dto.StudentDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.StudentManagement;

@Controller
@RequestMapping("/admin/")
public class StudentController {
	StudentManagement management;
	GroupManagement groupManagement;
	private static final String URL = "admin/view/student-view";
	private static final String NEWURL = "admin/edit/student-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "studentDTO";
	private static final String PRINT = "studentDTOs";
	private static final String GROUPS = "groups";

	public StudentController(StudentManagement management, GroupManagement groupManagement) {
		this.management = management;
		this.groupManagement = groupManagement;
	}

	@GetMapping("view/student")
	public String getStudent(Model model) {
		return URL;
	}

	@GetMapping("view/findstudent")
	public String findStudent(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
			return URL;
		}
		try {
			StudentDTO studentDTO = management.findStudent(id.get());
			model.addAttribute(PRINT, studentDTO);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
		}
		return URL;
	}

	@GetMapping(value = "edit/deletestudent/{id}")
	public String deleteStudent(@PathVariable Long id, Model model) {
		boolean deleted = false;
		try {
			deleted = management.deleteStudent(id);
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}
		if (deleted) {
			model.addAttribute(MESSAGE, "Student deleted...");
		} else {
			model.addAttribute(MESSAGE, "Student not found...");
		}
		return URL;
	}

	@GetMapping("view/showstudent")
	public String showStudent(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit, Model model) {
		try {
			if (offset.isEmpty() || limit.isEmpty()) {
				model.addAttribute(PRINT, management.getStudents());
			} else {
				model.addAttribute(PRINT, management.getStudentsByLimit(offset.get(), limit.get()));
			}
		} catch (InvalidArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
		}
		return URL;
	}

	@GetMapping("view/addstudent")
	public String newStudentForm(Model model) {
		model.addAttribute(ATTRIBUTE, new StudentDTO());
		addGroup(model);
		return NEWURL;
	}

	@GetMapping(value = "view/editstudent/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		model.addAttribute(ATTRIBUTE, management.findStudent(id));
		addGroup(model);
		return NEWURL;
	}

	@PostMapping("edit/addstudent")
	public String addStudent(@Valid @ModelAttribute(ATTRIBUTE) StudentDTO studentDTO, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			addGroup(model);
			return NEWURL;
		}
		try {
			StudentDTO addedStudent = management.addStudent(studentDTO);
			model.addAttribute(PRINT, addedStudent);
		} catch (InvalidArgumentException | MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			addGroup(model);
			return NEWURL;
		}
		return getStudent(model);
	}

	private Model addGroup(Model model) {
		return model.addAttribute(GROUPS, groupManagement.getGroups());
	}

}
