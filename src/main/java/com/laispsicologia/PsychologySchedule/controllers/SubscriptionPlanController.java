package com.laispsicologia.PsychologySchedule.controllers;

import com.laispsicologia.PsychologySchedule.dto.CustomErrorDTO;
import com.laispsicologia.PsychologySchedule.dto.SubscriptionPlanDTO;
import com.laispsicologia.PsychologySchedule.dto.ValidationErrorDTO;
import com.laispsicologia.PsychologySchedule.services.SubscriptionPlanService;
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
@RequestMapping(value = "/plans")
@Tag(name = "SubscriptionPlans", description = "Controller for SubscriptionPlans")
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService service;

    @Operation(description = "Retrieve a page of subscription plans", summary = "Return subscription plans paged")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "SubscriptionPlans retrieved successfully")})
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<SubscriptionPlanDTO>> findAll(Pageable pageable) {
        Page<SubscriptionPlanDTO> result = service.findAll(pageable);
        return ResponseEntity.ok().body(result);
    }

    @Operation(description = "Retrieve a subscription plan filtered by id", summary = "Return subscription plan by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SubscriptionPlan retrieved successfully", content = @Content(schema = @Schema(implementation = SubscriptionPlanDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<SubscriptionPlanDTO> findById(@PathVariable Long id) {
        SubscriptionPlanDTO result = service.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(description = "Insert new subscription plan", summary = "Insert subscription plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SubscriptionPlan created successfully", content = @Content(schema = @Schema(implementation = SubscriptionPlanDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(produces = "application/json")
    public ResponseEntity<SubscriptionPlanDTO> insert(@Valid @RequestBody SubscriptionPlanDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(description = "Update a subscription plan data", summary = "Update subscription plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SubscriptionPlan updated successfully", content = @Content(schema = @Schema(implementation = SubscriptionPlanDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PutMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<SubscriptionPlanDTO> update(@PathVariable Long id, @Valid @RequestBody SubscriptionPlanDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(description = "Delete a subscription plan filtered by id", summary = "Delete subscription plan by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "SubscriptionPlan deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @DeleteMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
