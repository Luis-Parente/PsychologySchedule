package com.laispsicologia.PsychologySchedule.appointment.dto;

import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class AppointmentDTO {

    private Long id;

    @NotNull(message = "Required field")
    private LocalDateTime startTime;

    @NotNull(message = "Required field")
    private LocalDateTime endTime;

    @NotNull(message = "Required field")
    private String appointmentStatus;
    private Double price;

    @NotNull(message = "Required field")
    private Boolean paid;

    @Positive(message = "Client id must be a positive number")
    @NotNull(message = "Required field")
    private Long clientId;

    public AppointmentDTO() {

    }

    public AppointmentDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, String appointmentStatus,
                          Double price, Boolean paid, Long clientId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentStatus = appointmentStatus;
        this.price = price;
        this.paid = paid;
        this.clientId = clientId;
    }

    public AppointmentDTO(Appointment entity) {
        this.id = entity.getId();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.appointmentStatus = entity.getAppointmentStatus().toString();
        this.price = entity.getPrice();
        this.paid = entity.getPaid();
        this.clientId = entity.getClient().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
