package com.igorzaitcev.management.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.Lecture;
import com.igorzaitcev.management.repository.LectureRepository;

@Service
@Transactional
public class LectureManagement {
	private final LectureRepository repository;
	private static final Logger log = LoggerFactory.getLogger(LectureManagement.class);

	public LectureManagement(LectureRepository dao) {
		this.repository = dao;
	}

	public Optional<Lecture> findLecture(Long id) {
		return repository.findById(id);
	}

	public boolean deleteLecture(Long id) {
		if (!repository.findById(id).get().getTimeTables().isEmpty()) {
			throw new InvalidArgumentException("Lecture can't be deleted id: " + id);
		}
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		log.debug("Service delete Lecture: " + id);
		repository.deleteById(id);
		return true;
	}

	public Lecture addLecture(Lecture lecture) {
		if (lecture == null) {
			throw new MissingArgumentException("Lecture cant be null!");
		} else {
			log.debug("Service add Lecture: " + lecture.getStartTime());
		}
		return repository.save(lecture);
	}

	public List<Lecture> getLectures() {
		return repository.findAll();
	}

	public Page<Lecture> getLecturesByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		return repository.findAll(pageRequest);
	}

	public long countLectures() {
		return repository.count();
	}

}
