package com.igorzaitcev.management.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.igorzaitcev.management.dto.TimeTableRequestDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.TimeTableRepository;
import com.igorzaitcev.management.repository.UserRepository;

@Service
public class TimeTableRequestDTOMapper {
	private UserRepository userDao;
	private TimeTableRepository tableDao;

	public TimeTableRequestDTOMapper(UserRepository userDao, TimeTableRepository tableDao) {
		this.userDao = userDao;
		this.tableDao = tableDao;
	}

	public List<TimeTableRequestDTO> getSchedule(Long interval, String email) {
		List<TimeTable> tables = getTimeTables(interval, email);
		if (tables.isEmpty()) {
			throw new InvalidArgumentException("Empty timetable");
		}
		return tables.stream()
				.map(this::convertToTimeTableRequestDTO).sorted(Comparator.comparingLong(TimeTableRequestDTO::getTableId))
				.collect(Collectors.toList());
	}

	public Page<TimeTableRequestDTO> getTimeTableDTO(Page<TimeTable> tables) {
		return new PageImpl<>(tables.stream().map(this::convertToTimeTableRequestDTO)
				.sorted(Comparator.comparingLong(TimeTableRequestDTO::getTableId)).collect(Collectors.toList()));
	}

	public TimeTableRequestDTO convertToTimeTableRequestDTO(TimeTable table) {
		TimeTableRequestDTO timeTableDTO = new TimeTableRequestDTO();
		timeTableDTO.setTableId(table.getId());
		timeTableDTO.setDate(table.getDate());
		Lecture lecture = table.getLecture();
		timeTableDTO.setStartTime(lecture.getStartTime());
		timeTableDTO.setFinishTime(lecture.getFinishTime());
		timeTableDTO.setCourse(table.getCourse().getName());
		timeTableDTO.setAuditorium(table.getAuditorium().getNumber());
		timeTableDTO.setTeacher(table.getTeacher().getLastName());
		timeTableDTO.setGroup(table.getGroup().getName());
		return timeTableDTO;
	}

	private List<TimeTable> getTimeTables(Long interval, String email) {
		User user = userDao.findByEmail(email).get();
		if (user.getType().equals(UserType.TEACHER)) {
			return findTeacherTables(interval, user);
		}
		return findStudentTables(interval, user);
	}

	private List<TimeTable> findStudentTables(Long interval, User user) {
		if (user.getStudent() == null) {
			throw new InvalidArgumentException("Sorry, you are not authorized");
		}
		return tableDao.findByGroup(user.getStudent().getGroup(), LocalDate.now(),
				LocalDate.now().plusDays(interval));
	}

	private List<TimeTable> findTeacherTables(Long interval, User user) {
		if (user.getTeacher() == null) {
			throw new InvalidArgumentException("Sorry, you are not authorized");
		}
		return tableDao.findByTeacher(user.getTeacher(), LocalDate.now(), LocalDate.now().plusDays(interval));
	}

}
