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

import com.igorzaitcev.management.dto.StudentDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Student;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.GroupRepository;
import com.igorzaitcev.management.repository.StudentRepository;
import com.igorzaitcev.management.repository.UserRepository;

@Service
@Transactional
public class StudentManagement {
	private final StudentRepository repository;
	private UserRepository userDao;
	private GroupRepository groupDao;
	private static final Logger log = LoggerFactory.getLogger(StudentManagement.class);

	public StudentManagement(StudentRepository dao, UserRepository userDao, GroupRepository groupDao) {
		this.repository = dao;
		this.userDao = userDao;
		this.groupDao = groupDao;
	}

	public StudentDTO findStudent(Long id) {
		Optional<Student> student = repository.findById(id);
		if (student.isEmpty()) {
			throw new InvalidArgumentException("Invalid Student ID: " + id);
		}
		return convertToStudentDTO(student.get());
	}

	public boolean deleteStudent(Long id) {
		log.debug("Service delete Student: " + id);
		Optional<Student> student = repository.findById(id);
		if (student.isEmpty()) {
			return false;
		}
		deleteAssociations(student.get());
		repository.deleteById(id);
		return true;
	}

	public StudentDTO addStudent(StudentDTO studentDTO) {
		checkStudentIsValid(studentDTO);
		Student student = convertToStudent(studentDTO);
		if (student.getId() != null) {
			repository.findById(student.getId()).ifPresent(this::deleteAssociations);
		}
		addAssociations(repository.save(student));
		return convertToStudentDTO(student);
	}


	public List<StudentDTO> getStudents() {
		List<Student> students = repository.findAll();
		if (students.isEmpty()) {
			throw new InvalidArgumentException("Students not found...");
		}
		return students.stream().map(this::convertToStudentDTO).collect(Collectors.toList());
	}

	public Page<StudentDTO> getStudentsByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		Page<Student> students = repository.findAll(pageRequest);
		if (students.isEmpty()) {
			throw new InvalidArgumentException("Students not found...");
		}
		return new PageImpl<>(students.stream().map(this::convertToStudentDTO).collect(Collectors.toList()));
	}

	public long countStudents() {
		return repository.count();
	}

	private void checkStudentIsValid(StudentDTO studentDTO) {
		if (!checkUserId(studentDTO.getUserId())) {
			throw new InvalidArgumentException("Invalid Student UserID: " + studentDTO.getUserId());
		} else {
			log.debug("Service add Student: " + studentDTO.getUserId());
		}
	}

	private boolean checkUserId(Long id) {
		Optional<User> user = userDao.findById(id);
		if (user.isPresent()) {
			return user.get().getType().equals(UserType.STUDENT);
		}
		return false;
	}

	private StudentDTO convertToStudentDTO(Student student) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setId(student.getId());
		studentDTO.setFirstName(student.getFirstName());
		studentDTO.setLastName(student.getLastName());
		studentDTO.setUserId(student.getUser().getId());
		studentDTO.setGroupId(student.getGroup().getId());
		return studentDTO;
	}

	private Student convertToStudent(StudentDTO studentDTO) {
		Student student = new Student();
		student.setId(studentDTO.getId());
		student.setFirstName(studentDTO.getFirstName());
		student.setLastName(studentDTO.getLastName());
		Group group = groupDao.findById(studentDTO.getGroupId()).get();
		student.setGroup(group);
		User user = userDao.findById(studentDTO.getUserId()).get();
		student.setUser(user);
		return student;
	}

	private void addAssociations(Student student) {
		student.getGroup().addStudent(student);
	}

	private void deleteAssociations(Student student) {
		student.getGroup().removeStudent(student);
		student.getUser().setStudent(null);
	}

}
