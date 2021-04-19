create table auditoriums (
	id BIGINT NOT NULL PRIMARY KEY,
	number INTEGER NOT NULL UNIQUE
);
CREATE SEQUENCE auditorium_sequence as BIGINT INCREMENT 1;