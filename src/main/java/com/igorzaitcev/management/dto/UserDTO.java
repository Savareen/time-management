package com.igorzaitcev.management.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

public class UserDTO {
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String email;
	private String password;
	@NotBlank
	private String position;
	private Long groupId;

	public UserDTO() {
	}

	public UserDTO(String firstName, String lastName, String email, String password, String position) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.position = position;
	}

	public UserDTO(String firstName, String lastName, String email, String password, String position, Long groupId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.position = position;
		this.groupId = groupId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "UserDTO [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password="
				+ password + ", position=" + position + ", groupId=" + groupId + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDTO that = (UserDTO) o;
		return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName)
				&& Objects.equals(email, that.email) && Objects.equals(password, that.password)
				&& Objects.equals(position, that.position) && Objects.equals(groupId, that.groupId);
	}

}
