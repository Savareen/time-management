package com.igorzaitcev.management.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.igorzaitcev.management.dto.CountDTO;
import com.igorzaitcev.management.service.AuditoriumManagement;
import com.igorzaitcev.management.service.CourseManagement;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.LectureManagement;
import com.igorzaitcev.management.service.StudentManagement;
import com.igorzaitcev.management.service.TeacherManagement;
import com.igorzaitcev.management.service.TimeTableManagement;
import com.igorzaitcev.management.service.UserManagement;

@Controller
@RequestMapping("/admin")
public class AdminController {
	AuditoriumManagement auditorium;
	CourseManagement course;
	GroupManagement group;
	LectureManagement lecture;
	StudentManagement student;
	TeacherManagement teacher;
	TimeTableManagement timeTable;
	UserManagement user;

	public AdminController(AuditoriumManagement auditorium, CourseManagement course, GroupManagement group,
			LectureManagement lecture, StudentManagement student, TeacherManagement teacher,
			TimeTableManagement timeTable, UserManagement user) {
		this.auditorium = auditorium;
		this.course = course;
		this.group = group;
		this.lecture = lecture;
		this.student = student;
		this.teacher = teacher;
		this.timeTable = timeTable;
		this.user = user;
	}

	@GetMapping("/")
	public String getIndex(Model model) {
		model.addAttribute("countDTO", createCountDTO());
		return "admin/admin-index";
	}
	
	private CountDTO createCountDTO() {
		CountDTO count = new CountDTO();
		count.setAuditoriumCount(auditorium.countAuditoriums());
		count.setCourseCount(course.countCourses());
		count.setGroupCount(group.countGroups());
		count.setLectureCount(lecture.countLectures());
		count.setStudentCount(student.countStudents());
		count.setTeacherCount(teacher.countTeachers());
		count.setTimeTableCount(timeTable.countTimeTables());
		count.setUserCount(user.countUsers());
		return count;
	}

}
