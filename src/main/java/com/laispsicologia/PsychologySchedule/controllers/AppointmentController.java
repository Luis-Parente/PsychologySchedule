package com.laispsicologia.PsychologySchedule.controllers;

import com.laispsicologia.PsychologySchedule.dto.*;
import com.laispsicologia.PsychologySchedule.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/appointments")
@Tag(name = "Appointments", description = "Controller for Appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @Operation(description = "Retrieve a list of appointments filtered by the initial date", summary = "List appointments filtered by date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(value = "/searchdate", produces = "application/json")
    public ResponseEntity<Page<AppointmentDTO>> findByDate(@RequestParam(defaultValue = "") LocalDateTime initialDate,
                                                           @RequestParam(defaultValue = "") LocalDateTime finalDate,
                                                           Pageable pageable) {
        Page<AppointmentDTO> result = service.searchByDate(initialDate, finalDate, pageable);
        return ResponseEntity.ok().body(result);
    }

    @Operation(description = "Insert new appointment", summary = "Insert appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(produces = "application/json")
    public ResponseEntity<AppointmentDTO> newAppointment(@RequestBody AppointmentMinDTO minDTO) {
        AppointmentDTO dto = service.newAppointment(minDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
