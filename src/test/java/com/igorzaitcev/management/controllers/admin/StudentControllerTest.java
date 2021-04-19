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

import com.igorzaitcev.management.dto.StudentDTO;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.StudentManagement;

@ActiveProfiles("test")
@WebMvcTest(StudentController.class)
@WithMockUser(roles = { "ADMIN" })
class StudentControllerTest {
	private static final String FINDACTION = "/admin/view/findstudent";
	private static final String DELETEACTION = "/admin/edit/deletestudent/{id}";
	private static final String ADDACTION = "/admin/view/addstudent";
	private static final String ADDSTUDENT = "/admin/edit/addstudent";
	private static final String EDITACTION = "/admin/view/editstudent/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/student-view";
	private static final String NEWVIEW = "admin/edit/student-edit";
	private static final String PRINT = "studentDTOs";
	private static final String ATTRIBUTE = "studentDTO";
	private static final StudentDTO STUDENTDTO = new StudentDTO(1L, "Marilyn", "Monroe", 1L, 2L);
	private static final Group TESTGROUP = new Group(1L, "aa_11");

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	private StudentManagement management;

	@MockBean
	private GroupManagement groupManagement;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithMessageWhenIdNullInFind() throws Exception {
		this.mockMvc.perform(get(FINDACTION)).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Id is not valid..."));
	}

	@Test
	void shouldReturnViewWithViewWhenTeacherFind() throws Exception {
		when(management.findStudent(1L)).thenReturn(STUDENTDTO);
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, STUDENTDTO));
	}

	@Test
	void shouldReturnViewWithMessageWhenStudentDelete() throws Exception {
		when(management.deleteStudent(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Student deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitStudents() throws Exception {
		when(management.getStudentsByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(STUDENTDTO)));
		this.mockMvc.perform(get("/admin/view/showstudent").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(STUDENTDTO))));
	}

	@Test
	void shouldReturnNewViewWithStudentWhenAdd() throws Exception {
		when(groupManagement.getGroups()).thenReturn(List.of(TESTGROUP));
		this.mockMvc.perform(get(ADDACTION)).andExpect(status().isOk()).andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithPrintWhenStudentAdd() throws Exception {
		when(management.addStudent(STUDENTDTO)).thenReturn(STUDENTDTO);
		this.mockMvc.perform(post(ADDSTUDENT).flashAttr(ATTRIBUTE, STUDENTDTO)).andExpect(status().isOk())
				.andExpect(view().name(VIEW)).andExpect(model().attribute(PRINT, STUDENTDTO));
	}

	@Test
	void shouldReturnViewWithStudentWhenEdit() throws Exception {
		when(management.findStudent(1L)).thenReturn(STUDENTDTO);
		when(groupManagement.getGroups()).thenReturn(List.of(TESTGROUP));
		this.mockMvc.perform(get(EDITACTION, "1").flashAttr("groups", TESTGROUP)).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, STUDENTDTO));
	}
}
