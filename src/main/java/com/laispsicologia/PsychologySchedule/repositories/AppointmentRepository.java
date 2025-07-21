package com.laispsicologia.PsychologySchedule.repositories;

import com.laispsicologia.PsychologySchedule.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_appointment WHERE start_time BETWEEN :initialDate AND :finalDate AND deleted_at IS NULL")
    Page<Appointment> searchByDate(LocalDateTime initialDate, LocalDateTime finalDate, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT 1 FROM tb_appointment WHERE ((:startDate BETWEEN start_time AND end_time) " +
            "OR (:endDate BETWEEN start_time AND end_time AND :endDate != start_time))" +
            "AND appointment_status = 3 " +
            "AND deleted_at IS NULL)")
    Boolean verifyAppointmentAvailability(LocalDateTime startDate, LocalDateTime endDate);
}
