package com.laispsicologia.PsychologySchedule.repositories;

import com.laispsicologia.PsychologySchedule.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE deleted_at IS NULL")
    Page<Client> findAllActive(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE id = :id AND deleted_at IS NULL")
    Optional<Client> findByIdActive(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE UPPER(name) LIKE CONCAT('%', UPPER(:name), '%') AND deleted_at IS NULL")
    Page<Client> findByName(String name, Pageable pageable);
}
