package com.igorzaitcev.management.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class TimeTableResponseDTO {
	private Long id;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	@NotNull
	private Long lectureId;
	@NotNull
	private Long auditoriumId;
	@NotNull
	private Long teacherId;
	@NotNull
	private Long groupId;
	@NotNull
	private Long courseId;

	public TimeTableResponseDTO() {
	}

	public TimeTableResponseDTO(LocalDate date, Long lectureId, Long auditoriumId, Long teacherId, Long groupId,
			Long courseId) {
		this.date = date;
		this.lectureId = lectureId;
		this.auditoriumId = auditoriumId;
		this.teacherId = teacherId;
		this.groupId = groupId;
		this.courseId = courseId;
	}

	public TimeTableResponseDTO(Long id, LocalDate date, Long lectureId, Long auditoriumId, Long teacherId, Long groupId,
			Long courseId) {
		this.id = id;
		this.date = date;
		this.lectureId = lectureId;
		this.auditoriumId = auditoriumId;
		this.teacherId = teacherId;
		this.groupId = groupId;
		this.courseId = courseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getLectureId() {
		return lectureId;
	}

	public void setLectureId(Long lectureId) {
		this.lectureId = lectureId;
	}

	public Long getAuditoriumId() {
		return auditoriumId;
	}

	public void setAuditoriumId(Long auditoriumId) {
		this.auditoriumId = auditoriumId;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "TimeTableMap [id=" + id + ", date=" + date + ", lectureId=" + lectureId + ", auditoriumId="
				+ auditoriumId + ", teacherId=" + teacherId + ", groupId=" + groupId + ", courseId=" + courseId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditoriumId == null) ? 0 : auditoriumId.hashCode());
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lectureId == null) ? 0 : lectureId.hashCode());
		result = prime * result + ((teacherId == null) ? 0 : teacherId.hashCode());
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
		TimeTableResponseDTO other = (TimeTableResponseDTO) obj;
		if (auditoriumId == null) {
			if (other.auditoriumId != null)
				return false;
		} else if (!auditoriumId.equals(other.auditoriumId))
			return false;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lectureId == null) {
			if (other.lectureId != null)
				return false;
		} else if (!lectureId.equals(other.lectureId))
			return false;
		if (teacherId == null) {
			if (other.teacherId != null)
				return false;
		} else if (!teacherId.equals(other.teacherId))
			return false;
		return true;
	}

}
