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

import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.GroupManagement;

@ActiveProfiles("test")
@WebMvcTest(GroupController.class)
@WithMockUser(roles = { "ADMIN" })
class GroupControllerTest {
	private static final String FINDACTION = "/admin/view/findgroup";
	private static final String DELETEACTION = "/admin/edit/deletegroup/{id}";
	private static final String ADDACTION = "/admin/view/addgroup";
	private static final String ADDGROUP = "/admin/edit/addgroup";
	private static final String EDITACTION = "/admin/view/editgroup/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/group-view";
	private static final String NEWVIEW = "admin/edit/group-edit";
	private static final String PRINT = "groups";
	private static final String ATTRIBUTE = "group";
	private static final Group TESTGROUP = new Group(1L, "aa_11");

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	private GroupManagement management;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithMessageWhenIdNullInFind() throws Exception {
		this.mockMvc.perform(get(FINDACTION)).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Id is not valid..."));
	}

	@Test
	void shouldReturnViewWithMessageWhenGroupNullInFind() throws Exception {
		when(management.findGroup(1L)).thenReturn(Optional.ofNullable(null));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Group not found..."));
	}

	@Test
	void shouldReturnViewWithViewWhenGroupFind() throws Exception {
		when(management.findGroup(1L)).thenReturn(Optional.ofNullable(TESTGROUP));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TESTGROUP));
	}

	@Test
	void shouldReturnViewWithMessageWhenGroupDelete() throws Exception {
		when(management.deleteGroup(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Group deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitGroups() throws Exception {
		when(management.getGroupsByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(TESTGROUP)));
		this.mockMvc.perform(get("/admin/view/showgroup").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(TESTGROUP))));
	}

	@Test
	void shouldReturnNewViewWithGroupWhenAdd() throws Exception {
		this.mockMvc.perform(get(ADDACTION)).andExpect(status().isOk()).andExpect(view().name(NEWVIEW))
				.andExpect(model().attribute(ATTRIBUTE, new Group()));
	}

	@Test
	void shouldReturnViewWithPrintWhenGroupAdd() throws Exception {
		when(management.addGroup(TESTGROUP)).thenReturn(TESTGROUP);
		this.mockMvc.perform(post(ADDGROUP).flashAttr(ATTRIBUTE, TESTGROUP)).andExpect(status().isOk())
				.andExpect(view().name(VIEW)).andExpect(model().attribute(PRINT, TESTGROUP));
	}

	@Test
	void shouldReturnNewViewWhenGroupHasEmptyName() throws Exception {
		Group group = new Group(1L, null);
		this.mockMvc.perform(post(ADDGROUP).flashAttr(ATTRIBUTE, group)).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithGroupWhenEdit() throws Exception {
		when(management.findGroup(1L)).thenReturn(Optional.ofNullable(TESTGROUP));
		this.mockMvc.perform(get(EDITACTION, "1")).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, TESTGROUP));
	}
}
