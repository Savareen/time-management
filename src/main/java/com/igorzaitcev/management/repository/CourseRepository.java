package com.igorzaitcev.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.management.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
