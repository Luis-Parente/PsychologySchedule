package com.laispsicologia.PsychologySchedule.client.dto;

import com.laispsicologia.PsychologySchedule.client.entity.Client;
import com.laispsicologia.PsychologySchedule.emergencyContact.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.EmergencyContact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientDTO {

    private Long id;

    @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
    @NotBlank(message = "Required field")
    private String name;

    @NotBlank(message = "Required field")
    private String cpf;
    private LocalDate birthDate;

    @Email(message = "Must be a well-formed email address")
    private String email;

    @NotBlank(message = "Required field")
    private String phoneNumber;

    private Double appointmentPrice;
    private Integer appointmentFrequency;
    private LocalDateTime treatmentStartDate;
    private LocalDateTime treatmentEndDate;
    private Long appointmentDurationInMinutes;

    private List<EmergencyContactDTO> contacts = new ArrayList<>();

    public ClientDTO() {

    }

    public ClientDTO(Long id, String name, String cpf, LocalDate birthDate, String email, String phoneNumber,
                     Double appointmentPrice, Integer appointmentFrequency, LocalDateTime treatmentStartDate,
                     LocalDateTime treatmentEndDate, Long appointmentDurationInMinutes) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.appointmentPrice = appointmentPrice;
        this.appointmentFrequency = appointmentFrequency;
        this.treatmentStartDate = treatmentStartDate;
        this.treatmentEndDate = treatmentEndDate;
        this.appointmentDurationInMinutes = appointmentDurationInMinutes;
    }

    public ClientDTO(Client entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.cpf = entity.getCpf();
        this.birthDate = entity.getBirthDate();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.appointmentPrice = entity.getAppointmentPrice();
        this.appointmentFrequency = entity.getAppointmentFrequency();
        this.treatmentStartDate = entity.getTreatmentStartDate();
        this.treatmentEndDate = entity.getTreatmentEndDate();
        this.appointmentDurationInMinutes = entity.getAppointmentDurationInMinutes();

        for(EmergencyContact contact : entity.getContacts()){
            this.contacts.add(new EmergencyContactDTO(contact));
        }
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public LocalDateTime getTreatmentStartDate() {
        return treatmentStartDate;
    }

    public void setTreatmentStartDate(LocalDateTime treatmentStartDate) {
        this.treatmentStartDate = treatmentStartDate;
    }

    public LocalDateTime getTreatmentEndDate() {
        return treatmentEndDate;
    }

    public void setTreatmentEndDate(LocalDateTime treatmentEndDate) {
        this.treatmentEndDate = treatmentEndDate;
    }

    public Long getAppointmentDurationInMinutes() {
        return appointmentDurationInMinutes;
    }

    public void setAppointmentDurationInMinutes(Long appointmentDurationInMinutes) {
        this.appointmentDurationInMinutes = appointmentDurationInMinutes;
    }

    public List<EmergencyContactDTO> getContacts() {
        return contacts;
    }
}
