package com.laispsicologia.PsychologySchedule.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/appointments")
@Tag(name = "Appointments", description = "Controller for Appointments")
public class AppointmentController {

}
