package com.laispsicologia.PsychologySchedule.entities;

import java.time.Duration;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_subscription_plan")
public class SubscriptionPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double appointmentPrice;
	private Integer appointmentFrequency;

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant startDate;

	private Duration appointmentDuration;

	@OneToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updatedAt;
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant deletedAt;

	public SubscriptionPlan() {

	}

	public SubscriptionPlan(Long id, Double appointmentPrice, Integer appointmentFrequency, Instant startDate,
			Duration appointmentDuration) {
		super();
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

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
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
}
