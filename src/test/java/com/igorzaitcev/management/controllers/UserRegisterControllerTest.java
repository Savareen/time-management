package com.igorzaitcev.management.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.igorzaitcev.management.dto.UserDTO;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.GroupManagement;
import com.igorzaitcev.management.service.UserDTOManagement;

@ActiveProfiles("test")
@WebMvcTest(UserRegisterController.class)
class UserRegisterControllerTest {

	private static final UserDTO TESTUSERDTO = new UserDTO("John", "Dow", "Fuel", "Tank", "Gas", 1L);
	@MockBean
	private GroupManagement groupManagement;
	@MockBean
	private UserDTOManagement userDTOManagement;
	@MockBean
	PasswordEncoder paswordEncoder;
	@MockBean
	ApplicationUserService applicationUserService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewUserDTO() throws Exception {
		this.mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("guest/register"))
				.andExpect(model().attribute("userDTO", new UserDTO()));
	}

	@Test
	void shouldReturnViewWelcomePage() throws Exception {
		this.mockMvc.perform(post("/registration").flashAttr("userDTO", TESTUSERDTO)).andExpect(status().isOk())
				.andExpect(view().name("guest/welcome"));
	}
}
