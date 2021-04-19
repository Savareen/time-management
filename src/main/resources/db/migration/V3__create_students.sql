create table students (
	id BIGINT NOT NULL PRIMARY KEY,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	group_id BIGINT,
	user_id BIGINT,
	FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE SEQUENCE student_sequence as BIGINT INCREMENT 1;