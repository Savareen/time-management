package com.igorzaitcev.management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.igorzaitcev.management.model.User;

@Service
public class PasswordService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User hashPassword(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return user;
	}
}
