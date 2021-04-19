package com.igorzaitcev.management.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.igorzaitcev.management.repository.AuditoriumRepository;
import com.igorzaitcev.management.repository.CourseRepository;
import com.igorzaitcev.management.repository.GroupRepository;
import com.igorzaitcev.management.repository.LectureRepository;
import com.igorzaitcev.management.repository.StudentRepository;
import com.igorzaitcev.management.repository.TeacherRepository;
import com.igorzaitcev.management.repository.TimeTableRepository;

@Profile("!test")
@Service
public class DBRegistrator {
	private static DBGenerator generator;
	private static CourseRepository courseDao;
	private static GroupRepository groupDao;
	private static LectureRepository lectureDao;
	private static AuditoriumRepository auditoriumDao;
	private static TimeTableRepository timeTableDao;


	public DBRegistrator(DBGenerator generator, CourseRepository courseDao, GroupRepository groupDao, LectureRepository lectureDao,
			AuditoriumRepository auditoriumDao, TeacherRepository teacherDao, StudentRepository studentDao, TimeTableRepository timeTableDao) {
		this.generator = generator;
		this.courseDao = courseDao;
		this.groupDao = groupDao;
		this.lectureDao = lectureDao;
		this.auditoriumDao = auditoriumDao;
		this.timeTableDao = timeTableDao;
	}


	public static void populateDB() {
		if (generator.isDatabaseEmpty()) {
			generator.getGroups().stream().forEach(groupDao::save);
			generator.generateUsers();
			generator.getCourses().stream().forEach(courseDao::save);
			generator.getLectures().stream().forEach(lectureDao::save);
			generator.getAuditoriums().stream().forEach(auditoriumDao::save);
			generator.getTimeTables().stream().forEach(timeTableDao::save);
		}
	}
}
