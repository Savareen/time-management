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
insert into groups (id, name) values ('1', 'Anna');
insert into groups (id, name) values ('2', 'John');