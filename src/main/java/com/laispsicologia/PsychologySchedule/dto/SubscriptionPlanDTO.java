package com.laispsicologia.PsychologySchedule.dto;

import com.laispsicologia.PsychologySchedule.entities.SubscriptionPlan;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubscriptionPlanDTO {

    private Long id;
    private Double appointmentPrice;
    private Integer appointmentFrequency;
    private LocalDateTime startDate;
    private Duration appointmentDuration;

    @Positive(message = "Must be a positive number")
    @NotNull(message = "Required field")
    private Long clientId;

    public SubscriptionPlanDTO() {

    }

    public SubscriptionPlanDTO(Long id, Double appointmentPrice, Integer appointmentFrequency, LocalDateTime startDate, Duration appointmentDuration, Long clientId) {
        this.id = id;
        this.appointmentPrice = appointmentPrice;
        this.appointmentFrequency = appointmentFrequency;
        this.startDate = startDate;
        this.appointmentDuration = appointmentDuration;
        this.clientId = clientId;
    }

    public SubscriptionPlanDTO(SubscriptionPlan entity) {
        this.id = entity.getId();
        this.appointmentPrice = entity.getAppointmentPrice();
        this.appointmentFrequency = entity.getAppointmentFrequency();
        this.startDate = entity.getStartDate();
        this.appointmentDuration = entity.getAppointmentDuration();
        this.clientId = entity.getClient().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAppointmentPrice() {
        return appointmentPrice;
    }

    public void setAppointmentPrice(Double appointmentPrice) {
        this.appointmentPrice = appointmentPrice;
    }

    public Integer getAppointmentFrequency() {
        return appointmentFrequency;
    }

    public void setAppointmentFrequency(Integer appointmentFrequency) {
        this.appointmentFrequency = appointmentFrequency;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Duration getAppointmentDuration() {
        return appointmentDuration;
    }

    public void setAppointmentDuration(Duration appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
