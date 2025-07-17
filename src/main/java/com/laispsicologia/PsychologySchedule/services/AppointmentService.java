package com.laispsicologia.PsychologySchedule.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laispsicologia.PsychologySchedule.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.dto.DateSearchDTO;
import com.laispsicologia.PsychologySchedule.repositories.AppointmentRepository;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository repository;

	public List<AppointmentDTO> searchByDate(DateSearchDTO dto) {

		return repository.searchByDate(dto.getInitialDate(), dto.getFinalDate()).stream()
				.map(appointment -> new AppointmentDTO(appointment)).toList();
	}

}
