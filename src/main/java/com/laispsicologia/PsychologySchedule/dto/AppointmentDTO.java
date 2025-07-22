package com.laispsicologia.PsychologySchedule.dto;

import com.laispsicologia.PsychologySchedule.entities.Appointment;

import java.time.LocalDateTime;

public class AppointmentDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String appointmentStatus;
    private Double price;
    private Boolean paid;
    private Long planId;
    private Long clientId;

    public AppointmentDTO() {

    }

    public AppointmentDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, String appointmentStatus, Double price, Boolean paid, Long planId, Long clientId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentStatus = appointmentStatus;
        this.price = price;
        this.paid = paid;
        this.planId = planId;
        this.clientId = clientId;
    }

    public AppointmentDTO(Appointment entity) {
        this.id = entity.getId();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.appointmentStatus = entity.getAppointmentStatus().toString();
        this.price = entity.getPrice();
        this.paid = entity.getPaid();
        this.planId = entity.getClient().getId();
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

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
