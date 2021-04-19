package com.igorzaitcev.management.utils;

import java.util.Random;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.igorzaitcev.management.dto.UserDTO;

@Component
public class UserMapper implements Function<String, UserDTO> {
	Random random = new Random();

	@Override
	public UserDTO apply(String s) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException("Input string cant be empty or null");
		}
		String[] names = s.split("_");
		UserDTO userDTO = new UserDTO(names[0], names[1], names[2], names[3], names[4]);
		if (userDTO.getPosition().equals("STUDENT")) {
			userDTO.setGroupId(getRandom3());
		}
		return userDTO;
	}

	private Long getRandom3() {
		return (long) random.nextInt(3) + 1;
	}
}
