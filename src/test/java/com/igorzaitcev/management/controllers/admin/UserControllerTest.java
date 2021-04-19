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

import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.UserManagement;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@WithMockUser(roles = { "ADMIN" })
class UserControllerTest {
	private static final String FINDACTION = "/admin/view/finduser";
	private static final String DELETEACTION = "/admin/edit/deleteuser/{id}";
	private static final String ADDACTION = "/admin/view/adduser";
	private static final String ADDUSER = "/admin/edit/adduser";
	private static final String EDITACTION = "/admin/view/edituser/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/user-view";
	private static final String NEWVIEW = "admin/edit/user-edit";
	private static final String PRINT = "users";
	private static final String ATTRIBUTE = "user";
	private static final User TESTUSER = new User(1L, "Mike", "123", UserRole.USER, UserType.STUDENT);

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	private UserManagement management;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithMessageWhenIdNullInFind() throws Exception {
		this.mockMvc.perform(get(FINDACTION)).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Id is not valid..."));
	}

	@Test
	void shouldReturnViewWithMessageWhenUserNullInFind() throws Exception {
		when(management.findUser(1L)).thenReturn(Optional.ofNullable(null));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "User not found..."));
	}

	@Test
	void shouldReturnViewWithViewWhenUserFind() throws Exception {
		when(management.findUser(1L)).thenReturn(Optional.ofNullable(TESTUSER));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TESTUSER));
	}

	@Test
	void shouldReturnViewWithMessageWhenUserDelete() throws Exception {
		when(management.deleteUser(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "User deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitUsers() throws Exception {
		when(management.getUsersByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(TESTUSER)));
		this.mockMvc.perform(get("/admin/view/showuser").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(TESTUSER))));
	}

	@Test
	void shouldReturnNewViewWithUserWhenAdd() throws Exception {
		this.mockMvc
				.perform(get(ADDACTION)).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, new User()));
	}

	@Test
	void shouldReturnViewWithPrintWhenUserAdd() throws Exception {
		when(management.addUser(TESTUSER)).thenReturn(TESTUSER);
		this.mockMvc
				.perform(post(ADDUSER).flashAttr(ATTRIBUTE, TESTUSER))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TESTUSER));
	}
	
	@Test
	void shouldReturnNewViewWhenUserhasEmptyName() throws Exception {
		User user = new User(1L, null, "123", UserRole.USER, UserType.STUDENT);
		this.mockMvc.perform(post(ADDUSER).flashAttr(ATTRIBUTE, user)).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithUserWhenEditUser() throws Exception {
		when(management.findUser(1L)).thenReturn(Optional.ofNullable(TESTUSER));
		this.mockMvc.perform(get(EDITACTION, "1")).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, TESTUSER));
	}
}
