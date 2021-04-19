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
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.repository.GroupRepository;

@Service
@Transactional
public class GroupManagement {
	private final GroupRepository repository;
	private static final Logger log = LoggerFactory.getLogger(GroupManagement.class);

	public GroupManagement(GroupRepository dao) {
		this.repository = dao;
	}

	public Optional<Group> findGroup(Long id) {
		return repository.findById(id);
	}

	public boolean deleteGroup(Long id) {
		Group group = repository.findById(id).get();
		if (!group.getTimeTables().isEmpty()) {
			throw new InvalidArgumentException("Group can't be deleted because of TimeTable id: " + id);
		} else if (!group.getStudents().isEmpty()) {
			throw new InvalidArgumentException("Group can't be deleted because of Student id: " + id);
		}
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		log.debug("Service delete Group: " + id);
		repository.deleteById(id);
		return true;
	}

	public Group addGroup(Group group) {
		if (group == null) {
			throw new MissingArgumentException("Group cant be null!");
		} else {
			log.debug("Service add Group: " + group.getName());
		}
		return repository.save(group);
	}

	public List<Group> getGroups() {
		return repository.findAll();
	}

	public Page<Group> getGroupsByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		return repository.findAll(pageRequest);
	}

	public long countGroups() {
		return repository.count();
	}

}
