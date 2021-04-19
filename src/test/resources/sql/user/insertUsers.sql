create table users (
	id BIGINT NOT NULL PRIMARY KEY,
	email VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(250) NOT NULL,
	role VARCHAR(50) NOT NULL,
	user_type VARCHAR(50) NOT NULL
);
CREATE SEQUENCE user_sequence as BIGINT INCREMENT 1;
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
insert into users (id, email, password, role, user_type) values ('1', 'Mike', '123', 'USER', 'STUDENT');
insert into users (id, email, password, role, user_type) values ('2', 'Anna', '222', 'USER', 'STUDENT');