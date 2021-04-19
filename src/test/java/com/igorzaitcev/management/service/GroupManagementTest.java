package com.igorzaitcev.management.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Student;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.GroupRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GroupManagementTest {
	private final Long expected = 1L;
	private static final Group GROUP = new Group(1L, "name");
	private static final User USER = new User(1L, "Marilyn Monroe", "password", UserRole.USER, UserType.STUDENT);
	private static final Student STUDENT = new Student(1L, "Marilyn", "Monroe", GROUP, USER);
	private static final TimeTable TIMETABLE = new TimeTable(1L, LocalDate.parse("2021-02-05"), GROUP);

	@Mock
	GroupRepository dao;

	@InjectMocks
	GroupManagement management;

	@Test
	void shouldFindGroup() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(GROUP));
		assertEquals(expected, management.findGroup(1L).get().getId());
	}

	@Test
	void shouldFindGroups() {
		Group group1 = new Group(2L, "name1");
		when(dao.findAll()).thenReturn(List.of(GROUP, group1));
		assertEquals(2, management.getGroups().size());
	}

	@Test
	void shouldDeleteGroup() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(GROUP));
		assertTrue(management.deleteGroup(1L));
	}

	@Test
	void shouldAddGroup() {
		when(dao.save(GROUP)).thenReturn(GROUP);
		assertEquals(expected, management.addGroup(GROUP).getId());
	}

	@Test
	void should_ThrowException_When_Group_Null() {
		MissingArgumentException exception = assertThrows(MissingArgumentException.class,
				() -> management.addGroup(null));
		assertEquals("Group cant be null!", exception.getMessage());
	}

	@Test
	void should_ThrowException_When_TimeTable_Has_Group() {
		Group group = new Group(1L, "name");
		group.addTimeTable(TIMETABLE);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(group));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteGroup(1L));
		assertEquals("Group can't be deleted because of TimeTable id: " + expected, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_Student_Has_Group() {
		Group group = new Group(1L, "name");
		group.addStudent(STUDENT);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(group));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteGroup(1L));
		assertEquals("Group can't be deleted because of Student id: " + expected, exception.getMessage());
	}
}
