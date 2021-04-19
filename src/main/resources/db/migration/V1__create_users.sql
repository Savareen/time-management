create table users (
	id BIGINT NOT NULL PRIMARY KEY,
	email VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(250) NOT NULL,
	role VARCHAR(50) NOT NULL,
	user_type VARCHAR(50) NOT NULL
);
CREATE SEQUENCE user_sequence as BIGINT INCREMENT 1;