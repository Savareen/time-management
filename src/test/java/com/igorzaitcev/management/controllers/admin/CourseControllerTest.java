package com.igorzaitcev.management.controllers.admin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.igorzaitcev.management.model.Course;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.CourseManagement;

@ActiveProfiles("test")
@WebMvcTest(CourseController.class)
@WithMockUser(roles = { "ADMIN" })
class CourseControllerTest {
	private static final String FINDACTION = "/admin/view/findcourse";
	private static final String DELETEACTION = "/admin/edit/deletecourse/{id}";
	private static final String ADDACTION = "/admin/view/addcourse";
	private static final String ADDCOURSE = "/admin/edit/addcourse";
	private static final String EDITACTION = "/admin/view/editcourse/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/course-view";
	private static final String NEWVIEW = "admin/edit/course-edit";
	private static final String PRINT = "courses";
	private static final String ATTRIBUTE = "course";
	private static final Course TESTCOURSE = new Course(1L, "Science");

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	private CourseManagement management;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithMessageWhenIdNullInFind() throws Exception {
		this.mockMvc.perform(get(FINDACTION)).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Id is not valid..."));
	}

	@Test
	void shouldReturnViewWithMessageWhenCourseNullInFind() throws Exception {
		when(management.findCourse(1L)).thenReturn(Optional.ofNullable(null));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Course not found..."));
	}

	@Test
	void shouldReturnViewWithViewWhenCourseFind() throws Exception {
		when(management.findCourse(1L)).thenReturn(Optional.ofNullable(TESTCOURSE));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TESTCOURSE));
	}

	@Test
	void shouldReturnViewWithMessageWhenCourseDelete() throws Exception {
		when(management.deleteCourse(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Course deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitCourses() throws Exception {
		when(management.getCoursesByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(TESTCOURSE)));
		this.mockMvc.perform(get("/admin/view/showcourse").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(TESTCOURSE))));
	}

	@Test
	void shouldReturnNewViewWithCourseWhenAdd() throws Exception {
		this.mockMvc.perform(get(ADDACTION)).andExpect(status().isOk()).andExpect(view().name(NEWVIEW))
				.andExpect(model().attribute(ATTRIBUTE, new Course()));
	}

	@Test
	void shouldReturnViewWithPrintWhenCourseAdd() throws Exception {
		when(management.addCourse(TESTCOURSE)).thenReturn(TESTCOURSE);
		this.mockMvc.perform(post(ADDCOURSE).flashAttr(ATTRIBUTE, TESTCOURSE)).andExpect(status().isOk())
				.andExpect(view().name(VIEW)).andExpect(model().attribute(PRINT, TESTCOURSE));
	}

	@Test
	void shouldReturnNewViewWhenCourseHasNameNull() throws Exception {
		Course course = new Course(1L, null);
		this.mockMvc.perform(post(ADDCOURSE).flashAttr(ATTRIBUTE, course)).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithCourseWhenEdit() throws Exception {
		when(management.findCourse(1L)).thenReturn(Optional.ofNullable(TESTCOURSE));
		this.mockMvc.perform(get(EDITACTION, "1")).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, TESTCOURSE));
	}
}
