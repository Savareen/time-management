package com.igorzaitcev.management.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Student;
import com.igorzaitcev.management.model.Teacher;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.UserRepository;
import com.igorzaitcev.management.security.PasswordService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserManagementTest {
	private final Long expected = 1L;
	private static final User USER = new User(1L, "user", "password", UserRole.USER, UserType.TEACHER);
	private static final Teacher TEACHER = new Teacher(1L, "Marilyn", "Monroe", USER);
	private static final Group GROUP = new Group(1L, "name");
	private static final User USERSTUDENT = new User(1L, "user", "password", UserRole.USER, UserType.STUDENT);
	private static final Student STUDENT = new Student(1L, "Marilyn", "Monroe", GROUP, USERSTUDENT);

	@Mock
	UserRepository dao;
	@Mock
	PasswordService passwordService;

	@InjectMocks
	UserManagement management;

	@Test
	void shouldFindUser() {
		User user = new User(1L, "name", "password", UserRole.USER, UserType.TEACHER);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(user));
		assertEquals(expected, management.findUser(1L).get().getId());
	}

	@Test
	void shouldFindUsers() {
		User user = new User(1L, "name", "password", UserRole.USER, UserType.TEACHER);
		User user1 = new User(2L, "name1", "password1", UserRole.USER, UserType.TEACHER);
		when(dao.findAll()).thenReturn(List.of(user, user1));
		assertEquals(2, management.getUsers().size());
	}

	@Test
	void shouldDeleteUser() {
		User user = new User(1L, "name", "password", UserRole.USER, UserType.TEACHER);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(user));
		assertTrue(management.deleteUser(1L));
	}

	@Test
	void shouldAddUser() {
		User user = new User(1L, "name", "password", UserRole.USER, UserType.TEACHER);
		when(dao.findByEmail("name")).thenReturn(Optional.ofNullable(null));
		when(dao.save(user)).thenReturn(user);
		assertEquals(expected, management.addUser(user).getId());
	}

	@Test
	void should_ThrowException_When_Teacher_Has_User() {
		USER.setTeacher(TEACHER);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(USER));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteUser(1L));
		assertEquals("User can't be deleted because of Teacher id: " + expected, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_Student_Has_User() {
		USERSTUDENT.setStudent(STUDENT);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(USERSTUDENT));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteUser(1L));
		assertEquals("User can't be deleted because of Student id: " + expected, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_Username_Invalid() {
		User user = new User(1L, "name", "password", UserRole.USER, UserType.TEACHER);
		User userInDB = new User(2L, "name", "password", UserRole.USER, UserType.TEACHER);
		when(dao.findByEmail("name")).thenReturn(Optional.ofNullable(userInDB));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.addUser(user));
		assertEquals("Invalid Email: " + user.getEmail(), exception.getMessage());
	}

}
