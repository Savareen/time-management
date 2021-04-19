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

import com.igorzaitcev.management.dto.TimeTableResponseDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.service.AuditoriumManagement;
import com.igorzaitcev.management.service.CourseManagement;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.LectureManagement;
import com.igorzaitcev.management.service.TeacherManagement;
import com.igorzaitcev.management.service.TimeTableManagement;

@Controller
@RequestMapping("/admin/")
public class TimeTableController {
	private static final String URL = "admin/view/time-table-view";
	private static final String NEWURL = "admin/edit/time-table-edit";
	private static final String MESSAGE = "message";
	private static final String ATTRIBUTE = "timeTableMap";
	private static final String PRINT = "timeTablesDTO";

	private TimeTableManagement management;
	private TeacherManagement teacherManagement;
	private LectureManagement lectureManagement;
	private CourseManagement courseManagement;
	private AuditoriumManagement auditoriumManagement;
	private GroupManagement groupManagement;

	public TimeTableController(TimeTableManagement management, TeacherManagement teacherManagement,
			LectureManagement lectureManagement, CourseManagement courseManagement,
			AuditoriumManagement auditoriumManagement, GroupManagement groupManagement) {
		super();
		this.management = management;
		this.teacherManagement = teacherManagement;
		this.lectureManagement = lectureManagement;
		this.courseManagement = courseManagement;
		this.auditoriumManagement = auditoriumManagement;
		this.groupManagement = groupManagement;
	}

	@GetMapping("view/table")
	public String getTimeTable(Model model) {
		return URL;
	}

	@GetMapping("view/findtable")
	public String findTimeTable(@RequestParam("id") Optional<Long> id, Model model) {
		if (id.isEmpty()) {
			model.addAttribute(MESSAGE, "Id is not valid...");
			return URL;
		}
		try {
			model.addAttribute(PRINT, management.getTimeTable(id.get()));
		} catch (InvalidArgumentException | MissingArgumentException ex) {
			model.addAttribute(MESSAGE, ex.getMessage());
			return URL;
		}

		return URL;
	}

	@GetMapping(value = "edit/deletetable/{id}")
	public String deleteTimeTable(@PathVariable Long id, Model model) {
		boolean deleted = false;
		deleted = management.deleteTimeTable(id);
		if (deleted) {
			model.addAttribute(MESSAGE, "TimeTable deleted...");
		} else {
			model.addAttribute(MESSAGE, "TimeTable not found...");
		}
		return URL;
	}

	@GetMapping("view/showtable")
	public String showTimeTable(@RequestParam("offset") Optional<Integer> offset,
			@RequestParam("limit") Optional<Integer> limit, Model model) {
		if (offset.isEmpty() || limit.isEmpty()) {
			model.addAttribute(PRINT, management.getTimeTablesByLimit(1, 50));
		} else {
			model.addAttribute(PRINT, management.getTimeTablesByLimit(offset.get(), limit.get()));
		}
		return URL;
	}

	@GetMapping("view/addtable")
	public String addTimeTableForm(Model model) {
		model.addAttribute(ATTRIBUTE, new TimeTableResponseDTO());
		fillSelectForms(model);
		return NEWURL;
	}

	@GetMapping(value = "view/edittable/{id}")
	public String editTimeTable(@PathVariable Long id, Model model) {
		model.addAttribute(ATTRIBUTE, management.findTimeTableMap(id));
		fillSelectForms(model);
		return NEWURL;
	}

	@PostMapping("edit/addtable")
	public String addTimeTable(@Valid @ModelAttribute("timeTableMap") TimeTableResponseDTO timeTableMap, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			fillSelectForms(model);
			System.out.println("has error");
			return NEWURL;
		}
		try {
			System.out.println("try save");
			model.addAttribute(PRINT, management.addTimeTable(timeTableMap));
		} catch (InvalidArgumentException | MissingArgumentException ex) {
			System.out.println("has massage");
			model.addAttribute(MESSAGE, ex.getMessage());
			fillSelectForms(model);
			return NEWURL;
		}
		return getTimeTable(model);
	}

	private Model fillSelectForms(Model model) {
		model.addAttribute("teachers", teacherManagement.getTeachers());
		model.addAttribute("lectures", lectureManagement.getLectures());
		model.addAttribute("auditoriums", auditoriumManagement.getAuditoriums());
		model.addAttribute("groups", groupManagement.getGroups());
		model.addAttribute("courses", courseManagement.getCourses());
		return model;
	}
}
