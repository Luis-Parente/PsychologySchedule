package com.laispsicologia.PsychologySchedule.client;

import com.laispsicologia.PsychologySchedule.client.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.client.dto.ClientMinDTO;
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

@RestController
@RequestMapping(value = "/clients")
@Tag(name = "Clients", description = "Controller for Clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @Operation(description = "Retrieve a page of clients", summary = "Return clients paged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<ClientMinDTO>> findAll(Pageable pageable) {
        Page<ClientMinDTO> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Retrieve a page of clients filtered by name", summary = "Return clients paged filtered by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(value = "/findByName",produces = "application/json")
    public ResponseEntity<Page<ClientMinDTO>> findByName(@RequestParam String name, Pageable pageable) {
        Page<ClientMinDTO> result = service.findByName(name, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Retrieve a client filtered by id", summary = "Return client by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully", content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        ClientDTO result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Insert new client", summary = "Insert client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully", content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(produces = "application/json")
    public ResponseEntity<ClientDTO> insert(@Valid @RequestBody ClientDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(description = "Update a client data", summary = "Update client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully", content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ClientDTO> update(@PathVariable Long id, @Valid @RequestBody ClientDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Delete a client filtered by id", summary = "Delete client by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class)))})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
