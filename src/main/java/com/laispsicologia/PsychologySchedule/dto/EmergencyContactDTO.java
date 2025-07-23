package com.laispsicologia.PsychologySchedule.dto;

import com.laispsicologia.PsychologySchedule.entities.EmergencyContact;

public class EmergencyContactDTO {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String relationship;
    private Long clientId;

    public EmergencyContactDTO() {

    }

    public EmergencyContactDTO(Long id, String name, String email, String phoneNumber, String relationship,
                               Long clientId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
        this.clientId = clientId;
    }

    public EmergencyContactDTO(EmergencyContact entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.relationship = entity.getRelationship().toString();
        this.clientId = entity.getClient().getId();
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
