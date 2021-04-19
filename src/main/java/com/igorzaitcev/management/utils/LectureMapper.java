package com.igorzaitcev.management.utils;

import java.time.LocalTime;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.igorzaitcev.management.model.Lecture;

@Component
public class LectureMapper implements Function<String, Lecture> {
	@Override
	public Lecture apply(String s) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException("Input string cant be empty or null");
		}
		String[] names = s.split("_");
		return new Lecture(LocalTime.parse(names[0]), LocalTime.parse(names[1]));
	}
}
