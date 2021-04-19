package com.igorzaitcev.management.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.Course;
import com.igorzaitcev.management.repository.CourseRepository;

@Service
@Transactional
public class CourseManagement {
	private final CourseRepository repository;
	private static final Logger log = LoggerFactory.getLogger(CourseManagement.class);

	public CourseManagement(CourseRepository dao) {
		this.repository = dao;
	}

	public Optional<Course> findCourse(Long id) {
		return repository.findById(id);
	}

	public boolean deleteCourse(Long id) {
		if (!repository.findById(id).get().getTimeTables().isEmpty()) {
			throw new InvalidArgumentException("Course can't be deleted id: " + id);
		}
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		log.debug("Service delete Course: " + id);
		repository.deleteById(id);
		return true;
	}

	public Course addCourse(Course course) {
		if (course == null) {
			throw new MissingArgumentException("Course cant be null!");
		} else {
			log.debug("Service add Course: " + course.getName());
		}
		return repository.save(course);
	}

	public List<Course> getCourses() {
		return repository.findAll();
	}

	public Page<Course> getCoursesByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		return repository.findAll(pageRequest);
	}

	public long countCourses() {
		return repository.count();
	}

}
