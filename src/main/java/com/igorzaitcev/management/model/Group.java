package com.igorzaitcev.management.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity(name = "Group")
@Table(name = "groups")
public class Group {

	@Id
	@SequenceGenerator(name = "group_sequence", sequenceName = "group_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@NotBlank
	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private List<TimeTable> timeTables = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private List<Student> students = new ArrayList<>();

	public Group() {
	}

	public Group(String name) {
		this.name = name;
	}

	public Group(Long groupId, String name) {
		this.id = groupId;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long groupId) {
		this.id = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTimeTable(TimeTable timeTable) {
		if (!this.timeTables.contains(timeTable)) {
			timeTables.add(timeTable);
		}
	}

	public void removeTimeTable(TimeTable timeTable) {
		if (this.timeTables.contains(timeTable)) {
			this.timeTables.remove(timeTable);
		}
	}

	public List<TimeTable> getTimeTables() {
		return timeTables;
	}

	public void setTimeTables(List<TimeTable> timeTables) {
		this.timeTables = timeTables;
	}

	public void addStudent(Student student) {
		if (!this.students.contains(student)) {
			students.add(student);
		}
	}

	public void removeStudent(Student student) {
		if (this.students.contains(student)) {
			this.students.remove(student);
		}
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", timeTables=" + timeTables + ", students=" + students + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
