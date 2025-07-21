package com.laispsicologia.PsychologySchedule.entities;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_subscription_plan")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double appointmentPrice;
    private Integer appointmentFrequency;
    private LocalDateTime startDate;
    private Duration appointmentDuration;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "plan")
    private List<Appointment> appointments = new ArrayList<>();

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant deletedAt;

    public SubscriptionPlan() {

    }

    public SubscriptionPlan(Long id, Double appointmentPrice, Integer appointmentFrequency, LocalDateTime startDate,
                            Duration appointmentDuration) {
        this.id = id;
        this.appointmentPrice = appointmentPrice;
        this.appointmentFrequency = appointmentFrequency;
        this.startDate = startDate;
        this.appointmentDuration = appointmentDuration;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Appointment> getAppointments() {
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubscriptionPlan other = (SubscriptionPlan) obj;
        return Objects.equals(id, other.id);
    }

}
