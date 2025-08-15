package com.laispsicologia.PsychologySchedule.client;

import com.laispsicologia.PsychologySchedule.client.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE deleted_at IS NULL")
    Page<Client> findAllActive(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE id = :id AND deleted_at IS NULL")
    Optional<Client> findByIdActive(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_client WHERE UPPER(name) LIKE CONCAT('%', UPPER(:name), '%') AND deleted_at IS NULL")
    Page<Client> findByName(String name, Pageable pageable);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.contacts WHERE c.id = :id")
    Optional<Client> findClientWithContacts(@Param("id") Long id);
}
