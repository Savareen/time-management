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
import com.igorzaitcev.management.model.Course;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.repository.CourseRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CourseManagementTest {
	private final Long expected = 1L;
	private static final Course COURSE = new Course(1L, "type");
	private static final TimeTable TIMETABLE = new TimeTable(1L, LocalDate.parse("2021-02-05"), COURSE);

	@Mock
	CourseRepository dao;

	@InjectMocks
	CourseManagement management;

	@Test
	void shouldFindCourse() {
		Course course = new Course(1L, "type");
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(course));
		assertEquals(expected, management.findCourse(1L).get().getId());
	}

	@Test
	void shouldFindCourses() {
		Course course = new Course(1L, "type");
		Course course1 = new Course(2L, "type1");
		when(dao.findAll()).thenReturn(List.of(course, course1));
		assertEquals(2, management.getCourses().size());
	}

	@Test
	void shouldDeleteCourse() {
		Course course = new Course(1L, "type");
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(course));
		assertTrue(management.deleteCourse(1L));
	}

	@Test
	void should_ThrowException_When_Teacher_Has_TimeTable() {
		Course course = new Course(1L, "type");
		course.addTimeTable(TIMETABLE);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(course));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteCourse(1L));
		assertEquals("Course can't be deleted id: " + expected, exception.getMessage());
	}

	@Test
	void should_Delete_Teacher_when_TimeTable_Remove() {
		Course course = new Course(1L, "type");
		course.addTimeTable(TIMETABLE);
		course.removeTimeTable(TIMETABLE);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(course));
		assertTrue(management.deleteCourse(1L));
	}

	@Test
	void shouldAddCourser() {
		Course course = new Course(1L, "type");
		when(dao.save(course)).thenReturn(course);
		assertEquals(expected, management.addCourse(course).getId());
	}

	@Test
	void should_ThrowException_When_Course_Null() {
		MissingArgumentException exception = assertThrows(MissingArgumentException.class,
				() -> management.addCourse(null));
		assertEquals("Course cant be null!", exception.getMessage());
	}

}
