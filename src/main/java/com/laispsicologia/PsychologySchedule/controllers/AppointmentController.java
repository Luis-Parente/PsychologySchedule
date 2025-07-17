package com.laispsicologia.PsychologySchedule.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laispsicologia.PsychologySchedule.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.dto.CustomErrorDTO;
import com.laispsicologia.PsychologySchedule.services.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/appointments")
@Tag(name = "Appointments", description = "Controller for Appointments")
public class AppointmentController {

	@Autowired
	private AppointmentService service;

	@Operation(description = "Retrieve a list of appointments filtered by the initial date", summary = "List appointments filtered by date")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Appointments retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))) })
	@GetMapping(value = "/searchdate", produces = "application/json")
	public ResponseEntity<List<AppointmentDTO>> findByDate(@RequestParam(defaultValue = "") String initialDate,
			@RequestParam(defaultValue = "") String finalDate) {
		List<AppointmentDTO> result = service.searchByDate(initialDate, finalDate);
		return ResponseEntity.ok().body(result);

	}
}
