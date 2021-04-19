package com.igorzaitcev.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.management.model.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

}
