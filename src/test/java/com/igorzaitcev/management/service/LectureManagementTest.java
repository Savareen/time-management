package com.igorzaitcev.management.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.repository.LectureRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LectureManagementTest {
	private final Long expected = 1L;
	private static final Lecture LECTURE = new Lecture(1L, LocalTime.parse("12:00:00"), LocalTime.parse("12:15:00"));
	private static final TimeTable TIMETABLE = new TimeTable(1L, LocalDate.parse("2021-02-05"), LECTURE);

	@Mock
	LectureRepository dao;

	@InjectMocks
	LectureManagement management;

	@Test
	void shouldFindLecture() {
		Lecture lecture = new Lecture(1L, LocalTime.parse("12:00:00"), LocalTime.parse("12:15:00"));
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(lecture));
		assertEquals(expected, management.findLecture(1L).get().getId());
	}

	@Test
	void shouldFindLectures() {
		Lecture lecture = new Lecture(1L, LocalTime.parse("12:00:00"), LocalTime.parse("12:15:00"));
		Lecture lecture1 = new Lecture(2L, LocalTime.parse("13:00:00"), LocalTime.parse("12:15:00"));
		when(dao.findAll()).thenReturn(List.of(lecture, lecture1));
		assertEquals(2, management.getLectures().size());
	}

	@Test
	void shouldDeleteLecture() {
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(LECTURE));
		assertTrue(management.deleteLecture(1L));
	}

	@Test
	void should_ThrowException_When_Teacher_Has_TimeTable() {
		Lecture lecture = new Lecture(1L, LocalTime.parse("12:00:00"), LocalTime.parse("12:15:00"));
		lecture.addTimeTable(TIMETABLE);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(lecture));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteLecture(1L));
		assertEquals("Lecture can't be deleted id: " + expected, exception.getMessage());
	}

	@Test
	void should_Delete_Teacher_when_TimeTable_Remove() {
		Lecture lecture = new Lecture(1L, LocalTime.parse("12:00:00"), LocalTime.parse("12:15:00"));
		lecture.addTimeTable(TIMETABLE);
		lecture.removeTimeTable(TIMETABLE);
		when(dao.findById(1L)).thenReturn(Optional.ofNullable(lecture));
		assertTrue(management.deleteLecture(1L));
	}

	@Test
	void shouldAddLecture() {
		Lecture lecture = new Lecture(1L, LocalTime.parse("12:00:00"), LocalTime.parse("12:15:00"));
		when(dao.save(lecture)).thenReturn(lecture);
		assertEquals(expected, management.addLecture(lecture).getId());
	}

	@Test
	void should_ThrowException_When_Lecture_Null() {
		MissingArgumentException exception = assertThrows(MissingArgumentException.class,
				() -> management.addLecture(null));
		assertEquals("Lecture cant be null!", exception.getMessage());
	}

}
