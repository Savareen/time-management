package com.igorzaitcev.management.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Teacher;
import com.igorzaitcev.management.model.TimeTable;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

	@Query("SELECT t FROM TimeTable t WHERE t.group = ?1 AND t.date BETWEEN ?2 AND ?3")
	List<TimeTable> findByGroup(Group group, LocalDate startDate, LocalDate finishDate);

	@Query("SELECT t FROM TimeTable t WHERE t.teacher = ?1 AND t.date BETWEEN ?2 AND ?3")
	List<TimeTable> findByTeacher(Teacher teacher, LocalDate startDate, LocalDate finishDate);
}
