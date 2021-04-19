package com.igorzaitcev.management.utils;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.igorzaitcev.management.model.Course;

@Component
public class CourseMapper implements Function<String, Course> {
	@Override
	public Course apply(String s) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException("Input string cant be empty or null");
		}
		return new Course(s);
	}
}
