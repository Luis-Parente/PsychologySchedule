package com.laispsicologia.PsychologySchedule.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laispsicologia.PsychologySchedule.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
