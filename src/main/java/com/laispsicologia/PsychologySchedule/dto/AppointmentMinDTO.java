package com.laispsicologia.PsychologySchedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentMinDTO {

    @NotNull(message = "Required field")
    private LocalDateTime startTime;

    @NotNull(message = "Required field")
    private Boolean paid;

    @NotNull(message = "Required field")
    private Long clientId;

    public AppointmentMinDTO() {

    }

    public AppointmentMinDTO(LocalDateTime startTime, Boolean paid, Long clientId) {
        this.startTime = startTime;
        this.paid = paid;
        this.clientId = clientId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
