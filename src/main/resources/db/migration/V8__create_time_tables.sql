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