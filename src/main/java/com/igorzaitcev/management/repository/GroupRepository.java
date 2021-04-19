package com.igorzaitcev.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.management.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
