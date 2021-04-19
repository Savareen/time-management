package com.igorzaitcev.management.model;

import java.time.LocalTime;
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

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "Lecture")
@Table(name = "classes")
public class Lecture {

	@Id
	@SequenceGenerator(name = "class_sequence", sequenceName = "class_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@NotNull
	@Column(name = "start_time", nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime startTime;
	@NotNull
	@Column(name = "finish_time", nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime finishTime;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lecture")
	private List<TimeTable> timeTables = new ArrayList<>();

	public Lecture() {
	}

	public Lecture(LocalTime startTime, LocalTime finishTime) {
		this.startTime = startTime;
		this.finishTime = finishTime;
	}

	public Lecture(Long id, LocalTime startTime, LocalTime finishTime) {
		this.id = id;
		this.startTime = startTime;
		this.finishTime = finishTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
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
		return "Lecture [id=" + id + ", startTime=" + startTime + ", finishTime=" + finishTime + ", timeTables="
				+ timeTables + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((finishTime == null) ? 0 : finishTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		Lecture other = (Lecture) obj;
		if (finishTime == null) {
			if (other.finishTime != null)
				return false;
		} else if (!finishTime.equals(other.finishTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
}
