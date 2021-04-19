create table users (
	id BIGINT NOT NULL PRIMARY KEY,
	email VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(250) NOT NULL,
	role VARCHAR(50) NOT NULL,
	user_type VARCHAR(50) NOT NULL
);
CREATE SEQUENCE user_sequence as BIGINT INCREMENT 1;
create table groups (
	id BIGINT NOT NULL PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);
CREATE SEQUENCE group_sequence as BIGINT INCREMENT 1;
create table teachers (
	id BIGINT NOT NULL PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	user_id BIGINT,
	FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE SEQUENCE teacher_sequence as BIGINT INCREMENT 1;
create table students (
	id BIGINT NOT NULL PRIMARY KEY,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	group_id BIGINT,
	user_id BIGINT,
	FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE SEQUENCE student_sequence as BIGINT INCREMENT 1;
create table auditoriums (
	id BIGINT NOT NULL PRIMARY KEY,
	number INTEGER NOT NULL UNIQUE
);
CREATE SEQUENCE auditorium_sequence as BIGINT INCREMENT 1;
create table courses (
	id BIGINT NOT NULL PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);
CREATE SEQUENCE course_sequence as BIGINT INCREMENT 1;
create table classes (
	id BIGINT NOT NULL PRIMARY KEY,
	start_time TIME NOT NULL,
	finish_time TIME NOT NULL
);
CREATE SEQUENCE class_sequence as BIGINT INCREMENT 1;
create table time_tables (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	local_date DATE NOT NULL,
	class_id BIGINT,
	auditorium_id BIGINT,
	teacher_id BIGINT,
	group_id BIGINT,
	course_id BIGINT,
	FOREIGN KEY (class_id) REFERENCES classes(id),
	FOREIGN KEY (auditorium_id) REFERENCES auditoriums(id),
	FOREIGN KEY (teacher_id) REFERENCES teachers(id),
	FOREIGN KEY (group_id) REFERENCES groups(id),
	FOREIGN KEY (course_id) REFERENCES courses(id)
);
CREATE SEQUENCE time_table_sequence as BIGINT INCREMENT 1;