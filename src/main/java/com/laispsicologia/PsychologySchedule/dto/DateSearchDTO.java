package com.laispsicologia.PsychologySchedule.dto;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import com.laispsicologia.PsychologySchedule.controllers.exceptions.InvalidDateException;

public class DateSearchDTO {

	private String initialDate;
	private String finalDate;

	public DateSearchDTO() {

	}

	public DateSearchDTO(String initialDate, String finalDate) {
		this.initialDate = initialDate;
		this.finalDate = finalDate;
	}

	public String getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(String initialDate) {
		this.initialDate = initialDate;
	}

	public String getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	public void setDates(String initialDate, String finalDate) {
		try {
			if (initialDate.isEmpty() || initialDate.isBlank()) {
				initialDate = Instant.now().minus(15, ChronoUnit.DAYS).toString();
			}

			if (finalDate.isEmpty() || finalDate.isBlank()) {
				finalDate = Instant.now().plus(15, ChronoUnit.DAYS).toString();
			}

			Instant initialInstant = Instant.parse(initialDate);
			Instant finalInstant = Instant.parse(finalDate);

			this.initialDate = initialInstant.toString();
			this.finalDate = finalInstant.toString();
		} catch (DateTimeParseException e) {
			throw new InvalidDateException("Invalid date format! Expected 'dd/MM/yyyy HH:mm:ss'");
		}

	}

}
