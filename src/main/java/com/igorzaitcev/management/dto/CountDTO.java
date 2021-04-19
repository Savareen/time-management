package com.igorzaitcev.management.dto;

import java.util.Objects;

public class CountDTO {
	private long auditoriumCount;
	private long courseCount;
	private long groupCount;
	private long lectureCount;
	private long studentCount;
	private long teacherCount;
	private long timeTableCount;
	private long userCount;

	public CountDTO() {
	}

	public CountDTO(long auditoriumCount, long courseCount, long groupCount, long lectureCount, long studentCount,
			long teacherCount, long timeTableCount, long userCount) {
		super();
		this.auditoriumCount = auditoriumCount;
		this.courseCount = courseCount;
		this.groupCount = groupCount;
		this.lectureCount = lectureCount;
		this.studentCount = studentCount;
		this.teacherCount = teacherCount;
		this.timeTableCount = timeTableCount;
		this.userCount = userCount;
	}

	public long getAuditoriumCount() {
		return auditoriumCount;
	}

	public void setAuditoriumCount(long auditoriumCount) {
		this.auditoriumCount = auditoriumCount;
	}

	public long getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(long courseCount) {
		this.courseCount = courseCount;
	}

	public long getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(long groupCount) {
		this.groupCount = groupCount;
	}

	public long getLectureCount() {
		return lectureCount;
	}

	public void setLectureCount(long lectureCount) {
		this.lectureCount = lectureCount;
	}

	public long getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(long studentCount) {
		this.studentCount = studentCount;
	}

	public long getTeacherCount() {
		return teacherCount;
	}

	public void setTeacherCount(long teacherCount) {
		this.teacherCount = teacherCount;
	}

	public long getTimeTableCount() {
		return timeTableCount;
	}

	public void setTimeTableCount(long timeTableCount) {
		this.timeTableCount = timeTableCount;
	}

	public long getUserCount() {
		return userCount;
	}

	public void setUserCount(long userCount) {
		this.userCount = userCount;
	}

	@Override
	public String toString() {
		return "CountDTO [auditoriumCount=" + auditoriumCount + ", courseCount=" + courseCount + ", groupCount="
				+ groupCount + ", lectureCount=" + lectureCount + ", studentCount=" + studentCount + ", teacherCount="
				+ teacherCount + ", timeTableCount=" + timeTableCount + ", userCount=" + userCount + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CountDTO that = (CountDTO) o;
		return Objects.equals(auditoriumCount, that.auditoriumCount) && Objects.equals(courseCount, that.courseCount)
				&& Objects.equals(groupCount, that.groupCount) && Objects.equals(lectureCount, that.lectureCount)
				&& Objects.equals(studentCount, that.studentCount) && Objects.equals(teacherCount, that.teacherCount)
				&& Objects.equals(userCount, that.userCount) && Objects.equals(timeTableCount, that.timeTableCount);
	}

}
