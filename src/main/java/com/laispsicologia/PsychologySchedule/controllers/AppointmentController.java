package com.laispsicologia.PsychologySchedule.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laispsicologia.PsychologySchedule.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.dto.DateSearchDTO;
import com.laispsicologia.PsychologySchedule.services.AppointmentService;

@RestController
@RequestMapping(value = "/appointments")
public class AppointmentController {

	@Autowired
	private AppointmentService service;

	@GetMapping(value = "/searchdate")
	public ResponseEntity<List<AppointmentDTO>> findByDate(@RequestParam(defaultValue = "") String initialDate,
			@RequestParam(defaultValue = "") String finalDate) {
		DateSearchDTO dto = new DateSearchDTO();
		dto.setDates(initialDate, finalDate);

		List<AppointmentDTO> result = service.searchByDate(dto);
		return ResponseEntity.ok().body(result);

	}
}
