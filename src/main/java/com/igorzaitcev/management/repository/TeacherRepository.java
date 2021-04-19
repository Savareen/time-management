package com.igorzaitcev.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.management.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
