create table groups (
	id BIGINT NOT NULL PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
);
CREATE SEQUENCE group_sequence as BIGINT INCREMENT 1;