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

import com.igorzaitcev.management.dto.StudentDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Student;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.GroupRepository;
import com.igorzaitcev.management.repository.StudentRepository;
import com.igorzaitcev.management.repository.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class StudentManagementTest {
	private static final User USER = new User(1L, "Marilyn Monroe", "password", UserRole.USER, UserType.STUDENT);
	private static final Group GROUP = new Group(1L, "aa_11");
	private static final Student STUDENT = new Student(1L, "Marilyn", "Monroe", GROUP, USER);
	private static final StudentDTO STUDENTDTO = new StudentDTO(1L, "Marilyn", "Monroe", 1L, 1L);

	@Mock
	StudentRepository dao;
	@Mock
	UserRepository userDao;
	@Mock
	GroupRepository groupDao;

	@InjectMocks
	StudentManagement management;

	@Test
	void shouldFindStudent() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(STUDENT));
		assertEquals(STUDENTDTO.getFirstName(), management.findStudent(1L).getFirstName());
	}


	@Test
	void shouldGetStudentDTOs() {
		when(dao.findAll()).thenReturn(List.of(STUDENT));
		assertEquals(1, management.getStudents().size());
	}
	
	@Test
	void shouldDeleteStudent() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(STUDENT));
		assertTrue(management.deleteStudent(1L));
	}

	@Test
	void shouldAddStudent() {
		when(groupDao.findById(1L)).thenReturn(Optional.of(GROUP));
		when(userDao.findById(1L)).thenReturn(Optional.of(USER));
		when(dao.save(STUDENT)).thenReturn(STUDENT);
		assertEquals(STUDENTDTO.getFirstName(), management.addStudent(STUDENTDTO).getFirstName());
	}

	@Test
	void should_ThrowException_When_UserId_Invalid() {
		User user = new User(2L, "new", "password", UserRole.USER, UserType.TEACHER);
		Long id = 1L;
		when(userDao.findById(id)).thenReturn(Optional.of(user));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.addStudent(STUDENTDTO));
		assertEquals("Invalid Student UserID: " + id, exception.getMessage());
	}

}
