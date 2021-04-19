package com.igorzaitcev.management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.management.dto.TeacherDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Teacher;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.TeacherRepository;
import com.igorzaitcev.management.repository.UserRepository;

@Service
@Transactional
public class TeacherManagement {
	private final TeacherRepository repository;
	private UserRepository userDao;

	private static final Logger log = LoggerFactory.getLogger(TeacherManagement.class);

	public TeacherManagement(TeacherRepository dao, UserRepository userDao) {
		this.repository = dao;
		this.userDao = userDao;
	}

	public TeacherDTO findTeacher(Long id) {
		Optional<Teacher> teacher = repository.findById(id);
		if (teacher.isEmpty()) {
			throw new InvalidArgumentException("Invalid Teacher ID: " + id);
		}
		return convertToTeacherDTO(teacher.get());
	}

	public boolean deleteTeacher(Long id) {
		Teacher teacher = repository.findById(id).get();
		if (!teacher.getTimeTables().isEmpty()) {
			throw new InvalidArgumentException("Teacher can't be deleted id: " + id);
		}
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		teacher.getUser().setTeacher(null);
		repository.deleteById(id);
		log.debug("Service delete Teacher: " + id);
		return true;
	}


	public TeacherDTO addTeacher(TeacherDTO teacherDTO) {
		checkTaecherIsValid(teacherDTO);
		Teacher teacher = convertToTeacher(teacherDTO);
		return convertToTeacherDTO(repository.save(teacher));
	}

	public List<TeacherDTO> getTeachers() {
		List<Teacher> teachers = repository.findAll();
		if (teachers.isEmpty()) {
			throw new InvalidArgumentException("Teachers not found...");
		}
		return teachers.stream().map(this::convertToTeacherDTO).collect(Collectors.toList());
	}

	public Page<TeacherDTO> getTeachersByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		Page<Teacher> teachers = repository.findAll(pageRequest);
		if (teachers.isEmpty()) {
			throw new InvalidArgumentException("Teachers not found...");
		}
		return new PageImpl<>(teachers.stream().map(this::convertToTeacherDTO).collect(Collectors.toList()));
	}

	public long countTeachers() {
		return repository.count();
	}


	private void checkTaecherIsValid(TeacherDTO teacherDTO) {
		if (!matchUserType(teacherDTO.getUserId())) {
			throw new InvalidArgumentException("Invalid Teacher UserID: " + teacherDTO.getUserId());
		} else {
			log.debug("Service add Teacher: " + teacherDTO.getUserId());
		}
	}

	private boolean matchUserType(long id) {
		Optional<User> user = userDao.findById(id);
		if (user.isPresent()) {
			return user.get().getType().equals(UserType.TEACHER);
		}
		return false;
	}

	private TeacherDTO convertToTeacherDTO(Teacher teacher) {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setId(teacher.getId());
		teacherDTO.setFirstName(teacher.getFirstName());
		teacherDTO.setLastName(teacher.getLastName());
		teacherDTO.setUserId(teacher.getUser().getId());
		return teacherDTO;
	}

	private Teacher convertToTeacher(TeacherDTO teacherDTO) {
		Teacher teacher = new Teacher();
		teacher.setId(teacherDTO.getId());
		teacher.setFirstName(teacherDTO.getFirstName());
		teacher.setLastName(teacherDTO.getLastName());
		User user = userDao.findById(teacherDTO.getUserId()).get();
		teacher.setUser(user);
		return teacher;
	}

}
