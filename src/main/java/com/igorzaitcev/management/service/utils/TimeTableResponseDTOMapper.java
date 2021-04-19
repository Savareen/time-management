package com.igorzaitcev.management.service.utils;

import org.springframework.stereotype.Component;

import com.igorzaitcev.management.dto.TimeTableResponseDTO;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.repository.AuditoriumRepository;
import com.igorzaitcev.management.repository.CourseRepository;
import com.igorzaitcev.management.repository.GroupRepository;
import com.igorzaitcev.management.repository.LectureRepository;
import com.igorzaitcev.management.repository.TeacherRepository;

@Component
public class TimeTableResponseDTOMapper {
	private LectureRepository lectureDao;
	private AuditoriumRepository auditoriumDao;
	private TeacherRepository teacherDao;
	private GroupRepository groupDao;
	private CourseRepository courseDao;

	public TimeTableResponseDTOMapper(LectureRepository lectureDao, AuditoriumRepository auditoriumDao, TeacherRepository teacherDao, GroupRepository groupDao,
			CourseRepository courseDao) {
		this.lectureDao = lectureDao;
		this.auditoriumDao = auditoriumDao;
		this.teacherDao = teacherDao;
		this.groupDao = groupDao;
		this.courseDao = courseDao;
	}

	public TimeTable converToTimeTable(TimeTableResponseDTO timeTableMap) {
		TimeTable table = new TimeTable();
		table.setId(timeTableMap.getId());
		table.setDate(timeTableMap.getDate());
		table.setLecture(lectureDao.findById(timeTableMap.getLectureId()).get());
		table.setAuditorium(auditoriumDao.findById(timeTableMap.getAuditoriumId()).get());
		table.setTeacher(teacherDao.findById(timeTableMap.getTeacherId()).get());
		table.setGroup(groupDao.findById(timeTableMap.getGroupId()).get());
		table.setCourse(courseDao.findById(timeTableMap.getCourseId()).get());
		return table;
	}

	public TimeTableResponseDTO convertToTimeTableResponseDTO(TimeTable table) {
		TimeTableResponseDTO map = new TimeTableResponseDTO();
		map.setId(table.getId());
		map.setDate(table.getDate());
		map.setLectureId(table.getLecture().getId());
		map.setAuditoriumId(table.getAuditorium().getId());
		map.setTeacherId(table.getTeacher().getId());
		map.setGroupId(table.getGroup().getId());
		map.setCourseId(table.getCourse().getId());
		return map;
	}
}
