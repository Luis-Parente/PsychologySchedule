package com.laispsicologia.PsychologySchedule.client.entity;

import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.EmergencyContact;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cpf;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private Double appointmentPrice;
    private Integer appointmentFrequency;
    private LocalDateTime treatmentStartDate;
    private LocalDateTime treatmentEndDate;
    private Long appointmentDurationInMinutes;

    @OneToMany(mappedBy = "client")
    private final Set<EmergencyContact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private final Set<Appointment> appointments = new HashSet<>();

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant deletedAt;

    public Client() {

    }

    public Client(Long id, String name, String cpf, LocalDate birthDate, String email, String phoneNumber,
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

    public Set<EmergencyContact> getContacts() {
        return contacts;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    public void restore() {
        this.deletedAt = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Client other = (Client) obj;
        return Objects.equals(id, other.id);
    }
}
