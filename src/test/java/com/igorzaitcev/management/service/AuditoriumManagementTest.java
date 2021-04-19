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
import com.igorzaitcev.management.model.Auditorium;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.repository.AuditoriumRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuditoriumManagementTest {
	private final Long expected = 1L;
	private static final Auditorium AUDITORIUM = new Auditorium(1L, 23);
	private static final TimeTable TIMETABLE = new TimeTable(1L, LocalDate.parse("2021-02-05"), AUDITORIUM);

	@Mock
	AuditoriumRepository repository;

	@InjectMocks
	AuditoriumManagement management;

	@Test
	void shouldFindAuditorium() {
		Auditorium auditorium = new Auditorium(1L, 23);
		when(repository.findById(1L)).thenReturn(Optional.ofNullable(auditorium));
		assertEquals(expected, management.findAuditorium(1L).get().getId());
	}

	@Test
	void shouldFindAuditoriums() {
		Auditorium auditorium = new Auditorium(1L, 23);
		Auditorium auditorium1 = new Auditorium(2L, 24);
		when(repository.findAll()).thenReturn(List.of(auditorium, auditorium1));
		assertEquals(2, management.getAuditoriums().size());
	}

	@Test
	void shouldDeleteAuditorium() {
		Auditorium auditorium = new Auditorium(1L, 23);
		when(repository.findById(1L)).thenReturn(Optional.ofNullable(auditorium));
		assertTrue(management.deleteAuditorium(1L));
	}

	@Test
	void should_ThrowException_When_Teacher_Has_TimeTable() {
		Auditorium auditorium = new Auditorium(1L, 23);
		auditorium.addTimeTable(TIMETABLE);
		when(repository.findById(1L)).thenReturn(Optional.ofNullable(auditorium));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteAuditorium(1L));
		assertEquals("Auditorium can't be deleted id: " + expected, exception.getMessage());
	}

	@Test
	void should_Delete_Teacher_when_TimeTable_Remove() {
		Auditorium auditorium = new Auditorium(1L, 23);
		auditorium.addTimeTable(TIMETABLE);
		auditorium.removeTimeTable(TIMETABLE);
		when(repository.findById(1L)).thenReturn(Optional.ofNullable(auditorium));
		assertTrue(management.deleteAuditorium(1L));
	}

	@Test
	void shouldAddAuditorium() {
		Auditorium auditorium = new Auditorium(1L, 23);
		when(repository.save(auditorium)).thenReturn(auditorium);
		assertEquals(expected, management.addAuditorium(auditorium).getId());
	}

	@Test
	void should_ThrowException_When_Auditorium_Null() {
		MissingArgumentException exception = assertThrows(MissingArgumentException.class,
				() -> management.addAuditorium(null));
		assertEquals("Auditorium cant be null!", exception.getMessage());
	}
}
