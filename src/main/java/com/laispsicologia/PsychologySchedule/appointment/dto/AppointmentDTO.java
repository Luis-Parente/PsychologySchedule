package com.laispsicologia.PsychologySchedule.appointment.dto;

import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AppointmentDTO implements Serializable {

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

    private String clientName;

    public AppointmentDTO() {

    }

    public AppointmentDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, String appointmentStatus,
                          Double price, Boolean paid, Long clientId, String clientName) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentStatus = appointmentStatus;
        this.price = price;
        this.paid = paid;
        this.clientId = clientId;
        this.clientName = clientName;
    }

    public AppointmentDTO(Appointment entity) {
        this.id = entity.getId();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.appointmentStatus = entity.getAppointmentStatus().toString();
        this.price = entity.getPrice();
        this.paid = entity.getPaid();
        this.clientId = entity.getClient().getId();
        this.clientName = entity.getClient().getName();
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
