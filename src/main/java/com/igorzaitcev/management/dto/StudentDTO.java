package com.igorzaitcev.management.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class StudentDTO {
	private Long id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotNull
	@Positive
	private Long userId;
	@Positive
	@NotNull
	private Long groupId;

	public StudentDTO() {
	}

	public StudentDTO(String firstName, String lastName, Long userId, Long groupId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
		this.groupId = groupId;
	}

	public StudentDTO(Long id, String firstName, String lastName, Long userId, Long groupId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
		this.groupId = groupId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "StudentDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userId=" + userId
				+ ", groupId=" + groupId + "]";
	}


}
