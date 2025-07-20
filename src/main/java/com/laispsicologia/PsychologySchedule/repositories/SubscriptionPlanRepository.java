package com.laispsicologia.PsychologySchedule.repositories;

import com.laispsicologia.PsychologySchedule.entities.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_subscription_plan WHERE deleted_at IS NULL")
    Page<SubscriptionPlan> findAllActive(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_subscription_plan WHERE id = :id AND deleted_at IS NULL")
    Optional<SubscriptionPlan> findByIdActive(Long id);

    @Query(nativeQuery = true, value = "SELECT EXISTS ( SELECT 1 FROM tb_subscription_plan WHERE client_id = :clientId AND deleted_at IS NULL) AS existingPlan")
    Boolean getSubscriptionPlanByClientId(Long clientId);
}
