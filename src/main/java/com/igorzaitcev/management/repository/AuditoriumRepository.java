package com.igorzaitcev.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.management.model.Auditorium;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {

}
