create table classes (
	id BIGINT NOT NULL PRIMARY KEY,
	start_time TIME NOT NULL,
	finish_time TIME NOT NULL
);
CREATE SEQUENCE class_sequence as BIGINT INCREMENT 1;