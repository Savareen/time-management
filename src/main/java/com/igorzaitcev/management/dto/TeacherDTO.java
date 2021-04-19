package com.igorzaitcev.management.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TeacherDTO {
	private Long id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotNull
	@Positive
	private Long userId;

	public TeacherDTO() {
	}

	public TeacherDTO(@NotBlank String firstName, @NotBlank String lastName, @NotNull @Positive Long userId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
	}

	public TeacherDTO(Long id, String firstName, String lastName, Long userId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
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

	@Override
	public String toString() {
		return "TeacherDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userId=" + userId
				+ "]";
	}
}
