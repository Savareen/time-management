package com.igorzaitcev.management.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Auditorium;
import com.igorzaitcev.management.model.Course;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.model.Student;
import com.igorzaitcev.management.model.Teacher;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.TimeTableRepository;
import com.igorzaitcev.management.repository.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TimeTableRequestDTOMapperTest {
	private static final String EMAIL = "user";
	private static final Long INTERVAL = 20L;
	private static final Lecture LECTURE = new Lecture(1L, LocalTime.parse("14:00:00"),
			LocalTime.parse("14:15:00"));
	private static final Course COURSE = new Course(1L, "Course");
	private static final Auditorium AUDITORIUM = new Auditorium(1L, 123);
	private static final User USER = new User(1L, "user", "password", UserRole.USER, UserType.TEACHER);
	private static final Teacher TEACHER = new Teacher(1L, "Marilyn", "Monroe", USER);
	private static final Group GROUP = new Group(1L, "name");
	private static final User USERSTUDENT = new User(1L, "user", "password", UserRole.USER, UserType.STUDENT);
	private static final Student STUDENT = new Student(1L, "Marilyn", "Monroe", GROUP, USERSTUDENT);
	private static final TimeTable TESTTABLE = new TimeTable(1L, LocalDate.parse("2021-02-05"), LECTURE, AUDITORIUM,
			TEACHER, GROUP, COURSE);
	
	@Mock
	TimeTableRepository timeTableDao;
	@Mock
	UserRepository userDao;

	@InjectMocks
	TimeTableRequestDTOMapper mapper;

	@Test
	void should_ThrowException_When_ListTimeTable_Empty() {
		USER.setTeacher(TEACHER);
		when(userDao.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(USER));
		when(timeTableDao.findByTeacher(TEACHER, LocalDate.now(), LocalDate.now().plusDays(INTERVAL)))
				.thenReturn(List.of());
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> mapper.getSchedule(INTERVAL, EMAIL));
		assertEquals("Empty timetable", exception.getMessage());
	}

	@Test
	void shouldFindTeacherTimeTable() {
		USER.setTeacher(TEACHER);
		when(userDao.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(USER));
		when(timeTableDao.findByTeacher(TEACHER, LocalDate.now(), LocalDate.now().plusDays(INTERVAL)))
				.thenReturn(List.of(TESTTABLE));
		assertEquals(1, mapper.getSchedule(INTERVAL, EMAIL).size());
	}

	@Test
	void shouldFindStudentTimeTable() {
		USERSTUDENT.setStudent(STUDENT);
		when(userDao.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(USERSTUDENT));
		when(timeTableDao.findByGroup(GROUP, LocalDate.now(), LocalDate.now().plusDays(INTERVAL)))
				.thenReturn(List.of(TESTTABLE));
		assertEquals(1, mapper.getSchedule(INTERVAL, EMAIL).size());
	}
}
