package com.igorzaitcev.management.controllers.admin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;
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

import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.LectureManagement;

@ActiveProfiles("test")
@WebMvcTest(LectureController.class)
@WithMockUser(roles = { "ADMIN" })
class LectureControllerTest {
	private static final String FINDACTION = "/admin/view/findclass";
	private static final String DELETEACTION = "/admin/edit/deleteclass/{id}";
	private static final String ADDACTION = "/admin/view/addclass";
	private static final String ADDCLASS = "/admin/edit/addclass";
	private static final String EDITACTION = "/admin/view/editclass/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/class-view";
	private static final String NEWVIEW = "admin/edit/class-edit";
	private static final String PRINT = "lectures";
	private static final String ATTRIBUTE = "lecture";
	private static final Lecture TESTLECTURE = new Lecture(1L, LocalTime.parse("14:00:00"),
			LocalTime.parse("14:15:00"));

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	private LectureManagement management;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithMessageWhenIdNullInFind() throws Exception {
		this.mockMvc.perform(get(FINDACTION)).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Id is not valid..."));
	}

	@Test
	void shouldReturnViewWithMessageWhenLectureNullInFind() throws Exception {
		when(management.findLecture(1L)).thenReturn(Optional.ofNullable(null));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Class not found..."));
	}

	@Test
	void shouldReturnViewWithViewWhenLectureFind() throws Exception {
		when(management.findLecture(1L)).thenReturn(Optional.ofNullable(TESTLECTURE));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TESTLECTURE));
	}

	@Test
	void shouldReturnViewWithMessageWhenLectureDelete() throws Exception {
		when(management.deleteLecture(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Class deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitLectures() throws Exception {
		when(management.getLecturesByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(TESTLECTURE)));
		this.mockMvc.perform(get("/admin/view/showclass").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(TESTLECTURE))));
	}

	@Test
	void shouldReturnNewViewWithLectureWhenAdd() throws Exception {
		this.mockMvc.perform(get(ADDACTION)).andExpect(status().isOk()).andExpect(view().name(NEWVIEW))
				.andExpect(model().attribute(ATTRIBUTE, new Lecture()));
	}

	@Test
	void shouldReturnViewWithPrintWhenLectureAdd() throws Exception {
		when(management.addLecture(TESTLECTURE)).thenReturn(TESTLECTURE);
		this.mockMvc.perform(post(ADDCLASS).flashAttr(ATTRIBUTE, TESTLECTURE)).andExpect(status().isOk())
				.andExpect(view().name(VIEW)).andExpect(model().attribute(PRINT, TESTLECTURE));
	}

	@Test
	void shouldReturnNewViewWhenLectureHasEmptyStartTime() throws Exception {
		Lecture lecture = new Lecture(1L, null, LocalTime.parse("14:15:00"));
		this.mockMvc.perform(post(ADDCLASS).flashAttr(ATTRIBUTE, lecture)).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithLectureWhenEditLecture() throws Exception {
		when(management.findLecture(1L)).thenReturn(Optional.ofNullable(TESTLECTURE));
		this.mockMvc.perform(get(EDITACTION, "1")).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, TESTLECTURE));
	}

}
