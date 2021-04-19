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

import com.igorzaitcev.management.model.Auditorium;
import com.igorzaitcev.management.security.ApplicationUserService;
import com.igorzaitcev.management.service.AuditoriumManagement;

@ActiveProfiles("test")
@WebMvcTest(AuditoriumController.class)
@WithMockUser(roles = { "ADMIN" })
class AuditoriumControllerTest {
	private static final String FINDACTION = "/admin/view/findauditorium";
	private static final String DELETEACTION = "/admin/edit/deleteauditorium/{id}";
	private static final String ADDACTION = "/admin/view/addauditorium";
	private static final String ADDAUDITORIUM = "/admin/edit/addauditorium";
	private static final String EDITACTION = "/admin/view/editauditorium/{id}";
	private static final String MESSAGE = "message";
	private static final String VIEW = "admin/view/auditorium-view";
	private static final String NEWVIEW = "admin/edit/auditorium-edit";
	private static final String PRINT = "auditoriums";
	private static final String ATTRIBUTE = "auditorium";
	private static final Auditorium TESTAUDITORIUM = new Auditorium(1L, 123);

	@MockBean
	PasswordEncoder paswordEncoder;

	@MockBean
	ApplicationUserService applicationUserService;

	@MockBean
	private AuditoriumManagement management;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnViewWithMessageWhenIdNullInFind() throws Exception {
		this.mockMvc.perform(get(FINDACTION)).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Id is not valid..."));
	}

	@Test
	void shouldReturnViewWithMessageWhenAuditoriumNullInFind() throws Exception {
		when(management.findAuditorium(1L)).thenReturn(Optional.ofNullable(null));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Auditorium not found..."));
	}

	@Test
	void shouldReturnViewWithViewWhenAuditoriumFind() throws Exception {
		when(management.findAuditorium(1L)).thenReturn(Optional.ofNullable(TESTAUDITORIUM));
		this.mockMvc.perform(get(FINDACTION).param("id", "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, TESTAUDITORIUM));
	}

	@Test
	void shouldReturnViewWithMessageWhenAuditoriumDelete() throws Exception {
		when(management.deleteAuditorium(1L)).thenReturn(true);
		this.mockMvc.perform(get(DELETEACTION, "1")).andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(MESSAGE, "Auditorium deleted..."));
	}

	@Test
	void shouldReturnViewWhenShowLimitAuditoriums() throws Exception {
		when(management.getAuditoriumsByLimit(0, 1)).thenReturn(new PageImpl<>(List.of(TESTAUDITORIUM)));
		this.mockMvc.perform(get("/admin/view/showauditorium").param("offset", "0").param("limit", "1"))
				.andExpect(status().isOk()).andExpect(view().name(VIEW))
				.andExpect(model().attribute(PRINT, new PageImpl<>(List.of(TESTAUDITORIUM))));
	}

	@Test
	void shouldReturnNewViewWithAuditoriumWhenAdd() throws Exception {
		this.mockMvc.perform(get(ADDACTION)).andExpect(status().isOk()).andExpect(view().name(NEWVIEW))
				.andExpect(model().attribute(ATTRIBUTE, new Auditorium()));
	}

	@Test
	void shouldReturnViewWithPrintWhenAuditoriumAdd() throws Exception {
		when(management.addAuditorium(TESTAUDITORIUM)).thenReturn(TESTAUDITORIUM);
		this.mockMvc.perform(post(ADDAUDITORIUM).flashAttr(ATTRIBUTE, TESTAUDITORIUM))
				.andExpect(status().isOk())
				.andExpect(view().name(VIEW)).andExpect(model().attribute(PRINT, TESTAUDITORIUM));
	}

	@Test
	void shouldReturnNewViewWhenAuditoriumhasNumberNull() throws Exception {
		Auditorium auditorium = new Auditorium(1L, null);
		this.mockMvc.perform(post(ADDAUDITORIUM).flashAttr(ATTRIBUTE, auditorium))
				.andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW));
	}

	@Test
	void shouldReturnViewWithAuditoriumWhenEditAuditorium() throws Exception {
		when(management.findAuditorium(1L)).thenReturn(Optional.ofNullable(TESTAUDITORIUM));
		this.mockMvc.perform(get(EDITACTION, "1")).andExpect(status().isOk())
				.andExpect(view().name(NEWVIEW)).andExpect(model().attribute(ATTRIBUTE, TESTAUDITORIUM));
	}
}
