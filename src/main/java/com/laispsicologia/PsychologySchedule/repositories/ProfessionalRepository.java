package com.laispsicologia.PsychologySchedule.repositories;

import com.laispsicologia.PsychologySchedule.entities.EmergencyContact;
import com.laispsicologia.PsychologySchedule.entities.Professional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_professional WHERE deleted_at IS NULL")
    Page<Professional> findAllActive(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_professional WHERE id = :id AND deleted_at IS NULL")
    Optional<Professional> findByIdActive(Long id);
}
