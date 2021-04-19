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

import com.igorzaitcev.management.dto.TeacherDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Teacher;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.TeacherRepository;
import com.igorzaitcev.management.repository.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TeacherManagementTest {
	private static final User USER = new User(1L, "Marilyn Monroe", "password", UserRole.USER, UserType.TEACHER);
	private static final Teacher TEACHER = new Teacher(1L, "Marilyn", "Monroe", USER);
	private static final TimeTable TIMETABLE = new TimeTable(1L, LocalDate.parse("2021-02-05"), TEACHER);
	private static final TeacherDTO TEACHERDTO = new TeacherDTO(1L, "Marilyn", "Monroe", 1L);

	@Mock
	TeacherRepository dao;
	@Mock
	UserRepository userDao;

	@InjectMocks
	TeacherManagement management;

	@Test
	void shouldFindTeacherDTO() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(TEACHER));
		assertTrue(management.findTeacher(1L).getFirstName().equals(TEACHER.getFirstName()));
	}

	@Test
	void shouldFindTeachers() {
		when(dao.findAll()).thenReturn(List.of(TEACHER));
		assertEquals(1, management.getTeachers().size());
	}

	@Test
	void shouldDeleteTeacher() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(TEACHER));
		assertTrue(management.deleteTeacher(1L));
	}

	@Test
	void should_ThrowException_When_Teacher_Has_TimeTable() {
		Teacher teacher = new Teacher(1L, "Marilyn", "Monroe", USER);
		teacher.addTimeTable(TIMETABLE);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(teacher));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteTeacher(1L));
		assertEquals("Teacher can't be deleted id: " + 1L, exception.getMessage());
	}

	@Test
	void should_Delete_Teacher_when_TimeTable_Remove() {
		Teacher teacher = new Teacher(1L, "Marilyn", "Monroe", USER);
		teacher.addTimeTable(TIMETABLE);
		teacher.removeTimeTable(TIMETABLE);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(teacher));
		assertTrue(management.deleteTeacher(1L));
	}

	@Test
	void shouldAddTeacher() {
		when(userDao.findById(1L)).thenReturn(Optional.of(USER));
		when(dao.save(TEACHER)).thenReturn(TEACHER);
		assertTrue(management.addTeacher(TEACHERDTO).getFirstName().equals(TEACHER.getFirstName()));
	}

	@Test
	void should_ThrowException_When_UserId_Invalid() {
		User user = new User(1L, "new", "password", UserRole.USER, UserType.STUDENT);
		when(userDao.findById(1L)).thenReturn(Optional.of(user));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.addTeacher(TEACHERDTO));
		assertEquals("Invalid Teacher UserID: " + TEACHERDTO.getUserId(), exception.getMessage());
	}

}
