package com.laispsicologia.PsychologySchedule.appointment;

import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.exceptions.dto.CustomErrorDTO;
import com.laispsicologia.PsychologySchedule.exceptions.dto.ValidationErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/appointments")
@Tag(name = "Appointments", description = "Controller for Appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @Operation(description = "Retrieve a page of appointment filtered by date", summary = "Return appointments paged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<AppointmentDTO>> findFilteredByDate(
            @RequestParam(defaultValue = "") LocalDate firstDate,
            @RequestParam(defaultValue = "") LocalDate lastDate,
            Pageable pageable) {
        Page<AppointmentDTO> appointmentsPaged = service.findFilteredByDate(firstDate, lastDate, pageable);
        return ResponseEntity.ok(appointmentsPaged);
    }

    @Operation(description = "Retrieve a client appointment by id", summary = "Return appointment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment retrieved successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<AppointmentDTO> findById(@PathVariable Long id) {
        AppointmentDTO result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Insert new appointment", summary = "Insert appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(produces = "application/json")
    public ResponseEntity<AppointmentDTO> insert(@Valid @RequestBody AppointmentMinDTO dto) {
        AppointmentDTO newAppointment = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newAppointment.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newAppointment);
    }

    @Operation(description = "Update a appointment data", summary = "Update appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment updated successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<AppointmentDTO> update(@PathVariable Long id, @Valid @RequestBody AppointmentDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Delete a appointment filtered by id", summary = "Delete appointment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
