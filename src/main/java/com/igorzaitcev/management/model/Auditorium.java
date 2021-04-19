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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity(name = "Auditorium")
@Table(name = "auditoriums")
public class Auditorium {

	@Id
	@SequenceGenerator(name = "auditorium_sequence", sequenceName = "auditorium_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auditorium_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@NotNull
	@Positive
	@Column(name = "number", unique = true, nullable = false)
	private Integer number;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auditorium")
	private List<TimeTable> timeTables = new ArrayList<>();

	public Auditorium() {
	}

	public Auditorium(Integer number) {
		this.number = number;
	}

	public Auditorium(Long auditoriumId, Integer number) {
		this.id = auditoriumId;
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long auditoriumId) {
		this.id = auditoriumId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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
		return "Auditorium [id=" + id + ", number=" + number + ", timeTables=" + timeTables + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		Auditorium other = (Auditorium) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

}
