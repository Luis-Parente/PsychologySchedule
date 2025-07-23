package com.laispsicologia.PsychologySchedule.dto;

import com.laispsicologia.PsychologySchedule.entities.Professional;

public class ProfessionalDTO {
    private Long id;
    private String name;
    private String registrationNumber;
    private String email;
    private String phoneNumber;

    public ProfessionalDTO() {

    }

    public ProfessionalDTO(Long id, String name, String registrationNumber, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public ProfessionalDTO(Professional entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.registrationNumber = entity.getRegistrationNumber();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
