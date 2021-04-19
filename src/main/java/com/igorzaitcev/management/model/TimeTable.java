package com.igorzaitcev.management.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "TimeTable")
@Table(name = "time_tables")
public class TimeTable {

	@Id
	@SequenceGenerator(name = "time_table_sequence", sequenceName = "time_table_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_table_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Column(name = "local_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", referencedColumnName = "id")
	private Lecture lecture;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auditorium_id", referencedColumnName = "id")
	private Auditorium auditorium;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", referencedColumnName = "id")
	private Teacher teacher;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	private Group group;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName = "id")
	private Course course;

	public TimeTable() {
	}

	public TimeTable(LocalDate date, Lecture lecture, Auditorium auditorium, Teacher teacher, Group group,
			Course course) {
		this.date = date;
		this.lecture = lecture;
		this.auditorium = auditorium;
		this.teacher = teacher;
		this.group = group;
		this.course = course;
	}

	public TimeTable(Long id, LocalDate date, Lecture lecture, Auditorium auditorium, Teacher teacher, Group group,
			Course course) {
		this.id = id;
		this.date = date;
		this.lecture = lecture;
		this.auditorium = auditorium;
		this.teacher = teacher;
		this.group = group;
		this.course = course;
	}

	public TimeTable(Long id, LocalDate date, Auditorium auditorium) {
		this.id = id;
		this.date = date;
		this.auditorium = auditorium;
	}

	public TimeTable(Long id, LocalDate date, Course course) {
		super();
		this.id = id;
		this.date = date;
		this.course = course;
	}

	public TimeTable(Long id, LocalDate date, Group group) {
		super();
		this.id = id;
		this.date = date;
		this.group = group;
	}

	public TimeTable(Long id, LocalDate date, Lecture lecture) {
		super();
		this.id = id;
		this.date = date;
		this.lecture = lecture;
	}

	public TimeTable(Long id, LocalDate date, Teacher teacher) {
		super();
		this.id = id;
		this.date = date;
		this.teacher = teacher;
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

	public Lecture getLecture() {
		return lecture;
	}

	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}

	public Auditorium getAuditorium() {
		return auditorium;
	}

	public void setAuditorium(Auditorium auditorium) {
		this.auditorium = auditorium;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "TimeTable [id=" + id + ", date=" + date + ", lecture=" + lecture + ", auditorium=" + auditorium
				+ ", teacher=" + teacher + ", group=" + group + ", course=" + course + "]";
	}

}
