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

@Entity(name = "Course")
@Table(name = "courses")
public class Course {

	@Id
	@SequenceGenerator(name = "course_sequence", sequenceName = "course_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@NotBlank
	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	private List<TimeTable> timeTables = new ArrayList<>();

	public Course() {
	}

	public Course(String name) {
		this.name = name;
	}

	public Course(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", timeTables=" + timeTables + "]";
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
		Course other = (Course) obj;
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
