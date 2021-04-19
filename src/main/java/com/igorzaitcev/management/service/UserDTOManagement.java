package com.igorzaitcev.management.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.management.dto.UserDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.model.Group;
import com.igorzaitcev.management.model.Student;
import com.igorzaitcev.management.model.Teacher;
import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.model.UserRole;
import com.igorzaitcev.management.model.UserType;
import com.igorzaitcev.management.repository.GroupRepository;
import com.igorzaitcev.management.repository.StudentRepository;
import com.igorzaitcev.management.repository.TeacherRepository;
import com.igorzaitcev.management.repository.UserRepository;
import com.igorzaitcev.management.security.PasswordService;

@Service
@Transactional
public class UserDTOManagement {
	private UserRepository userDao;
	private GroupRepository groupDao;
	private TeacherRepository teacherDao;
	private StudentRepository studentDao;
	private PasswordService passwordService;

	public UserDTOManagement(UserRepository userDao, GroupRepository groupDao, TeacherRepository teacherDao, StudentRepository studentDao,
			PasswordService passwordService) {
		this.userDao = userDao;
		this.groupDao = groupDao;
		this.teacherDao = teacherDao;
		this.studentDao = studentDao;
		this.passwordService = passwordService;
	}

	public void registerClient(UserDTO userDTO) {
		if (userDTO.getPassword() == null) {
			throw new InvalidArgumentException("Password cannot be empty.");
		}
		if (userDTO.getPosition().equals("STUDENT")) {
			saveNewStudent(userDTO);
		} else if (userDTO.getPosition().equals("ADMIN")) {
			saveNewAdmin(userDTO);
		} else if (userDTO.getPosition().equals("TEACHER")) {
			saveNewTeacher(userDTO);
		}
	}

	public UserDTO getUserDTO(String email) {
		User user = userDao.findByEmail(email).get();
		if (user.getType().equals(UserType.STUDENT)) {
			return convertStudentToUserDTO(user);
		}
		return converTeacherToUserDTO(user);
	}

	public void updateClient(UserDTO userDTO, String email) {
		User user = userDao.findByEmail(email).get();
		if (user.getType().equals(UserType.STUDENT)) {
			updateStudent(user, userDTO);
		} else {
			updateTeacher(user, userDTO);
		}
	}

	private void updateStudent(User user, UserDTO userDTO) {
		Student student = user.getStudent();
		student.setFirstName(userDTO.getFirstName());
		student.setLastName(userDTO.getLastName());
		Group group = groupDao.findById(userDTO.getGroupId()).get();
		student.setGroup(group);
		studentDao.save(student);
	}

	private void updateTeacher(User user, UserDTO userDTO) {
		Teacher teacher = user.getTeacher();
		teacher.setFirstName(userDTO.getFirstName());
		teacher.setLastName(userDTO.getLastName());
		teacherDao.save(teacher);
	}

	private void saveNewAdmin(UserDTO userDTO) {
		userDao.save(createNewUser(userDTO));
	}

	private void saveNewStudent(UserDTO userDTO) {
		User user = userDao.save(createNewUser(userDTO));
		Student student = new Student();
		student.setFirstName(userDTO.getFirstName());
		student.setLastName(userDTO.getLastName());
		Group group = groupDao.findById(userDTO.getGroupId()).get();
		student.setGroup(group);
		student.setUser(user);
		studentDao.save(student);
	}

	private void saveNewTeacher(UserDTO userDTO) {
		User user = createNewUser(userDTO);
		Teacher teacher = new Teacher();
		teacher.setFirstName(userDTO.getFirstName());
		teacher.setLastName(userDTO.getLastName());
		teacher.setUser(user);
		teacherDao.save(teacher);
	}

	private User createNewUser(UserDTO userDTO) {
		User user = converToUser(userDTO);
		passwordService.hashPassword(user);
		return user;
	}

	private User converToUser(UserDTO userDTO) {
		User user = new User();
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		if (userDTO.getPosition().equals("ADMIN")) {
			user.setType(UserType.TEACHER);
			user.setRole(UserRole.ADMIN);
		} else {
			user.setType(UserType.valueOf(userDTO.getPosition()));
			user.setRole(UserRole.USER);
		}
		return user;
	}

	private UserDTO convertStudentToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		Student student = user.getStudent();
		userDTO.setFirstName(student.getFirstName());
		userDTO.setLastName(student.getLastName());
		userDTO.setGroupId(student.getGroup().getId());
		userDTO.setEmail(user.getEmail());
		userDTO.setPosition(user.getType().toString());
		userDTO.setPassword(null);
		return userDTO;
	}

	private UserDTO converTeacherToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		Teacher teacher = user.getTeacher();
		userDTO.setFirstName(teacher.getFirstName());
		userDTO.setLastName(teacher.getLastName());
		userDTO.setEmail(user.getEmail());
		userDTO.setPosition(user.getType().toString());
		userDTO.setPassword(null);
		return userDTO;
	}
}
