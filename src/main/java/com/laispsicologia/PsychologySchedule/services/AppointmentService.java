package com.laispsicologia.PsychologySchedule.services;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laispsicologia.PsychologySchedule.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.repositories.AppointmentRepository;
import com.laispsicologia.PsychologySchedule.service.exceptions.InvalidDateException;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository repository;

	public List<AppointmentDTO> searchByDate(String initialDate, String finalDate) {
		try {
			if (initialDate.isEmpty() || initialDate.isBlank()) {
				initialDate = Instant.now().minus(15, ChronoUnit.DAYS).toString();
			}

			if (finalDate.isEmpty() || finalDate.isBlank()) {
				finalDate = Instant.now().plus(15, ChronoUnit.DAYS).toString();
			}

			Instant initialInstant = Instant.parse(initialDate);
			Instant finalInstant = Instant.parse(finalDate);

			initialDate = initialInstant.toString();
			finalDate = finalInstant.toString();

			return repository.searchByDate(initialDate, finalDate).stream()
					.map(appointment -> new AppointmentDTO(appointment)).toList();
		} catch (DateTimeParseException e) {
			throw new InvalidDateException("Invalid date format! Expected 'dd/MM/yyyyTHH:mm:ssZ'");
		}

	}

}
