package com.laispsicologia.PsychologySchedule.controllers;

import com.laispsicologia.PsychologySchedule.dto.CustomErrorDTO;
import com.laispsicologia.PsychologySchedule.dto.ProfessionalDTO;
import com.laispsicologia.PsychologySchedule.dto.ValidationErrorDTO;
import com.laispsicologia.PsychologySchedule.services.ProfessionalService;
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
@RequestMapping(value = "/professionals")
@Tag(name = "Professionals", description = "Controller for Professionals")
public class ProfessionalController {

    @Autowired
    private ProfessionalService service;

    @Operation(description = "Retrieve a page of professionals", summary = "Return professionals paged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professionals retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<ProfessionalDTO>> findAll(Pageable pageable) {
        Page<ProfessionalDTO> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Retrieve a professional filtered by id", summary = "Return professional by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professional retrieved successfully", content = @Content(schema = @Schema(implementation = ProfessionalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProfessionalDTO> findById(@PathVariable Long id) {
        ProfessionalDTO result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Insert new professional", summary = "Insert professional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Professional created successfully", content = @Content(schema = @Schema(implementation = ProfessionalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(produces = "application/json")
    public ResponseEntity<ProfessionalDTO> insert(@Valid @RequestBody ProfessionalDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(description = "Update a professional", summary = "Update professional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professional updated successfully", content = @Content(schema = @Schema(implementation = ProfessionalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProfessionalDTO> update(@PathVariable Long id, @Valid @RequestBody ProfessionalDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Delete a professional filtered by id", summary = "Delete professional by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Professional deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
