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
import com.igorzaitcev.management.model.Auditorium;
import com.igorzaitcev.management.repository.AuditoriumRepository;

@Service
@Transactional
public class AuditoriumManagement {
	private final AuditoriumRepository repository;
	private static final Logger log = LoggerFactory.getLogger(AuditoriumManagement.class);

	public AuditoriumManagement(AuditoriumRepository repository) {
		this.repository = repository;
	}

	public Optional<Auditorium> findAuditorium(Long id) {
		return repository.findById(id);
	}

	public boolean deleteAuditorium(Long id) {
		if (!repository.findById(id).get().getTimeTables().isEmpty()) {
			throw new InvalidArgumentException("Auditorium can't be deleted id: " + id);
		}
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		log.debug("Service delete Auditorium: " + id);
		repository.deleteById(id);
		return true;
	}

	public Auditorium addAuditorium(Auditorium auditorium) {
		if (auditorium == null) {
			throw new MissingArgumentException("Auditorium cant be null!");
		} else {
			log.debug("Service add Auditorium: " + auditorium.getNumber());
		}
		return repository.save(auditorium);
	}

	public List<Auditorium> getAuditoriums() {
		return repository.findAll();
	}

	public Page<Auditorium> getAuditoriumsByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		return repository.findAll(pageRequest);
	}

	public long countAuditoriums() {
		return repository.count();
	}

}
