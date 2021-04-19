package com.igorzaitcev.management.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.management.dto.TimeTableRequestDTO;
import com.igorzaitcev.management.dto.TimeTableResponseDTO;
import com.igorzaitcev.management.exceptions.InvalidArgumentException;
import com.igorzaitcev.management.exceptions.MissingArgumentException;
import com.igorzaitcev.management.model.TimeTable;
import com.igorzaitcev.management.repository.TimeTableRepository;
import com.igorzaitcev.management.service.utils.TimeTableResponseDTOMapper;

@Service
@Transactional
public class TimeTableManagement {
	private TimeTableRepository repository;
	private TimeTableRequestDTOMapper dtoMapper;
	private TimeTableResponseDTOMapper timeTableMapper;

	private static final Logger log = LoggerFactory.getLogger(TimeTableManagement.class);

	public TimeTableManagement(TimeTableRepository dao, TimeTableRequestDTOMapper dtoMapper, TimeTableResponseDTOMapper timeTableMapper) {
		super();
		this.repository = dao;
		this.dtoMapper = dtoMapper;
		this.timeTableMapper = timeTableMapper;
	}

	public TimeTableRequestDTO getTimeTable(Long id) {
		Optional<TimeTable> table = repository.findById(id);
		if (repository.findById(id).isEmpty()) {
			throw new MissingArgumentException("TimeTable not found...");
		}
		return dtoMapper.convertToTimeTableRequestDTO(table.get());
	}

	public Optional<TimeTable> findTimeTable(Long id) {
		return repository.findById(id);
	}

	public TimeTableResponseDTO findTimeTableMap(Long id) {
		return timeTableMapper.convertToTimeTableResponseDTO(repository.findById(id).get());
	}

	public boolean deleteTimeTable(Long id) {
		Optional<TimeTable> timeTable = repository.findById(id);
		if (timeTable.isEmpty()) {
			return false;
		}
		log.debug("Service delete TimeTable: " + id);
		deleteAssociations(timeTable.get());
		repository.deleteById(id);
		return true;
	}

	public TimeTableRequestDTO addTimeTable(TimeTableResponseDTO timeTableMap) {
		if (timeTableMap == null) {
			throw new MissingArgumentException("TimeTable cant be null!");
		}
		log.debug("Service add TimeTable: " + timeTableMap.getDate());
		TimeTable timeTable = timeTableMapper.converToTimeTable(timeTableMap);
		if (timeTable.getId() != null) {
			repository.findById(timeTable.getId()).ifPresent(this::deleteAssociations);
		}
		addAssociations(repository.save(timeTable));
		return dtoMapper.convertToTimeTableRequestDTO(timeTable);
	}

	public Page<TimeTableRequestDTO> getTimeTablesByLimit(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
		Page<TimeTable> timeTables = repository.findAll(pageRequest);
		if (timeTables.isEmpty()) {
			throw new InvalidArgumentException("Tables not found...");
		}
		return dtoMapper.getTimeTableDTO(timeTables);
	}

	public long countTimeTables() {
		return repository.count();
	}

	private void addAssociations(TimeTable timeTable) {
		timeTable.getLecture().addTimeTable(timeTable);
		timeTable.getAuditorium().addTimeTable(timeTable);
		timeTable.getTeacher().addTimeTable(timeTable);
		timeTable.getGroup().addTimeTable(timeTable);
		timeTable.getCourse().addTimeTable(timeTable);
	}

	private void deleteAssociations(TimeTable timeTable) {
		timeTable.getLecture().removeTimeTable(timeTable);
		timeTable.getAuditorium().removeTimeTable(timeTable);
		timeTable.getTeacher().removeTimeTable(timeTable);
		timeTable.getGroup().removeTimeTable(timeTable);
		timeTable.getCourse().removeTimeTable(timeTable);
	}


}
