package com.laispsicologia.PsychologySchedule.controllers;

import com.laispsicologia.PsychologySchedule.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.services.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @GetMapping(value = "/findByDate")
    public ResponseEntity<Page<AppointmentDTO>> findFilteredByDate(@RequestParam(defaultValue = "") LocalDateTime firstDate, @RequestParam(defaultValue = "") LocalDateTime lastDate,
                                                                      Pageable pageable) {
        Page<AppointmentDTO> appointmentsPaged = service.findFilteredByDate(firstDate, lastDate, pageable);
        return ResponseEntity.ok(appointmentsPaged);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> insert(@Valid @RequestBody AppointmentMinDTO dto){
        AppointmentDTO newAppointment = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newAppointment.getId()).toUri();
        return ResponseEntity.created(uri).body(newAppointment);
    }

}
