package com.igorzaitcev.management.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeTableRequestDTO {
	private Long tableId;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime finishTime;
	private String course;
	private Integer auditorium;
	private String group;
	private String teacher;

	public TimeTableRequestDTO() {
	}

	public TimeTableRequestDTO(Long tableId, LocalDate date, LocalTime startTime, LocalTime finishTime, String course,
			Integer auditorium) {
		this.tableId = tableId;
		this.date = date;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.course = course;
		this.auditorium = auditorium;
	}

	public TimeTableRequestDTO(Long tableId, LocalDate date, LocalTime startTime, LocalTime finishTime, String course,
			Integer auditorium, String group, String teacher) {
		this.tableId = tableId;
		this.date = date;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.course = course;
		this.auditorium = auditorium;
		this.group = group;
		this.teacher = teacher;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Integer getAuditorium() {
		return auditorium;
	}

	public void setAuditorium(Integer auditorium) {
		this.auditorium = auditorium;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	@Override
	public String toString() {
		return "TimeTableDTO [tableId=" + tableId + ", date=" + date + ", startTime=" + startTime + ", finishTime="
				+ finishTime + ", course=" + course + ", auditorium=" + auditorium + ", group=" + group + ", teacher="
				+ teacher + "]";
	}

}
