package com.laispsicologia.PsychologySchedule.dto;

import java.time.Duration;

public class AppointmentMinDTO {

    private Double appointmentPrice;
    private String startDate;
    private Duration appointmentDuration;
    private Boolean paid;
    private Long planId;

    public AppointmentMinDTO() {

    }

    public AppointmentMinDTO(Double appointmentPrice, String startDate, Duration appointmentDuration, Boolean paid, Long planId) {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
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
