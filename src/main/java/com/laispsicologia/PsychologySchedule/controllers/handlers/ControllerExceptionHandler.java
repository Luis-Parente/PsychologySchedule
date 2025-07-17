package com.laispsicologia.PsychologySchedule.controllers.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.laispsicologia.PsychologySchedule.controllers.exceptions.InvalidDateException;
import com.laispsicologia.PsychologySchedule.dto.CustomErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(InvalidDateException.class)
	public ResponseEntity<CustomErrorDTO> resourceNotFound(InvalidDateException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}
