package com.laispsicologia.PsychologySchedule.dto;

import java.time.LocalDate;

import com.laispsicologia.PsychologySchedule.entities.Client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientDTO {

	private Long id;

	@Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
	@NotBlank(message = "Required field")
	private String name;

	@Size(min = 14, max = 14, message = "CPF must be 14 characters! Excepted pattern xxx.xxx.xxx-xx")
	@NotBlank(message = "Required field")
	private String cpf;
	private LocalDate birthDate;

	@Email(message = "Must be a well-formed email address")
	private String email;

	@NotBlank(message = "Required field")
	private String phoneNumber;

	public ClientDTO() {

	}

	public ClientDTO(Long id, String name, String cpf, LocalDate birthDate, String email, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.birthDate = birthDate;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public ClientDTO(Client entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cpf = entity.getCpf();
		this.birthDate = entity.getBirthDate();
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

}
