package com.laispsicologia.PsychologySchedule.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.laispsicologia.PsychologySchedule.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE deleted_at IS NULL")
	Page<Client> findAllActive(Pageable pageable);
	
	@Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE id = :id AND deleted_at IS NULL")
	Optional<Client> findByIdActive(Long id);
}
