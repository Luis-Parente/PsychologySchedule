package com.laispsicologia.PsychologySchedule.emergencyContact;

import com.laispsicologia.PsychologySchedule.emergencyContact.entity.EmergencyContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_emergency_contact WHERE deleted_at IS NULL")
    Page<EmergencyContact> findAllActive(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_emergency_contact WHERE id = :id AND deleted_at IS NULL")
    Optional<EmergencyContact> findByIdActive(Long id);
}
