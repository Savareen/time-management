package com.igorzaitcev.management.controllers.admin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.igorzaitcev.management.dto.TeacherDTO;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.TeacherManagement;

@ActiveProfiles("test")
@WebMvcTest(TeacherController.class)
@WithMockUser(roles = { "ADMIN" })
class TeacherControllerTest {
	private static final String FINDACTION = "/admin/view/findteacher";
	private static final String DELETEACTION = "/admin/edit/deleteteacher/{id}";
	private static final String ADDACTION = "/admin/view/addteacher";
	private static final String ADDTEACHER = "/admin/edit/addteacher";
	private static final String EDITACTION = "/admin/view/editteacher/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/teacher-view";
	private static final String NEWVIEW = "admin/edit/teacher-edit";
	private static final String PRINT = "teacherDTOs";
	private static final String ATTRIBUTE = "teacherDTO";
	private static final TeacherDTO TEACHERDTO = new TeacherDTO(1L, "Marilyn", "Monroe", 2L);

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	private TeacherManagement management;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithMessageWhenIdNullInFind() throws Exception {
		this.mockMvc.perform(get(FINDACTION)).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Id is not valid..."));
	}

	@Test
	void shouldReturnViewWithViewWhenTeacherFind() throws Exception {
		when(management.findTeacher(1L)).thenReturn(TEACHERDTO);
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TEACHERDTO));
	}

	@Test
	void shouldReturnViewWithMessageWhenTeacherDelete() throws Exception {
		when(management.deleteTeacher(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Teacher deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitTeachers() throws Exception {
		when(management.getTeachersByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(TEACHERDTO)));
		this.mockMvc.perform(get("/admin/view/showteacher").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(TEACHERDTO))));
	}

	@Test
	void shouldReturnNewViewWithTeacherWhenAdd() throws Exception {
		this.mockMvc.perform(get(ADDACTION)).andExpect(status().isOk()).andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithPrintWhenTeacherAdd() throws Exception {
		when(management.addTeacher(TEACHERDTO)).thenReturn(TEACHERDTO);
		this.mockMvc.perform(post(ADDTEACHER).flashAttr(ATTRIBUTE, TEACHERDTO)).andExpect(status().isOk())
				.andExpect(view().name(VIEW)).andExpect(model().attribute(PRINT, TEACHERDTO));
	}

	@Test
	void shouldReturnNewViewWhenTeacherhasUserIdNull() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO(1L, "New", "Teacher", null);
		this.mockMvc.perform(post(ADDTEACHER).flashAttr(ATTRIBUTE, teacherDTO)).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithTeacherWhenEdit() throws Exception {
		when(management.findTeacher(1L)).thenReturn(TEACHERDTO);
		this.mockMvc.perform(get(EDITACTION, "1")).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, TEACHERDTO));
	}
}
