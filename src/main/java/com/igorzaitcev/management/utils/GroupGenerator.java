package com.igorzaitcev.management.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.igorzaitcev.management.model.Group;

@Component
public class GroupGenerator {
	private List<Group> groups;
	private Random random;

	public GroupGenerator() {
		groups = new ArrayList<>();
		random = new Random();
	}

	public List<Group> getGroups() {
		for (int i = 0; i < 3; i++) {
			groups.add(new Group(createGroupName()));
		}
		return groups;
	}

	private String createGroupName() {
		String groupString = random.ints(97, 122 + 1).limit(2)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		String groupNumber = Integer.toString(random.nextInt(90) + 10);
		return groupString + "_" + groupNumber;
	}
}
