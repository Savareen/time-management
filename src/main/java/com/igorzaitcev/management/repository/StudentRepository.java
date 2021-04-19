package com.igorzaitcev.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.management.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
