package com.laispsicologia.PsychologySchedule.controllers;

import com.laispsicologia.PsychologySchedule.dto.CustomErrorDTO;
import com.laispsicologia.PsychologySchedule.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.dto.ValidationErrorDTO;
import com.laispsicologia.PsychologySchedule.services.EmergencyContactService;
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

@RestController
@RequestMapping(value = "/contacts")
@Tag(name = "EmergencyContacts", description = "Controller for EmergencyContacts")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService service;

    @Operation(description = "Retrieve a page of emergency contacts", summary = "Return emergency contacts paged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "EmergencyContacts retrieved successfully")})
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<EmergencyContactDTO>> findAll(Pageable pageable) {
        Page<EmergencyContactDTO> result = service.findAll(pageable);
        return ResponseEntity.ok().body(result);
    }

    @Operation(description = "Retrieve a emergency contact filtered by id", summary = "Return emergency contact by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "EmergencyContact retrieved successfully", content = @Content(schema = @Schema(implementation = EmergencyContactDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EmergencyContactDTO> findById(@PathVariable Long id) {
        EmergencyContactDTO result = service.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(description = "Insert new emergency contact", summary = "Insert emergency contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "EmergencyContact created successfully", content = @Content(schema = @Schema(implementation = EmergencyContactDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(produces = "application/json")
    public ResponseEntity<EmergencyContactDTO> insert(@Valid @RequestBody EmergencyContactDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(description = "Update a emergency contact", summary = "Update emergency contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "EmergencyContact updated successfully", content = @Content(schema = @Schema(implementation = EmergencyContactDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PutMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<EmergencyContactDTO> update(@PathVariable Long id, @Valid @RequestBody EmergencyContactDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(description = "Delete a emergency contact filtered by id", summary = "Delete emergency contact by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "EmergencyContact deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @DeleteMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
