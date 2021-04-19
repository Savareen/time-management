package com.igorzaitcev.management.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.igorzaitcev.management.dto.TimeTableResponseDTO;
import com.igorzaitcev.management.dto.UserDTO;
import com.igorzaitcev.management.model.Auditorium;
import com.igorzaitcev.management.model.Course;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.service.UserDTOManagement;
import com.igorzaitcev.management.service.UserManagement;
import com.igorzaitcev.management.service.utils.TimeTableResponseDTOMapper;

@Profile("!test")
@Component
public class DBGenerator {
	List<TimeTable> tables = new ArrayList<>();
	Random random = new Random();

	private UserMapper mapper;
	private CourseMapper courseMapper;
	private GroupGenerator groupGenerator;
	private LectureMapper lectureMapper;
	private UserManagement userManagement;
	private UserDTOManagement userDTOManagement;
	private TimeTableResponseDTOMapper timeTableMapper;

	public DBGenerator(UserMapper mapper, CourseMapper courseMapper, GroupGenerator groupGenerator,
			LectureMapper lectureMapper, UserManagement userManagement, UserDTOManagement userDTOManagement,
			TimeTableResponseDTOMapper timeTableMapper) {
		this.mapper = mapper;
		this.courseMapper = courseMapper;
		this.groupGenerator = groupGenerator;
		this.lectureMapper = lectureMapper;
		this.userManagement = userManagement;
		this.userDTOManagement = userDTOManagement;
		this.timeTableMapper = timeTableMapper;
	}

	public boolean isDatabaseEmpty() {
		return userManagement.countUsers() == 0L;
	}

	public List<Course> getCourses() {
		return DataLoader.loadData(courseMapper, "data/courses.txt");
	}

	public List<Group> getGroups() {
		return groupGenerator.getGroups();
	}

	public List<Lecture> getLectures() {
		return DataLoader.loadData(lectureMapper, "data/classes.txt");
	}

	public List<Auditorium> getAuditoriums() {
		return new Random().ints(1, 300).distinct().limit(5).boxed().map(Auditorium::new)
				.collect(Collectors.toList());
	}

	public List<TimeTable> getTimeTables() {
		generateTimeTable();
		return tables;
	}

	public void generateUsers() {
		List<UserDTO> users = DataLoader.loadData(mapper, "data/user.txt");
		users.stream().forEach(userDTOManagement::registerClient);
	}

	private void generateTimeTable() {
		for (int i=0; i<30;i++) {
			Long interval = (long) i;
			List<TimeTable> table = IntStream.range(1, 6)
					.mapToObj(lecture -> new TimeTableResponseDTO(LocalDate.now().plusDays(interval), (long) lecture,
							getRandom5(), getRandom5(), getRandom3(), getRandom5()))
					.map(timeTableMapper::converToTimeTable)
					.collect(Collectors.toList());
			tables = Stream.concat(tables.stream(), table.stream()).collect(Collectors.toList());
		}
	}

	private Long getRandom3() {
		return (long) random.nextInt(3) + 1;
	}

	private Long getRandom5() {
		return (long) random.nextInt(5) + 1;
	}

}
