package com.igorzaitcev.management.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.igorzaitcev.management.dto.TimeTableRequestDTO;
import com.igorzaitcev.management.dto.TimeTableResponseDTO;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.Auditorium;
import com.igorzaitcev.management.model.Course;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.model.Teacher;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.TimeTableRepository;
import com.igorzaitcev.management.service.utils.TimeTableResponseDTOMapper;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TimeTableManagementTest {
	private final Long expected = 1L;
	private static final Lecture LECTURE = new Lecture(1L, LocalTime.parse("14:00:00"), LocalTime.parse("14:15:00"));
	private static final Course COURSE = new Course(1L, "Course");
	private static final Auditorium AUDITORIUM = new Auditorium(1L, 123);
	private static final User USER = new User(1L, "user", "password", UserRole.USER, UserType.TEACHER);
	private static final Teacher TEACHER = new Teacher(1L, "Marilyn", "Monroe", USER);
	private static final Group GROUP = new Group(1L, "name");
	private static final TimeTable TESTTABLE = new TimeTable(1L, LocalDate.parse("2021-02-05"), LECTURE, AUDITORIUM,
			TEACHER, GROUP, COURSE);
	private static final TimeTableResponseDTO TESTTABLEMAP = new TimeTableResponseDTO(1L, LocalDate.parse("2021-02-05"), 1L, 1L, 1L, 1L,
			1L);
	private static final TimeTableRequestDTO TESTDTOTABLE = new TimeTableRequestDTO(1L, LocalDate.parse("2021-02-05"),
			LocalTime.parse("19:15"), LocalTime.parse("20:00"), "John", 123, "Dow", "aa_11");

	@Mock
	private TimeTableRepository dao;
	@Mock
	private TimeTableResponseDTOMapper timeTableMapper;
	@Mock
	private TimeTableRequestDTOMapper dtoMapper;

	@InjectMocks
	TimeTableManagement management;

	@Test
	void shouldFindTimeTable() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(TESTTABLE));
		assertEquals(expected, management.findTimeTable(1L).get().getId());
	}

	@Test
	void shouldFindTimeTableDTO() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(TESTTABLE));
		when(dtoMapper.convertToTimeTableRequestDTO(TESTTABLE)).thenReturn(TESTDTOTABLE);
		assertEquals(TESTDTOTABLE, management.getTimeTable(1L));
	}

	@Test
	void shouldDeleteTimeTable() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(TESTTABLE));
		assertTrue(management.deleteTimeTable(1L));
	}

	@Test
	void shouldAddTimeTable() {
		when(timeTableMapper.converToTimeTable(TESTTABLEMAP)).thenReturn(TESTTABLE);
		when(dao.save(TESTTABLE)).thenReturn(TESTTABLE);
		when(dtoMapper.convertToTimeTableRequestDTO(TESTTABLE)).thenReturn(TESTDTOTABLE);
		assertEquals(TESTDTOTABLE, management.addTimeTable(TESTTABLEMAP));
	}

	@Test
	void should_ThrowException_When_TimeTable_Null() {
		MissingArgumentException exception = assertThrows(MissingArgumentException.class,
				() -> management.addTimeTable(null));
		assertEquals("TimeTable cant be null!", exception.getMessage());
	}

}
