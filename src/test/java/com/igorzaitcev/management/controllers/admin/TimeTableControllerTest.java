package com.igorzaitcev.management.controllers.admin;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.igorzaitcev.management.dto.TimeTableRequestDTO;
import com.igorzaitcev.management.dto.TimeTableResponseDTO;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.AuditoriumManagement;
import com.igorzaitcev.management.service.CourseManagement;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.LectureManagement;
import com.igorzaitcev.management.service.TeacherManagement;
import com.igorzaitcev.management.service.TimeTableManagement;

@ActiveProfiles("test")
@WebMvcTest(TimeTableController.class)
@WithMockUser(roles = { "ADMIN" })
class TimeTableControllerTest {
	private static final String FINDACTION = "/admin/view/findtable";
	private static final String DELETEACTION = "/admin/edit/deletetable/{id}";
	private static final String ADDACTION = "/admin/view/addtable";
	private static final String EDITACTION = "/admin/view/edittable/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/time-table-view";
	private static final String NEWVIEW = "admin/edit/time-table-edit";
	private static final String PRINT = "timeTablesDTO";
	private static final String ATTRIBUTE = "timeTableMap";
	private static final TimeTableResponseDTO TESTTABLEMAP = new TimeTableResponseDTO(1L, LocalDate.parse("2021-02-05"), 2L, 2L, 2L, 2L,
			2L);
	private static final TimeTableRequestDTO TESTDTOTABLE = new TimeTableRequestDTO(1L, LocalDate.parse("2021-02-05"),
			LocalTime.parse("19:15"), LocalTime.parse("20:00"), "Jhon", 123, "Dow", "aa_11");
	@MockBean
	PasswordEncoder paswordEncoder;
	@MockBean
	ApplicationUserService applicationUserService;
	@MockBean
	private TimeTableManagement management;
	@MockBean
	private TeacherManagement teacherManagement;
	@MockBean
	private LectureManagement lectureManagement;
	@MockBean
	private CourseManagement courseManagement;
	@MockBean
	private AuditoriumManagement auditoriumManagement;
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
	void shouldReturnViewWithViewWhenTimeTableFind() throws Exception {
		when(management.getTimeTable(1L)).thenReturn(TESTDTOTABLE);
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TESTDTOTABLE));
	}

	@Test
	void shouldReturnViewWithMessageWhenTimeTableDelete() throws Exception {
		when(management.deleteTimeTable(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "TimeTable deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitTimeTables() throws Exception {
		when(management.getTimeTablesByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(TESTDTOTABLE)));
		this.mockMvc.perform(get("/admin/view/showtable").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(TESTDTOTABLE))));
	}

	@Test
	void shouldReturnViewWithPrintWhenTimeTableAdd() throws Exception {
		when(management.addTimeTable(TESTTABLEMAP)).thenReturn(TESTDTOTABLE);
		this.mockMvc
				.perform(post("/admin/edit/addtable").flashAttr(ATTRIBUTE, TESTTABLEMAP)).andExpect(status().isOk())
				.andExpect(view().name(VIEW)).andExpect(model().attribute(PRINT, TESTDTOTABLE));
	}

	@Test
	void shouldReturnViewWithTimeTableWhenEdit() throws Exception {
		when(management.findTimeTableMap(1L)).thenReturn(TESTTABLEMAP);
		this.mockMvc.perform(get(EDITACTION, "1")).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, TESTTABLEMAP));
	}
}
