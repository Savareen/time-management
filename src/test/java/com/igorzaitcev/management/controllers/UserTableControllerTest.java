package com.igorzaitcev.management.controllers;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.igorzaitcev.management.dto.TimeTableRequestDTO;
import com.igorzaitcev.management.dto.UserDTO;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.TimeTableRequestDTOMapper;
import com.igorzaitcev.management.service.UserDTOManagement;

@ActiveProfiles("test")
@WebMvcTest(UserTableController.class)
@WithMockUser(username = "user", roles = { "USER" })
class UserTableControllerTest {
	private static final TimeTableRequestDTO TESTDTO = new TimeTableRequestDTO(1L, LocalDate.parse("2021-02-02"),
			LocalTime.parse("13:00"), LocalTime.parse("14:00"), "Science", 123);
	private static final UserDTO TESTUSERDTO = new UserDTO("John", "Dow", "Fuel", "Tank", "Gas", 1L);

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	UserDTOManagement userDTOManagement;

	@MockBean
	GroupManagement groupManagement;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	TimeTableRequestDTOMapper mapper;

	@MockBean
	GetUserController getUser;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnPrintView() throws Exception {
		when(getUser.currentUserName()).thenReturn("user");
		when(userDTOManagement.getUserDTO("user")).thenReturn(TESTUSERDTO);
		when(mapper.getSchedule(30L, "user")).thenReturn(List.of(TESTDTO));
		this.mockMvc.perform(get("/user/findtable").param("interval", "30")).andExpect(status().isOk())
				.andExpect(view().name("user/user-table"))
				.andExpect(model().attribute("studentDTOs", List.of(TESTDTO)));
	}

	@Test
	void shouldReturnEditPage() throws Exception {
		when(getUser.currentUserName()).thenReturn("John");
		when(userDTOManagement.getUserDTO("John")).thenReturn(TESTUSERDTO);
		when(groupManagement.getGroups()).thenReturn(List.of(new Group()));
		this.mockMvc.perform(get("/user/editprofile/")).andExpect(status().isOk())
				.andExpect(view().name("user/edit-profile"))
				.andExpect(model().attribute("userDTO", TESTUSERDTO));
	}

	@Test
	void shouldReturnUpdatePage() throws Exception {
		when(getUser.currentUserName()).thenReturn("user");
		when(userDTOManagement.getUserDTO("user")).thenReturn(TESTUSERDTO);
		this.mockMvc.perform(post("/user/update").flashAttr("userDTO", TESTUSERDTO)).andExpect(status().isOk())
				.andExpect(view().name("user/update"));
	}

}
