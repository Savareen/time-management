create table teachers (
	id BIGINT NOT NULL PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	user_id BIGINT,
	FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE SEQUENCE teacher_sequence as BIGINT INCREMENT 1;