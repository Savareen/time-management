package com.igorzaitcev.management.controllers.admin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.igorzaitcev.management.dto.CountDTO;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.AuditoriumManagement;
import com.igorzaitcev.management.service.CourseManagement;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.LectureManagement;
import com.igorzaitcev.management.service.StudentManagement;
import com.igorzaitcev.management.service.TeacherManagement;
import com.igorzaitcev.management.service.TimeTableManagement;
import com.igorzaitcev.management.service.UserManagement;

@ActiveProfiles("test")
@WebMvcTest(AdminController.class)
@WithMockUser(roles = { "ADMIN" })
class AdminControllerTest {
	private static final CountDTO COUNTDTO = new CountDTO(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L);
	@MockBean
	PasswordEncoder paswordEncoder;
	@MockBean
	ApplicationUserService applicationUserService;
	@MockBean
	AuditoriumManagement auditorium;
	@MockBean
	CourseManagement course;
	@MockBean
	GroupManagement group;
	@MockBean
	LectureManagement lecture;
	@MockBean
	StudentManagement student;
	@MockBean
	TeacherManagement teacher;
	@MockBean
	TimeTableManagement timeTable;
	@MockBean
	UserManagement user;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithCountDTO() throws Exception {
		when(auditorium.countAuditoriums()).thenReturn(1L);
		when(course.countCourses()).thenReturn(1L);
		when(group.countGroups()).thenReturn(1L);
		when(lecture.countLectures()).thenReturn(1L);
		when(student.countStudents()).thenReturn(1L);
		when(teacher.countTeachers()).thenReturn(1L);
		when(timeTable.countTimeTables()).thenReturn(1L);
		when(user.countUsers()).thenReturn(1L);
		this.mockMvc.perform(get("/admin/")).andExpect(status().isOk()).andExpect(view().name("admin/admin-index"))
				.andExpect(model().attribute("countDTO", COUNTDTO));
	}

}
