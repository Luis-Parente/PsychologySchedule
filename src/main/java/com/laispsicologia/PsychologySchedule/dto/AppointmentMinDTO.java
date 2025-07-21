package com.laispsicologia.PsychologySchedule.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class AppointmentMinDTO {

    private Double appointmentPrice;
    private LocalDateTime startDate;
    private Duration appointmentDuration;
    private Boolean paid;
    private Long planId;

    public AppointmentMinDTO() {

    }

    public AppointmentMinDTO(Double appointmentPrice, LocalDateTime startDate, Duration appointmentDuration, Boolean paid, Long planId) {
        this.appointmentPrice = appointmentPrice;
        this.startDate = startDate;
        this.appointmentDuration = appointmentDuration;
        this.paid = paid;
        this.planId = planId;
    }

    public Double getAppointmentPrice() {
        return appointmentPrice;
    }

    public void setAppointmentPrice(Double appointmentPrice) {
        this.appointmentPrice = appointmentPrice;
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
}
