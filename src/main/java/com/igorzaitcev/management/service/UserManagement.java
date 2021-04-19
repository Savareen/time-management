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
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.repository.UserRepository;
import com.igorzaitcev.management.security.PasswordService;

@Service
@Transactional
public class UserManagement {
	private final UserRepository repository;
	private PasswordService passwordService;

	private static final Logger log = LoggerFactory.getLogger(UserManagement.class);

	public UserManagement(UserRepository dao, PasswordService passwordService) {
		this.repository = dao;
		this.passwordService = passwordService;
	}

	public Optional<User> findUser(Long id) {
		return repository.findById(id);
	}

	public Optional<User> findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public boolean deleteUser(Long id) {
		User user = repository.findById(id).get();
		checkRelationships(user);
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		log.debug("Service delete User: " + id);
		repository.deleteById(id);
		return true;
	}

	public User addUser(User user) {
		checkEmail(user);
		if (user.getId() == null) {
			passwordService.hashPassword(user);
		}
		log.debug("Service add User: " + user.getEmail());
		return repository.save(user);
	}

	public List<User> getUsers() {
		return repository.findAll();
	}

	public Page<User> getUsersByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		return repository.findAll(pageRequest);
	}

	public long countUsers() {
		return repository.count();
	}

	private void checkEmail(User user) {
		if (checkUpdate(user)) {
			throw new InvalidArgumentException("Invalid Email: " + user.getEmail());
		}
	}

	private boolean checkUpdate(User user) {
		boolean update = false;
		Optional<User> userInDB = repository.findByEmail(user.getEmail());
		if (userInDB.isPresent() && user.getId() != null) {
			update = (user.getId().equals(userInDB.get().getId()));
		}
		return userInDB.isPresent() && !update;
	}

	private void checkRelationships(User user) {
		if (user.getTeacher() != null) {
			throw new InvalidArgumentException(
					"User can't be deleted because of Teacher id: " + user.getTeacher().getId());
		} else if (user.getStudent() != null) {
			throw new InvalidArgumentException(
					"User can't be deleted because of Student id: " + user.getStudent().getId());
		}
	}
}
